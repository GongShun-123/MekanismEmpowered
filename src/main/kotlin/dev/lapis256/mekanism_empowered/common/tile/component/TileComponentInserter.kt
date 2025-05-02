package dev.lapis256.mekanism_empowered.common.tile.component

import dev.lapis256.mekanism_empowered.api.MekEmpSerializationConstants
import dev.lapis256.mekanism_empowered.api.MekEmpUpgrade
import dev.lapis256.mekanism_empowered.common.config.GeneralConfig
import dev.lapis256.mekanism_empowered.core.common.util.WrappedCapabilityCache
import dev.lapis256.mekanism_empowered.core.extension.canInput
import dev.lapis256.mekanism_empowered.core.extension.getInstalledOrDefault
import dev.lapis256.mekanism_empowered.core.extension.isUpgradeInstalled
import dev.lapis256.mekanism_empowered.extension.inserterConfig
import mekanism.api.Action
import mekanism.api.AutomationType
import mekanism.api.math.MathUtils
import mekanism.common.lib.transmitter.TransmissionType
import mekanism.common.tile.component.ITileComponent
import mekanism.common.tile.component.config.ConfigInfo
import mekanism.common.tile.component.config.DataType
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo
import mekanism.common.tile.component.config.slot.EnergySlotInfo
import mekanism.common.tile.component.config.slot.FluidSlotInfo
import mekanism.common.tile.component.config.slot.InventorySlotInfo
import mekanism.common.tile.prefab.TileEntityConfigurableMachine
import mekanism.common.util.EnumUtils
import net.minecraft.SharedConstants
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction
import net.neoforged.neoforge.items.IItemHandler
import java.util.*
import kotlin.math.pow


class TileComponentInserter(private val tile: TileEntityConfigurableMachine) : ITileComponent {
    private val wrappedCapabilityCaches = EnumMap<TransmissionType, WrappedCapabilityCache<*, *>>(TransmissionType::class.java)

    private var tickDelay = 0

    private val sideConfig = tile.config

    private var ioCapacities = buildIOCapacities()

    init {
        tile.addComponent(this)
    }

    fun tickServer() {
        if (!tile.isUpgradeInstalled(MekEmpUpgrade.AUTO_INSERTER)) {
            return
        }

        ioCapacities = buildIOCapacities()

        for (type in EnumUtils.TRANSMISSION_TYPES) {
            val info = sideConfig.getConfig(type) ?: continue

            if (type == TransmissionType.ITEM) {
                if (tickDelay == 0) {
                    insertItems(tile.direction, info)
                } else {
                    tickDelay--
                }
            } else if (type != TransmissionType.HEAT) {
                insert(tile.direction, type, info)
            }
        }
    }

    private fun insert(facing: Direction, type: TransmissionType, info: ConfigInfo) {
        val level = tile.level as? ServerLevel ?: return
        val typeCaches = wrappedCapabilityCaches.computeIfAbsent(type) { WrappedCapabilityCache.newCache(type) } ?: return

        for (dataType in info.supportedDataTypes) {
            if (!dataType.canInput) {
                continue
            }
            val slotInfo = info.getSlotInfo(dataType) ?: continue

            for (side in getSidesForData(info, facing, dataType)) {
                when {
                    slotInfo is ChemicalSlotInfo && typeCaches is WrappedCapabilityCache.Chemical -> {
                        val capability = typeCaches.getCapability(level, tile.blockPos, side) ?: return

                        for (tank in slotInfo.tanks) {
                            val simulated = capability.extractChemical(ioCapacities[type] ?: 1024, Action.SIMULATE)
                            if (simulated.isEmpty) {
                                continue
                            }
                            val remaining = tank.insert(simulated, Action.EXECUTE, AutomationType.EXTERNAL)
                            simulated.amount -= remaining.amount
                            capability.extractChemical(simulated, Action.EXECUTE)
                        }
                    }

                    slotInfo is FluidSlotInfo && typeCaches is WrappedCapabilityCache.Fluid -> {
                        val capability = typeCaches.getCapability(level, tile.blockPos, side) ?: return

                        for (tank in slotInfo.tanks) {
                            val simulated = capability.drain(ioCapacities[type]?.toInt() ?: 1024, FluidAction.SIMULATE)
                            if (simulated.isEmpty) {
                                continue
                            }
                            val remaining = tank.insert(simulated, Action.EXECUTE, AutomationType.EXTERNAL)
                            simulated.amount -= remaining.amount
                            capability.fill(simulated, FluidAction.EXECUTE)
                        }
                    }

                    slotInfo is EnergySlotInfo && typeCaches is WrappedCapabilityCache.Energy -> {
                        val capability = typeCaches.getCapability(level, tile.blockPos, side) ?: return

                        for (container in slotInfo.containers) {
                            var simulated = capability.extractEnergy(ioCapacities[type] ?: 1024, Action.SIMULATE)
                            if (simulated <= 0) {
                                continue
                            }
                            val remaining = container.insert(simulated, Action.EXECUTE, AutomationType.EXTERNAL)
                            simulated -= remaining
                            capability.insertEnergy(simulated, Action.EXECUTE)
                        }
                    }
                }
            }
        }
    }

    private fun insertItems(facing: Direction, info: ConfigInfo) {
        val level = tile.level as? ServerLevel ?: return
        val typeCaches = wrappedCapabilityCaches.computeIfAbsent(TransmissionType.ITEM) { WrappedCapabilityCache.Item() }

        for (dataType in info.supportedDataTypes) {
            if (!dataType.canInput) {
                continue
            }
            val slotInfo = info.getSlotInfo(dataType) as? InventorySlotInfo ?: continue

            for (side in getSidesForData(info, facing, dataType)) {
                val capability = typeCaches.getCapability(level, tile.blockPos, side) as? IItemHandler ?: continue

                val notEmptySlots = (0..<capability.slots).filterNot { capability.getStackInSlot(it).isEmpty }.toMutableList()
                if (notEmptySlots.isEmpty()) {
                    continue
                }

                for (slot in slotInfo.slots) {
                    var extractCount = ioCapacities[TransmissionType.ITEM]?.toInt() ?: 8

                    for (i in notEmptySlots.toList()) {
                        val simulated = capability.extractItem(i, extractCount, true)
                        if (simulated.isEmpty) {
                            notEmptySlots.remove(i)
                            continue
                        }

                        val remaining = slot.insertItem(simulated, Action.EXECUTE, AutomationType.EXTERNAL)
                        simulated.count -= remaining.count
                        extractCount -= simulated.count
                        capability.extractItem(i, simulated.count, false)

                        if (extractCount <= 0) {
                            break
                        }
                    }
                }
            }
        }

        tickDelay = run {
            val max = MekEmpUpgrade.FAST_ITEM_INSERT.max.toDouble()
            val installed = tile.getInstalledOrDefault(MekEmpUpgrade.FAST_ITEM_INSERT)
            MathUtils.clampToInt((SharedConstants.TICKS_PER_SECOND + 1.0).pow((max - installed) / max) - 1)
        }
    }

    override fun getComponentKey() = MekEmpSerializationConstants.COMPONENT_INSERTER
    override fun deserialize(componentTag: CompoundTag, provider: HolderLookup.Provider) = Unit
    override fun serialize(provider: HolderLookup.Provider) = CompoundTag()

    private fun getSidesForData(info: ConfigInfo, facing: Direction, dataType: DataType): MutableSet<Direction> {
        return EnumSet.noneOf(Direction::class.java).also {
            for (entry in info.sideConfig) {
                if (entry.value == dataType && tile.inserterConfig.isSideEnabled(entry.key)) {
                    it.add(entry.key.getDirection(facing))
                }
            }
        }
    }

    private fun buildIOCapacities() = buildMap {
        val installed = tile.getInstalledOrDefault(MekEmpUpgrade.IO_CAPACITY)
        val max = MekEmpUpgrade.IO_CAPACITY.max.toDouble()
        put(TransmissionType.ITEM, MathUtils.clampToLong(GeneralConfig.AutoInsert.itemRate * 8.0.pow(installed / max)))
        put(TransmissionType.CHEMICAL, MathUtils.clampToLong(GeneralConfig.AutoInsert.chemicalRate * (1 + 32 * (installed / max))))
        put(TransmissionType.FLUID, MathUtils.clampToLong(GeneralConfig.AutoInsert.fluidRate * (1 + 32 * (installed / max))))
        put(TransmissionType.ENERGY, MathUtils.clampToLong(GeneralConfig.AutoInsert.energyRate * (1 + 32 * (installed / max))))
    }
}

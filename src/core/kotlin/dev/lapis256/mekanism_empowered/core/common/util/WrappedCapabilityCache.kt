package dev.lapis256.mekanism_empowered.core.common.util

import mekanism.api.chemical.IChemicalHandler
import mekanism.api.energy.IStrictEnergyHandler
import mekanism.common.capabilities.Capabilities
import mekanism.common.integration.energy.BlockEnergyCapabilityCache
import mekanism.common.lib.transmitter.TransmissionType
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.neoforged.neoforge.capabilities.BlockCapabilityCache
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler
import java.util.EnumMap


sealed class WrappedCapabilityCache<CACHE, CAPABILITY : Any>(val type: TransmissionType) {
    abstract fun createCache(level: ServerLevel, blockPos: BlockPos, side: Direction): CACHE
    abstract fun getCapability(level: ServerLevel, blockPos: BlockPos, side: Direction): CAPABILITY?

    val caches: MutableMap<Direction, CACHE> = EnumMap(Direction::class.java)

    companion object {
        fun newCache(type: TransmissionType): WrappedCapabilityCache<out Any, out Any>? = when (type) {
            TransmissionType.ITEM -> Item()
            TransmissionType.CHEMICAL -> Chemical()
            TransmissionType.FLUID -> Fluid()
            TransmissionType.ENERGY -> Energy()
            else -> error("unrecognized transmission type $type")
        }
    }

    fun getCache(level: ServerLevel, blockPos: BlockPos, side: Direction): CACHE = caches.computeIfAbsent(side) { createCache(level, blockPos, side) }

    class Item : WrappedCapabilityCache<BlockCapabilityCache<IItemHandler, Direction?>, IItemHandler>(TransmissionType.ITEM) {
        override fun createCache(level: ServerLevel, blockPos: BlockPos, side: Direction): BlockCapabilityCache<IItemHandler, Direction?> =
            Capabilities.ITEM.createCache(level, blockPos.relative(side), side.opposite)

        override fun getCapability(level: ServerLevel, blockPos: BlockPos, side: Direction) = getCache(level, blockPos, side).capability
    }

    class Chemical : WrappedCapabilityCache<BlockCapabilityCache<IChemicalHandler, Direction?>, IChemicalHandler>(TransmissionType.CHEMICAL) {
        override fun createCache(level: ServerLevel, blockPos: BlockPos, side: Direction): BlockCapabilityCache<IChemicalHandler, Direction?> =
            Capabilities.CHEMICAL.createCache(level, blockPos.relative(side), side.opposite)

        override fun getCapability(level: ServerLevel, blockPos: BlockPos, side: Direction) = getCache(level, blockPos, side).capability
    }

    class Fluid : WrappedCapabilityCache<BlockCapabilityCache<IFluidHandler, Direction?>, IFluidHandler>(TransmissionType.FLUID) {
        override fun createCache(level: ServerLevel, blockPos: BlockPos, side: Direction): BlockCapabilityCache<IFluidHandler, Direction?> =
            Capabilities.FLUID.createCache(level, blockPos.relative(side), side.opposite)

        override fun getCapability(level: ServerLevel, blockPos: BlockPos, side: Direction) = getCache(level, blockPos, side).capability
    }

    class Energy : WrappedCapabilityCache<BlockEnergyCapabilityCache, IStrictEnergyHandler>(TransmissionType.ENERGY) {
        override fun createCache(level: ServerLevel, blockPos: BlockPos, side: Direction): BlockEnergyCapabilityCache =
            BlockEnergyCapabilityCache.create(level, blockPos.relative(side), side.opposite)

        override fun getCapability(level: ServerLevel, blockPos: BlockPos, side: Direction) = getCache(level, blockPos, side).capability
    }
}

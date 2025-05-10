package dev.lapis256.mekanism_empowered.common.tile.component

import dev.lapis256.mekanism_empowered.api.MekEmpSerializationConstants
import dev.lapis256.mekanism_empowered.common.tile.component.config.InserterConfigInfo
import mekanism.api.NBTConstants
import mekanism.api.RelativeSide
import mekanism.common.tile.base.TileEntityMekanism
import mekanism.common.tile.component.ITileComponent
import mekanism.common.util.EnumUtils
import mekanism.common.util.NBTUtils
import net.minecraft.nbt.CompoundTag


class TileComponentInserterConfig(val tile: TileEntityMekanism) : ITileComponent {
    val configInfo = InserterConfigInfo()

    init {
        tile.addComponent(this)
    }

    fun toggleSideConfig(relativeSide: RelativeSide) {
        configInfo.toggleSideConfig(relativeSide)
        tile.markForSave()
        tile.sendUpdatePacket()
    }

    fun isSideEnabled(relativeSide: RelativeSide) = configInfo.isSideEnabled(relativeSide)

    fun getComponentKey() = MekEmpSerializationConstants.COMPONENT_INSERTER_CONFIG

    private fun readFromNBT(componentTag: CompoundTag) {
        if (componentTag.contains(NBTConstants.CONFIG)) {
            componentTag
                .getByteArray(NBTConstants.CONFIG)
                .forEachIndexed { i, byte -> configInfo.setSideConfig(RelativeSide.byIndex(i), byte == 1.toByte()) }
        }
    }

    override fun readFromUpdateTag(updateTag: CompoundTag) {
        NBTUtils.setCompoundIfPresent(updateTag, getComponentKey(), ::readFromNBT)
    }

    override fun read(componentTag: CompoundTag) {
        readFromNBT(componentTag)
    }

    private fun writeToNBT(componentTag: CompoundTag) {
        componentTag.putByteArray(
            NBTConstants.CONFIG,
            EnumUtils.SIDES.sortedBy(RelativeSide::ordinal).map { if (configInfo.isSideEnabled(it)) 1 else 0 }
        )
    }

    override fun write(componentTag: CompoundTag) {
        writeToNBT(componentTag)
    }

    override fun addToUpdateTag(updateTag: CompoundTag) {
        updateTag.put(getComponentKey(), CompoundTag().also(::writeToNBT))
    }
}

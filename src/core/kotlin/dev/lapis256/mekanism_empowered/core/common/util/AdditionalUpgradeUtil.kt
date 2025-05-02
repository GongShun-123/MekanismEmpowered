package dev.lapis256.mekanism_empowered.core.common.util

import mekanism.api.Upgrade
import mekanism.common.block.attribute.AttributeUpgradeSupport
import mekanism.common.content.blocktype.BlockType


object AdditionalUpgradeUtil {
    /**
     * Adds additional supported upgrades to the given block type.
     *
     * This method appends the specified upgrades to the block's existing list
     * of supported upgrades via the [AttributeUpgradeSupport] attribute.
     *
     * @param blockType The block type to modify.
     * @param upgrades  The upgrades to add.
     */
    @JvmStatic
    fun addSupported(blockType: BlockType, vararg upgrades: Upgrade) {
        val attribute = blockType.get(AttributeUpgradeSupport::class.java) ?: return
        val supportedUpgrades = arrayOf(*attribute.supportedUpgrades.toTypedArray(), *upgrades)
        blockType.add(AttributeUpgradeSupport.create(*supportedUpgrades))
    }
}

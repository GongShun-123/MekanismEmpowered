package dev.lapis256.mekanism_empowered.common.init

import dev.lapis256.mekanism_empowered.api.MekEmpUpgrade
import dev.lapis256.mekanism_empowered.api.MekanismEmpoweredAPI
import dev.lapis256.mekanism_empowered.core.common.upgrade.UpgradeItemRegistry
import mekanism.api.Upgrade
import mekanism.common.item.ItemUpgrade
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister


object MekEmpItems {
    val REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(MekanismEmpoweredAPI.MOD_ID)

    val EMPOWERED_SPEED = registerUpgrade(MekEmpUpgrade.EMPOWERED_SPEED)
    val EMPOWERED_ENERGY = registerUpgrade(MekEmpUpgrade.EMPOWERED_ENERGY)
    val FAST_ITEM_EJECT = registerUpgrade(MekEmpUpgrade.FAST_ITEM_EJECT)
    val FAST_ITEM_INSERT = registerUpgrade(MekEmpUpgrade.FAST_ITEM_INSERT)
    val AUTO_INSERTER = registerUpgrade(MekEmpUpgrade.AUTO_INSERTER)
    val IO_CAPACITY = registerUpgrade(MekEmpUpgrade.IO_CAPACITY)

    init {
        UpgradeItemRegistry.register(MekEmpUpgrade.EMPOWERED_SPEED, EMPOWERED_SPEED)
        UpgradeItemRegistry.register(MekEmpUpgrade.EMPOWERED_ENERGY, EMPOWERED_ENERGY)
        UpgradeItemRegistry.register(MekEmpUpgrade.FAST_ITEM_EJECT, FAST_ITEM_EJECT)
        UpgradeItemRegistry.register(MekEmpUpgrade.FAST_ITEM_INSERT, FAST_ITEM_INSERT)
        UpgradeItemRegistry.register(MekEmpUpgrade.AUTO_INSERTER, AUTO_INSERTER)
        UpgradeItemRegistry.register(MekEmpUpgrade.IO_CAPACITY, IO_CAPACITY)
    }

    private fun registerUpgrade(upgrade: Upgrade): DeferredItem<ItemUpgrade> {
        return REGISTRY.registerItem("upgrade_${upgrade.serializedName}") { ItemUpgrade(upgrade, it) }
    }
}

package dev.lapis256.mekanism_empowered.common.init

import dev.lapis256.mekanism_empowered.api.MekEmpUpgrade
import dev.lapis256.mekanism_empowered.api.MekanismEmpoweredAPI
import dev.lapis256.mekanism_empowered.common.config.MekEmpTierConfig
import dev.lapis256.mekanism_empowered.common.item.ItemTieredGaugeDropper
import dev.lapis256.mekanism_empowered.core.common.upgrade.UpgradeItemRegistry
import mekanism.api.Upgrade
import mekanism.api.tier.BaseTier
import mekanism.common.attachments.containers.ContainerType
import mekanism.common.item.ItemUpgrade
import mekanism.common.registration.impl.ItemDeferredRegister
import mekanism.common.registration.impl.ItemRegistryObject


@Suppress("UnstableApiUsage")
object MekEmpItems {
    val REGISTRY = ItemDeferredRegister(MekanismEmpoweredAPI.MOD_ID)

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

    val BASIC_GAUGE_DROPPER: ItemRegistryObject<ItemTieredGaugeDropper> = registerTiredGaugeDropper(BaseTier.BASIC)
    val ADVANCED_GAUGE_DROPPER: ItemRegistryObject<ItemTieredGaugeDropper> = registerTiredGaugeDropper(BaseTier.ADVANCED)
    val ELITE_GAUGE_DROPPER: ItemRegistryObject<ItemTieredGaugeDropper> = registerTiredGaugeDropper(BaseTier.ELITE)
    val ULTIMATE_GAUGE_DROPPER: ItemRegistryObject<ItemTieredGaugeDropper> = registerTiredGaugeDropper(BaseTier.ULTIMATE)

    private fun registerTiredGaugeDropper(tier: BaseTier): ItemRegistryObject<ItemTieredGaugeDropper> {
        val (rete, capacity) = when (tier) {
            BaseTier.BASIC -> MekEmpTierConfig.GaugeDropper.basicRate to MekEmpTierConfig.GaugeDropper.basicCapacity
            BaseTier.ADVANCED -> MekEmpTierConfig.GaugeDropper.advancedRate to MekEmpTierConfig.GaugeDropper.advancedCapacity
            BaseTier.ELITE -> MekEmpTierConfig.GaugeDropper.eliteRate to MekEmpTierConfig.GaugeDropper.eliteCapacity
            BaseTier.ULTIMATE -> MekEmpTierConfig.GaugeDropper.ultimateRate to MekEmpTierConfig.GaugeDropper.ultimateCapacity
            else -> error("Invalid tier: $tier")
        }

        return REGISTRY.registerItem("${tier.lowerName}_gauge_dropper") { properties -> ItemTieredGaugeDropper(tier, properties) }
            .addAttachedContainerCapabilities(
                ContainerType.CHEMICAL,
                { ItemTieredGaugeDropper.getChemicalTankCreator(rete, capacity) },
                MekEmpTierConfig
            )
            .addAttachedContainerCapabilities(
                ContainerType.FLUID,
                { ItemTieredGaugeDropper.getFluidTankCreator(rete, capacity) },
                MekEmpTierConfig
            )
    }

    private fun registerUpgrade(upgrade: Upgrade): ItemRegistryObject<ItemUpgrade> =
        REGISTRY.registerItem("upgrade_${upgrade.serializedName}") { ItemUpgrade(upgrade, it) }
}

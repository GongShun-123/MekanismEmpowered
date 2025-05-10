package dev.lapis256.mekanism_empowered.common.init

import com.jerry.mekanism_extras.common.registry.ExtraBlockType
import com.jerry.mekanism_extras.common.tier.AdvancedFactoryTier
import com.jerry.mekanism_extras.common.tile.machine.TileEntityAdvancedElectricPump
import dev.lapis256.mekanism_empowered.api.MekEmpUpgrade
import dev.lapis256.mekanism_empowered.core.common.upgrade.UpgradeInfoHandler
import dev.lapis256.mekanism_empowered.core.common.util.AdditionalUpgradeUtil
import dev.lapis256.mekanism_empowered.core.extension.getInstalledOrDefault
import mekanism.api.Upgrade
import mekanism.common.content.blocktype.FactoryType
import mekanism.common.registries.MekanismBlockTypes
import mekanism.common.tile.interfaces.IUpgradeTile
import mekanism.common.tile.machine.TileEntityElectricPump
import mekanism.common.util.EnumUtils
import mekanism.common.util.UpgradeUtils
import net.minecraft.network.chat.Component
import net.minecraftforge.fml.ModList


object MekEmpUpgrades {
    fun registerUpgradeInfo() {
        val empoweredSpeedUpgradePumpInfo = { tile: IUpgradeTile ->
            listOf(Component.literal("Effect: +" + tile.getInstalledOrDefault(MekEmpUpgrade.EMPOWERED_SPEED) * 100 + "%"))
        }

        UpgradeInfoHandler.register(MekEmpUpgrade.EMPOWERED_SPEED, UpgradeUtils::getExpScaledInfo)
            .registerOverrideForTiles(TileEntityElectricPump::class) { it, _ -> empoweredSpeedUpgradePumpInfo.invoke(it) }
            .conditionallyRegisterOverride(ModList.get().isLoaded("mekanism_extras")) {
                registerOverrideForTiles(TileEntityAdvancedElectricPump::class) { it, _ -> empoweredSpeedUpgradePumpInfo.invoke(it) }
            }

        UpgradeInfoHandler.register(MekEmpUpgrade.EMPOWERED_ENERGY, UpgradeUtils::getMultScaledInfo)

        UpgradeInfoHandler.register(MekEmpUpgrade.IO_CAPACITY) { it, _ ->
            listOf(Component.literal("Effect: +" + it.getInstalledOrDefault(MekEmpUpgrade.IO_CAPACITY) * 3_200 + "%"))
        }
    }

    fun registerSupportedUpgrades() {
        val speedAndEnergyUpgrades = arrayOf(MekEmpUpgrade.EMPOWERED_SPEED, MekEmpUpgrade.EMPOWERED_ENERGY)
        val machineUpgrades = arrayOf(*speedAndEnergyUpgrades, MekEmpUpgrade.IO_CAPACITY, MekEmpUpgrade.AUTO_INSERTER)
        val itemInputMachineUpgrades = arrayOf(*machineUpgrades, MekEmpUpgrade.FAST_ITEM_INSERT)
        val itemOutputMachineUpgrades = arrayOf(*machineUpgrades, MekEmpUpgrade.FAST_ITEM_EJECT)
        val itemInOutMachineUpgrades = arrayOf(*machineUpgrades, MekEmpUpgrade.FAST_ITEM_INSERT, MekEmpUpgrade.FAST_ITEM_EJECT)

        val qioUpgrades = arrayOf(MekEmpUpgrade.EMPOWERED_SPEED, MekEmpUpgrade.IO_CAPACITY)

        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.ENRICHMENT_CHAMBER, *itemInOutMachineUpgrades)
        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.CRUSHER, *itemInOutMachineUpgrades)
        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.ENERGIZED_SMELTER, *itemInOutMachineUpgrades)
        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.PRECISION_SAWMILL, *itemInOutMachineUpgrades)
        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.OSMIUM_COMPRESSOR, *itemInOutMachineUpgrades)
        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.COMBINER, *itemInOutMachineUpgrades)
        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.METALLURGIC_INFUSER, *itemInOutMachineUpgrades)
        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.PURIFICATION_CHAMBER, *itemInOutMachineUpgrades)
        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.CHEMICAL_INJECTION_CHAMBER, *itemInOutMachineUpgrades)

        registerFactoryUpgrades(FactoryType.ENRICHING, itemInOutMachineUpgrades)
        registerFactoryUpgrades(FactoryType.CRUSHING, itemInOutMachineUpgrades)
        registerFactoryUpgrades(FactoryType.SMELTING, itemInOutMachineUpgrades)
        registerFactoryUpgrades(FactoryType.SAWING, itemInOutMachineUpgrades)
        registerFactoryUpgrades(FactoryType.COMPRESSING, itemInOutMachineUpgrades)
        registerFactoryUpgrades(FactoryType.COMBINING, itemInOutMachineUpgrades)
        registerFactoryUpgrades(FactoryType.INFUSING, itemInOutMachineUpgrades)
        registerFactoryUpgrades(FactoryType.PURIFYING, itemInOutMachineUpgrades)
        registerFactoryUpgrades(FactoryType.INJECTING, itemInOutMachineUpgrades)

        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.PRESSURIZED_REACTION_CHAMBER, *itemInOutMachineUpgrades)
        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.FORMULAIC_ASSEMBLICATOR, *itemInOutMachineUpgrades)
        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.NUTRITIONAL_LIQUIFIER, *itemInOutMachineUpgrades)
        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.PAINTING_MACHINE, *itemInOutMachineUpgrades)
        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.ANTIPROTONIC_NUCLEOSYNTHESIZER, *itemInOutMachineUpgrades)

        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.CHEMICAL_OXIDIZER, *itemInputMachineUpgrades)
        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.CHEMICAL_DISSOLUTION_CHAMBER, *itemInputMachineUpgrades)
        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.PIGMENT_EXTRACTOR, *itemInputMachineUpgrades)

        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.CHEMICAL_CRYSTALLIZER, *itemOutputMachineUpgrades)

        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.CHEMICAL_INFUSER, *machineUpgrades)
        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.CHEMICAL_WASHER, *machineUpgrades)
        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.ROTARY_CONDENSENTRATOR, *machineUpgrades)
        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.ELECTROLYTIC_SEPARATOR, *machineUpgrades)
        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.ISOTOPIC_CENTRIFUGE, *machineUpgrades)
        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.PIGMENT_MIXER, *machineUpgrades)

        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.QIO_IMPORTER, *qioUpgrades, MekEmpUpgrade.FAST_ITEM_INSERT)
        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.QIO_EXPORTER, *qioUpgrades, MekEmpUpgrade.FAST_ITEM_EJECT)

        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.DIGITAL_MINER, *speedAndEnergyUpgrades)
        AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.ELECTRIC_PUMP, *speedAndEnergyUpgrades)

        if (ModList.get().isLoaded("mekanism_extras")) {
            AdditionalUpgradeUtil.addSupported(ExtraBlockType.ADVANCED_ELECTRIC_PUMP, *speedAndEnergyUpgrades)
        }
    }

    private fun registerFactoryUpgrades(type: FactoryType, upgrades: Array<Upgrade>) {
        for (tier in EnumUtils.FACTORY_TIERS) {
            AdditionalUpgradeUtil.addSupported(MekanismBlockTypes.getFactory(tier, type), *upgrades)
        }

        if (ModList.get().isLoaded("mekanism_extras")) {
            AdditionalUpgradeUtil.addSupported(ExtraBlockType.getAdvancedFactory(AdvancedFactoryTier.ABSOLUTE, type), *upgrades)
            AdditionalUpgradeUtil.addSupported(ExtraBlockType.getAdvancedFactory(AdvancedFactoryTier.SUPREME, type), *upgrades)
            AdditionalUpgradeUtil.addSupported(ExtraBlockType.getAdvancedFactory(AdvancedFactoryTier.COSMIC, type), *upgrades)
            AdditionalUpgradeUtil.addSupported(ExtraBlockType.getAdvancedFactory(AdvancedFactoryTier.INFINITE, type), *upgrades)
        }
    }
}

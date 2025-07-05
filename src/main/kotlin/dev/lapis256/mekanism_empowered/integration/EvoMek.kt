package dev.lapis256.mekanism_empowered.integration

import dev.lapis256.mekanism_empowered.common.init.MekEmpUpgrades.ITEM_IN_OUT_MACHINE_UPGRADES
import dev.lapis256.mekanism_empowered.common.init.MekEmpUpgrades.registerFactoryUpgrades
import dev.lapis256.mekanism_empowered.core.common.util.AdditionalUpgradeUtil
import fr.iglee42.evolvedmekanism.registries.EMBlockTypes
import fr.iglee42.evolvedmekanism.registries.EMFactoryType


object EvoMek : IIntegration {
    override val modId = "evolvedmekanism"

    override fun initCommon() {
        registerFactoryUpgrades(EMFactoryType.ALLOYING, ITEM_IN_OUT_MACHINE_UPGRADES)

        AdditionalUpgradeUtil.addSupported(EMBlockTypes.ALLOYER, *ITEM_IN_OUT_MACHINE_UPGRADES)
        AdditionalUpgradeUtil.addSupported(EMBlockTypes.CHEMIXER, *ITEM_IN_OUT_MACHINE_UPGRADES)
    }

    override fun initClient() {
    }
}

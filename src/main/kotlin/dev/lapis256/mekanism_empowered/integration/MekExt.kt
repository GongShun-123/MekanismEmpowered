package dev.lapis256.mekanism_empowered.integration

import com.jerry.mekanism_extras.common.registry.ExtraBlockType
import dev.lapis256.mekanism_empowered.common.init.MekEmpUpgrades.SPEED_AND_ENERGY_UPGRADES
import dev.lapis256.mekanism_empowered.core.common.util.AdditionalUpgradeUtil


object MekExt : IIntegration {
    override val modId = "mekanism_extras"

    override fun initCommon() {
        AdditionalUpgradeUtil.addSupported(ExtraBlockType.ADVANCED_ELECTRIC_PUMP, *SPEED_AND_ENERGY_UPGRADES)
    }

    override fun initClient() {
    }
}

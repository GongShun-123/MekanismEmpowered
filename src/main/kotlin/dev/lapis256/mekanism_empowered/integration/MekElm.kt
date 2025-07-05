package dev.lapis256.mekanism_empowered.integration

import com.fxd927.mekanismelements.common.registries.MSBlockTypes
import dev.lapis256.mekanism_empowered.common.init.MekEmpUpgrades.ITEM_INPUT_MACHINE_UPGRADES
import dev.lapis256.mekanism_empowered.common.init.MekEmpUpgrades.MACHINE_UPGRADES
import dev.lapis256.mekanism_empowered.core.common.util.AdditionalUpgradeUtil


object MekElm : IIntegration {
    override val modId = "mekanismelements"

    override fun initCommon() {
        AdditionalUpgradeUtil.addSupported(MSBlockTypes.ADSORPTION_SEPARATOR, *MACHINE_UPGRADES)
        AdditionalUpgradeUtil.addSupported(MSBlockTypes.RADIATION_IRRADIATOR, *ITEM_INPUT_MACHINE_UPGRADES)
        AdditionalUpgradeUtil.addSupported(MSBlockTypes.SEAWATER_PUMP, *MACHINE_UPGRADES)
    }

    override fun initClient() {
    }
}

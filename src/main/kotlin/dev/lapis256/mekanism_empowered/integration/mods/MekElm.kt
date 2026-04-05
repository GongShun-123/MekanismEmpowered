package dev.lapis256.mekanism_empowered.integration.mods

import com.fxd927.mekanismelements.common.registries.MSBlockTypes
import dev.lapis256.mekanism_empowered.common.init.MekEmpUpgrades.ITEM_INPUT_MACHINE_UPGRADES
import dev.lapis256.mekanism_empowered.common.init.MekEmpUpgrades.MACHINE_UPGRADES
import dev.lapis256.mekanism_empowered.core.common.util.AdditionalUpgradeUtil
import dev.lapis256.mekanism_empowered.integration.ModIntegration
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent


internal object MekElm : ModIntegration {
    override val modId = "mekanismelements"

    override fun initCommon(modEventBus: IEventBus) {
        modEventBus.addListener { _: FMLCommonSetupEvent ->
            AdditionalUpgradeUtil.addSupported(MSBlockTypes.ADSORPTION_SEPARATOR, *MACHINE_UPGRADES)
            AdditionalUpgradeUtil.addSupported(MSBlockTypes.RADIATION_IRRADIATOR, *ITEM_INPUT_MACHINE_UPGRADES)
            AdditionalUpgradeUtil.addSupported(MSBlockTypes.SEAWATER_PUMP, *MACHINE_UPGRADES)
        }
    }
}

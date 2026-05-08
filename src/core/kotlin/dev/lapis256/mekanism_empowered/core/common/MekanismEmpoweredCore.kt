package dev.lapis256.mekanism_empowered.core.common

import dev.lapis256.mekanism_empowered.core.api.MekanismEmpoweredCoreAPI
import dev.lapis256.mekanism_empowered.core.common.init.GlobalLootModifierSerializers
import dev.lapis256.mekanism_empowered.core.common.init.LootConditionTypes
import dev.lapis256.mekanism_empowered.core.common.util.AdditionalUpgradeUtil
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory


@Mod(MekanismEmpoweredCoreAPI.MOD_ID)
class MekanismEmpoweredCore(context: FMLJavaModLoadingContext) {
    companion object {
        @JvmField
        val LOGGER: Logger = LoggerFactory.getLogger(MekanismEmpoweredCoreAPI.MOD_ID)
    }

    init {
        val modEventBus = context.modEventBus

        GlobalLootModifierSerializers.REGISTRY.register(modEventBus)
        LootConditionTypes.REGISTRY.register(modEventBus)

        modEventBus.addListener<FMLCommonSetupEvent> {
            AdditionalUpgradeUtil.applyDeferredSupportedUpgrades()
        }
    }
}

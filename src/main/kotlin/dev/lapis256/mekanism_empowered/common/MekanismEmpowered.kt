package dev.lapis256.mekanism_empowered.common

import dev.lapis256.mekanism_empowered.api.MekanismEmpoweredAPI
import dev.lapis256.mekanism_empowered.client.MekanismEmpoweredClient
import dev.lapis256.mekanism_empowered.common.config.MekEmpConfig
import dev.lapis256.mekanism_empowered.common.init.MekEmpCreativeTab
import dev.lapis256.mekanism_empowered.common.init.MekEmpItems
import dev.lapis256.mekanism_empowered.common.init.MekEmpUpgrades
import dev.lapis256.mekanism_empowered.common.network.MekEmpPacketHandler
import dev.lapis256.mekanism_empowered.integration.Integrations
import mekanism.common.lib.Version
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory


@Mod(MekanismEmpoweredAPI.MOD_ID)
class MekanismEmpowered(context: FMLJavaModLoadingContext) {
    init {
        MekEmpConfig.registerConfigs(context)

        MekEmpUpgrades.registerUpgradeInfo()

        val modEventBus = context.modEventBus
        modEventBus.addListener { _: FMLCommonSetupEvent -> MekEmpUpgrades.registerSupportedUpgrades() }


        modEventBus.addListener(MekEmpConfig::onConfigLoad)

        MekEmpItems.REGISTRY.register(modEventBus)
        MekEmpCreativeTab.REGISTRY.register(modEventBus)

        Integrations.initCommon(modEventBus)

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT) {
            Runnable {
                MekanismEmpoweredClient.init(modEventBus)
            }
        }

        instance = this
    }

    val versionNumber = Version(context.container)
    val packetHandler = MekEmpPacketHandler(versionNumber).apply { initialize() }

    companion object {
        @JvmField
        val LOGGER: Logger = LoggerFactory.getLogger(MekanismEmpoweredAPI.MOD_ID)

        lateinit var instance: MekanismEmpowered
            private set

        val packetHandler get() = instance.packetHandler
    }
}

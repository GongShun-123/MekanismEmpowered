package dev.lapis256.mekanism_empowered.client

import dev.lapis256.mekanism_empowered.integration.Integrations
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent


class MekanismEmpoweredClient() {
    init {
        Integrations.initClient()
    }

    private fun clientSetup(event: FMLClientSetupEvent) {
    }
}

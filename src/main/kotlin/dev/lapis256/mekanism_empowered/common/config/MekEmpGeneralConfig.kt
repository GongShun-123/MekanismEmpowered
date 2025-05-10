package dev.lapis256.mekanism_empowered.common.config

import dev.lapis256.easy_nest_config.api.Comment
import dev.lapis256.easy_nest_config.api.NestConfig
import dev.lapis256.mekanism_empowered.core.common.config.MekanismNestConfig
import net.minecraftforge.fml.config.ModConfig


object MekEmpGeneralConfig : MekanismNestConfig(ModConfig.Type.SERVER, "general") {
    val maxUpgradeMultiplier by builder.comment("Base factor for working out machine performance with upgrades - UpgradeModifier * (UpgradesInstalled/UpgradesPossible).")
        .defineInRange("maxUpgradeMultiplier", 20, 1, Int.MAX_VALUE).cached()

    @NestConfig
    @Comment("Configurable Auto Inserter")
    object AutoInserter {
        val itemRate by builder.comment("Configurable item rate for Auto Inserter")
            .defineInRange("itemRate", 8, 1, 64).cached()
        val chemicalRate by builder.comment("Configurable chemical rate for Auto Inserter")
            .defineInRange("chemicalRate", 128L, 1L, Long.MAX_VALUE).cached()
        val fluidRate by builder.comment("Configurable fluid rate for Auto Inserter")
            .defineInRange("fluidRate", 128, 1, Int.MAX_VALUE).cached()
        val energyRate by builder.comment("Configurable energy rate for Auto Inserter")
            .defineInRange("energyRate", 128L, 1L, Long.MAX_VALUE).cached()
    }
}

package dev.lapis256.mekanism_empowered.common.config

import dev.lapis256.easy_nest_config.api.NestConfig
import dev.lapis256.mekanism_empowered.core.common.config.MekanismNestConfig
import net.neoforged.fml.config.ModConfig


object GeneralConfig : MekanismNestConfig(ModConfig.Type.SERVER, "general") {
    val maxUpgradeMultiplier by MekEmpConfigTranslations.GENERAL_UPGRADE_MULTIPLIER.applyToBuilder(builder)
        .defineInRange("maxUpgradeMultiplier", 20, 1, Int.MAX_VALUE).cached()

    @NestConfig
    @ConfigTranslation(MekEmpConfigTranslations.GENERAL_AUTO_INSERTER)
    object AutoInsert {
        val itemRate by MekEmpConfigTranslations.GENERAL_AUTO_INSERTER_ITEM_RATE.applyToBuilder(builder)
            .defineInRange("itemRate", 8, 1, 64).cached()
        val chemicalRate by MekEmpConfigTranslations.GENERAL_AUTO_INSERTER_CHEMICAL_RATE.applyToBuilder(builder)
            .defineInRange("chemicalRate", 128L, 1L, Long.MAX_VALUE).cached()
        val fluidRate by MekEmpConfigTranslations.GENERAL_AUTO_INSERTER_FLUID_RATE.applyToBuilder(builder)
            .defineInRange("fluidRate", 128, 1, Int.MAX_VALUE).cached()
        val energyRate by MekEmpConfigTranslations.GENERAL_AUTO_INSERTER_ENERGY_RATE.applyToBuilder(builder)
            .defineInRange("energyRate", 128L, 1L, Long.MAX_VALUE).cached()
    }

    override fun getTranslation() = "General Config"
}

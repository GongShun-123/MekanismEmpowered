package dev.lapis256.mekanism_empowered.common.config

import dev.lapis256.mekanism_empowered.api.MekanismEmpoweredAPI
import mekanism.common.config.IConfigTranslation
import net.minecraft.Util


enum class MekEmpConfigTranslations(path: String, private val title: String, private val tooltip: String, private val button: String?) : IConfigTranslation {
    GENERAL_UPGRADE_MULTIPLIER("general.misc.upgrade_multiplier", "Max Upgrade Multiplier",
          "Base factor for working out machine performance with upgrades - UpgradeModifier * (UpgradesInstalled/UpgradesPossible)."),

    GENERAL_AUTO_INSERTER("general.auto_inserter", "Auto Inserter", "Configurable Auto Inserter"),
    GENERAL_AUTO_INSERTER_ITEM_RATE("general.auto_inserter.item_rate", "Item Rate", "Configurable item rate for Auto Inserter"),
    GENERAL_AUTO_INSERTER_CHEMICAL_RATE("general.auto_inserter.chemical_rate", "Chemical Rate", "Configurable chemical rate for Auto Inserter"),
    GENERAL_AUTO_INSERTER_FLUID_RATE("general.auto_inserter.fluid_rate", "Fluid Rate", "Configurable fluid rate for Auto Inserter"),
    GENERAL_AUTO_INSERTER_ENERGY_RATE("general.auto_inserter.energy_rate", "Energy Rate", "Configurable energy rate for Auto Inserter"),
    ;

    constructor(path: String, title: String, tooltip: String, isSection: Boolean = false) :
        this(path, title, tooltip, IConfigTranslation.getSectionTitle(title, isSection))

    private val key = Util.makeDescriptionId("configuration", MekanismEmpoweredAPI.rl(path))

    override fun title() = title
    override fun tooltip() = tooltip
    override fun button() = button
    override fun getTranslationKey(): String = key
}

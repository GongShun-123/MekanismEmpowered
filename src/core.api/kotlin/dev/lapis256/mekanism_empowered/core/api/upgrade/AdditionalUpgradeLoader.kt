package dev.lapis256.mekanism_empowered.core.api.upgrade

import dev.lapis256.mekanism_empowered.core.api.enum_extension.EnumExtensionLoader
import mekanism.api.Upgrade
import mekanism.api.text.EnumColor
import mekanism.api.text.ILangEntry


class AdditionalUpgradeLoader(
    val constructor: (String, Int, String, ILangEntry, ILangEntry, Int, EnumColor) -> Upgrade,
    val added: (Int) -> Unit
) : EnumExtensionLoader<Upgrade, AdditionalUpgradeDelegate>() {
    override fun initEnumExtensions() = loadEnumExtensions(IAdditionalUpgrades::class)

    override fun constructEntry(ordinal: Int, property: AdditionalUpgradeDelegate) = constructor(
        property.internalName, ordinal.also(added), property.name, property.langKey, property.descLangKey, property.maxStack, property.color
    )
}

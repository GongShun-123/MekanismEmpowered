package dev.lapis256.mekanism_empowered.core.mixin_impl

import dev.lapis256.mekanism_empowered.core.api.upgrade.AdditionalUpgradeLoader
import dev.lapis256.mekanism_empowered.core.common.MekanismEmpoweredCore
import dev.lapis256.mekanism_empowered.core.mixin.common.InvokeAPILang
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import mekanism.api.Upgrade
import mekanism.api.text.APILang
import mekanism.api.text.EnumColor
import mekanism.api.text.ILangEntry
import mekanism.common.util.EnumUtils
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import java.util.*


class MixinImplUpgrade(val constructor: (String, Int, String, APILang, APILang, Int, EnumColor) -> Upgrade) {
    private val additionalOrdinals = IntOpenHashSet()

    private val upgradeMap by lazy { EnumUtils.UPGRADES.associateBy(Upgrade::getRawName) }

    private fun createDummy(key: ILangEntry) = InvokeAPILang.createDummy("MEKANISM_EMPOWERED_CORE_DUMMY_API_LANG", 999, key.translationKey)

    private fun altConstructor(
        internalName: String,
        ordinal: Int,
        name: String,
        langKey: ILangEntry,
        descLangKey: ILangEntry,
        maxStack: Int,
        color: EnumColor
    ) =
        constructor(internalName, ordinal, name, createDummy(langKey), createDummy(descLangKey), maxStack, color)

    private val loader = AdditionalUpgradeLoader(::altConstructor, additionalOrdinals::add)

    fun initAdditionalUpgrades(builtInUpgrades: Array<Upgrade>) = loader.initAdditionalEnumEntry(builtInUpgrades)

    fun buildAdditionalMap(upgrades: MutableMap<Upgrade, Int>?, nbtTags: CompoundTag?): MutableMap<Upgrade, Int>? {
        nbtTags ?: return null

        if (!nbtTags.contains(MekanismEmpoweredCore.SerializationConstants.UPGRADES, Tag.TAG_COMPOUND.toInt())) {
            return null
        }

        val upgrades = upgrades ?: EnumMap(Upgrade::class.java)
        val compound = nbtTags.getCompound(MekanismEmpoweredCore.SerializationConstants.UPGRADES)

        for (entry in compound.allKeys) {
            val upgrade = upgradeMap[entry] ?: continue
            val amount = compound.getInt(entry)
            if (amount > 0) {
                upgrades[upgrade] = amount
            }
        }

        return upgrades
    }

    fun filterUpgrades(original: Set<Map.Entry<Upgrade, Int>>): Set<Map.Entry<Upgrade, Int>> {
        return original
            .filter { e -> !additionalOrdinals.contains(e.key.ordinal) }
            .toSet()
    }

    fun saveAdditionalMap(upgrades: Map<Upgrade, Int>, nbtTags: CompoundTag) {
        val additionalUpgrades = upgrades
            .filter { e -> additionalOrdinals.contains(e.key.ordinal) }

        nbtTags.put(MekanismEmpoweredCore.SerializationConstants.UPGRADES, CompoundTag().apply {
            for ((upgrade, amount) in additionalUpgrades) {
                putInt(upgrade.rawName, amount)
            }
        })
    }
}

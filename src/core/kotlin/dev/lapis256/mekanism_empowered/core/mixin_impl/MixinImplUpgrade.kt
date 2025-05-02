package dev.lapis256.mekanism_empowered.core.mixin_impl

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.UnboundedMapCodec
import dev.lapis256.mekanism_empowered.core.common.MekanismEmpoweredCore
import dev.lapis256.mekanism_empowered.core.api.upgrade.AdditionalUpgradeLoader
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import mekanism.api.Upgrade
import mekanism.api.text.EnumColor
import mekanism.api.text.ILangEntry
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.util.ExtraCodecs
import java.util.*


class MixinImplUpgrade(constructor: (String, Int, String, ILangEntry, ILangEntry, Int, EnumColor) -> Upgrade) {
    private val additionalOrdinals = IntOpenHashSet()

    private val loader = AdditionalUpgradeLoader(constructor, additionalOrdinals::add)

    var codec: Codec<Upgrade>? = null

    private val additionalCodec: UnboundedMapCodec<Upgrade, Int> by lazy {
        codec ?: error("Codec not initialized")
        Codec.unboundedMap(codec, ExtraCodecs.POSITIVE_INT)
    }

    fun initAdditionalUpgrades(builtInUpgrades: Array<Upgrade>) = loader.initAdditionalEnumEntry(builtInUpgrades)

    fun buildAdditionalMap(upgrades: MutableMap<Upgrade, Int>?, nbtTags: CompoundTag?): MutableMap<Upgrade, Int>? {
        nbtTags ?: return null

        if (!nbtTags.contains(MekanismEmpoweredCore.SerializationConstants.UPGRADES, Tag.TAG_COMPOUND.toInt())) {
            return null
        }

        val upgrades = upgrades ?: EnumMap(Upgrade::class.java)

        additionalCodec.parse(
            NbtOps.INSTANCE,
            nbtTags.getCompound(MekanismEmpoweredCore.SerializationConstants.UPGRADES)
        )
            .ifSuccess(upgrades::putAll)
            .ifError { e -> MekanismEmpoweredCore.LOGGER.error("Failed to parse additional upgrades: {}", e) }

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

        additionalCodec.encodeStart(NbtOps.INSTANCE, additionalUpgrades)
            .ifSuccess { tag -> nbtTags.put(MekanismEmpoweredCore.SerializationConstants.UPGRADES, tag) }
            .ifError { e -> MekanismEmpoweredCore.LOGGER.error("Failed to save additional upgrades: {}", e) }
    }
}

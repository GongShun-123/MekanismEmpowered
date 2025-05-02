package dev.lapis256.mekanism_empowered.data.provider

import dev.lapis256.mekanism_empowered.core.extension.processedTag
import dev.lapis256.mekanism_empowered.api.MekanismEmpoweredAPI
import dev.lapis256.mekanism_empowered.common.init.MekEmpItems
import mekanism.common.registries.MekanismBlocks
import mekanism.common.registries.MekanismItems
import mekanism.common.resource.PrimaryResource
import mekanism.common.resource.ResourceType
import mekanism.common.tags.MekanismTags
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.DeferredHolder
import java.util.concurrent.CompletableFuture


class MekEmpRecipeProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : RecipeProvider(output, lookupProvider) {
    override fun buildRecipes(output: RecipeOutput) {
        advancedUpgrade(output, MekEmpItems.EMPOWERED_SPEED, PrimaryResource.OSMIUM.processedTag(ResourceType.DUST))
        advancedUpgrade(output, MekEmpItems.EMPOWERED_ENERGY,  PrimaryResource.GOLD.processedTag(ResourceType.DUST))
        advancedUpgrade(output, MekEmpItems.FAST_ITEM_EJECT,  Blocks.PISTON)
        advancedUpgrade(output, MekEmpItems.FAST_ITEM_INSERT,  Blocks.STICKY_PISTON)

        upgrade(output, MekEmpItems.AUTO_INSERTER, Blocks.STICKY_PISTON)
        upgrade(output, MekEmpItems.IO_CAPACITY,  MekanismTags.Items.DUSTS_LITHIUM)
    }

    private fun upgrade(upgrade: DeferredHolder<Item, out Item>): ShapedRecipeBuilder {
        return ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, upgrade.get())
            .pattern(" g ")
            .pattern("aca")
            .pattern(" g ")
            .define('g', Tags.Items.GLASS_BLOCKS)
            .define('a', MekanismTags.Items.ALLOYS_ADVANCED)
    }

    private fun upgrade(output: RecipeOutput, upgrade: DeferredHolder<Item, out Item>, core: ItemLike) {
        build(
            output,
            upgrade(upgrade)
                .define('c', core)
                .unlockedBy("has_core_item", has(core)),
            upgrade.id
        )
    }

    private fun upgrade(output: RecipeOutput, upgrade: DeferredHolder<Item, out Item>, core: TagKey<Item>) {
        build(
            output,
            upgrade(upgrade)
                .define('c', core)
                .unlockedBy("has_core_item", has(core)),
            upgrade.id
        )
    }

    private fun advancedUpgrade(upgrade: DeferredHolder<Item, out Item>): ShapedRecipeBuilder {
        return ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, upgrade.get())
            .pattern("hgh")
            .pattern("aca")
            .pattern("hgh")
            .define('h', MekanismItems.HDPE_SHEET)
            .define('g', MekanismBlocks.STRUCTURAL_GLASS)
            .define('a', MekanismTags.Items.ALLOYS_ATOMIC)
    }

    private fun advancedUpgrade(output: RecipeOutput, upgrade: DeferredHolder<Item, out Item>, core: TagKey<Item>) {
        build(
            output,
            advancedUpgrade(upgrade)
                .define('c', core)
                .unlockedBy("has_core_item", has(core)),
            upgrade.id
        )
    }

    private fun advancedUpgrade(output: RecipeOutput, upgrade: DeferredHolder<Item, out Item>, core: ItemLike) {
        build(
            output,
            advancedUpgrade(upgrade)
                .define('c', core)
                .unlockedBy("has_core_item", has(core)),
            upgrade.id
        )
    }

    private fun build(output: RecipeOutput, builder: RecipeBuilder, id: ResourceLocation) {
        builder.save(output, MekanismEmpoweredAPI.rl(id.path))
    }
}

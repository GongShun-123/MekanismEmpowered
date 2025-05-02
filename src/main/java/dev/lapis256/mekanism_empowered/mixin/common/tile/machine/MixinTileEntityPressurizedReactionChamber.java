package dev.lapis256.mekanism_empowered.mixin.common.tile.machine;

import dev.lapis256.mekanism_empowered.mixin_impl.MixinImplTileMachine;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.tile.machine.TileEntityPressurizedReactionChamber;
import mekanism.common.tile.prefab.TileEntityProgressMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = TileEntityPressurizedReactionChamber.class, remap = false)
public abstract class MixinTileEntityPressurizedReactionChamber extends TileEntityProgressMachine<MekanismRecipe<?>> {
    protected MixinTileEntityPressurizedReactionChamber(Holder<Block> blockProvider, BlockPos pos, BlockState state, List<CachedRecipe.OperationTracker.RecipeError> errorTypes, int baseTicksRequired) {
        super(blockProvider, pos, state, errorTypes, baseTicksRequired);
    }

    @Inject(method = "onCachedRecipeChanged", at = @At(value = "INVOKE", target = "Lmekanism/common/tile/machine/TileEntityPressurizedReactionChamber;recalculateUpgrades(Lmekanism/api/Upgrade;)V"))
    private void mekanismEmpowered$recalculateAdditionalUpgrades(CallbackInfo ci) {
        MixinImplTileMachine.prcRecalculateAdditionalUpgrades(this);
    }
}

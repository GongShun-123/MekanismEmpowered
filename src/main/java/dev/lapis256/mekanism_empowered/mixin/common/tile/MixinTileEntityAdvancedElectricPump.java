package dev.lapis256.mekanism_empowered.mixin.common.tile;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.lapis256.mekanism_empowered.mixin_impl.MixinImplTileEntityElectricPump;
import mekanism.common.tile.interfaces.IUpgradeTile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;


@Pseudo
@Mixin(targets = "com.jerry.mekanism_extras.common.tile.machine.TileEntityAdvancedElectricPump", remap = false)
public abstract class MixinTileEntityAdvancedElectricPump implements IUpgradeTile {

    @ModifyArg(method = "onUpdateServer", at = @At(value = "INVOKE", target = "Lmekanism/common/util/FluidUtils;emit(Ljava/util/Set;Lmekanism/api/fluid/IExtendedFluidTank;Lnet/minecraft/world/level/block/entity/BlockEntity;I)V"))
    private int mekanismEmpowered$modifyOutputRate(int original) {
        return MixinImplTileEntityElectricPump.modifyOutputRate(this, original);
    }

    @ModifyArg(method = "getOutput", at = @At(value = "INVOKE", target = "Lmekanism/common/registration/impl/FluidRegistryObject;getFluidStack(I)Lnet/minecraftforge/fluids/FluidStack;"))
    private int mekanismEmpowered$modifyHeavyWaterOutputAmount(int original) {
        return MixinImplTileEntityElectricPump.modifyWaterOutputAmount(this, original);
    }

    @ModifyExpressionValue(method = "getOutput", at = @At(value = "CONSTANT", args = "intValue=100000"))
    private int mekanismEmpowered$modifyWaterOutputAmount(int original) {
        return MixinImplTileEntityElectricPump.modifyWaterOutputAmount(this, original);
    }
}

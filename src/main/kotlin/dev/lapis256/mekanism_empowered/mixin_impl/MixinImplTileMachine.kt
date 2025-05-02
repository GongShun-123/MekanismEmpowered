package dev.lapis256.mekanism_empowered.mixin_impl

import dev.lapis256.mekanism_empowered.api.MekEmpUpgrade
import dev.lapis256.mekanism_empowered.core.extension.getInstalled
import dev.lapis256.mekanism_empowered.core.extension.isSpeedMaxed
import mekanism.api.recipes.MekanismRecipe
import mekanism.common.tile.base.TileEntityMekanism
import mekanism.common.tile.prefab.TileEntityProgressMachine
import kotlin.math.pow


object MixinImplTileMachine {
    @JvmStatic
    fun TileEntityProgressMachine<MekanismRecipe<*>>.prcRecalculateAdditionalUpgrades() {
        recalculateUpgrades(MekEmpUpgrade.EMPOWERED_SPEED)
    }

    @JvmStatic
    fun TileEntityMekanism.modifyRecalculationBaselineMaxOperations(original: Double): Double {
        if (!isSpeedMaxed()) {
            return original
        }
        val speed = getInstalled(MekEmpUpgrade.EMPOWERED_SPEED) ?: return original
        return original + 2 * 2.0.pow(speed.toDouble())
    }
}

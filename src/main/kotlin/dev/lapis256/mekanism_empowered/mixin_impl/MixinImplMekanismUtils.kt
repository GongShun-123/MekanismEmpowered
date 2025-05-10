package dev.lapis256.mekanism_empowered.mixin_impl

import dev.lapis256.mekanism_empowered.api.MekEmpUpgrade
import dev.lapis256.mekanism_empowered.common.config.MekEmpGeneralConfig
import dev.lapis256.mekanism_empowered.core.extension.fractionUpgrades
import dev.lapis256.mekanism_empowered.core.extension.getInstalledOrDefault
import dev.lapis256.mekanism_empowered.core.extension.isEnergyMaxed
import dev.lapis256.mekanism_empowered.core.extension.isSpeedMaxed
import dev.lapis256.mekanism_empowered.core.extension.times
import mekanism.api.Upgrade
import mekanism.api.math.FloatingLong
import mekanism.common.tile.interfaces.IUpgradeTile
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow


object MixinImplMekanismUtils {
    private val multiplier: Double
        get() = MekEmpGeneralConfig.maxUpgradeMultiplier.toDouble()

    @JvmStatic
    fun IUpgradeTile.modifyTicks(original: Double): Double {
        if (!isSpeedMaxed()) {
            return original
        }
        return original * multiplier.pow(-fractionUpgrades(MekEmpUpgrade.EMPOWERED_SPEED))
    }

    @JvmStatic
    fun IUpgradeTile.modifyEnergyPerTick(original: FloatingLong): FloatingLong {
        val speed = getInstalledOrDefault(MekEmpUpgrade.EMPOWERED_SPEED)
        if (!isSpeedMaxed() || speed <= 0) {
            return original
        }
        val energy = getInstalledOrDefault(MekEmpUpgrade.EMPOWERED_ENERGY)
        return original * multiplier.pow((2 * speed - min(energy, max(speed, 8))) / 8.toDouble())
    }

    private fun modifyEnergy(original: FloatingLong, fraction: Double) = original * multiplier.pow(2 * fraction)

    @JvmStatic
    fun IUpgradeTile.modifyTileMaxEnergy(original: FloatingLong): FloatingLong {
        if (!isEnergyMaxed()) {
            return original
        }
        return modifyEnergy(original, fractionUpgrades(MekEmpUpgrade.EMPOWERED_ENERGY))
    }

    @JvmStatic
    fun modifyItemStackMaxEnergy(original: FloatingLong, energyInstalled: Double, upgrades: Map<Upgrade, Int>?): FloatingLong {
        upgrades ?: return original
        if (energyInstalled < Upgrade.ENERGY.max) {
            return original
        }
        val installed = upgrades[MekEmpUpgrade.EMPOWERED_ENERGY] ?: return original
        return modifyEnergy(original, installed / Upgrade.ENERGY.max.toDouble())
    }
}

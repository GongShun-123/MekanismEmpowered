package dev.lapis256.mekanism_empowered.common.item

import mekanism.api.functions.ConstantPredicates
import mekanism.api.text.TextComponentUtil
import mekanism.api.tier.BaseTier
import mekanism.common.attachments.containers.chemical.AttachedChemicals
import mekanism.common.attachments.containers.chemical.ChemicalTanksBuilder
import mekanism.common.attachments.containers.chemical.ComponentBackedChemicalTank
import mekanism.common.attachments.containers.creator.BaseContainerCreator
import mekanism.common.attachments.containers.creator.IBasicContainerCreator
import mekanism.common.attachments.containers.fluid.AttachedFluids
import mekanism.common.attachments.containers.fluid.ComponentBackedFluidTank
import mekanism.common.attachments.containers.fluid.FluidTanksBuilder
import mekanism.common.item.ItemGaugeDropper
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import java.util.function.IntSupplier
import java.util.function.LongSupplier


class ItemTieredGaugeDropper(val tier: BaseTier, properties: Properties) : ItemGaugeDropper(properties) {
    companion object {
        fun getChemicalTankCreator(rete: LongSupplier, capacity: LongSupplier): BaseContainerCreator<AttachedChemicals, ComponentBackedChemicalTank> =
            ChemicalTanksBuilder.builder().addTank(IBasicContainerCreator { type, attachedTo, containerIndex ->
                ComponentBackedChemicalTank(
                    attachedTo,
                    containerIndex,
                    ConstantPredicates.alwaysTrueBi(),
                    ConstantPredicates.alwaysTrueBi(),
                    ConstantPredicates.alwaysTrue(),
                    rete,
                    capacity,
                    null
                )
            }).build()

        fun getFluidTankCreator(rete: IntSupplier, capacity: IntSupplier): BaseContainerCreator<AttachedFluids, ComponentBackedFluidTank> =
            FluidTanksBuilder.builder().addTank(IBasicContainerCreator { type, attachedTo, containerIndex ->
                ComponentBackedFluidTank(
                    attachedTo,
                    containerIndex,
                    ConstantPredicates.alwaysTrueBi(),
                    ConstantPredicates.alwaysTrueBi(),
                    ConstantPredicates.alwaysTrue(),
                    rete,
                    capacity
                )
            }).build()
    }

    override fun getName(stack: ItemStack): Component = TextComponentUtil.build(tier.color, super.getName(stack))
}

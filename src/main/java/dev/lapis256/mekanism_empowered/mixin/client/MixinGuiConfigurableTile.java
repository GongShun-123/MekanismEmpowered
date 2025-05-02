package dev.lapis256.mekanism_empowered.mixin.client;

import dev.lapis256.mekanism_empowered.api.MekEmpUpgrade;
import dev.lapis256.mekanism_empowered.client.gui.element.tab.window.GuiSideInserterConfigurationTab;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.ISideConfiguration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = GuiConfigurableTile.class, remap = false)
public abstract class MixinGuiConfigurableTile<TILE extends TileEntityMekanism & ISideConfiguration, CONTAINER extends MekanismTileContainer<TILE>> extends GuiMekanismTile<TILE, CONTAINER> {
    protected MixinGuiConfigurableTile(CONTAINER container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Unique
    private GuiSideInserterConfigurationTab<TILE> mekanismEmpowered$sideConfigTab;

    @Inject(method = "addGuiElements", at = @At("TAIL"))
    private void mekanismEmpowered$addSideConfigTab(CallbackInfo ci) {
        if (tile.supportsUpgrades() && tile.getComponent().isUpgradeInstalled(MekEmpUpgrade.getAUTO_INSERTER())) {
            mekanismEmpowered$sideConfigTab = addRenderableWidget(new GuiSideInserterConfigurationTab<>(this, tile, () -> mekanismEmpowered$sideConfigTab));
        }
    }
}

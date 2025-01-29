package org.eu.pnxlr.lithonate.mixins.core.gui;

import com.mojang.datafixers.util.Pair;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.widgets.WidgetConfigOption;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptions;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptionsBase;
import org.eu.pnxlr.lithonate.gui.LithonateConfigGui;
import org.eu.pnxlr.lithonate.gui.LithonateOptionLabel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Mixin(WidgetListConfigOptions.class)
public abstract class WidgetListConfigOptionsMixin extends WidgetListConfigOptionsBase<GuiConfigsBase.ConfigOptionWrapper, WidgetConfigOption> {
    @Shadow(remap = false)
    @Final
    protected GuiConfigsBase parent;

    public WidgetListConfigOptionsMixin(int x, int y, int width, int height, int configWidth) {
        super(x, y, width, height, configWidth);
    }

    @ModifyArg(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/malilib/gui/widgets/WidgetSearchBar;<init>(IIIIILfi/dy/masa/malilib/gui/interfaces/IGuiIcon;Lfi/dy/masa/malilib/gui/LeftRight;)V"
            ),
            index = 2,
            remap = false
    )
    private int lithonateSearchBarWidth(int width) {
        if (this.parent instanceof LithonateConfigGui) {
            // a default value.
            // a more precise width control wil be applied during the initGui of LithonateConfigGui
            width -= 150;
        }
        return width;
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL"),
            remap = false
    )
    private void lithonateRecordSearchBar(CallbackInfo ci) {
        if (this.parent instanceof LithonateConfigGui) {
            ((LithonateConfigGui) this.parent).setSearchBar(this.widgetSearchBar);
        }
    }

    @ModifyVariable(
            method = "getMaxNameLengthWrapped",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;iterator()Ljava/util/Iterator;",
                    remap = false
            ),
            remap = false
    )
    private int updateMaxNameLengthIfUsingLithonateOptionLabelAndShowsOriginalText$lithonate(int maxWidth, List<GuiConfigsBase.ConfigOptionWrapper> wrappers) {
        if (this.parent instanceof LithonateConfigGui) {
            for (GuiConfigsBase.ConfigOptionWrapper wrapper : wrappers) {
                if (wrapper.getType() == GuiConfigsBase.ConfigOptionWrapper.Type.CONFIG) {
                    IConfigBase config = Objects.requireNonNull(wrapper.getConfig());
                    maxWidth = Math.max(maxWidth, this.getStringWidth(config.getConfigGuiDisplayName()));
                    if (LithonateOptionLabel.willShowOriginalLines(new String[]{config.getConfigGuiDisplayName()}, new String[]{config.getName()})) {
                        maxWidth = Math.max(maxWidth, (int) (this.getStringWidth(config.getName()) * LithonateOptionLabel.TRANSLATION_SCALE));
                    }
                }
            }
        }
        return maxWidth;
    }

    @Inject(
            method = "reCreateListEntryWidgets",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/malilib/gui/widgets/WidgetListConfigOptionsBase;reCreateListEntryWidgets()V",
                    remap = false
            ),
            remap = false
    )
    private void adjustConfigAndOptionPanelWidth$lithonate(CallbackInfo ci) {
        if (this.parent instanceof LithonateConfigGui) {
            Pair<Integer, Integer> result = ((LithonateConfigGui) this.parent).adjustWidths(this.totalWidth, this.maxLabelWidth);
            this.maxLabelWidth = result.getFirst();
            this.configWidth = result.getSecond();
        }
    }
}

package org.eu.pnxlr.lithonate.mixins.core.gui;

import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptions;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import org.eu.pnxlr.lithonate.gui.LithonateConfigGui;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WidgetListBase.class)
public abstract class WidgetListBaseMixin<TYPE, WIDGET extends WidgetListEntryBase<TYPE>> {
    private boolean shouldRenderLithonateConfigGuiDropDownList = false;

    @Inject(method = "drawContents", at = @At("HEAD"), remap = false)
    private void drawLithonateConfigGuiDropDownListSetFlag$lithonate(CallbackInfo ci) {
        shouldRenderLithonateConfigGuiDropDownList = true;
    }

    @Inject(
            method = "drawContents",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/malilib/gui/widgets/WidgetBase;postRenderHovered(IIZLnet/minecraft/client/util/math/MatrixStack;)V",
                    remap = true
            ),
            remap = false
    )
    private void drawLithonateConfigGuiDropDownListAgainBeforeHover$lithonate(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        this.drawLithonateConfigGuiDropDownListAgain$lithonate(matrixStack, mouseX, mouseY);
    }

    @Inject(method = "drawContents", at = @At("TAIL"), remap = false)
    private void drawLithonateConfigGuiDropDownListAgainAfterHover$lithonate(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        this.drawLithonateConfigGuiDropDownListAgain$lithonate(matrixStack, mouseX, mouseY);
    }

    @SuppressWarnings("ConstantConditions")
    private boolean isLithonateConfigGui() {
        if ((WidgetListBase<?, ?>) (Object) this instanceof WidgetListConfigOptions) {
            GuiConfigsBase guiConfig = ((WidgetListConfigOptionsAccessor) this).getParent();
            return guiConfig instanceof LithonateConfigGui;
        }
        return false;
    }

    private void drawLithonateConfigGuiDropDownListAgain$lithonate(MatrixStack matrixStack, int mouseX, int mouseY) {
        if (this.isLithonateConfigGui() && this.shouldRenderLithonateConfigGuiDropDownList) {
            GuiConfigsBase guiConfig = ((WidgetListConfigOptionsAccessor) this).getParent();

            // render it again to make sure it's on the top but below hovering widgets
            ((LithonateConfigGui) guiConfig).renderHoveringWidgets(matrixStack, mouseX, mouseY);
            this.shouldRenderLithonateConfigGuiDropDownList = false;
        }
    }
}

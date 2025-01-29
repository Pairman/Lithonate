package org.eu.pnxlr.lithonate.mixins.core.gui;

import com.google.common.base.Joiner;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBase;
import org.eu.pnxlr.lithonate.config.LithonateConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ConfigBase.class)
public abstract class ConfigBaseMixin {
    @Inject(method = "getComment", at = @At("TAIL"), cancellable = true, remap = false)
    private void appendModRequirementHeader$lithonate(CallbackInfoReturnable<String> cir) {
        LithonateConfigs.getOptionFromConfig((IConfigBase) this).ifPresent(lithonateOption -> {
            List<String> footers = lithonateOption.getModRelationsFooter();
            if (!footers.isEmpty()) {
                cir.setReturnValue(cir.getReturnValue() + "\n" + Joiner.on("\n").join(footers));
            }
        });
    }
}

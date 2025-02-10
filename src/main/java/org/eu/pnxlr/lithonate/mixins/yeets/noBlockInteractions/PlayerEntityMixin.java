package org.eu.pnxlr.lithonate.mixins.yeets.noBlockInteractions;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import org.eu.pnxlr.lithonate.config.LithonateConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
@Environment(EnvType.CLIENT)
public class PlayerEntityMixin {

    @Inject(method = "shouldCancelInteraction()Z", at = @At("HEAD"), cancellable = true)
    public void onShouldCancelInteraction(CallbackInfoReturnable<Boolean> cir) {
        if (!LithonateConfigs.SETTING_ENABLE.getBooleanValue() ||
            !LithonateConfigs.YEET_NO_BLOCK_INTERACTIONS.getBooleanValue())
            return;
        cir.setReturnValue(true);
    }
}

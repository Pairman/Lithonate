package org.eu.pnxlr.lithonate.mixins.yeets.noClickingWitherRoses;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.WitherRoseBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.eu.pnxlr.lithonate.config.LithonateConfigs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(
        method = "attackBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z",
        at = @At("HEAD"), cancellable = true
    )
    private void onAttackBlock(BlockPos blockPos, Direction direction,
            CallbackInfoReturnable<Boolean> cir) {
        if (!LithonateConfigs.SETTING_ENABLE.getBooleanValue() ||
            !LithonateConfigs.YEET_NO_CLICKING_WITHER_ROSES.getBooleanValue())
            return;
        Block block = this.client.world.getBlockState(blockPos).getBlock();
        if (block instanceof WitherRoseBlock)
            cir.setReturnValue(false);
    }

    @Inject(
        method = "updateBlockBreakingProgress(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z",
        at = @At("HEAD"), cancellable = true
    )
    private void onUpdateBlockBreakingProgress(BlockPos blockPos, Direction direction,
            CallbackInfoReturnable<Boolean> cir) {
        if (!LithonateConfigs.SETTING_ENABLE.getBooleanValue() ||
            !LithonateConfigs.YEET_NO_CLICKING_WITHER_ROSES.getBooleanValue())
            return;
        Block block = this.client.world.getBlockState(blockPos).getBlock();
        if (block instanceof WitherRoseBlock)
            cir.setReturnValue(false);
    }

    @Inject(
        method = "interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;",
        at = @At("HEAD"), cancellable = true
    )
    private void onInteractBlock(ClientPlayerEntity player, ClientWorld world,
            Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (!LithonateConfigs.SETTING_ENABLE.getBooleanValue() ||
            !LithonateConfigs.YEET_NO_CLICKING_WITHER_ROSES.getBooleanValue())
            return;
        BlockPos blockPos = hitResult.getBlockPos();
        Block block = player.world.getBlockState(blockPos).getBlock();
        if (block instanceof WitherRoseBlock)
            cir.setReturnValue(ActionResult.FAIL);
    }
}

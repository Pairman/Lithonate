package org.eu.pnxlr.lithonate.mixins.tweaks.transparentBlockClicks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.Block;
import net.minecraft.block.CarpetBlock;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.eu.pnxlr.lithonate.config.LithonateConfigs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @ModifyVariable(
        method = "interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;",
        at = @At("HEAD"), ordinal = 0
    )
    private BlockHitResult onInteractBlock(BlockHitResult hitResult) {
        if (!LithonateConfigs.SETTING_ENABLE.getBooleanValue() ||
            !LithonateConfigs.TWEAK_TRANSPARENT_BLOCK_CLICKS.getBooleanValue() ||
            Screen.hasShiftDown())
            return hitResult;
        BlockPos pos = hitResult.getBlockPos();
        Block block = this.client.world.getBlockState(pos).getBlock();
        if (block instanceof AbstractRailBlock || block instanceof RedstoneWireBlock || block instanceof CarpetBlock ||
            (LithonateConfigs.TWEAK_TRANSPARENT_BLOCK_CLICKS_BANNER_SIGN.getBooleanValue() &&
                (block instanceof AbstractSignBlock || block instanceof AbstractBannerBlock))) {
            Direction targetSide = hitResult.getSide().getOpposite();
            BlockPos targetPos = pos.offset(targetSide);
            return new BlockHitResult(hitResult.getPos(), targetSide, targetPos, false);
        }
        return hitResult;
    }
}

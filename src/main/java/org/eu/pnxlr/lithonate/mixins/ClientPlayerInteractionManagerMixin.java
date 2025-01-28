package org.eu.pnxlr.lithonate.mixins;

import org.eu.pnxlr.lithonate.config.LithonateConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.Block;
import net.minecraft.block.CarpetBlock;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    private static final LithonateConfig CONFIGS = LithonateConfig.getInstance();

    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    private void onInteractBlock(ClientPlayerEntity player, ClientWorld world,
            Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (!CONFIGS.isEnabled() || !CONFIGS.isTransparentClicks() || Screen.hasShiftDown())
            return;
        BlockPos pos = hitResult.getBlockPos();
        Block block = world.getBlockState(pos).getBlock();

        boolean isTransparent = block instanceof AbstractRailBlock ||
                                block instanceof RedstoneWireBlock ||
                                block instanceof CarpetBlock || (
                                    CONFIGS.isTransparentClicksBannerSign() && (
                                        block instanceof AbstractSignBlock ||
                                        block instanceof AbstractBannerBlock
                                    )
                                );
        if (isTransparent) {
            Direction targetSide = hitResult.getSide().getOpposite();
            BlockPos targetPos = pos.offset(targetSide);
            BlockHitResult targetHitResult = new BlockHitResult(
                    hitResult.getPos(), targetSide, targetPos, false);
            cir.setReturnValue(((ClientPlayerInteractionManager) (Object) this)
                    .interactBlock(player, world, hand, targetHitResult));
        }
    }
}

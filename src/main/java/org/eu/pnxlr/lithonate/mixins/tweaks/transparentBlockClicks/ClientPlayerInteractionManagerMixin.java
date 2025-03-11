package org.eu.pnxlr.lithonate.mixins.tweaks.transparentBlockClicks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CarpetBlock;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.eu.pnxlr.lithonate.config.LithonateConfigs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
        Block block = this.client.world.getBlockState(hitResult.getBlockPos()).getBlock();
        if (block instanceof AbstractRailBlock || block instanceof RedstoneWireBlock || block instanceof CarpetBlock ||
            (LithonateConfigs.TWEAK_TRANSPARENT_BLOCK_CLICKS_BANNER_SIGN.getBooleanValue() &&
                (block instanceof AbstractSignBlock || block instanceof AbstractBannerBlock))) {
            Vec3d pos = hitResult.getPos();
            Vec3d ray = this.client.player.getRotationVec(1.0F);
            double dx = (MathHelper.floor(pos.x) - pos.x + (ray.x > 0 ? 1 : 0)) / ray.x;
            double dy = (MathHelper.floor(pos.y) - pos.y + (ray.y > 0 ? 1 : 0)) / ray.y;
            double dz = (MathHelper.floor(pos.z) - pos.z + (ray.z > 0 ? 1 : 0)) / ray.z;
            Vec3d targetPos = pos.add(ray.multiply(Math.min(dy, Math.min(dx, dz)) + 0.025));
            BlockPos targetBlockPos = new BlockPos(targetPos);
            Direction targetSide = Direction.getFacing(targetPos.x, targetPos.y, targetPos.z);
            return new BlockHitResult(pos, targetSide, targetBlockPos, false);
        }
        return hitResult;
    }

    @Inject(
        method = "interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;",
        at = @At("HEAD"), cancellable = true
    )
    private void onInteractBlock(ClientPlayerEntity player, ClientWorld world,
            Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (!LithonateConfigs.SETTING_ENABLE.getBooleanValue() ||
            !LithonateConfigs.TWEAK_TRANSPARENT_BLOCK_CLICKS.getBooleanValue() ||
            !LithonateConfigs.TWEAK_TRANSPARENT_BLOCK_CLICKS_NO_OPENING_TRAPDOORS.getBooleanValue() ||
            Screen.hasShiftDown())
            return;
        BlockState blockState = this.client.world.getBlockState(hitResult.getBlockPos());
        if (blockState.getBlock() instanceof TrapdoorBlock && !blockState.get(TrapdoorBlock.OPEN)) {
            Item item = player.getStackInHand(hand).getItem();
            if (item == Items.REDSTONE || item == Items.ACTIVATOR_RAIL || item == Items.DETECTOR_RAIL ||
                item == Items.POWERED_RAIL || item == Items.RAIL)
                cir.setReturnValue(ActionResult.FAIL);
        }
    }
}

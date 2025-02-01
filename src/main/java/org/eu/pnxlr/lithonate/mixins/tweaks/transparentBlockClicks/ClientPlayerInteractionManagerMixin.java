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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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
        BlockPos blockPos = hitResult.getBlockPos();
        Block block = this.client.world.getBlockState(blockPos).getBlock();
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
}

package org.eu.pnxlr.lithonate.mixins.tweaks.autoAntiGhostBlocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
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

    @Shadow
    @Final
    private ClientPlayNetworkHandler networkHandler;

    @Inject(
        method = {"breakBlock(Lnet/minecraft/util/math/BlockPos;)Z"},
        at = @At("RETURN")
    )
    private void onBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        this.handler(pos);
    }

    @Inject(
        method = {"interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;"},
        at = @At("RETURN")
    )
    private void onInteractBlock(ClientPlayerEntity player, ClientWorld world, Hand hand,
            BlockHitResult hitResult, CallbackInfoReturnable<Boolean> cir) {
        this.handler(hitResult.getBlockPos());
    }

    private void handler(BlockPos pos) {
        if (!LithonateConfigs.SETTING_ENABLE.getBooleanValue() ||
            !LithonateConfigs.TWEAK_AUTO_ANTI_GHOST_BLOCKS.getBooleanValue())
            return;
        int bx = pos.getX();
        int by = pos.getY();
        int bz = pos.getZ();
        for (int x = bx - 2; x <= bx + 2; ++x)
            for (int z = bz - 2; z <= bz + 2; ++z)
                for (int y = by - 1; y <= by + 1; ++y) {
                    BlockPos pos2 = new BlockPos(x, y, z);
                    Block block = this.client.world.getBlockState(pos2).getBlock();
                    if (block instanceof AbstractRailBlock || block instanceof RedstoneWireBlock)
                        this.networkHandler.sendPacket(new PlayerActionC2SPacket(
                                PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, pos2, Direction.UP));
                }
    }
}
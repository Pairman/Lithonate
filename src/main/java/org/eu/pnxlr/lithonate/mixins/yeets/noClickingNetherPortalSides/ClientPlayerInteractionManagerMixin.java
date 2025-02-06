package org.eu.pnxlr.lithonate.mixins.yeets.noClickingNetherPortalSides;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
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
        method = "interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;",
        at = @At("HEAD"), cancellable = true
    )
    private void onInteractBlock(ClientPlayerEntity player, ClientWorld world,
            Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (!LithonateConfigs.SETTING_ENABLE.getBooleanValue() ||
            !LithonateConfigs.YEET_NO_CLICKING_NETHER_PORTAL_SIDES.getBooleanValue())
            return;
        BlockPos blockPos = hitResult.getBlockPos();
        BlockState blockState = this.client.world.getBlockState(blockPos);
        if (blockState.getBlock() instanceof NetherPortalBlock) {
            Direction.Axis axis = blockState.get(NetherPortalBlock.AXIS);
            Direction.Axis targetAxis = hitResult.getSide().getAxis();
            if (targetAxis == axis || targetAxis == Direction.Axis.Y)
                cir.setReturnValue(ActionResult.FAIL);
        }
    }
}

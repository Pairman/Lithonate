package org.eu.pnxlr.lithonate.mixins.tweaks.betterDustsRailsPlacementOnTrapdoors;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.eu.pnxlr.lithonate.config.LithonateConfigs;
import org.eu.pnxlr.lithonate.fakes.IClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Inject(
        method = "interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;",
        at = @At("HEAD"), cancellable = true
    )
    private void onInteractBlock(ClientPlayerEntity player, ClientWorld world,
            Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (!LithonateConfigs.SETTING_ENABLE.getBooleanValue() ||
            !LithonateConfigs.TWEAK_BETTER_DUSTS_RAILS_PLACEMENT_ON_TRAPDOORS.getBooleanValue())
            return;
        BlockPos blockPos = hitResult.getBlockPos();
        BlockState blockState = player.world.getBlockState(blockPos);
        if (blockState.getBlock() instanceof TrapdoorBlock && blockState.get(TrapdoorBlock.HALF) == BlockHalf.TOP) {
            if (blockState.get(TrapdoorBlock.OPEN))
                ((IClientPlayerInteractionManager) this).interactBlockInternal(player, world, hand, hitResult);
            ItemStack itemStack = player.getStackInHand(hand);
            Item item = itemStack.getItem();
            if (item == Items.REDSTONE || item == Items.ACTIVATOR_RAIL || item == Items.DETECTOR_RAIL || item == Items.POWERED_RAIL || item == Items.RAIL) {
                BlockPos blockPos2 = blockPos.up();
                BlockHitResult hitResult2 = new BlockHitResult(hitResult.getPos(), Direction.UP, blockPos2, false);
                if (player.world.getBlockState(blockPos2).canReplace(new ItemPlacementContext(new ItemUsageContext(player, hand, hitResult2))))
                    cir.setReturnValue(((IClientPlayerInteractionManager) this).interactBlockInternal(player, world, hand, hitResult2));
                else
                    cir.setReturnValue(ActionResult.FAIL);
            }
        }
    }
}

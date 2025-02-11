package org.eu.pnxlr.lithonate.mixins.fakes;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import org.eu.pnxlr.lithonate.fakes.IClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin implements IClientPlayerInteractionManager {

    @Shadow
    @Final
    private ClientPlayNetworkHandler networkHandler;

    @Shadow
    private GameMode gameMode;

    public ActionResult interactBlockInternal(ClientPlayerEntity player, ClientWorld world, Hand hand, BlockHitResult hitResult) {
        if (this.gameMode == GameMode.SPECTATOR) {
            this.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(hand, hitResult));
            return ActionResult.SUCCESS;
        }
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isEmpty() || player.getItemCooldownManager().isCoolingDown(itemStack.getItem())) 
            return ActionResult.PASS;
        BlockPos blockPos = hitResult.getBlockPos();
        ActionResult actionResult = world.getBlockState(blockPos).onUse(world, player, hand, hitResult);
        this.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(hand, hitResult));
        ItemUsageContext itemUsageContext = new ItemUsageContext(player, hand, hitResult);
        if (this.gameMode.isCreative()) {
            int i = itemStack.getCount();
            actionResult = itemStack.useOnBlock(itemUsageContext);
            itemStack.setCount(i);
        } else {
            actionResult = itemStack.useOnBlock(itemUsageContext);
        }
        return actionResult;
    }
}

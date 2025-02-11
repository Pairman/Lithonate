package org.eu.pnxlr.lithonate.fakes;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;

public interface IClientPlayerInteractionManager {

    public ActionResult interactBlockInternal(ClientPlayerEntity player, ClientWorld world, Hand hand, BlockHitResult hitResult);
}

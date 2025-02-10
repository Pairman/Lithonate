package org.eu.pnxlr.lithonate.mixins.tweaks.biggerCocoaBlockHitboxes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.eu.pnxlr.lithonate.config.LithonateConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(CocoaBlock.class)
public class CocoaBlockMixin {

    @Inject(
        method = "getOutlineShape(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;",
        at = @At("HEAD"), cancellable = true
    )
    public void onGetOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context,
            CallbackInfoReturnable<VoxelShape> cir) {
        if (!LithonateConfigs.SETTING_ENABLE.getBooleanValue() ||
            !LithonateConfigs.TWEAK_BIGGER_COCOA_BLOCK_HITBOXES.getBooleanValue())
            return;
        cir.setReturnValue(VoxelShapes.fullCube());
    }
}

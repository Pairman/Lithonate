package org.eu.pnxlr.lithonate.mixins.tweaks.transparentWitherRoseHitboxes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.WitherRoseBlock;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.eu.pnxlr.lithonate.config.LithonateConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(WitherRoseBlock.class)
public class WitherRoseBlockMixin extends FlowerBlock {

    @Unique
    private static final VoxelShape CUBOID_EMPTY = Block.createCuboidShape(0, 0, 0, 0.001, 0.001, 0.001);

    public WitherRoseBlockMixin(StatusEffect effect, int effectDuration, AbstractBlock.Settings settings) {
        super(effect, effectDuration, settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (!LithonateConfigs.SETTING_ENABLE.getBooleanValue() ||
            !LithonateConfigs.YEET_NO_CLICKING_NETHER_PORTAL_SIDES.getBooleanValue())
            return super.getOutlineShape(state, world, pos, context);
        return CUBOID_EMPTY;
    }
}

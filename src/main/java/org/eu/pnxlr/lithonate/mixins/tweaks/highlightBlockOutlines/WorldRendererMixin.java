package org.eu.pnxlr.lithonate.mixins.tweaks.highlightBlockOutlines;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.eu.pnxlr.lithonate.config.LithonateConfigs;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow
    private ClientWorld world;

    @Shadow
    @Final
    private MinecraftClient client;
    
    @Shadow
    private static void drawShapeOutline(MatrixStack matrixStack, VertexConsumer vertexConsumer,
            VoxelShape voxelShape, double d, double e, double f, float g, float h, float i, float j) {
    }

    @Unique
    private boolean is_render = false;

    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/WorldRenderer;drawBlockOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/entity/Entity;DDDLnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V",
            ordinal = 0
        )
    )
    private void onRenderDrawBlockOutline(WorldRenderer worldRenderer, MatrixStack matrixStack, VertexConsumer vertexConsumer,
            Entity entity, double d, double e, double f, BlockPos blockPos, BlockState blockState) {
        if (!LithonateConfigs.SETTING_ENABLE.getBooleanValue() ||
            !LithonateConfigs.TWEAK_HIGHLIGHT_BLOCK_OUTLINES.getBooleanValue())
            drawShapeOutline(matrixStack, vertexConsumer, blockState.getOutlineShape(world, blockPos, ShapeContext.of(entity)),
                    blockPos.getX() - d, blockPos.getY() - e, blockPos.getZ() - f, 0.0F, 0.0F, 0.0F, 0.4F);
        else
            is_render = true;
    }
    
    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/WorldRenderer;renderWorldBorder(Lnet/minecraft/client/render/Camera;)V",
            shift = At.Shift.AFTER
        )
    )
    private void onRenderRenderWorldBorder(MatrixStack matrices, float delta,
            long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer,
            LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
        if (!is_render)
            return;
        BlockPos blockPos = ((BlockHitResult) client.crosshairTarget).getBlockPos();
        VoxelShape shape = world.getBlockState(blockPos).getOutlineShape(
                world, blockPos, ShapeContext.of(camera.getFocusedEntity()));
        Vec3d camPos = camera.getPos();
        renderBlockOutline(shape, blockPos.getX() - camPos.getX(),
                blockPos.getY() - camPos.getY(), blockPos.getZ() - camPos.getZ());
        is_render = false;
    }

    private static void renderBlockOutline(VoxelShape shape, double dx, double dy, double dz) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR,
                GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR,
                GlStateManager.SrcFactor.ONE,
                GlStateManager.DstFactor.ZERO);
        RenderSystem.disableTexture();
        RenderSystem.disableCull();
        int width = LithonateConfigs.TWEAK_HIGHLIGHT_BLOCK_OUTLINES_WIDTH.getIntegerValue();
        boolean is_thru = LithonateConfigs.TWEAK_HIGHLIGHT_BLOCK_OUTLINES_THROUGH.getBooleanValue();
        if (is_thru) {
            width /= 2;
            RenderSystem.disableDepthTest();
        }
        RenderSystem.depthMask(false);
        RenderSystem.lineWidth(width);
        buffer.begin(GL11.GL_LINES, VertexFormats.POSITION);
        shape.forEachEdge((x1, y1, z1, x2, y2, z2) -> {
            buffer.vertex((float) (x1 + dx), (float) (y1 + dy), (float) (z1 + dz)).next();
            buffer.vertex((float) (x2 + dx), (float) (y2 + dy), (float) (z2 + dz)).next();
        });
        tessellator.draw();
        RenderSystem.depthMask(true);
        if (is_thru)
            RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
}

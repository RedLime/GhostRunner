package com.redlimerl.ghostrunner.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class GhostRenderFix extends RenderPhase {
    public GhostRenderFix(String name, Runnable beginAction, Runnable endAction) {
        super(name, beginAction, endAction);
    }

    public static boolean isRender;

    public static final RenderPhase.Target FIX_ITEM_TARGET = new RenderPhase.Target("item_entity_target", () -> {
        Framebuffer buffer = MinecraftClient.getInstance().worldRenderer.getEntityFramebuffer();
        if (buffer != null) buffer.beginWrite(false);
    }, () -> MinecraftClient.getInstance().getFramebuffer().beginWrite(false));

    public static RenderLayer getRenderLayer(Identifier texture) {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().texture(
                new RenderPhase.Texture(texture, false, false))
                .transparency(TRANSLUCENT_TRANSPARENCY)
                .target(ITEM_TARGET)
                .diffuseLighting(ENABLE_DIFFUSE_LIGHTING)
                .alpha(ONE_TENTH_ALPHA)
                .lightmap(ENABLE_LIGHTMAP)
                .overlay(ENABLE_OVERLAY_COLOR)
                .writeMaskState(RenderPhase.ALL_MASK)
                .build(true);
        return RenderLayer.of("ghost_entity_translucent_cull", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, 7, 256, true, true, multiPhaseParameters);
    }


    public static RenderLayer getEntityTranslucentCull(Identifier texture) {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().texture(
                new RenderPhase.Texture(texture, false, false))
                .transparency(TRANSLUCENT_TRANSPARENCY)
                .diffuseLighting(ENABLE_DIFFUSE_LIGHTING)
                .alpha(ONE_TENTH_ALPHA)
                .lightmap(ENABLE_LIGHTMAP)
                .overlay(ENABLE_OVERLAY_COLOR)
                .build(true);
        return RenderLayer.of("entity_translucent_cull", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, 7, 256, true, true, multiPhaseParameters);
    }
}

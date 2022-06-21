package com.redlimerl.ghostrunner.mixin;

import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.data.RunnerOptions;
import com.redlimerl.ghostrunner.entity.GhostEntity;
import com.redlimerl.speedrunigt.option.SpeedRunOption;
import com.redlimerl.speedrunigt.option.SpeedRunOptions;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"))
    private void injected(EntityModel<?> entityModel, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        if (entityModel instanceof GhostEntity.Model) {
            if (SpeedRunOption.getOption(RunnerOptions.TOGGLE_GHOST) && SpeedRunOption.getOption(RunnerOptions.GHOST_OPACITY) > 0) {
                entityModel.render(matrices, vertices, light, overlay, red, green, blue, SpeedRunOption.getOption(RunnerOptions.GHOST_OPACITY));
                if (!GhostRunner.IS_USE_GHOST) GhostRunner.IS_USE_GHOST = true;
            }
        }  else {
            entityModel.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        }
    }
}


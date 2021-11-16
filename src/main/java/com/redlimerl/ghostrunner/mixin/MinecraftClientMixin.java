package com.redlimerl.ghostrunner.mixin;

import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.record.GhostInfo;
import com.redlimerl.ghostrunner.record.ReplayGhost;
import com.redlimerl.ghostrunner.render.GhostRenderFix;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.Difficulty;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "isFabulousGraphicsOrBetter", at = @At("HEAD"), cancellable = true)
    private static void isFabulousGraphicsOrBetter(CallbackInfoReturnable<Boolean> cir) {
        if (GhostRenderFix.isRender) cir.setReturnValue(true);
    }

    @Inject(method = "createWorld", at = @At("TAIL"))
    private void createWorld(String worldName, LevelInfo levelInfo, DynamicRegistryManager.Impl registryTracker, GeneratorOptions generatorOptions, CallbackInfo ci) {
        GhostRunner.IS_USE_F3 = false;
        GhostRunner.MINIMUM_DIFFICULTY = Difficulty.HARD;

        GhostInfo.INSTANCE.setup(generatorOptions);
        ReplayGhost.insertBrains(generatorOptions.getSeed());
    }
}

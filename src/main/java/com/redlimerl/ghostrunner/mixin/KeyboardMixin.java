package com.redlimerl.ghostrunner.mixin;

import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.record.GhostInfo;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Shadow private boolean switchF3State;

    @Shadow @Final private MinecraftClient client;

    @Inject(at = @At("RETURN"), method = "onKey")
    public void onKey(long window, int key, int scancode, int i, int j, CallbackInfo ci) {
        if (!GhostRunner.IS_USE_F3 && (switchF3State || client.options.debugEnabled)) {
            GhostRunner.IS_USE_F3 = true;
        }
    }
}

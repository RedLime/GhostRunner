package com.redlimerl.ghostrunner.mixin.screen;

import com.redlimerl.ghostrunner.GhostRunner;
import net.minecraft.client.gui.screen.world.MoreOptionsDialog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.OptionalLong;

@Mixin(MoreOptionsDialog.class)
public abstract class MoreOptionsDialogMixin {

    @Shadow private String seedText;

    @Inject(method = "tick", at = @At("TAIL"))
    private void getSeed(CallbackInfo ci) {
        GhostRunner.optionalLong = tryParseLong2(seedText);
    }

    private static OptionalLong tryParseLong2(String string) {
        try {
            return OptionalLong.of(Long.parseLong(string));
        } catch (NumberFormatException var2) {
            return OptionalLong.empty();
        }
    }
}

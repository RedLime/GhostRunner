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

    @Shadow protected abstract OptionalLong getSeed();

    @Inject(method = "tickSeedTextField", at = @At("TAIL"))
    private void getSeed(CallbackInfo ci) {
        GhostRunner.OPTIONAL_LONG = getSeed();
    }
}

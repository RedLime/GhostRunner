package com.redlimerl.ghostrunner.mixin.screen;

import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.gui.screen.GhostListScreen;
import com.redlimerl.ghostrunner.util.Utils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    @Shadow @Final private boolean doBackgroundFade;

    @Shadow private long backgroundFadeStart;

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        int j = this.height / 4 + 48;

        this.addDrawableChild(new TexturedButtonWidget(this.width / 2 + 104, j, 20, 20,
                Utils.getUpdateButtonOffset(), 0, 20, GhostRunner.BUTTON_ICON_TEXTURE, 64, 64, (buttonWidget) -> {
            if (this.client != null) {
                this.client.setScreen(new GhostListScreen(this));
            }
        }, new TranslatableText("ghostrunner.title")));
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        float f = this.doBackgroundFade ? (float)(Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0F : 1.0F;
        float g = this.doBackgroundFade ? MathHelper.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;
        int l = MathHelper.ceil(g * 255.0F) << 24;
        if ((l & -67108864) != 0) {
            GhostRunner.UPDATE_STATUS.popNotice();
        }
    }
}
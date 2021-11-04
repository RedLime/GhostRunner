package com.redlimerl.ghostrunner.mixin.screen;

import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.gui.screen.GhostListScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        int j = this.height / 4 + 48;

        this.addButton(new TexturedButtonWidget(this.width / 2 + 104, j, 20, 20, 0, 0, 20, GhostRunner.BUTTON_ICON_TEXTURE, 32, 64, (buttonWidget) -> {
            if (this.client != null) {
                this.client.openScreen(new GhostListScreen(this));
            }
        }, new TranslatableText("ghostrunner.title")));
    }
}
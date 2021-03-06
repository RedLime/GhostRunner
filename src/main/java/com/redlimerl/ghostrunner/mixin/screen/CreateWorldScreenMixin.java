package com.redlimerl.ghostrunner.mixin.screen;

import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.data.RunnerOptions;
import com.redlimerl.ghostrunner.gui.screen.GhostSelectScreen;
import com.redlimerl.speedrunigt.option.SpeedRunOption;
import com.redlimerl.speedrunigt.option.SpeedRunOptions;
import com.redlimerl.speedrunigt.timer.category.RunCategories;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin extends Screen {

    @Shadow protected abstract <T extends AbstractButtonWidget> T addButton(T button);

    @Shadow public boolean hardcore;
    private ButtonWidget fsgButton;
    private ButtonWidget ghostButton;

    protected CreateWorldScreenMixin(Text title) {
        super(title);
    }


    @Inject(method = "init", at = @At("HEAD"))
    private void initButton(CallbackInfo ci) {
        this.fsgButton = addButton(new ButtonWidget(this.width / 2 + 5, 151, 150, 20,
                new TranslatableText("ghostrunner.world.is_fsg").append(": ").append(GhostRunner.IS_FSG ? ScreenTexts.YES : ScreenTexts.NO), (buttonWidget) -> {
            GhostRunner.IS_FSG = !GhostRunner.IS_FSG;
            buttonWidget.setMessage(new TranslatableText("ghostrunner.world.is_fsg").append(": ").append(GhostRunner.IS_FSG ? ScreenTexts.YES : ScreenTexts.NO));
        }));
        this.fsgButton.visible = false;

        this.ghostButton = addButton(new TexturedButtonWidget(this.width / 2 + 104, 60, 20, 20, 0, 0, 20, GhostRunner.BUTTON_ICON_TEXTURE, 64, 64, (buttonWidget) -> {
            if (this.client != null && GhostRunner.OPTIONAL_LONG.isPresent()) {
                if (!GhostRunner.IS_SHOW_USE_GHOST_WARN) {
                    this.client.openScreen(new ConfirmScreen(bool -> {
                        this.client.openScreen(bool ? new GhostSelectScreen(this, GhostRunner.OPTIONAL_LONG.getAsLong()) : this);
                        GhostRunner.IS_SHOW_USE_GHOST_WARN = bool;
                    }, new TranslatableText("createWorld.customize.custom.confirmTitle").formatted(Formatting.RED, Formatting.BOLD),
                            new TranslatableText("ghostrunner.message.not_submittable_with_ghosts").formatted(Formatting.YELLOW)));
                } else {
                    this.client.openScreen(new GhostSelectScreen(this, GhostRunner.OPTIONAL_LONG.getAsLong()));
                }
            }
        }));
        this.ghostButton.visible = false;
    }

    @Inject(method = "setMoreOptionsOpen(Z)V", at = @At("HEAD"))
    private void moreOption(boolean b, CallbackInfo ci) {
        if (SpeedRunOption.getOption(SpeedRunOptions.TIMER_CATEGORY) == RunCategories.ANY) {
            this.fsgButton.visible = b;
        }
        this.ghostButton.visible = b && GhostRunner.OPTIONAL_LONG.isPresent();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        GhostRunner.IS_HARDCORE = this.hardcore;
        this.ghostButton.visible = GhostRunner.OPTIONAL_LONG.isPresent();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/world/MoreOptionsDialog;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V"))
    private void injected(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (SpeedRunOption.getOption(SpeedRunOptions.TIMER_CATEGORY) == RunCategories.ANY)
            drawStringWithShadow(matrices, this.textRenderer, I18n.translate("ghostrunner.world.is_fsg.description"), this.width / 2 + 5, 172, -6250336);
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        boolean result = super.changeFocus(lookForwards);
        while ((!SpeedRunOption.getOption(RunnerOptions.TOGGLE_MACRO_FOR_FSG) && getFocused() == this.fsgButton) || getFocused() == this.ghostButton) {
            result = super.changeFocus(lookForwards);
        }
        return result;
    }
}

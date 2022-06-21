package com.redlimerl.ghostrunner.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.data.RunnerOptions;
import com.redlimerl.ghostrunner.data.UpdateStatus;
import com.redlimerl.ghostrunner.data.UpdateStatus.Status;
import com.redlimerl.speedrunigt.option.SpeedRunOption;
import com.redlimerl.speedrunigt.option.SpeedRunOptions;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

import java.util.Locale;

public class GhostRunnerInfoScreen extends Screen {
    private final Screen parent;

    public GhostRunnerInfoScreen(Screen parent) {
        super(new TranslatableText("ghostrunner.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        assert client != null;
        UpdateStatus us = GhostRunner.UPDATE_STATUS;

        if (us.getStatus() == Status.OUTDATED || us.getStatus() == Status.UPDATED) {
            ButtonWidget update = addButton(new ButtonWidget(width / 2 - 75, height - 104, 150, 20, new TranslatableText("ghostrunner.menu.download_update"), (ButtonWidget button) -> Util.getOperatingSystem().open(us.getDownloadUrl())));
            update.active = us.getStatus() == Status.OUTDATED;
        }
        addButton(new ButtonWidget(width / 2 - 75, height - 80, 150, 20, new TranslatableText("ghostrunner.menu.open_github_repo"), (ButtonWidget button) -> Util.getOperatingSystem().open("https://github.com/RedLime/GhostRunner/")));
        addButton(new ButtonWidget(width / 2 - 100, height - 40, 200, 20, ScreenTexts.BACK, (ButtonWidget button) -> client.openScreen(parent)));

        addButton(new CheckboxWidget(20, 20, 20, 20, new TranslatableText("ghostrunner.option.toggle_update_notification"), SpeedRunOption.getOption(RunnerOptions.UPDATE_NOTIFICATION)) {
            @Override
            public void onPress() {
                super.onPress();
                SpeedRunOption.setOption(RunnerOptions.UPDATE_NOTIFICATION, isChecked());
            }
        });
    }

    @SuppressWarnings("deprecation")
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        RenderSystem.pushMatrix();
        RenderSystem.scalef(1.5F, 1.5F, 1.5F);
        this.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 3, 15, 16777215);
        RenderSystem.popMatrix();
        this.drawCenteredText(matrices, this.textRenderer, new LiteralText("Made by RedLime"), this.width / 2, 50, 16777215);
        this.drawCenteredText(matrices, this.textRenderer, new LiteralText("License - MIT"), this.width / 2, 62, 16777215);
        this.drawCenteredText(matrices, this.textRenderer, new LiteralText("Version : "+ GhostRunner.MOD_VERSION), this.width / 2, 78, 16777215);
        if (GhostRunner.UPDATE_STATUS.getStatus() != Status.NONE) {
            if (GhostRunner.UPDATE_STATUS.getStatus() == Status.OUTDATED) {
                this.drawCenteredText(matrices, this.textRenderer, new LiteralText("Updated Version : "+ GhostRunner.UPDATE_STATUS.getLastVersion()).formatted(Formatting.YELLOW), this.width / 2, 88, 16777215);
            }
            this.drawCenteredText(matrices, this.textRenderer,
                    new TranslatableText("ghostrunner.message.update."+GhostRunner.UPDATE_STATUS.getStatus().name().toLowerCase(Locale.ROOT)),
                    this.width / 2, 116, 16777215);
        }

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        if (client != null) {
            client.openScreen(parent);
        }
    }
}

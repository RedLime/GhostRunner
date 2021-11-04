package com.redlimerl.ghostrunner.gui.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class GhostErrorScreen extends Screen {
    private final Screen parent;
    private final Text message;

    public GhostErrorScreen(Screen parent, Text title, Text text) {
        super(title);
        this.parent = parent;
        this.message = text;
    }

    protected void init() {
        super.init();
        this.addButton(new ButtonWidget(this.width / 2 - 100, 140, 200, 20, ScreenTexts.CANCEL, (buttonWidget) -> {
            if (this.client != null) {
                this.client.openScreen(parent);
            }
        }));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 90, 16777215);
        this.drawCenteredText(matrices, this.textRenderer, this.message, this.width / 2, 110, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }
}

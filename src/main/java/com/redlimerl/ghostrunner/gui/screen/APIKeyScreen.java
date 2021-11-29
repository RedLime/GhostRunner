package com.redlimerl.ghostrunner.gui.screen;

import com.redlimerl.ghostrunner.data.RunnerOptions;
import com.redlimerl.speedrunigt.option.SpeedRunOptions;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

public class APIKeyScreen extends Screen {

    private final BooleanConsumer booleanConsumer;
    private TextFieldWidget apiKeyTextField;
    private ButtonWidget saveButton;
    private String finalKey = "";

    public APIKeyScreen(BooleanConsumer booleanConsumer) {
        super(new TranslatableText("ghostrunner.menu.register_api_key"));
        this.booleanConsumer = booleanConsumer;
    }

    @Override
    protected void init() {
        assert client != null;
        client.keyboard.setRepeatEvents(true);

        this.saveButton = addDrawableChild(new ButtonWidget(width / 2 - 100, height / 4 + 32, 98, 20, new TranslatableText("selectWorld.edit.save"),
                (button) -> {
                    SpeedRunOptions.setOption(RunnerOptions.SPEEDRUN_COM_API_KEY, finalKey);
                    booleanConsumer.accept(true);
                }));

        addDrawableChild(new ButtonWidget(width / 2 + 2, height / 4 + 32, 98, 20, ScreenTexts.CANCEL, (button) ->  booleanConsumer.accept(false)));

        addDrawableChild(new ButtonWidget(width / 2 - 100, height - 30, 200, 20, new TranslatableText("ghostrunner.menu.open_speedrun_com"), (button) -> Util.getOperatingSystem().open("https://www.speedrun.com/mc")));

        this.apiKeyTextField = new TextFieldWidget(textRenderer, width / 2 - 100, height / 4 + 9, 200, 20, new LiteralText("API Key..."));
        this.apiKeyTextField.setMaxLength(30);
        this.apiKeyTextField.setText(SpeedRunOptions.getOption(RunnerOptions.SPEEDRUN_COM_API_KEY));
        addSelectableChild(this.apiKeyTextField);
        setInitialFocus(this.apiKeyTextField);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        saveButton.active = apiKeyTextField.getText().length() >= 24;

        String beforeText = this.apiKeyTextField.getText();
        this.finalKey = beforeText;
        this.apiKeyTextField.setText(beforeText.replaceAll("\\w", "*"));

        this.renderBackground(matrices);

        drawCenteredText(matrices, textRenderer, title, width / 2, 30, 16777215);
        drawTextWithShadow(matrices, textRenderer, new LiteralText("Speedrun.com API Key"), width / 2 - 100, height / 4 - 3, 16777215);

        drawCenteredText(matrices, textRenderer, new TranslatableText("ghostrunner.message.guide.api_key.title").formatted(Formatting.AQUA), width / 2, height / 4 + 62, 16777215);

        int i = 0;
        for (String s : new TranslatableText("ghostrunner.message.guide.api_key").getString().split("\n")) {
            drawCenteredText(matrices, textRenderer, new LiteralText(s), width / 2, height / 4 + 76 + (i++ * 10), 16777215);
        }

        this.apiKeyTextField.render(matrices, mouseX, mouseY, delta);

        super.render(matrices, mouseX, mouseY, delta);

        this.apiKeyTextField.setText(beforeText);
    }

    @Override
    public void tick() {
        apiKeyTextField.tick();
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        return apiKeyTextField.charTyped(chr, keyCode);
    }

    @Override
    public void onClose() {
        booleanConsumer.accept(false);
    }

    @Override
    public void removed() {
        if (client != null) client.keyboard.setRepeatEvents(false);
    }
}

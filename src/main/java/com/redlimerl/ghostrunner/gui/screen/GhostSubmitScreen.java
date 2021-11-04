package com.redlimerl.ghostrunner.gui.screen;

import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.MCSpeedRunAPI;
import com.redlimerl.ghostrunner.data.SubmitData;
import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.util.Utils;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;

import java.io.IOException;

public class GhostSubmitScreen extends Screen {

    private final Screen parent;
    private final GhostData ghostData;
    private TextFieldWidget descriptionField;
    private TextFieldWidget videoUrlField;
    private ButtonWidget submitButton;

    public GhostSubmitScreen(Screen parent, GhostData ghostData) {
        super(new TranslatableText("ghostrunner.menu.submit_record"));
        this.parent = parent;
        this.ghostData = ghostData;
    }

    @Override
    protected void init() {
        assert client != null;

        client.keyboard.enableRepeatEvents(true);
        this.submitButton = addButton(new ButtonWidget(width / 2 - 100, height - 65, 200, 20, ScreenTexts.PROCEED,
                (button) -> {
                    ProgressScreen ps = new ProgressScreen();
                    ps.method_15414(new TranslatableText("ghostrunner.message.submitting_record"));
                    client.openScreen(ps);
                    client.execute(() -> {
                        try {
                            String record = MCSpeedRunAPI.request(new SubmitData(ghostData, this.descriptionField.getText(), this.videoUrlField.getText()));
                            ghostData.setSubmitted(true);
                            ghostData.setRecordURL(record);
                            ghostData.update();
                            client.openScreen(new ConfirmScreen(bool -> {
                                if (bool) {
                                    Util.getOperatingSystem().open(record);
                                } else {
                                    client.openScreen(parent);
                                }
                            }, new TranslatableText("ghostrunner.title.success"), new TranslatableText("ghostrunner.message.submitted_record"), new TranslatableText("chat.link.open"), ScreenTexts.DONE));
                        } catch (IOException e) {
                            client.openScreen(new GhostErrorScreen(parent, new TranslatableText("selectWorld.recreate.error.title"), new TranslatableText("ghostrunner.message.failed_submit_record")));
                            e.printStackTrace();
                        }
                    });
                }));

        addButton(new ButtonWidget(width / 2 - 100, this.submitButton.y + 24, 200, 20, ScreenTexts.CANCEL, (button) -> client.openScreen(parent)));

        this.addButton(new TexturedButtonWidget(submitButton.x - 24, submitButton.y, 20, 20, 0, 0, 20, GhostRunner.BUTTON_ICON_TEXTURE, 32, 64, (buttonWidget) -> {
            if (this.client != null) {
                this.client.openScreen(new GhostInfoScreen(this, ghostData));
            }
        }, new TranslatableText("ghostrunner.title")));

        this.videoUrlField = new TextFieldWidget(textRenderer, width / 2 - 100, height / 4 + 9, 200, 20, new LiteralText("Video URL..."));
        this.videoUrlField.setMaxLength(200);
        children.add(this.videoUrlField);
        setInitialFocus(this.videoUrlField);

        this.descriptionField = new TextFieldWidget(textRenderer, width / 2 - 100, height / 4 + 49, 200, 20, new LiteralText("..."));
        this.descriptionField.setMaxLength(200);
        children.add(this.descriptionField);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        submitButton.active = Utils.isUrl(videoUrlField.getText());

        this.renderBackground(matrices);
        drawCenteredText(matrices, textRenderer, title, width / 2, 30, 16777215);
        drawCenteredText(matrices, textRenderer, new TranslatableText("ghostrunner.message.not_need_seed_description"), width / 2, this.descriptionField.y + 28, 16777215);
        drawTextWithShadow(matrices, textRenderer, new TranslatableText("ghostrunner.title.description"), width / 2 - 100, this.descriptionField.y - 10, 16777215);
        drawTextWithShadow(matrices, textRenderer, new TranslatableText("ghostrunner.title.video_url"), width / 2 - 100, this.videoUrlField.y - 10, 16777215);
        descriptionField.render(matrices, mouseX, mouseY, delta);
        videoUrlField.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void tick() {
        descriptionField.tick();
        videoUrlField.tick();
    }

    @Override
    public void onClose() {
        if (client != null) client.openScreen(parent);
    }

    @Override
    public void removed() {
        if (client != null) client.keyboard.enableRepeatEvents(false);
    }
}

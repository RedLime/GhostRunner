package com.redlimerl.ghostrunner.gui.screen;

import com.redlimerl.ghostrunner.data.RunnerOptions;
import com.redlimerl.ghostrunner.gui.GenericToast;
import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.util.Utils;
import com.redlimerl.speedrunigt.option.SpeedRunOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;

import java.util.Objects;

public class GhostEditScreen extends Screen {
    private final GhostData ghost;
    private TextFieldWidget ghostNameTextField;
    private ButtonWidget saveButton;
    private ButtonWidget submitButton;
    private final boolean isOwnGhost;
    private final GhostListScreen parent;
    private boolean isChangedName = false;

    public GhostEditScreen(GhostListScreen parent, GhostData ghost) {
        super(new TranslatableText("ghostrunner.title.edit_ghost"));
        this.parent = parent;
        this.ghost = ghost;
        this.isOwnGhost = ghost.getGhostUserUuid().toString().equals(Utils.UUIDFromString(MinecraftClient.getInstance().getSession().getUuid()).toString());
    }

    private final int yPos = 53;

    @Override
    protected void init() {
        assert client != null;
        client.keyboard.enableRepeatEvents(true);

        this.ghostNameTextField = new TextFieldWidget(textRenderer, width / 2 - 100, yPos, 200, 20, new TranslatableText("selectWorld.enterName"));
        this.ghostNameTextField.setMaxLength(64);
        this.ghostNameTextField.setText(ghost.getGhostName());
        children.add(this.ghostNameTextField);
        setInitialFocus(this.ghostNameTextField);

        this.saveButton = addButton(new ButtonWidget(width / 2 - 100, yPos + 24, 200, 20, new TranslatableText("ghostrunner.menu.change_ghost_name"),
                (button) -> {
                    this.ghost.setGhostName(this.ghostNameTextField.getText());
                    this.ghost.update();
                    isChangedName = true;
                }));

        addButton(new ButtonWidget(width / 2 - 100, yPos + 57, 200, 20, new TranslatableText("ghostrunner.menu.copy_seed"),
                (button) -> {
                    client.keyboard.setClipboard(String.valueOf(ghost.getSeed()));
                    client.getToastManager().add(
                            new GenericToast("ghostrunner.message.copy_seed_to_clipboard", null, new ItemStack(Items.WRITABLE_BOOK))
                    );
                }));

        addButton(new ButtonWidget(width / 2 - 100, yPos + 78, 200, 20, new TranslatableText("ghostrunner.menu.show_ghost_info"), (button) -> client.openScreen(new GhostInfoScreen(this, ghost))));

        this.submitButton = addButton(new ButtonWidget(width / 2 - 100, yPos + 99, 200, 20,
                ghost.getRecordURL().isEmpty() ? new TranslatableText("ghostrunner.menu.submit_record") : new TranslatableText("ghostrunner.menu.open_speedrun_com"),
                ghost.getRecordURL().isEmpty() ? (button) -> {
                    if (SpeedRunOptions.getOption(RunnerOptions.SPEEDRUN_COM_API_KEY).length() < 24) {
                        client.openScreen(new ConfirmScreen(bool ->
                                client.openScreen(bool ? new APIKeyScreen(bool2 ->
                                        client.openScreen(bool2 ? new GhostSubmitScreen(this, ghost) : this)) : this),
                                new TranslatableText("selectWorld.recreate.error.title"),
                                new TranslatableText("ghostrunner.message.not_registered_api_key"),
                                new TranslatableText("ghostrunner.menu.register_api_key"),
                                ScreenTexts.CANCEL));
                    } else {
                        client.openScreen(new GhostSubmitScreen(this, ghost));
                    }
                } : (button) -> Util.getOperatingSystem().open(ghost.getRecordURL())));
        this.submitButton.active = !ghost.getRecordURL().isEmpty() || (!ghost.isSubmitted() && isOwnGhost);

        addButton(new ButtonWidget(width / 2 - 100, height - 40, 200, 20, ScreenTexts.CANCEL, (button) -> close()));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        saveButton.active = !ghostNameTextField.getText().isEmpty() && !Objects.equals(ghost.getGhostName(), ghostNameTextField.getText());

        this.renderBackground(matrices);

        if (!ghost.isSubmitted() && !isOwnGhost && ghost.getRecordURL().isEmpty()) {
            drawCenteredText(matrices, textRenderer, new TranslatableText("ghostrunner.message.submit.not_own_ghost"), width / 2, submitButton.y + 25, BackgroundHelper.ColorMixer.getArgb(255, 255, 80, 80));
        }
        drawCenteredText(matrices, textRenderer, title, width / 2, 15, 16777215);
        drawTextWithShadow(matrices, textRenderer, new TranslatableText("ghostrunner.title.ghost_name"), width / 2 - 100, yPos - 14, 16777215);
        ghostNameTextField.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void tick() {
        ghostNameTextField.tick();
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        return ghostNameTextField.charTyped(chr, keyCode);
    }

    @Override
    public void onClose() {
        close();
    }

    public void close() {
        if (client != null) {
            client.openScreen(parent);
            if (isChangedName) parent.refresh();
        }
    }

    @Override
    public void removed() {
        if (client != null) client.keyboard.enableRepeatEvents(false);
    }
}

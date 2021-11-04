package com.redlimerl.ghostrunner.gui.screen;

import com.redlimerl.ghostrunner.gui.widget.GhostSelectWidget;
import com.redlimerl.ghostrunner.record.ReplayGhost;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.UUID;

public class GhostSelectScreen extends Screen {

    public static int WHITE_COLOR = BackgroundHelper.ColorMixer.getArgb(255, 255, 255, 255);

    private GhostSelectWidget listWidget;
    public ArrayList<UUID> selectionGhost = new ArrayList<>();
    private final long seed;
    private final Screen parent;

    public GhostSelectScreen(Screen parent, long seed) {
        super(new TranslatableText("ghostrunner.title.select_ghosts"));
        selectionGhost.addAll(ReplayGhost.getSelectedGhosts(seed));
        this.seed = seed;
        this.parent = parent;
    }

    @Override
    protected void init() {
        int i = width / 2 - 50;

        assert client != null;

        addButton(new ButtonWidget(i - 50, 190, 98, 20, ScreenTexts.PROCEED, button ->
        {
            ReplayGhost.toSelectGhosts(seed, selectionGhost.toArray(new UUID[0]));
            client.openScreen(parent);
        }));
        addButton(new ButtonWidget(i + 50, 190, 98, 20, ScreenTexts.CANCEL, button -> {
            client.openScreen(parent);
        }));

        listWidget = new GhostSelectWidget(this, this.client, width, height, 48, 180, 24);
        addChild(listWidget);
        listWidget.update(seed, listWidget.getScrollAmount(), true);
    }

    @Override
    public void renderBackground(MatrixStack matrices) {
        int i = (width - 236) / 2;
        super.renderBackground(matrices);
        this.drawTexture(matrices, i, 44, 1, 1, 236, 8);
        int j = 8;
        for (int i1 = 0; i1 < j; i1++) {
            this.drawTexture(matrices, i, 52 + 16 * i1, 1, 10, 236, 16);
        }
        this.drawTexture(matrices, i, 52 + 16 * j, 1, 27, 236, 8);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        listWidget.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, textRenderer, this.title, width / 2, 24, WHITE_COLOR);
        drawCenteredText(matrices, textRenderer, new TranslatableText("ghostrunner.message.ghosts_select_limit"), width / 2, 214, WHITE_COLOR);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        if (client != null) client.openScreen(parent);
    }
}

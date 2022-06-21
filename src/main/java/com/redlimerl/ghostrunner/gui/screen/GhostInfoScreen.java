package com.redlimerl.ghostrunner.gui.screen;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.speedrunigt.timer.InGameTimer;
import com.redlimerl.speedrunigt.timer.InGameTimerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.text.SimpleDateFormat;

public class GhostInfoScreen extends Screen {

    private final Screen parent;
    private final GhostData ghostData;

    public GhostInfoScreen(Screen parent, GhostData ghostData) {
        super(new TranslatableText("ghostrunner.ghostdata.title"));
        this.parent = parent;
        this.ghostData = ghostData;
    }

    private GeneralInfoListWidget generalList;

    @Override
    protected void init() {
        generalList = new GeneralInfoListWidget(client, this);
        addButton(new ButtonWidget(width / 2 - 100, height - 28, 200, 20, ScreenTexts.DONE, button -> {
            if (client != null) client.openScreen(parent);
        }));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.generalList.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, textRenderer, title, width / 2, 20, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }


    public static class GeneralInfoListWidget extends AlwaysSelectedEntryListWidget<GeneralInfoListWidget.Entry> {

        private final GhostInfoScreen infoScreen;

        public GeneralInfoListWidget(MinecraftClient minecraftClient, GhostInfoScreen infoScreen) {
            super(minecraftClient, infoScreen.width, infoScreen.height, 32, infoScreen.height - 32, 10);

            GhostData ghostData = infoScreen.ghostData;
            addEntry(new Entry(this, "seed", String.valueOf(ghostData.getSeed())));
            addEntry(new Entry(this, "realtime", InGameTimerUtils.timeToStringFormat(ghostData.getRealTimeAttack())));
            addEntry(new Entry(this, "ingametime", InGameTimerUtils.timeToStringFormat(ghostData.getInGameTime())));
            addEntry(new Entry(this, "category", ghostData.getGhostCategory().getText().getString()));
            addEntry(new Entry(this, "gametype", ghostData.getType().getContext()));
            addEntry(new Entry(this, "clientversion", ghostData.getClientVersion()));
            addEntry(new Entry(this, "ishardcore", ghostData.isHardcore() ? ScreenTexts.YES.getString() : ScreenTexts.NO.getString()));
            addEntry(new Entry(this, "difficulty", ghostData.getDifficulty().getTranslatableName().getString()));
            addEntry(new Entry(this, "isf3", ghostData.isUseF3() ? ScreenTexts.YES.getString() : ScreenTexts.NO.getString()));
            addEntry(new Entry(this, "createddate", new SimpleDateFormat("yyyy-MM-dd").format(ghostData.getCreatedDate())));
            addEntry(new Entry(this, "ghostowner", ghostData.getGhostUserName()));
            addEntry(new Entry(this, "issubmitted", ghostData.isSubmitted() ? ScreenTexts.YES.getString() : ScreenTexts.NO.getString()));

            this.infoScreen = infoScreen;
        }

        @Override
        protected void renderBackground(MatrixStack matrices) {
            infoScreen.renderBackground(matrices);
        }

        public static class Entry extends AlwaysSelectedEntryListWidget.Entry<Entry> {

            private final GeneralInfoListWidget statsListWidget;
            private final String key;
            private final String value;

            public Entry(GeneralInfoListWidget generalInfoListWidget, String key, String value) {
                this.statsListWidget = generalInfoListWidget;
                this.key = key;
                this.value = value;
            }

            @Override
            public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
                TextRenderer textRenderer = statsListWidget.infoScreen.textRenderer;
                MutableText text = new TranslatableText("ghostrunner.ghostdata." + key).formatted(Formatting.GRAY);
                statsListWidget.drawStringWithShadow(matrices, textRenderer, text.getString(), x + 2, y + 1, index % 2 == 0 ? 16777215 : 9474192);
                statsListWidget.drawStringWithShadow(matrices, textRenderer, value, x + 2 + 213 - textRenderer.getWidth(value), y + 1, index % 2 == 0 ? 16777215 : 9474192);
            }
        }
    }

    @Override
    public void onClose() {
        if (client != null) {
            client.openScreen(parent);
        }
    }
}

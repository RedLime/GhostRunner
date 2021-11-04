package com.redlimerl.ghostrunner.gui.screen;

import com.redlimerl.ghostrunner.data.RunnerStatistic;
import com.redlimerl.speedrunigt.timer.InGameTimer;
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
import net.minecraft.util.Pair;

import java.util.Locale;

public class GhostStatisticScreen extends Screen {

    private final Screen parent;

    public GhostStatisticScreen(Screen parent) {
        super(new TranslatableText("gui.stats"));
        this.parent = parent;
    }

    private GeneralStatsListWidget generalList;

    @Override
    protected void init() {
        generalList = new GeneralStatsListWidget(client, this);
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


    public static class GeneralStatsListWidget extends AlwaysSelectedEntryListWidget<GeneralStatsListWidget.Entry> {

        private final GhostStatisticScreen statsScreen;

        public GeneralStatsListWidget(MinecraftClient minecraftClient, GhostStatisticScreen statsScreen) {
            super(minecraftClient, statsScreen.width, statsScreen.height, 32, statsScreen.height - 32, 10);

            for (RunnerStatistic.Type value : RunnerStatistic.Type.values()) {
                addEntry(new Entry(this, new Pair<>(value, RunnerStatistic.getStatistic(value))));
            }

            this.statsScreen = statsScreen;
        }

        @Override
        protected void renderBackground(MatrixStack matrices) {
            statsScreen.renderBackground(matrices);
        }

        public static class Entry extends AlwaysSelectedEntryListWidget.Entry<Entry> {

            private final GeneralStatsListWidget statsListWidget;
            private final Pair<RunnerStatistic.Type, Integer> pair;

            public Entry(GeneralStatsListWidget statsListWidget, Pair<RunnerStatistic.Type, Integer> pair) {
                this.statsListWidget = statsListWidget;
                this.pair = pair;
            }

            @Override
            public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
                TextRenderer textRenderer = statsListWidget.statsScreen.textRenderer;
                MutableText text = new TranslatableText("ghostrunner.statistic." + pair.getLeft().name().toLowerCase(Locale.ROOT)).formatted(Formatting.GRAY);
                statsListWidget.drawStringWithShadow(matrices, textRenderer, text.getString(), x + 2, y + 1, index % 2 == 0 ? 16777215 : 9474192);
                String string = pair.getLeft().name().contains("_TIME") ? InGameTimer.timeToStringFormat((long) pair.getRight()) : pair.getRight().toString();
                statsListWidget.drawStringWithShadow(matrices, textRenderer, string, x + 2 + 213 - textRenderer.getWidth(string), y + 1, index % 2 == 0 ? 16777215 : 9474192);
            }
        }
    }
}

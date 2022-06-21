package com.redlimerl.ghostrunner.gui.widget;

import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.gui.screen.GhostSelectScreen;
import com.redlimerl.ghostrunner.record.ReplayGhost;
import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.speedrunigt.timer.InGameTimer;
import com.redlimerl.speedrunigt.timer.InGameTimerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class GhostSelectWidget extends ElementListWidget<GhostSelectWidget.GhostEntry> {

    private final GhostSelectScreen parent;
    private List<GhostData> ghosts;
    private final long openTime = System.currentTimeMillis();

    public GhostSelectWidget(GhostSelectScreen parent, MinecraftClient client, int i, int j, int k, int l, int m) {
        super(client, i, j, k, l, m);
        this.parent = parent;
    }

    public void update(long seed, double scrollAmount, boolean update) {
        if (update) {
            ArrayList<GhostData> list = new ArrayList<>();
            File[] files = GhostRunner.GHOSTS_PATH.toFile().listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        try {
                            list.add(GhostData.loadData(file.toPath()));
                        } catch (Exception ignore) {}
                    }
                }
            }
            ghosts = list.stream().filter(ghostData -> ghostData.getSeed() == seed && Objects.equals(ghostData.getClientVersion(), GhostRunner.CLIENT_VERSION))
                    .sorted(Comparator.comparing(GhostData::getInGameTime)).collect(Collectors.toList());
        }
        this.replaceEntries(ghosts.stream().map( ghostData -> new GhostEntry(ghostData, this, seed)).collect(Collectors.toList()));
        this.setScrollAmount(scrollAmount);
    }

    public static class GhostEntry extends ElementListWidget.Entry<GhostEntry> {

        private static final MutableText ON_TEXT = ScreenTexts.ON.copy().formatted(Formatting.BOLD, Formatting.GREEN);
        private static final MutableText OFF_TEXT = ScreenTexts.OFF.copy().formatted(Formatting.BOLD, Formatting.RED);
        private static MutableText toggleText(boolean bool) {
            return bool ? ON_TEXT : OFF_TEXT;
        }

        private final GhostData ghostData;
        private final GhostSelectWidget selectWidget;

        private final ArrayList<UUID> ghostList;
        private boolean isToggle;
        private final ButtonWidget selectButton;

        private final ArrayList<ButtonWidget> buttons = new ArrayList<>();

        public GhostEntry(GhostData ghostData, GhostSelectWidget selectWidget, long seed) {
            super();
            this.ghostData = ghostData;
            this.selectWidget = selectWidget;

            this.ghostList = selectWidget.parent.selectionGhost;
            this.isToggle = ReplayGhost.getSelectedGhosts(seed).contains(ghostData.getUuid());
            this.selectButton = new ButtonWidget(0, 0, 30, 20, toggleText(isToggle), button -> {
                isToggle = !isToggle;
                button.setMessage(toggleText(isToggle));
                if (isToggle) {
                    ghostList.add(ghostData.getUuid());
                } else {
                    ghostList.remove(ghostData.getUuid());
                }
                for (GhostEntry child : selectWidget.children()) {
                    child.selectButton.active = ghostList.size() < 4 || child.isToggle;
                }
            });

            buttons.add(selectButton);
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.selectButton.x = x + (entryWidth - this.selectButton.getWidth() - 4);
            this.selectButton.y = y + (entryHeight - this.selectButton.getHeight()) / 2;
            this.selectButton.render(matrices, mouseX, mouseY, tickDelta);

            String ghostName = ghostData.getGhostName();
            if (ghostName.length() > 20) {
                ghostName = "   " + ghostName + "     ";
                int indexCount = (int) ((System.currentTimeMillis() - selectWidget.openTime) / 300 % (ghostName.length() - 20));
                ghostName = ghostName.substring(indexCount, 20+indexCount);
            }

            selectWidget.client.textRenderer.draw(matrices, new LiteralText("").append(new LiteralText("[" + InGameTimerUtils.timeToStringFormat(ghostData.getInGameTime()) + "] ")
                    .formatted(Formatting.GRAY)).append(new LiteralText(ghostName)), (float) (x + 4), (float) (y + 6), GhostSelectScreen.WHITE_COLOR);
        }

        @Override
        public List<? extends Element> children() {
            return buttons;
        }
    }
}

package com.redlimerl.ghostrunner.gui.widget;

import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.gui.screen.GhostEditScreen;
import com.redlimerl.ghostrunner.gui.screen.GhostListScreen;
import com.redlimerl.ghostrunner.record.ReplayGhost;
import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.speedrunigt.timer.InGameTimer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.function.Supplier;

public class GhostListWidget extends AlwaysSelectedEntryListWidget<GhostListWidget.GhostEntry> {


    private final long openTime = System.currentTimeMillis();
    private final GhostListScreen parent;
    public ArrayList<GhostData> ghosts = new ArrayList<>();

    public GhostListWidget(GhostListScreen parent, MinecraftClient minecraftClient, int i, int j, int k, int l, int m, Supplier<String> searchFilter, GhostListWidget ghostList) {
        super(minecraftClient, i, j, k, l, m);
        if (ghostList != null) ghosts = ghostList.ghosts;
        this.parent = parent;
        this.filter(searchFilter, 0, 0, true);
    }

    @Override
    public boolean isFocused() {
        return this.parent.getFocused() == this;
    }

    @Override
    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 20 + 5;
    }

    @Override
    public int getRowWidth() {
        return super.getRowWidth() + 50;
    }

    public void filter(Supplier<String> supplier, int filter, int order, boolean load) {
        clearEntries();
        if (load) {
            ArrayList<GhostData> list = new ArrayList<>();
            File[] files = GhostRunner.GHOSTS_PATH.toFile().listFiles();
            assert files != null;
            for (File file : files) {
                if (file.isDirectory()) {
                    try {
                        GhostData record = GhostData.loadData(file.toPath());
                        list.add(record);
                    } catch (Exception ignored) {}
                }
            }
            ghosts = list;
        }

        ghosts.sort(Comparator.comparing(GhostData::getInGameTime));

        if (order == 0) ghosts.sort(Comparator.comparing(GhostData::getInGameTime));
        else if (order == 1) ghosts.sort(Comparator.comparing(GhostData::getGhostName));
        else if (order == 2) ghosts.sort(Comparator.comparing(GhostData::getCreatedDate));
        else if (order == 3) ghosts.sort(Comparator.comparing(GhostData::getCreatedDate).reversed());
        else ghosts.sort(Comparator.comparing(GhostData::getSeed));

        for (GhostData ghost : ghosts) {
            if ((filter == 1 && ghost.getType() == GhostType.SSG) || (filter == 2 && ghost.getType() == GhostType.RSG) || (filter == 3 && ghost.getType() == GhostType.FSG) || !(filter > 0 && filter < 4)) {
                boolean b1 = ghost.getGhostName().toLowerCase(Locale.ROOT).contains(supplier.get().toLowerCase(Locale.ROOT));
                boolean b2 = supplier.get().toLowerCase(Locale.ROOT).contains(ghost.getGhostName().toLowerCase(Locale.ROOT));
                boolean b3 = String.valueOf(ghost.getSeed()).contains(supplier.get());
                boolean b4 = ghost.getGhostUserName().toLowerCase(Locale.ROOT).contains(supplier.get().toLowerCase(Locale.ROOT));
                boolean b5 = supplier.get().toLowerCase(Locale.ROOT).contains(ghost.getGhostUserName().toLowerCase(Locale.ROOT));
                if (b1 || b2 || b3 || b4 || b5) addEntry(new GhostEntry(this, ghost, client.textRenderer));
            }
        }
    }

    @Override
    protected void moveSelection(MoveDirection direction) {
        moveSelectionIf(direction, o -> true);
    }

    @Override
    public void setSelected(@Nullable GhostEntry entry) {
        super.setSelected(entry);
        this.parent.ghostSelected(entry != null);
        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }

    public static class GhostEntry extends EntryListWidget.Entry<GhostEntry> {

        private final GhostListWidget ghostListWidget;
        public final GhostData ghost;
        private final TextRenderer textRenderer;
        private final GhostListScreen ghostSelection;
        private final MinecraftClient client;

        public GhostEntry(GhostListWidget ghostListWidget, GhostData ghost, TextRenderer textRenderer) {
            this.ghostListWidget = ghostListWidget;
            this.ghost = ghost;
            this.textRenderer = textRenderer;
            this.ghostSelection = ghostListWidget.parent;
            this.client = ghostListWidget.client;
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            String ghostName = ghost.getGhostName();
            if (ghostName.length() > 42) {
                ghostName = "    " + ghostName + "     ";
                int indexCount = (int) ((System.currentTimeMillis() - ghostListWidget.openTime) / 300 % (ghostName.length() - 42));
                ghostName = ghostName.substring(indexCount, 42 + indexCount);
            }

            float xOffset = (float) x;

            MutableText timeContext = new LiteralText(" IGT: ").formatted(Formatting.GRAY)
                    .append(new LiteralText(InGameTimer.timeToStringFormat(ghost.getInGameTime())).formatted(Formatting.UNDERLINE).formatted(Formatting.YELLOW))
                    .append(new LiteralText(" / RTA: ").formatted(Formatting.GRAY))
                    .append(new LiteralText(InGameTimer.timeToStringFormat(ghost.getRealTimeAttack())).formatted(Formatting.UNDERLINE).formatted(Formatting.LIGHT_PURPLE));
            MutableText runnerContext = new TranslatableText("ghostrunner.context.by").append(": ")
                    .append(new LiteralText(ghost.getGhostUserName()).formatted(Formatting.UNDERLINE).formatted(Formatting.GRAY));
            MutableText modeContext = new LiteralText(" ")
                    .append(new TranslatableText("ghostrunner.context.method"))
                    .append(": ")
                    .append(new LiteralText(ghost.getType() == GhostType.RSG ? "RSG" : ghost.getType() == GhostType.SSG ? "SSG" : "FSG")
                            .formatted(Formatting.UNDERLINE).formatted(Formatting.GRAY));
            if (ghost.isHardcore())
                modeContext.append(new LiteralText(", ")).append(new TranslatableText("gameMode.hardcore").formatted(Formatting.UNDERLINE).formatted(Formatting.RED));

            modeContext.append(new LiteralText("   ")).append(runnerContext);

            MutableText worldContext = new LiteralText(" ")
                    .append(new TranslatableText("ghostrunner.context.seed"))
                    .append(": ")
                    .append(new LiteralText(String.valueOf(ghost.getSeed())).formatted(Formatting.GRAY).formatted(Formatting.GRAY))
                    .append(new LiteralText("  "))
                    .append(new TranslatableText("ghostrunner.context.version"))
                    .append(": ")
                    .append(new LiteralText(ghost.getClientVersion()).formatted(Formatting.GRAY));

            if (index != 0) {
                String air = "                                                                   ";
                textRenderer.draw(matrices, new LiteralText(air).formatted(Formatting.STRIKETHROUGH).formatted(Formatting.GRAY), xOffset, y - 6.1f, 16777215);
            }

            textRenderer.drawWithShadow(matrices, ghostName, xOffset, (float) (y + 3), 16777215);
            textRenderer.draw(matrices, timeContext, xOffset, (float) (y + 15), 16777215);
            textRenderer.draw(matrices, modeContext, xOffset, (float) (y + 28), 16777215);
            textRenderer.draw(matrices, worldContext, xOffset, (float) (y + 39), 16777215);

            if (this.client.options.touchscreen || hovered) {
                ArrayList<Text> textList = new ArrayList<>();
                textList.add(new TranslatableText("ghostrunner.context.record_date").formatted(Formatting.GOLD)
                        .append(": ")
                        .append(new TranslatableText(new SimpleDateFormat("yy.MM.dd HH:mm:ss")
                                .format(ghost.getCreatedDate())).formatted(Formatting.WHITE)));
                this.ghostSelection.setTooltip(textList);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            ghostListWidget.setSelected(this);
            ghostSelection.ghostSelected(true);
            return false;
        }

        public void delete() {
            client.openScreen(new ConfirmScreen(t -> {
                if (t) {
                    ReplayGhost.removeInSelectedGhosts(ghost.getSeed(), ghost.getUuid());
                    client.openScreen(new ProgressScreen());
                    File file = ghost.getPath().toFile();
                    if (file.exists() && file.isDirectory()) {
                        try {
                            FileUtils.deleteDirectory(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    ghostListWidget.filter(() -> ghostSelection.searchBox.getText(), ghostSelection.filterType, ghostSelection.order, true);
                }
                client.openScreen(this.ghostSelection);
            }, new TranslatableText("ghostrunner.message.delete_ghost"),
                    new TranslatableText("selectWorld.deleteWarning", this.ghost.getGhostName()),
                    new TranslatableText("selectWorld.deleteButton"),
                    ScreenTexts.CANCEL)
            );
        }

        public void edit() {
            client.openScreen(new GhostEditScreen(ghostSelection, ghost));
        }
    }
}

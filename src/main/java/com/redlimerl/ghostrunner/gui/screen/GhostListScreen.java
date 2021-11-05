package com.redlimerl.ghostrunner.gui.screen;

import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.data.UpdateStatus;
import com.redlimerl.ghostrunner.gui.GenericToast;
import com.redlimerl.ghostrunner.gui.widget.GhostListWidget;
import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.util.TarGzUtil;
import com.redlimerl.ghostrunner.util.Utils;
import com.redlimerl.speedrunigt.option.SpeedRunOptionScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GhostListScreen extends Screen {
    public TextFieldWidget searchBox;
    public GhostListWidget ghostList;
    public int filterType = 0;
    public int order = 0;
    private ButtonWidget exportButton;
    private ButtonWidget editButton;
    private ButtonWidget deleteButton;
    private final Screen parent;
    private List<Text> tooltipText = null;

    public GhostListScreen(Screen parent) {
        super(new TranslatableText("ghostrunner.title.my_ghost"));
        this.parent = parent;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void init() {

        assert this.client != null;
        this.client.keyboard.enableRepeatEvents(true);

        this.searchBox = new TextFieldWidget(textRenderer, width / 2 - 22, 22, 180, 20, null, new TranslatableText("selectWorld.search"));
        this.searchBox.setChangedListener((string) -> this.ghostList.filter(() -> string, filterType, order, false));

        this.ghostList = new GhostListWidget(this, client, width, height, 48, height - 64, 54, () -> searchBox.getText(), this.ghostList);

        children.add(this.searchBox);
        children.add(this.ghostList);

        addButton(new TexturedButtonWidget(width / 2 + 168, 22, 20, 20, Utils.getUpdateButtonOffset(), 0,
                20, GhostRunner.BUTTON_ICON_TEXTURE, 64, 64,
                (button) -> client.openScreen(new GhostRunnerInfoScreen(this)), new TranslatableText("ghostrunner.title")));

        addButton(new ButtonWidget(width / 2 - 176, 22, 84, 20,
                new TranslatableText("ghostrunner.menu.order").append(": ").append(getOrder()), (buttonWidget) -> {
            order++;
            if (order >= 4) order = 0;
            buttonWidget.setMessage(new TranslatableText("ghostrunner.menu.order").append(": ").append(getOrder()));
            this.ghostList.filter(() -> searchBox.getText(), filterType, order, false);
        }));

        addButton(new ButtonWidget(width / 2 - 88, 22, 60, 20,
                new TranslatableText("ghostrunner.menu.show").append(": ").append(getFilterType()), (buttonWidget) -> {
            filterType++;
            if (filterType >= 4) filterType = 0;
            buttonWidget.setMessage(new TranslatableText("ghostrunner.menu.show").append(": ").append(getFilterType()));
            this.ghostList.filter(() -> searchBox.getText(), filterType, order, false);
        }));

        addButton(new ButtonWidget(width / 2 + 4, height - 52, 150, 20, new TranslatableText("ghostrunner.menu.import_ghost_file"), (buttonWidget) -> {
            MemoryStack stack = MemoryStack.stackPush();
            PointerBuffer filters = stack.mallocPointer(1);
            filters.put(stack.UTF8("*.mcg"));
            filters.flip();
            String string = TinyFileDialogs.tinyfd_openFileDialog("Import Ghost", GhostRunner.GHOST_SHARE_PATH.toString(),
                    filters, "Minecraft Ghost (*.mcg)", true);

            ArrayList<String> successList = new ArrayList<>();
            ArrayList<String> failList = new ArrayList<>();

            if (string != null) {
                for (String s : string.split("\\|")) {
                    Path path = Paths.get(s);
                    String name = path.getFileName().toString().substring(0, path.getFileName().toString().length() - 4);

                    try {
                        Path tempPath = GhostRunner.GHOST_TEMP_PATH.resolve(name);
                        tempPath.toFile().mkdirs();
                        TarGzUtil.decompressTarGzipFile(path, tempPath);

                        GhostData ghostData = GhostData.loadData(tempPath);
                        if (ghostList.ghosts.stream().anyMatch(baseGhostData -> baseGhostData.getUuid() == ghostData.getUuid())) {
                            tempPath.toFile().delete();
                            failList.add(ghostData.getGhostName());
                        } else {
                            if (tempPath.toFile().renameTo(ghostData.getPath().toFile())) {
                                successList.add(ghostData.getGhostName());
                            } else {
                                failList.add(ghostData.getGhostName());
                            }
                        }
                    } catch (Exception e) {
                        failList.add(name);
                    }
                }
            }

            for (String success : successList) {
                MinecraftClient.getInstance().getToastManager().add(
                        new GenericToast("ghostrunner.message.import_success", ": "+success, new ItemStack(Items.CHEST)));
            }

            for (String fail : failList) {
                MinecraftClient.getInstance().getToastManager().add(
                        new GenericToast("ghostrunner.message.import_fail", ": "+fail, new ItemStack(Items.BEDROCK)));
            }

            if (!successList.isEmpty()) {
                this.ghostList.filter(() -> searchBox.getText(), filterType, order, true);
            }
            stack.pop();
        }));

        this.exportButton = addButton(new ButtonWidget(width / 2 - 154, height - 52, 150, 20, new TranslatableText("ghostrunner.menu.export_ghost_file"),
                (button) -> {
            if (ghostList.getSelected() != null && ghostList.getSelected().ghost != null) {
                GhostData ghost = ghostList.getSelected().ghost;
                button.active = false;
                new Thread(() -> {
                    int i = 0;
                    if (GhostRunner.GHOST_SHARE_PATH.resolve(ghost.getGhostName() + ".mcg").toFile().exists()) {
                        i++;
                        while (GhostRunner.GHOST_SHARE_PATH.resolve(ghost.getGhostName() + "_" + i + ".mcg").toFile().exists()) {
                            i++;
                        }
                    }
                    TarGzUtil.createTarGzipFolder(ghost.getPath(), GhostRunner.GHOST_SHARE_PATH.resolve(ghost.getGhostName() + (i > 0 ? "_"+i : "") + ".mcg"));
                    Util.getOperatingSystem().open(GhostRunner.GHOST_SHARE_PATH.toFile());
                    client.execute(() -> exportButton.active = true);
                }).start();
            }
        }));

        this.editButton = addButton(new ButtonWidget(width / 2 - 154, height - 28, 72, 20, new TranslatableText("selectWorld.edit"),
                        (button) -> {
                            if (ghostList != null && ghostList.getSelected() != null) ghostList.getSelected().edit();
                        }));

        this.deleteButton = addButton(new ButtonWidget(width / 2 - 76, height - 28, 72, 20, new TranslatableText("selectWorld.delete"),
                        (button) -> {
                            if (ghostList != null && ghostList.getSelected() != null) ghostList.getSelected().delete();
                        }));


        addButton(new ButtonWidget(width / 2 + 4, height - 28, 72, 20, new TranslatableText("options.title"),
                (button) -> client.openScreen(new SpeedRunOptionScreen(this))));
        addButton(new ButtonWidget(width / 2 + 82, height - 28, 72, 20, ScreenTexts.CANCEL, (button) -> client.openScreen(parent)));

        setInitialFocus(this.searchBox);

        ghostSelected(false);
    }

    @Override
    public void tick() {
        searchBox.tick();
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        return searchBox.charTyped(chr, keyCode);
    }

    @Override
    public void onClose() {
        if (client != null) client.openScreen(parent);
    }

    @Override
    public void removed() {
        if (client != null) client.keyboard.enableRepeatEvents(false);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers) || searchBox.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.tooltipText = null;
        this.renderBackground(matrices);
        this.ghostList.render(matrices, mouseX, mouseY, delta);
        this.searchBox.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, textRenderer, title, width / 2, 8, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
        if (tooltipText != null) this.renderTooltip(matrices, tooltipText, mouseX, mouseY);
    }

    public void setTooltip(List<Text> tooltipText) {
        this.tooltipText = tooltipText;
    }

    public void ghostSelected(boolean active) {
        this.editButton.active = active;
        this.deleteButton.active = active;
        this.exportButton.active = active;
    }

    private TranslatableText getOrder() {
        if (order == 0) return new TranslatableText("ghostrunner.menu.order.record");
        else if (order == 1) return new TranslatableText("ghostrunner.menu.order.name");
        else if (order == 2) return new TranslatableText("ghostrunner.menu.order.oldest");
        else return new TranslatableText("ghostrunner.menu.order.recent");
    }

    private Text getFilterType() {
        if (filterType == 0) return new TranslatableText("gui.all");
        else if (filterType == 1) return new LiteralText("SSG");
        else if (filterType == 2) return new LiteralText("RSG");
        else return new LiteralText("FSG");
    }

    public void refresh() {
        ghostList.filter(() -> this.searchBox.getText(), this.filterType, this.order, true);
    }
}

package com.redlimerl.ghostrunner.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class GenericToast implements Toast {

    private static Identifier TEXTURE = new Identifier("textures/gui/toasts.png");

    private boolean justUpdated = true;
    private long startTime;
    protected String titleKey;
    protected String descriptionKey;
    protected ItemStack icon;

    public GenericToast(String titleKey, String descriptionKey, ItemStack icon) {
        this.titleKey = titleKey;
        this.descriptionKey = descriptionKey;
        this.icon = icon;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Visibility draw(MatrixStack matrices, ToastManager manager, long currentTime) {
        if (this.justUpdated) {
            this.startTime = currentTime;
            this.justUpdated = false;
        }

        MinecraftClient client = manager.getGame();
        TextRenderer textRenderer = client.textRenderer;
        client.getTextureManager().bindTexture(TEXTURE);
        RenderSystem.color3f(1.0F, 1.0F, 1.0F);
        manager.drawTexture(matrices, 0, 0, 0, 0, 160, 32);

        int xPos = this.icon != null ? 30: 10;
        int lineSize;
        List<OrderedText> titleList = textRenderer.wrapLines(StringVisitable.plain(this.titleKey), 115 + (this.icon != null ? 10 : 30));
        if (this.descriptionKey == null) {
            String[] title = this.titleKey.split("\n");
            if (title.length == 2) {
                textRenderer.draw(matrices, title[0], xPos, 7, 16777215);
                textRenderer.draw(matrices, title[1], xPos, 18, 16777215);
                lineSize = 2;
            } else if (titleList.size() > 1) {
                textRenderer.draw(matrices, titleList.get(0), xPos, 7, 16777215);
                textRenderer.draw(matrices, titleList.get(1), xPos, 18, 16777215);
                lineSize = 2;
            } else {
                textRenderer.draw(matrices, this.titleKey, xPos, 12, 16777215);
                lineSize = 1;
            }
        } else {
            List<OrderedText> descriptionList = textRenderer.wrapLines(StringVisitable.plain(this.descriptionKey), 115 + (this.icon != null ? 10 : 30));
            if (titleList.size() + descriptionList.size() > 2) {
                lineSize = Math.min(titleList.size(), 2) + Math.min(descriptionList.size(), 2);

                long timestamp = Math.abs(2000L + (lineSize * 500L) - (currentTime - this.startTime));
                float col = Math.min(timestamp / 400F, 1F);
                int color = BackgroundHelper.ColorMixer.getArgb(MathHelper.floor(col * 255), 255, 255, 255);

                if (timestamp > 8) {
                    if (currentTime - this.startTime >= 2000L + (lineSize * 500L)) {
                        if (descriptionList.size() > 1) {
                            textRenderer.draw(matrices, descriptionList.get(0), xPos, 7, color);
                            textRenderer.draw(matrices, descriptionList.get(1), xPos, 18, color);
                        } else {
                            textRenderer.draw(matrices, this.descriptionKey, xPos, 12, color);
                        }
                    } else {
                        if (titleList.size() > 1) {
                            textRenderer.draw(matrices, titleList.get(0), xPos, 7, color);
                            textRenderer.draw(matrices, titleList.get(1), xPos, 18, color);
                        } else {
                            textRenderer.draw(matrices, this.titleKey, xPos, 12, color);
                        }
                    }
                }
            } else {
                textRenderer.draw(matrices, this.titleKey, xPos, 7, 0xFFFFFF);
                textRenderer.draw(matrices, this.descriptionKey, xPos, 18, 0xFFFFFF);
                lineSize = 2;
            }
        }

        if(this.icon != null) manager.getGame().getItemRenderer().renderGuiItemIcon(this.icon, 8, 8);

        return currentTime - this.startTime < 4000L + (lineSize * 1000L) ? Toast.Visibility.SHOW : Toast.Visibility.HIDE;
    }
}

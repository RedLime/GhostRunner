package com.redlimerl.ghostrunner.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class GenericToast implements Toast {

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
        client.getTextureManager().bindTexture(TOASTS_TEX);
        RenderSystem.color3f(1.0F, 1.0F, 1.0F);
        manager.drawTexture(matrices, 0, 0, 0, 32, 160, 32);
        float xpos = this.icon != null ? 30: 10;
        if (this.descriptionKey == null) {
            client.textRenderer.draw(matrices, this.getLocalizedTitle(), xpos, 12, 0x000000);
        } else {
            client.textRenderer.draw(matrices, this.getLocalizedTitle(), xpos, 7, 0x000000);
            client.textRenderer.draw(matrices, this.getLocalizedDescription(), xpos, 18, 0x000000);
        }

        if(this.icon != null) manager.getGame().getItemRenderer().renderGuiItemIcon(this.icon, 8, 8);

        return currentTime - this.startTime < 5000L ? Toast.Visibility.SHOW : Toast.Visibility.HIDE;
    }

    public String getLocalizedDescription() {
        return I18n.translate(this.descriptionKey);
    }

    public String getLocalizedTitle() {
        return I18n.translate(this.titleKey);
    }

}

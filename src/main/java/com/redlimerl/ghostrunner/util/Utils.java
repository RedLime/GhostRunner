package com.redlimerl.ghostrunner.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.data.RunnerOptions;
import com.redlimerl.ghostrunner.data.UpdateStatus;
import com.redlimerl.ghostrunner.record.ReplayGhost;
import com.redlimerl.speedrunigt.option.SpeedRunOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public class Utils {

    public static int getUpdateButtonOffset() {
        return GhostRunner.UPDATE_STATUS.getStatus() == UpdateStatus.Status.OUTDATED && SpeedRunOptions.getOption(RunnerOptions.UPDATE_NOTIFICATION) ? 20 : 0;
    }

    public static UUID UUIDFromString(String string) {
        if (string.contains("-")) {
            return UUID.fromString(string);
        } else {
            Pattern pattern = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");
            String uuid = pattern.matcher(string).replaceAll("$1-$2-$3-$4-$5");
            return UUID.fromString(uuid);
        }
    }

    public static void downloadPlayerSkin(UUID uuid) {
        try {
            new Thread(() -> {
                MinecraftSessionService sessionService = MinecraftClient.getInstance().getSessionService();
                PlayerSkinProvider skinProvider = MinecraftClient.getInstance().getSkinProvider();

                GameProfile profile = sessionService.fillProfileProperties(new GameProfile(uuid, null), false);

                if (profile != null) {
                    Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> minecraftProfileTexture = skinProvider.getTextures(profile);

                    if (minecraftProfileTexture != null) {
                        Identifier skin = skinProvider.loadSkin(minecraftProfileTexture.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
                        ReplayGhost.addPlayerSkin(uuid, skin);
                    }
                }
            }).start();
        } catch (Exception e) {
            GhostRunner.debug("Failed load ghost's skin.");
        }
    }

    public static boolean isUrl(String text) {
        return text.matches("^(http|https|ftp)://.*$");
    }
}

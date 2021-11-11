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
import org.jetbrains.annotations.NotNull;

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
        new Thread(() -> {
            try {
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
            } catch (Exception e) {
                GhostRunner.debug("Failed load ghost's skin.");
            }
        }).start();
    }

    public static boolean isUrl(String text) {
        return text.matches("^(http|https|ftp)://.*$");
    }

    /**
     * @param left compare version A
     * @param right compare version B
     * @return if left > right = 1 / if left == right = 0 / if left < right = -1
     */
    public static int compareVersion(@NotNull String left, @NotNull String right) {
        if (left.equals(right)) {
            return 0;
        }
        int leftStart = 0, rightStart = 0, result;
        do {
            int leftEnd = left.indexOf('.', leftStart);
            int rightEnd = right.indexOf('.', rightStart);
            Integer leftValue = Integer.parseInt(leftEnd < 0
                    ? left.substring(leftStart)
                    : left.substring(leftStart, leftEnd));
            Integer rightValue = Integer.parseInt(rightEnd < 0
                    ? right.substring(rightStart)
                    : right.substring(rightStart, rightEnd));
            result = leftValue.compareTo(rightValue);
            leftStart = leftEnd + 1;
            rightStart = rightEnd + 1;
        } while (result == 0 && leftStart > 0 && rightStart > 0);
        if (result == 0) {
            if (leftStart > rightStart) {
                return containsNonZeroValue(left, leftStart) ? 1 : 0;
            }
            if (leftStart < rightStart) {
                return containsNonZeroValue(right, rightStart) ? -1 : 0;
            }
        }
        return result;
    }

    private static boolean containsNonZeroValue(String str, int beginIndex) {
        for (int i = beginIndex; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c != '0' && c != '.') {
                return true;
            }
        }
        return false;
    }
}

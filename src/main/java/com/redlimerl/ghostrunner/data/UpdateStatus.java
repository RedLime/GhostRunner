package com.redlimerl.ghostrunner.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.gui.GenericToast;
import com.redlimerl.speedrunigt.option.SpeedRunOptions;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.impl.util.version.VersionParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UpdateStatus {

    private Status status = Status.NONE;
    private String lastVersion = "";
    private String downloadUrl = "";
    private boolean isPopped = false;

    public enum Status {
        NONE, UNKNOWN, UPDATED, OUTDATED
    }

    public void check() {
        new Thread(() -> {
            try {
                URL u = new URL("https://api.github.com/repos/RedLime/GhostRunner/releases");
                HttpURLConnection c = (HttpURLConnection) u.openConnection();

                c.setConnectTimeout(10000);
                c.setReadTimeout(10000);

                InputStreamReader r = new InputStreamReader(c.getInputStream(), StandardCharsets.UTF_8);
                JsonElement jsonElement = new JsonParser().parse(r);
                if (jsonElement.getAsJsonArray().size() == 0) {
                    this.status = Status.UNKNOWN;
                } else {
                    SemanticVersion version = VersionParser.parseSemantic(GhostRunner.MOD_VERSION);
                    for (JsonElement element : jsonElement.getAsJsonArray()) {
                        JsonObject versionData = element.getAsJsonObject();
                        Version target = Version.parse(versionData.get("tag_name").getAsString());
                        if (version.compareTo(target) < 0 && !versionData.get("prerelease").getAsBoolean()) {
                            for (JsonElement asset : versionData.get("assets").getAsJsonArray()) {
                                JsonObject assetData = asset.getAsJsonObject();
                                if (assetData.get("name").getAsString().endsWith(GhostRunner.CLIENT_VERSION + ".jar")) {
                                    this.status = Status.OUTDATED;
                                    this.downloadUrl = assetData.get("browser_download_url").getAsString();
                                    this.lastVersion = target.getFriendlyString();
                                    break;
                                }
                            }
                        }
                    }

                    if (status == Status.NONE) {
                        this.status = Status.UPDATED;
                    }
                }
            } catch (IOException | VersionParsingException e) {
                this.status = Status.UNKNOWN;
            }
        }).start();
    }

    public Status getStatus() {
        return status;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getLastVersion() {
        return lastVersion;
    }

    public void popNotice() {
        if (!isPopped && status == Status.OUTDATED) {
            isPopped = true;
            if (!SpeedRunOptions.getOption(RunnerOptions.UPDATE_NOTIFICATION)) return;
            MinecraftClient client = MinecraftClient.getInstance();
            client.getToastManager().add(new GenericToast(I18n.translate("ghostrunner.message.update.found_new_update"), null, new ItemStack(Items.ANVIL)));
        }
    }

}

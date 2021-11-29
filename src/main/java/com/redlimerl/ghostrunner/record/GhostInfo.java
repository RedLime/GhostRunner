package com.redlimerl.ghostrunner.record;

import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.crypt.Crypto;
import com.redlimerl.ghostrunner.gui.GenericToast;
import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.record.data.Timeline;
import com.redlimerl.speedrunigt.option.SpeedRunOptions;
import com.redlimerl.speedrunigt.timer.InGameTimer;
import com.redlimerl.speedrunigt.timer.RunCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.gen.GeneratorOptions;
import org.apache.commons.compress.utils.Charsets;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.OptionalLong;
import java.util.Queue;
import java.util.UUID;

public class GhostInfo {

    public static final GhostInfo INSTANCE = new GhostInfo();
    public static GhostType currentGhostType = GhostType.RANDOM_SEED;
    static {
        InGameTimer.onComplete(igt -> {
            InGameTimer timer = InGameTimer.getInstance();
            MinecraftClient.getInstance().getToastManager().add(
                    new GenericToast("IGT: "+InGameTimer.timeToStringFormat(timer.getInGameTime()),
                            "RTA: "+InGameTimer.timeToStringFormat(timer.getRealTimeAttack()), new ItemStack(Items.DRAGON_EGG))
            );
            MinecraftClient.getInstance().getToastManager().add(
                    new GenericToast("Category : " + timer.getCategory().getText().getString(),
                            "Seed Type : " + currentGhostType.getContext(), null)
            );
            MinecraftClient.getInstance().getToastManager().add(
                    new GenericToast("Seed : "+INSTANCE.ghostData.getSeed(), null,null)
            );
            INSTANCE.save();
        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static GhostInfo fromData(UUID uuid) {
        Path path = GhostRunner.GHOSTS_PATH.resolve(uuid.toString());
        path.toFile().mkdirs();

        File grDataFile = new File(path.toFile(), ".grd");
        File grTimelineFile = new File(path.toFile(), ".grt");

        if (!grDataFile.exists()) throw new IllegalArgumentException("Not found a Record file");

        GhostInfo ghost = new GhostInfo();
        GhostData ghostData = GhostData.loadData(path);

        try {
            for (String s : Crypto.decrypt(FileUtils.readFileToString(grDataFile, StandardCharsets.UTF_8), ghostData.getKey()).split("&")) {
                ghost.update(PlayerLog.of(s));
            }

            ghost.timeline = new Timeline(FileUtils.readFileToString(grTimelineFile, StandardCharsets.UTF_8));
            ghost.ghostData = ghostData;
            return ghost;
        } catch (IOException e) {
            throw new IllegalArgumentException("Corrupted record files");
        }
    }


    public final Queue<PlayerLog> logData = new LinkedList<>();
    private Timeline timeline = new Timeline();
    private GhostData ghostData = null;

    public GhostData getGhostData() {
        return ghostData;
    }

    private InGameTimer getTimer() {
        return InGameTimer.getInstance();
    }

    public String toDataString() {
        StringBuilder stringBuilder = new StringBuilder();
        PlayerLog prevLog = null;
        for (PlayerLog logDatum : logData) {
            if (prevLog == null) {
                stringBuilder.append(logDatum.toString()).append("&");
            } else {
                stringBuilder.append(logDatum.toString(prevLog)).append("&");
            }
            prevLog = logDatum;
        }
        return stringBuilder.substring(0, stringBuilder.length()-1);
    }

    public void setup(GeneratorOptions generatorOptions) {
        this.clear();
        currentGhostType = GhostRunner.OPTIONAL_LONG.isPresent()
                ? (GhostRunner.IS_FSG && SpeedRunOptions.getOption(SpeedRunOptions.TIMER_CATEGORY) == RunCategory.ANY ? GhostType.FILTERED_SEED : GhostType.SET_SEED)
                : GhostType.RANDOM_SEED;
        ghostData = GhostData.create(generatorOptions, currentGhostType, GhostRunner.IS_HARDCORE);

        GhostRunner.OPTIONAL_LONG = OptionalLong.empty();
    }

    public void clear() {
        ghostData = null;
        timeline = new Timeline();
        logData.clear();
    }

    public boolean setCheckPoint(Timeline.Moment moment) {
        return timeline.addTimeline(moment, getTimer().getInGameTime());
    }

    public long getCheckPoint(Timeline.Moment moment) {
        return timeline.getTimeline(moment);
    }

    public void update(PlayerLog log) {
        logData.add(log);
    }

    public PlayerLog pollPlayerLog() {
        return logData.poll();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void save() {
        boolean isSolo = MinecraftClient.getInstance().isInSingleplayer();
        new Thread(() -> {
            ghostData.setRealTimeAttack(getTimer().getRealTimeAttack());
            ghostData.setInGameTime(getTimer().getInGameTime());
            ghostData.setGhostCategory(getTimer().getCategory());
            ghostData.updateCreatedDate();
            ghostData.setUseF3(GhostRunner.IS_USE_F3);
            ghostData.setSubmittable(!(!isSolo || GhostRunner.IS_USE_GHOST));
            ghostData.setGhostName(ghostData.getDefaultName());
            String playData = Crypto.encrypt(this.toDataString(), ghostData.getKey());

            File ghostFile = ghostData.getPath().toFile();
            ghostFile.mkdirs();

            try {
                FileUtils.writeStringToFile(new File(ghostFile, ".gri"), ghostData.toString(), Charsets.UTF_8);
                FileUtils.writeStringToFile(new File(ghostFile, ".grt"), timeline.toString(), Charsets.UTF_8);
                FileUtils.writeStringToFile(new File(ghostFile, ".grd"), playData, Charsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

package com.redlimerl.ghostrunner.record;

import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.crypt.Crypto;
import com.redlimerl.ghostrunner.data.RunnerStatistic;
import com.redlimerl.ghostrunner.gui.GenericToast;
import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.record.data.Timeline;
import com.redlimerl.speedrunigt.timer.InGameTimer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
    public static GhostType currentGhostType = GhostType.RSG;
    private static final InGameTimer inGameTimer = InGameTimer.INSTANCE;
    static {
        InGameTimer.onComplete(igt -> {
            GhostRunner.isComplete = true;
            MinecraftClient.getInstance().getToastManager().add(
                    new GenericToast("IGT: "+InGameTimer.timeToStringFormat(inGameTimer.getInGameTime()),
                            "RTA: "+InGameTimer.timeToStringFormat(inGameTimer.getRealTimeAttack()), new ItemStack(Items.DRAGON_EGG))
            );
            
            RunnerStatistic.addStatistic(RunnerStatistic.Type.SHOW_CREDIT_SCREENS);
            RunnerStatistic.updateBestStatistic(RunnerStatistic.Type.BEST_TIME, (int) igt.getInGameTime());
            if (INSTANCE.ghostData.getType() == GhostType.RSG)
                RunnerStatistic.updateBestStatistic(RunnerStatistic.Type.BEST_RSG_TIME, (int) igt.getInGameTime());
            else if (INSTANCE.ghostData.getType() == GhostType.FSG)
                RunnerStatistic.updateBestStatistic(RunnerStatistic.Type.BEST_FSG_TIME, (int) igt.getInGameTime());
            else
                RunnerStatistic.updateBestStatistic(RunnerStatistic.Type.BEST_SSG_TIME, (int) igt.getInGameTime());
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

    public void setup(long seed) {
        this.clear();
        currentGhostType = GhostRunner.optionalLong.isPresent() ? (GhostRunner.isFsg ? GhostType.FSG : GhostType.SSG) : GhostType.RSG;
        ghostData = GhostData.create(seed, currentGhostType, GhostRunner.isHardcore);

        GhostRunner.optionalLong = OptionalLong.empty();
    }

    public void clear() {
        ghostData = null;
        timeline = new Timeline();
        logData.clear();
    }

    public boolean setCheckPoint(Timeline.Moment moment) {
        return timeline.addTimeline(moment, inGameTimer.getInGameTime());
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
        ghostData.setRealTimeAttack(inGameTimer.getRealTimeAttack());
        ghostData.setInGameTime(inGameTimer.getInGameTime());
        ghostData.updateCreatedDate();
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
    }

    @Override
    public String toString() {
        return "GhostInfo{" +
                "logData=" + logData.size() +
                ", timeline=" + timeline +
                ", ghostData=" + ghostData +
                '}';
    }
}

package com.redlimerl.ghostrunner.data;

import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.record.data.GhostType;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class RunnerStatistic {

    public enum Type {
        CREATED_WORLDS, CREATED_FSG_WORLDS, CREATED_RSG_WORLDS, CREATED_SSG_WORLDS, CREATED_HARDCORE_WORLDS,
        SHOW_CREDIT_SCREENS, TOTAL_DEATHS, BEST_TIME, BEST_FSG_TIME, BEST_RSG_TIME, BEST_SSG_TIME
    }

    private static final HashMap<Type, Integer> statistic = new HashMap<>();
    static {
        statistic.putIfAbsent(Type.CREATED_WORLDS, 0); statistic.putIfAbsent(Type.CREATED_FSG_WORLDS, 0); statistic.putIfAbsent(Type.CREATED_RSG_WORLDS, 0);
        statistic.putIfAbsent(Type.CREATED_SSG_WORLDS, 0); statistic.putIfAbsent(Type.CREATED_HARDCORE_WORLDS, 0); statistic.putIfAbsent(Type.SHOW_CREDIT_SCREENS, 0);
        statistic.putIfAbsent(Type.TOTAL_DEATHS, 0); statistic.putIfAbsent(Type.BEST_TIME, 0); statistic.putIfAbsent(Type.BEST_FSG_TIME, 0);
        statistic.putIfAbsent(Type.BEST_RSG_TIME, 0); statistic.putIfAbsent(Type.BEST_SSG_TIME, 0);
    }

    public static int getStatistic(Type type) {
        return statistic.getOrDefault(type, 0);
    }

    public static void addStatistic(Type type, int amount) {
        statistic.put(type, getStatistic(type) + amount);
        save();
    }

    public static void addStatistic(Type type) {
        addStatistic(type, 1);
    }

    public static void updateBestStatistic(Type type, int update) {
        int before = getStatistic(type);
        if (before == 0 || before > update) {
            statistic.put(type, update);
            save();
        }
    }

    public static boolean isBestRecord(GhostType ghostType, long time) {
        if (ghostType == GhostType.RSG) {
            return getStatistic(Type.BEST_RSG_TIME) > time || getStatistic(Type.BEST_RSG_TIME) == 0;
        } else if (ghostType == GhostType.SSG) {
            return getStatistic(Type.BEST_SSG_TIME) > time || getStatistic(Type.BEST_SSG_TIME) == 0;
        } else {
            return getStatistic(Type.BEST_FSG_TIME) > time || getStatistic(Type.BEST_FSG_TIME) == 0;
        }
    }

    public static void init() {
        Path path = GhostRunner.MAIN_PATH;

        File statisticFile = new File(path.toFile(), "statistics.txt");
        if (statisticFile.exists()) {
            try {
                for (String s : FileUtils.readFileToString(statisticFile, StandardCharsets.UTF_8).split("\n")) {
                    String[] op = s.split(":");
                    try {
                        statistic.put(Type.valueOf(op[0]), Integer.parseInt(op[1]));
                    } catch (Exception ignore) {}
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void save() {
        Path path = GhostRunner.MAIN_PATH;
        final StringBuilder builder = new StringBuilder();
        for (Map.Entry<Type, Integer> typeIntegerEntry : statistic.entrySet()) {
            builder.append(typeIntegerEntry.getKey().name()).append(":").append(typeIntegerEntry.getValue()).append("\n");
        }
        try {
            FileUtils.writeStringToFile(new File(path.toFile(), "statistics.txt"), builder.substring(0, builder.length()-1), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

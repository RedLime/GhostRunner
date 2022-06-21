package com.redlimerl.ghostrunner.record.data;

import java.util.HashMap;

public class Timeline {
    public enum Moment {
        ENTER_NETHER, ENTER_PIGLIN_BRUTE, ENTER_NETHER_FORTRESS, ENTER_THE_END
    }

    public Timeline() {}
    public Timeline(String data) {
        if (data.isEmpty()) return;
        for (String s : data.split("\n")) {
            String[] d = s.split(":");
            timeline.put(Moment.valueOf(d[0]), Long.parseLong(d[1]));
        }
    }

    private final HashMap<Moment, Long> timeline = new HashMap<>();

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        timeline.forEach((moment, integer) -> stringBuilder.append(moment.name()).append(":").append(integer).append("\n"));
        return timeline.size() == 0 ? "" : stringBuilder.substring(0, stringBuilder.length()-1);
    }

    public boolean addTimeline(Moment moment, long time) {
        if (!timeline.containsKey(moment)) {
            timeline.put(moment, time);
            return true;
        }
        return false;
    }

    public long getTimeline(Moment moment) {
        return timeline.getOrDefault(moment, 0L);
    }
}

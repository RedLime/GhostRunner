package com.redlimerl.ghostrunner.record.data;

public enum GhostType {
    SET_SEED(2, "Set Seed"), RANDOM_SEED(4, "Random Seed"), FILTERED_SEED(8, "Filtered Seed");

    private final int id;
    private final String context;

    GhostType(int id, String context) {
        this.id = id;
        this.context = context;
    }

    public int getId() {
        return id;
    }

    public String getContext() {
        return context;
    }
}

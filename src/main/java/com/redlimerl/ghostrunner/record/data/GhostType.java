package com.redlimerl.ghostrunner.record.data;

public enum GhostType {
    SSG(2), RSG(4), FSG(8);

    int id;
    GhostType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

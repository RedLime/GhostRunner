package com.redlimerl.ghostrunner.util.submit;

import com.redlimerl.ghostrunner.util.Utils;

public class SubmitVersionGroup {

    private final String value;
    private final String version1;
    private final String version2;

    public SubmitVersionGroup(String value, String version1, String version2) {
        this.value = value;
        this.version1 = version1;
        this.version2 = version2;
    }

    public boolean isIn(String version) {
        return Utils.compareVersion(version, version1) >= 0 && (version2.isEmpty() || Utils.compareVersion(version, version2) < 0);
    }

    public String getValue() {
        return value;
    }
}

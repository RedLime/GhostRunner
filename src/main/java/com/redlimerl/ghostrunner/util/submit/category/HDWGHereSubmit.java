package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;
import com.redlimerl.ghostrunner.util.submit.SubmitVersionGroup;

import java.util.ArrayList;
import java.util.List;

public class HDWGHereSubmit extends ExtensionCategorySubmit {

    public HDWGHereSubmit(GhostData ghostData, String description, String videoUrl, boolean isGlitch) {
        super(ghostData, description, videoUrl, isGlitch);

        //SS/RS/SSG/RSG
        if (isGlitchRun()) {
            this.updateVariable(new SubmitVariable("onv23j0l", ghostData.getType() == GhostType.RANDOM_SEED ? "21g22wnl" : "4qye44d1"));
        } else {
            this.updateVariable(new SubmitVariable("onv23j0l", ghostData.getType() == GhostType.RANDOM_SEED ? "klryyejq" : "jqzxxrg1"));
        }

        //Version (HDWGH)
        this.updateVariable(getVersionRangeVariable("e8m6yrwl", getVersion()));
    }

    @Override
    public boolean isSupportGlitchRun() {
        return true;
    }

    @Override
    public String getCategoryKey() {
        return "xk987oy2";
    }

    public List<SubmitVersionGroup> getVersion() {
        ArrayList<SubmitVersionGroup> versionGroups = new ArrayList<>();
        versionGroups.add(new SubmitVersionGroup("21dn8npl", "1.12", "1.13"));
        versionGroups.add(new SubmitVersionGroup("5q83v3y1", "1.13", "1.14"));
        versionGroups.add(new SubmitVersionGroup("4qym2m6q", "1.14", ""));
        return versionGroups;
    }
}

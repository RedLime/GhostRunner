package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;
import com.redlimerl.ghostrunner.util.submit.SubmitVersionGroup;

import java.util.ArrayList;
import java.util.List;

public class AllAdvancementSubmit extends MainCategorySubmit {
    public AllAdvancementSubmit(GhostData ghostData, String description, String videoUrl, boolean isGlitch) {
        super(ghostData, description, videoUrl, isGlitch);

        //Seed Type
        if (isGlitchRun()) {
            this.updateVariable(new SubmitVariable("p853vv0n", ghostData.getType() == GhostType.SET_SEED ? "5lmwz58q" : "zqo2d45l"));
        } else {
            this.updateVariable(new SubmitVariable("p853vv0n", ghostData.getType() == GhostType.SET_SEED ? "81wr3yml" : "01343grl"));
        }
    }

    @Override
    public boolean isSupportGlitchRun() {
        return true;
    }

    @Override
    public String getCategoryKey() {
        return "xk9gz16d";
    }

    @Override
    public String getVersionRangeVariableKey() {
        return "789je4qn";
    }

    @Override
    public List<SubmitVersionGroup> getVersionRange() {
        ArrayList<SubmitVersionGroup> versionGroups = new ArrayList<>();
        versionGroups.add(new SubmitVersionGroup("81pd4881", "1.12", "1.13"));
        versionGroups.add(new SubmitVersionGroup("xqkm97kl", "1.13", "1.14"));
        versionGroups.add(new SubmitVersionGroup("gq7r9kdl", "1.14", "1.15"));
        versionGroups.add(new SubmitVersionGroup("81p687gq", "1.15", "1.16"));
        versionGroups.add(new SubmitVersionGroup("klrw35m1", "1.16", "1.17"));
        versionGroups.add(new SubmitVersionGroup("mlnno7nl", "1.17", ""));
        return versionGroups;
    }
}

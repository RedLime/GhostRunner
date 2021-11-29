package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;
import com.redlimerl.ghostrunner.util.submit.SubmitVersionGroup;

import java.util.ArrayList;
import java.util.List;

public class AnyPercentSubmit extends MainCategorySubmit {

    public AnyPercentSubmit(GhostData ghostData, String description, String videoUrl, boolean isGlitch) {
        super(ghostData, description, videoUrl, isGlitch);

        //Seed Type
        if (isGlitchRun()) {
            this.updateVariable(new SubmitVariable("2lgzk1o8", ghostData.getType() == GhostType.SET_SEED ? "rqv4pz7q" : "5lekrv5l"));
        } else {
            this.updateVariable(new SubmitVariable("r8rg67rn", ghostData.getType() == GhostType.SET_SEED ? "klrzpjo1" : "21d4zvp1"));
        }
    }

    @Override
    public boolean isSupportGlitchRun() {
        return true;
    }

    @Override
    public String getCategoryKey() {
        return this.isGlitchRun() ? "wkpn0vdr" : "mkeyl926";
    }

    @Override
    public String getVersionRangeVariableKey() {
        return "wlexoyr8";
    }

    @Override
    public List<SubmitVersionGroup> getVersionRange() {
        ArrayList<SubmitVersionGroup> versionGroups = new ArrayList<>();
        if (isGlitchRun()) {
            versionGroups.add(new SubmitVersionGroup("jqzywv2l", "1.0", "1.9"));
            versionGroups.add(new SubmitVersionGroup("81pjwxn1", "1.9", "1.14"));
            versionGroups.add(new SubmitVersionGroup("klr6djol", "1.14", ""));
        } else {
            versionGroups.add(new SubmitVersionGroup("gq7zo9p1", "1.0", "1.9"));
            versionGroups.add(new SubmitVersionGroup("21go6e6q", "1.9", "1.16"));
            versionGroups.add(new SubmitVersionGroup("4qye4731", "1.16", ""));
        }
        return versionGroups;
    }
}

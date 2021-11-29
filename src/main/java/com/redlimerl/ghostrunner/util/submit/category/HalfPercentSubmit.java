package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;
import com.redlimerl.ghostrunner.util.submit.SubmitVersionGroup;

import java.util.ArrayList;
import java.util.List;

public class HalfPercentSubmit extends ExtensionCategorySubmit {

    public HalfPercentSubmit(GhostData ghostData, String description, String videoUrl, boolean isGlitch) {
        super(ghostData, description, videoUrl, isGlitch);

        //Version (Half%)
        this.updateVariable(this.getVersionRangeVariable("p85r42xn", getVersion()));

        //SS/SSG/RS/RSG
        if (isGlitchRun()) {
            this.updateVariable(new SubmitVariable("38dpzqx8", ghostData.getType() == GhostType.RANDOM_SEED ? "9qjvjv7q" : "jq6n3nvl"));
        } else {
            this.updateVariable(new SubmitVariable("38dpzqx8", ghostData.getType() == GhostType.RANDOM_SEED ? "mlnprwo1" : "810g3gol"));
        }
    }

    @Override
    public boolean isSupportGlitchRun() {
        return true;
    }

    @Override
    public String getCategoryKey() {
        return "xd148xrd";
    }

    public List<SubmitVersionGroup> getVersion() {
        ArrayList<SubmitVersionGroup> versionGroups = new ArrayList<>();
        versionGroups.add(new SubmitVersionGroup("4qyd3p6q", "1.16", "1.17"));
        return versionGroups;
    }
}

package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;
import com.redlimerl.ghostrunner.util.submit.SubmitVersionGroup;

import java.util.ArrayList;
import java.util.List;

public class FilteredSeedSubmit extends ExtensionCategorySubmit {

    public FilteredSeedSubmit(GhostData ghostData, String description, String videoUrl, boolean isGlitch) {
        super(ghostData, description, videoUrl, isGlitch);

        //Player Count
        this.updateVariable(new SubmitVariable("ql61eov8", "81pvroe1"));

        //Version (Filtered Seed Glitchless)
        this.updateVariable(this.getVersionRangeVariable("jlzrovq8", getVersion()));
    }

    @Override
    public boolean isSupportGlitchRun() {
        return false;
    }

    @Override
    public String getCategoryKey() {
        return "n2y9z41d";
    }

    public List<SubmitVersionGroup> getVersion() {
        ArrayList<SubmitVersionGroup> versionGroups = new ArrayList<>();
        versionGroups.add(new SubmitVersionGroup("mlnp8rd1", "1.16", ""));
        return versionGroups;
    }
}

package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;
import com.redlimerl.ghostrunner.util.submit.SubmitVersionGroup;

import java.util.ArrayList;
import java.util.List;

public class FSGlitchlessSubmit extends ExtensionCategorySubmit {

    public FSGlitchlessSubmit(GhostData ghostData, String description, String videoUrl) {
        super(ghostData, description, videoUrl);

        //Player Count
        this.updateVariable(new SubmitVariable("ql61eov8", "81pvroe1"));

        //Version (Filtered Seed Glitchless)
        this.updateVariable(this.getVersionGroupVariable("jlzrovq8", getVersion()));
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

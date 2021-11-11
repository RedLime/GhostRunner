package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;
import com.redlimerl.ghostrunner.util.submit.SubmitVersionGroup;

import java.util.ArrayList;
import java.util.List;

public class HalfPercentSubmit extends ExtensionCategorySubmit {

    public HalfPercentSubmit(GhostData ghostData, String description, String videoUrl) {
        super(ghostData, description, videoUrl);

        //Version (Half%)
        this.updateVariable(this.getVersionGroupVariable("p85r42xn", getVersion()));

        //SS/SSG/RS/RSG
        this.updateVariable(new SubmitVariable("38dpzqx8", ghostData.getType() == GhostType.RSG ? "mlnprwo1" : "810g3gol"));
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

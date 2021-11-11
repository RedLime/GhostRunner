package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;
import com.redlimerl.ghostrunner.util.submit.SubmitVersionGroup;

import java.util.ArrayList;
import java.util.List;

public class AllMineralsSubmit extends ExtensionCategorySubmit {

    public AllMineralsSubmit(GhostData ghostData, String description, String videoUrl) {
        super(ghostData, description, videoUrl);

        //SSG/RSG
        this.updateVariable(new SubmitVariable("ylqkwzvl", ghostData.getType() == GhostType.RSG ? "21dowjpq" : "klr6meol"));

        //Structures
        this.updateVariable(new SubmitVariable("gnxr1zgn", ghostData.isGenerateStructures() ? "mlnkn4ol" : "81098vo1"));

        //Version (All Minerals)
        this.updateVariable(getVersionGroupVariable("9l77rgzl", getVersion()));
    }

    @Override
    public String getCategoryKey() {
        return "ndx9ezod";
    }

    public List<SubmitVersionGroup> getVersion() {
        ArrayList<SubmitVersionGroup> versionGroups = new ArrayList<>();
        versionGroups.add(new SubmitVersionGroup("013eonkq", "1.5", "1.16"));
        versionGroups.add(new SubmitVersionGroup("rqvwk551", "1.16", "1.17"));
        versionGroups.add(new SubmitVersionGroup("xqk5on4q", "1.17", ""));
        return versionGroups;
    }
}

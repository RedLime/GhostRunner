package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;
import com.redlimerl.ghostrunner.util.submit.SubmitVersionGroup;

import java.util.ArrayList;
import java.util.List;

public class AllSwordsSubmit extends ExtensionCategorySubmit {

    public AllSwordsSubmit(GhostData ghostData, String description, String videoUrl) {
        super(ghostData, description, videoUrl);

        //SSG/RSG
        this.updateVariable(new SubmitVariable("yn21z528", ghostData.getType() == GhostType.RSG ? "0q5g53nl" : "5lem4xz1"));

        //Structures
        this.updateVariable(new SubmitVariable("wlexrpk8", ghostData.isGenerateStructures() ? "klr6nmml" : "21dorw5q"));

        //Version (All Swords)
        this.updateVariable(getVersionGroupVariable("dloyre58", getVersion()));
    }

    @Override
    public String getCategoryKey() {
        return "7dg4popd";
    }

    public List<SubmitVersionGroup> getVersion() {
        ArrayList<SubmitVersionGroup> versionGroups = new ArrayList<>();
        versionGroups.add(new SubmitVersionGroup("jq6k96nl", "1.0", "1.16"));
        versionGroups.add(new SubmitVersionGroup("5lmjr0yl", "1.16", ""));
        return versionGroups;
    }
}

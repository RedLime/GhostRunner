package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;

public class FullIA15LevelsSubmit extends ExtensionCategorySubmit {

    public FullIA15LevelsSubmit(GhostData ghostData, String description, String videoUrl) {
        super(ghostData, description, videoUrl);

        //SSG/RSG
        this.updateVariable(new SubmitVariable("ql64j7k8", ghostData.getType() == GhostType.RSG ? "klry022q" : "jqzx53m1"));

        //Structures
        this.updateVariable(new SubmitVariable("68kdv6yl", ghostData.isGenerateStructures() ? "5q8r5o31" : "4qy83r21"));
    }

    @Override
    public String getCategoryKey() {
        return "wk6vy5xd";
    }
}

package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;

public class ArbalisticSubmit extends ExtensionCategorySubmit {

    public ArbalisticSubmit(GhostData ghostData, String description, String videoUrl) {
        super(ghostData, description, videoUrl);

        //SSG/RSG
        this.updateVariable(new SubmitVariable("j84edxyn", ghostData.getType() == GhostType.RSG ? "5legeoz1" : "013e203q"));

        //Structures
        this.updateVariable(new SubmitVariable("onvj9e0n", ghostData.isGenerateStructures() ? "013e3jrq" : "rqvw30r1"));
    }

    @Override
    public String getCategoryKey() {
        return "jdrlexgd";
    }
}

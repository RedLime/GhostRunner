package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;

public class EnterNetherSubmit extends ExtensionCategorySubmit {

    public EnterNetherSubmit(GhostData ghostData, String description, String videoUrl) {
        super(ghostData, description, videoUrl);

        //SSG/RSG
        this.updateVariable(new SubmitVariable("9l71q7ql", ghostData.getType() == GhostType.RSG ? "rqveryw1" : "0135m63q"));

        //Structures
        this.updateVariable(new SubmitVariable("wl337w9l", ghostData.isGenerateStructures() ? "21goy78q" : "jqzykd8l"));
    }

    @Override
    public String getCategoryKey() {
        return "z27lj0gd";
    }
}

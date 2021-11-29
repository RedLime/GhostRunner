package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;

public class EnterNetherSubmit extends ExtensionCategorySubmit {

    public EnterNetherSubmit(GhostData ghostData, String description, String videoUrl, boolean isGlitch) {
        super(ghostData, description, videoUrl, isGlitch);

        //SSG/RSG
        this.updateVariable(new SubmitVariable("9l71q7ql", ghostData.getType() == GhostType.RANDOM_SEED ? "rqveryw1" : "0135m63q"));

        //Structures
        this.updateVariable(new SubmitVariable("wl337w9l", ghostData.isGenerateStructures() ? "21goy78q" : "jqzykd8l"));
    }

    @Override
    public boolean isSupportGlitchRun() {
        return false;
    }

    @Override
    public String getCategoryKey() {
        return "z27lj0gd";
    }
}

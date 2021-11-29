package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;

public class HighPercentSubmit extends ExtensionCategorySubmit {

    public HighPercentSubmit(GhostData ghostData, String description, String videoUrl, boolean isGlitch) {
        super(ghostData, description, videoUrl, isGlitch);

        //RSG/SSG/RS/SS
        if (isGlitchRun()) {
            this.updateVariable(new SubmitVariable("j8453v5n", ghostData.getType() == GhostType.RANDOM_SEED ? "21d5d03l" : "klr97g0l"));
        } else {
            this.updateVariable(new SubmitVariable("j8453v5n", ghostData.getType() == GhostType.RANDOM_SEED ? "jqz94j41" : "21g5gmxl"));
        }
    }

    @Override
    public boolean isSupportGlitchRun() {
        return true;
    }

    @Override
    public String getCategoryKey() {
        return "jdr95l0d";
    }
}

package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;

public class HeroVillageSubmit extends ExtensionCategorySubmit {

    public HeroVillageSubmit(GhostData ghostData, String description, String videoUrl, boolean isGlitch) {
        super(ghostData, description, videoUrl, isGlitch);

        //SSG/RSG
        this.updateVariable(new SubmitVariable("ylqmmpzn", ghostData.getType() == GhostType.RANDOM_SEED ? "21gy6gm1" : "xqk738yl"));
    }

    @Override
    public boolean isSupportGlitchRun() {
        return false;
    }

    @Override
    public String getCategoryKey() {
        return "q25gyyyd";
    }
}

package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;

public class HeroVillageSubmit extends ExtensionCategorySubmit {

    public HeroVillageSubmit(GhostData ghostData, String description, String videoUrl) {
        super(ghostData, description, videoUrl);

        //SSG/RSG
        this.updateVariable(new SubmitVariable("ylqmmpzn", ghostData.getType() == GhostType.RSG ? "21gy6gm1" : "xqk738yl"));
    }

    @Override
    public String getCategoryKey() {
        return "q25gyyyd";
    }
}

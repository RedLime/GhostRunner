package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;

public class AllWorkstationsSubmit extends ExtensionCategorySubmit {

    public AllWorkstationsSubmit(GhostData ghostData, String description, String videoUrl) {
        super(ghostData, description, videoUrl);

        //SSG/RSG
        this.updateVariable(new SubmitVariable("78966qq8", ghostData.getType() == GhostType.RSG ? "mlnrvxjq" : "5q85vy3q"));
    }

    @Override
    public String getCategoryKey() {
        return "5dwg0znk";
    }
}

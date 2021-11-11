package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;

public class FullInventorySubmit extends ExtensionCategorySubmit {

    public FullInventorySubmit(GhostData ghostData, String description, String videoUrl) {
        super(ghostData, description, videoUrl);

        //SSG/RSG
        this.updateVariable(new SubmitVariable("gnxvj0pl", ghostData.getType() == GhostType.RSG ? "81032zw1" : "mlnrgw0q"));

        //Structures
        this.updateVariable(new SubmitVariable("9l77x1pl", ghostData.isGenerateStructures() ? "p123gpvl" : "81p8r2el"));
    }

    @Override
    public String getCategoryKey() {
        return "7kj6vw42";
    }
}

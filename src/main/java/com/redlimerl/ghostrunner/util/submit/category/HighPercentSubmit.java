package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.util.submit.SubmitData;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;
import com.redlimerl.ghostrunner.util.submit.SubmitVersionGroup;
import com.redlimerl.speedrunigt.timer.RunCategory;

import java.util.ArrayList;
import java.util.List;

public class HighPercentSubmit extends ExtensionCategorySubmit {

    public HighPercentSubmit(GhostData ghostData, String description, String videoUrl) {
        super(ghostData, description, videoUrl);

        //RSG/SSG/RS/SS
        this.updateVariable(new SubmitVariable("j8453v5n", ghostData.getType() == GhostType.RSG ? "jqz94j41" : "21g5gmxl"));
    }

    @Override
    public String getCategoryKey() {
        return "jdr95l0d";
    }
}

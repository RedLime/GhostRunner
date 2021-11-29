package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;

public class EnterEndSubmit extends ExtensionCategorySubmit {

    public EnterEndSubmit(GhostData ghostData, String description, String videoUrl, boolean isGlitch) {
        super(ghostData, description, videoUrl, isGlitch);

        //SS / SSG
        this.updateVariable(new SubmitVariable("ylqmo4wn", isGlitchRun() ? "81pw7jkl" : "xqkkrdyq"));
    }

    @Override
    public boolean isSupportGlitchRun() {
        return true;
    }

    @Override
    public String getCategoryKey() {
        return "mke4r1j2";
    }
}

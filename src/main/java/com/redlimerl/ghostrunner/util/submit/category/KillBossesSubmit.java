package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;
import com.redlimerl.speedrunigt.timer.category.RunCategories;

public class KillBossesSubmit extends ExtensionCategorySubmit {

    public KillBossesSubmit(GhostData ghostData, String description, String videoUrl, boolean isGlitch) {
        super(ghostData, description, videoUrl, isGlitch);

        //Boss
        this.updateVariable(new SubmitVariable("dlo31yel",
                ghostData.getGhostCategory() == RunCategories.KILL_ALL_BOSSES ? "9qjx48gl"
                        : ghostData.getGhostCategory() == RunCategories.KILL_WITHER ? "mlnz0odl"
                        : ghostData.getGhostCategory() == RunCategories.KILL_ELDER_GUARDIAN ? "810wmd5q" : ""));

        //SS/RS/SSG/RSG
        if (isGlitchRun()) {
            this.updateVariable(new SubmitVariable("jlz51w0l", ghostData.getType() == GhostType.RANDOM_SEED ? "81w5d051" : "jq6zxe7l"));
        } else {
            this.updateVariable(new SubmitVariable("jlz51w0l", ghostData.getType() == GhostType.RANDOM_SEED ? "zqor3v2q" : "5lmg4m4l"));
        }
    }

    @Override
    public boolean isSupportGlitchRun() {
        return true;
    }

    @Override
    public String getCategoryKey() {
        return "7kj1wexk";
    }
}

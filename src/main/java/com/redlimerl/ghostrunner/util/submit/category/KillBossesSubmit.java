package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;
import com.redlimerl.speedrunigt.timer.RunCategory;

public class KillBossesSubmit extends ExtensionCategorySubmit {

    public KillBossesSubmit(GhostData ghostData, String description, String videoUrl) {
        super(ghostData, description, videoUrl);

        //Boss
        this.updateVariable(new SubmitVariable("dlo31yel",
                ghostData.getGhostCategory() == RunCategory.KILL_ALL_BOSSES ? "9qjx48gl"
                        : ghostData.getGhostCategory() == RunCategory.KILL_WITHER ? "mlnz0odl"
                        : ghostData.getGhostCategory() == RunCategory.KILL_ELDER_GUARDIAN ? "810wmd5q" : ""));

        //SS/RS/SSG/RSG
        this.updateVariable(new SubmitVariable("jlz51w0l", ghostData.getType() == GhostType.RSG ? "zqor3v2q" : "5lmg4m4l"));
    }

    @Override
    public String getCategoryKey() {
        return "7kj1wexk";
    }
}

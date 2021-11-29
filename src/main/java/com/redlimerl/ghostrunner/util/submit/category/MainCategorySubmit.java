package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.util.submit.SubmitData;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;
import net.minecraft.world.Difficulty;

public abstract class MainCategorySubmit extends SubmitData {
    public MainCategorySubmit(GhostData ghostData, String description, String videoUrl, boolean isGlitch) {
        super(ghostData, description, videoUrl, isGlitch);
    }

    @Override
    public String getVersionsJson() {
        return "{\"mln68v0q\":\"1.16.1\",\"21go7k8q\":\"1.15.2\",\"gq7rrnnl\":\"1.14.4\",\"4lx5gk41\":\"1.8.9\",\"013xkx15\":\"1.7.10\",\"5lm2emqv\":\"1.7.4\",\"jq6vwj1m\":\"1.7.2\",\"9qj2o314\":\"1.6.4\",\"rqvx06l6\":\"1.0\",\"5len0klo\":\"1.1\",\"0q54m2lp\":\"1.2.1\",\"4lxgk4q2\":\"1.2.2\",\"81496vqd\":\"1.2.3\",\"z19xv814\":\"1.2.4\",\"p129k4lx\":\"1.2.5\",\"81pez8l7\":\"1.3.1\",\"xqko3k19\":\"1.3.2\",\"gq72od1p\":\"1.4.2\",\"21g968qz\":\"1.4.4\",\"jqzgw8lp\":\"1.4.5\",\"klrxdmlp\":\"1.4.6\",\"21d6n5qe\":\"1.4.7\",\"5q8433ld\":\"1.5.1\",\"4qy7m2q7\":\"1.5.2\",\"mlne7jlp\":\"1.6.1\",\"8106y21v\":\"1.6.2\",\"21d43441\":\"1.7.3\",\"81w72vq4\":\"1.7.5\",\"814o96vq\":\"1.7.6\",\"z192xv8q\":\"1.7.7\",\"p12v9k4q\":\"1.7.8\",\"zqojex1y\":\"1.7.9\",\"rqvx26l6\":\"1.8\",\"5lenyklo\":\"1.8.1\",\"21dv7g1e\":\"1.8.2\",\"5q8276qd\":\"1.8.3\",\"5le86mlo\":\"1.8.4\",\"01340kl5\":\"1.8.5\",\"rqvz7516\":\"1.8.6\",\"5levop1o\":\"1.8.7\",\"gq7zyr1p\":\"1.8.8\",\"81pyez81\":\"1.9\",\"xqkeo3kq\":\"1.9.1\",\"gq752od1\":\"1.9.2\",\"21gn968l\":\"1.9.3\",\"jqzngw8q\":\"1.9.4\",\"klr3xdml\":\"1.10\",\"5lmygj8l\":\"1.10.1\",\"jq678gv1\":\"1.10.2\",\"0q54o3nl\":\"1.11\",\"zqo0rw5q\":\"1.11.1\",\"4lxgvw4q\":\"1.11.2\",\"xqkonxn1\":\"1.12\",\"5q8270kq\":\"1.12.1\",\"p12ongvl\":\"1.12.2\",\"8142pg0l\":\"1.13\",\"z19rz201\":\"1.13.1\",\"z19ryv41\":\"1.13.2\",\"9qj49koq\":\"1.14\",\"01305er1\":\"1.14.1\",\"814vn2v1\":\"1.14.2\",\"jqzx69m1\":\"1.14.3\",\"gq7zrdd1\":\"1.15\",\"21dor7gq\":\"1.15.1\",\"mln64j6q\":\"1.16\",\"21d7zo31\":\"1.16.2\",\"21d7evp1\":\"1.16.3\",\"21dgwkj1\":\"1.16.4\",\"21dzz0jl\":\"1.16.5\",\"5q8ojzr1\":\"1.17\",\"4qy93w3l\":\"1.17.1\"}";
    }

    @Override
    public String getVersionVariableKey() {
        return "jlzkwql2";
    }

    @Override
    public SubmitVariable getModsVariable() {
        return new SubmitVariable("dloymqd8", "jq6kxd3l");
    }

    @Override
    public SubmitVariable getF3Variable() {
        return new SubmitVariable("ql6g2ow8", this.getGhostData().isUseF3() ? "rqvmvz6q" : "5lee2vkl");
    }

    @Override
    public SubmitVariable getDifficultyVariable() {
        String keyValue = null;
        if (this.getGhostData().isHardcore()) {
            keyValue = "p129j4lx";
        } else {
            if (this.getGhostData().getDifficulty() == Difficulty.EASY) keyValue = "4lxg24q2";
            else if (this.getGhostData().getDifficulty() == Difficulty.NORMAL) keyValue = "8149mvqd";
            else if (this.getGhostData().getDifficulty() == Difficulty.HARD) keyValue = "z19xe814";
        }
        return new SubmitVariable("9l737pn1", keyValue);
    }
}

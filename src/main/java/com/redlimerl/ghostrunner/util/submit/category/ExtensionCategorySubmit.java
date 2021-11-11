package com.redlimerl.ghostrunner.util.submit.category;

import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.util.submit.SubmitData;
import com.redlimerl.ghostrunner.util.submit.SubmitVariable;
import com.redlimerl.ghostrunner.util.submit.SubmitVersionGroup;
import net.minecraft.world.Difficulty;

import java.util.ArrayList;
import java.util.List;

public abstract class ExtensionCategorySubmit extends SubmitData {

    public ExtensionCategorySubmit(GhostData ghostData, String description, String videoUrl) {
        super(ghostData, description, videoUrl);
    }

    @Override
    public SubmitVariable getModsVariable() {
        return new SubmitVariable("jlzwkmql", "5lmj45jl");
    }

    @Override
    public SubmitVariable getF3Variable() {
        return new SubmitVariable("ylqkjo3l", this.getGhostData().isUseF3() ? "zqorkv5q" : "81w5z0m1");
    }

    @Override
    public SubmitVariable getDifficultyVariable() {
        String keyValue = null;
        if (this.getGhostData().isHardcore()) {
            keyValue = "21d33k4q";
        } else {
            if (this.getGhostData().getDifficulty() == Difficulty.EASY) keyValue = "21g22nnl";
            else if (this.getGhostData().getDifficulty() == Difficulty.NORMAL) keyValue = "jqzxxng1";
            else if (this.getGhostData().getDifficulty() == Difficulty.HARD) keyValue = "klryy3jq";
        }
        return new SubmitVariable("0nwkeorn", keyValue);
    }

    @Override
    public String getVersionsJson() {
        return "{\"21d7k251\":\"1.16.1\",\"81w5zz61\":\"1.0\",\"21d3wjpq\":\"1.1\",\"klr6xe0l\":\"1.2.1\",\"21do6j3q\":\"1.2.2\",\"5q8r4xr1\":\"1.2.3\",\"4qy87v31\":\"1.2.4\",\"mlnke46l\":\"1.2.5\",\"jq6gv9n1\":\"1.3.1\",\"5lmw2ryq\":\"1.3.2\",\"81wr7v9l\":\"1.4.2\",\"zqo2j8gl\":\"1.4.4\",\"0134xokl\":\"1.4.5\",\"rqvzxk51\":\"1.4.6\",\"5levn6p1\":\"1.4.7\",\"xqk6oe91\":\"1.5.1\",\"21g2v6ol\":\"1.5.2\",\"jqzygn4l\":\"1.6.1\",\"klr6x30l\":\"1.6.2\",\"gq7z25r1\":\"1.6.4\",\"rqveg7y1\":\"1.7.2\",\"p12nxx41\":\"1.7.3\",\"21go9noq\":\"1.7.4\",\"jqzygnkl\":\"1.7.5\",\"klr6x3wl\":\"1.7.6\",\"21do6kgq\":\"1.7.7\",\"5q8r4k61\":\"1.7.8\",\"4qyj9x7q\":\"1.7.9\",\"9qje2601\":\"1.7.10\",\"4qy87zd1\":\"1.8.1\",\"xqkm234l\":\"1.8\",\"mlnke8nl\":\"1.8.2\",\"81096vp1\":\"1.8.3\",\"9qje27o1\":\"1.8.4\",\"jq6gv5o1\":\"1.8.5\",\"5lmw2o0q\":\"1.8.6\",\"81wr746l\":\"1.8.7\",\"0q5gywvl\":\"1.8.8\",\"0q5gy6vl\":\"1.8.9\",\"0134x9yl\":\"1.9\",\"rqvzxvy1\":\"1.9.1\",\"5levnn61\":\"1.9.2\",\"0q5744vl\":\"1.9.3\",\"4lxdgggq\":\"1.9.4\",\"814499k1\":\"1.10\",\"z19dxx4l\":\"1.10.1\",\"p124992q\":\"1.10.2\",\"klrymeoq\":\"1.11\",\"81p6eenq\":\"1.11.1\",\"xqk6oo41\":\"1.11.2\",\"81w5zg61\":\"1.12\",\"gq7z22r1\":\"1.12.1\",\"4qyjdrdq\":\"1.12.2\",\"21go99oq\":\"1.13\",\"jqzyggkl\":\"1.13.1\",\"814786k1\":\"1.13.2\",\"gq7r0orl\":\"1.14\",\"klr6xxwl\":\"1.14.1\",\"mlnzpnnl\":\"1.14.2\",\"4lxerkg1\":\"1.14.3\",\"rqveg9y1\":\"1.14.4\",\"9qje4pg1\":\"1.15\",\"21go2jxq\":\"1.15.1\",\"5lmwp28q\":\"1.15.2\",\"81wo6g9l\":\"20w14∞\",\"4qyevg31\":\"1.16\",\"jq6krmjl\":\"1.16.2\",\"xqkkxv4q\":\"1.16.3\",\"81poj3kq\":\"1.16.4\",\"p12ddn4q\":\"1.16.5\",\"mlnpzj01\":\"15w14a\",\"gq7pv7yq\":\"1.17\",\"z19k460q\":\"alpha 1.0.4\",\"21g5ovml\":\"1.17.1\",\"jqz9ydm1\":\"1.17.2\",\"5le0zj5q\":\"1.6\",\"81pz9jnl\":\"1.17.41\"}";
    }

    @Override
    public String getVersionVariableKey() {
        return "j846z5wl";
    }

    @Override
    public String getVersionGroupVariableKey() {
        return "ylpm5erl";
    }

    @Override
    public List<SubmitVersionGroup> getVersionGroups() {
        ArrayList<SubmitVersionGroup> versionGroups = new ArrayList<>();
        versionGroups.add(new SubmitVersionGroup("5lemm651", "1.7", "1.8"));
        versionGroups.add(new SubmitVersionGroup("0q5gg9ml", "1.8", "1.9"));
        versionGroups.add(new SubmitVersionGroup("4lxeev21", "1.9", "1.10"));
        versionGroups.add(new SubmitVersionGroup("81477501", "1.10", "1.11"));
        versionGroups.add(new SubmitVersionGroup("z1988j0q", "1.11", "1.12"));
        versionGroups.add(new SubmitVersionGroup("p12xx67l", "1.12", "1.13"));
        versionGroups.add(new SubmitVersionGroup("81pdd3g1", "1.13", "1.14"));
        versionGroups.add(new SubmitVersionGroup("xqkmmgnl", "1.14", "1.15"));
        versionGroups.add(new SubmitVersionGroup("jq6gx871", "1.15", "1.16"));
        versionGroups.add(new SubmitVersionGroup("81p8o6el", "1.16", "1.17"));
        versionGroups.add(new SubmitVersionGroup("810po8jl", "1.17", ""));
        return versionGroups;
    }
}

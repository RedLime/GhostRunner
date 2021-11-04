package com.redlimerl.ghostrunner.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.MCSpeedRunAPI;
import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.util.Utils;

import java.text.SimpleDateFormat;
import java.util.HashMap;

@SuppressWarnings("unused")
public class SubmitData {

    private static final Gson GSON = new GsonBuilder().create();

    public final String category;
    public final String date;
    public final String platform = "8gej2n93";
    public final boolean verified = false;
    public final boolean emulated = false;
    public final String video;
    public final String comment;
    public final HashMap<String, Float> times = new HashMap<>();
    public final HashMap<String, Variable> variables = new HashMap<>();

    public static class Variable {
        public final String value;
        public final String type;

        public Variable(String value) {
            this.type = "pre-defined";
            this.value = value;
        }
    }

    public SubmitData(GhostData ghostData, String description, String videoUrl) {
        this.category = ghostData.getType() == GhostType.FSG ? MCSpeedRunAPI.CATEGORY_FSG : MCSpeedRunAPI.CATEGORY_ANY_PERCENT;
        this.date = new SimpleDateFormat("yyyy-MM-dd").format(ghostData.getCreatedDate());

        if (!Utils.isUrl(videoUrl)) throw new IllegalArgumentException("video url is not URL");
        this.video = videoUrl;
        this.comment = "Seed : "+ghostData.getSeed() + "\r\n"+description+"\r\nSubmitted by GhostRunner Mod";
        times.put("realtime", ghostData.getRealTimeAttack()/1000.f);
        times.put("realtime_noloads", 0f);
        times.put("ingame", ghostData.getInGameTime()/1000.f);

        if (ghostData.getType() != GhostType.FSG) {
            variables.put("jlzkwql2", new Variable(MCSpeedRunAPI.getAnyPVersionKey(GhostRunner.CLIENT_VERSION))); //Version
            variables.put("9l737pn1", new Variable(ghostData.isHardcore() ? "p129j4lx" : MCSpeedRunAPI.getAnyPDifficultyKey(ghostData.getDifficulty()))); //Difficulty
            variables.put("ql6g2ow8", new Variable(ghostData.isUseF3() ? "rqvmvz6q" : "5lee2vkl")); // F3?
            variables.put("r8rg67rn", new Variable(ghostData.getType() == GhostType.SSG ? "klrzpjo1" : "21d4zvp1")); //SSG or RSG
            variables.put("dloymqd8", new Variable("jq6kxd3l")); //Modded
            variables.put("wl33kewl", new Variable("4qye4731")); //Version Range
        } else {
            variables.put("j846z5wl", new Variable(MCSpeedRunAPI.getExtensionVersionKey(GhostRunner.CLIENT_VERSION))); //Sub Version
            variables.put("ylpm5erl", new Variable(MCSpeedRunAPI.getExtensionVersionGroupKey(GhostRunner.CLIENT_VERSION))); //Version
            variables.put("0nwkeorn", new Variable(ghostData.isHardcore() ? "21d33k4q" : MCSpeedRunAPI.getExtensionDifficultyKey(ghostData.getDifficulty()))); //Difficulty
            variables.put("ylqkjo3l", new Variable(ghostData.isUseF3() ? "zqorkv5q" : "81w5z0m1")); // F3?
            variables.put("ql61eov8", new Variable("81pvroe1")); //Player Count
            variables.put("jlzwkmql", new Variable("5lmj45jl")); //Modded
            variables.put("jlzrovq8", new Variable("mlnp8rd1")); //Version (Filtered Seed Glitchless)
        }
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}

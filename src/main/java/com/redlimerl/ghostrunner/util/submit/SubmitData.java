package com.redlimerl.ghostrunner.util.submit;

import com.google.gson.*;
import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.data.RunnerOptions;
import com.redlimerl.ghostrunner.record.data.GhostData;
import com.redlimerl.ghostrunner.record.data.GhostType;
import com.redlimerl.ghostrunner.util.Utils;
import com.redlimerl.ghostrunner.util.submit.category.*;
import com.redlimerl.speedrunigt.option.SpeedRunOptions;
import com.redlimerl.speedrunigt.timer.RunCategory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class SubmitData {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static SubmitData create(GhostData ghostData, String description, String videoUrl) {
        if (ghostData.getGhostCategory() == RunCategory.ANY) {
            if (ghostData.getType() == GhostType.FSG)
                return new FSGlitchlessSubmit(ghostData, description, videoUrl);
            else return new AnyGlitchlessSubmit(ghostData, description, videoUrl);
        }
        if (ghostData.getGhostCategory() == RunCategory.HIGH) {
            return new HighPercentSubmit(ghostData, description, videoUrl);
        }
        if (ghostData.getGhostCategory() == RunCategory.KILL_ALL_BOSSES || ghostData.getGhostCategory() == RunCategory.KILL_WITHER || ghostData.getGhostCategory() == RunCategory.KILL_ELDER_GUARDIAN) {
            return new KillBossesSubmit(ghostData, description, videoUrl);
        }
        if (ghostData.getGhostCategory() == RunCategory.HOW_DID_WE_GET_HERE) {
            return new HDWGHereSubmit(ghostData, description, videoUrl);
        }
        if (ghostData.getGhostCategory() == RunCategory.HERO_OF_VILLAGE) {
            return new HeroVillageSubmit(ghostData, description, videoUrl);
        }
        if (ghostData.getGhostCategory() == RunCategory.ARBALISTIC) {
            return new ArbalisticSubmit(ghostData, description, videoUrl);
        }
        if (ghostData.getGhostCategory() == RunCategory.ENTER_NETHER) {
            return new EnterNetherSubmit(ghostData, description, videoUrl);
        }
        if (ghostData.getGhostCategory() == RunCategory.ALL_SWORDS) {
            return new AllSwordsSubmit(ghostData, description, videoUrl);
        }
        if (ghostData.getGhostCategory() == RunCategory.ALL_MINERALS) {
            return new AllMineralsSubmit(ghostData, description, videoUrl);
        }
        if (ghostData.getGhostCategory() == RunCategory.FULL_IA_15_LVL) {
            return new FullIA15LevelsSubmit(ghostData, description, videoUrl);
        }
        if (ghostData.getGhostCategory() == RunCategory.ALL_WORKSTATIONS) {
            return new AllWorkstationsSubmit(ghostData, description, videoUrl);
        }
        if (ghostData.getGhostCategory() == RunCategory.FULL_INV) {
            return new FullInventorySubmit(ghostData, description, videoUrl);
        }
        if (ghostData.getGhostCategory() == RunCategory.ENTER_END) {
            return new EnterEndSubmit(ghostData, description, videoUrl);
        }
        if (ghostData.getGhostCategory() == RunCategory.HALF) {
            return new HalfPercentSubmit(ghostData, description, videoUrl);
        }
        return null;
    }



    private final GhostData ghostData;
    private final String date;
    private final String video;
    private final String comment;
    public final HashMap<String, Float> times = new HashMap<>();
    public final ArrayList<SubmitVariable> variables = new ArrayList<>();

    public SubmitData(GhostData ghostData, String description, String videoUrl) {
        if (!Utils.isUrl(videoUrl)) throw new IllegalArgumentException("video url is not URL");

        this.date = new SimpleDateFormat("yyyy-MM-dd").format(ghostData.getCreatedDate());
        this.video = videoUrl;
        this.comment = "Seed : "+ghostData.getSeed() + "\r\n"+description+"\r\nSubmitted by GhostRunner Mod";
        this.times.put("realtime", ghostData.getRealTimeAttack()/1000.f);
        this.times.put("realtime_noloads", 0f);
        this.times.put("ingame", ghostData.getInGameTime()/1000.f);
        this.ghostData = ghostData;

        this.updateVariable(getModsVariable());
        this.updateVariable(getF3Variable());
        this.updateVariable(getDifficultyVariable());
        this.updateVariable(getVersionVariable());
        this.updateVariable(getVersionGroupVariable());
    }

    public GhostData getGhostData() {
        return ghostData;
    }

    public abstract String getCategoryKey();

    public abstract SubmitVariable getModsVariable();

    public abstract SubmitVariable getF3Variable();

    public abstract SubmitVariable getDifficultyVariable();

    public abstract String getVersionsJson();

    public abstract String getVersionVariableKey();

    public SubmitVariable getVersionVariable() {
        JsonElement versions = new JsonParser().parse(getVersionsJson());
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : versions.getAsJsonObject().entrySet()) {
            if (Objects.equals(stringJsonElementEntry.getValue().getAsString(), ghostData.getClientVersion()))
                return new SubmitVariable(getVersionVariableKey(), stringJsonElementEntry.getKey());
        }
        return null;
    }

    public abstract String getVersionGroupVariableKey();

    public abstract List<SubmitVersionGroup> getVersionGroups();

    public SubmitVariable getVersionGroupVariable() {
        return this.getVersionGroupVariable(getVersionGroupVariableKey(), getVersionGroups());
    }
    public SubmitVariable getVersionGroupVariable(String key, List<SubmitVersionGroup> groups) {
        for (SubmitVersionGroup versionGroup : groups) {
            if (versionGroup.isIn(ghostData.getClientVersion()))
                return new SubmitVariable(key, versionGroup.getValue());
        }
        return null;
    }

    public void updateVariable(SubmitVariable variable) {
        this.variables.add(variable);
    }

    public String submit() throws IOException, IllegalStateException, ClassCastException  {
        String authKey = SpeedRunOptions.getOption(RunnerOptions.SPEEDRUN_COM_API_KEY);

        URL u = new URL("https://www.speedrun.com/api/v1/runs");
        HttpURLConnection c = (HttpURLConnection) u.openConnection();

        c.setDoOutput(true);
        c.setRequestMethod("POST");
        c.setRequestProperty("Content-Type", "application/json");
        c.setRequestProperty("Accept-Charset", "UTF-8");
        c.addRequestProperty("User-Agent", "GhostRunner/"+ GhostRunner.MOD_VERSION);
        c.addRequestProperty("X-Api-Key", authKey);
        c.setConnectTimeout(10000);
        c.setReadTimeout(10000);

        OutputStream os = c.getOutputStream();
        String data = this.toString();
        os.write(("{\"run\":" + data + "}").getBytes(StandardCharsets.UTF_8));
        os.flush();

        InputStreamReader r = new InputStreamReader(c.getInputStream(), StandardCharsets.UTF_8);
        JsonElement jsonElement = new JsonParser().parse(r);
        r.close();


        return jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("weblink").getAsString();
    }

    @Override
    public String toString() {
        final boolean verified = false;
        final boolean emulated = false;
        final String platform = "8gej2n93";

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("category", getCategoryKey());
        jsonObject.addProperty("date", this.date);
        jsonObject.addProperty("video", this.video);
        jsonObject.addProperty("comment", this.comment);
        jsonObject.addProperty("verified", verified);
        jsonObject.addProperty("platform", platform);
        jsonObject.addProperty("emulated", emulated);
        jsonObject.add("times", GSON.toJsonTree(this.times));

        JsonObject variablesObject = new JsonObject();
        for (SubmitVariable variable : this.variables) {
            JsonObject variableObject = new JsonObject();
            variableObject.addProperty("type", variable.type);
            variableObject.addProperty("value", variable.value);
            variablesObject.add(variable.key, variableObject);
        }
        jsonObject.add("variables", variablesObject);

        return GSON.toJson(jsonObject);
    }
}
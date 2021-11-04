package com.redlimerl.ghostrunner;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.redlimerl.ghostrunner.data.RunnerOptions;
import com.redlimerl.ghostrunner.data.SubmitData;
import com.redlimerl.speedrunigt.option.SpeedRunOptions;
import net.minecraft.world.Difficulty;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class MCSpeedRunAPI {

    public static String CATEGORY_ANY_PERCENT = "mkeyl926";
    public static String CATEGORY_FSG = "n2y9z41d";

    public static String request(SubmitData submitData) throws IOException {
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
        os.write(("{\"run\":"+ submitData.toString() + "}").getBytes(StandardCharsets.UTF_8));
        os.flush();

        InputStreamReader r = new InputStreamReader(c.getInputStream(), StandardCharsets.UTF_8);
        JsonElement jsonElement = new JsonParser().parse(r);
        System.out.println(jsonElement.toString());
        r.close();

        return jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("weblink").getAsString();
    }

    public static String getAnyPVersionKey(String clientVersion) {
        String versionsString = "{\"mln68v0q\":\"1.16.1\",\"21go7k8q\":\"1.15.2\",\"gq7rrnnl\":\"1.14.4\",\"4lx5gk41\":\"1.8.9\",\"013xkx15\":\"1.7.10\",\"5lm2emqv\":\"1.7.4\",\"jq6vwj1m\":\"1.7.2\",\"9qj2o314\":\"1.6.4\",\"rqvx06l6\":\"1.0\",\"5len0klo\":\"1.1\",\"0q54m2lp\":\"1.2.1\",\"4lxgk4q2\":\"1.2.2\",\"81496vqd\":\"1.2.3\",\"z19xv814\":\"1.2.4\",\"p129k4lx\":\"1.2.5\",\"81pez8l7\":\"1.3.1\",\"xqko3k19\":\"1.3.2\",\"gq72od1p\":\"1.4.2\",\"21g968qz\":\"1.4.4\",\"jqzgw8lp\":\"1.4.5\",\"klrxdmlp\":\"1.4.6\",\"21d6n5qe\":\"1.4.7\",\"5q8433ld\":\"1.5.1\",\"4qy7m2q7\":\"1.5.2\",\"mlne7jlp\":\"1.6.1\",\"8106y21v\":\"1.6.2\",\"21d43441\":\"1.7.3\",\"81w72vq4\":\"1.7.5\",\"814o96vq\":\"1.7.6\",\"z192xv8q\":\"1.7.7\",\"p12v9k4q\":\"1.7.8\",\"zqojex1y\":\"1.7.9\",\"rqvx26l6\":\"1.8\",\"5lenyklo\":\"1.8.1\",\"21dv7g1e\":\"1.8.2\",\"5q8276qd\":\"1.8.3\",\"5le86mlo\":\"1.8.4\",\"01340kl5\":\"1.8.5\",\"rqvz7516\":\"1.8.6\",\"5levop1o\":\"1.8.7\",\"gq7zyr1p\":\"1.8.8\",\"81pyez81\":\"1.9\",\"xqkeo3kq\":\"1.9.1\",\"gq752od1\":\"1.9.2\",\"21gn968l\":\"1.9.3\",\"jqzngw8q\":\"1.9.4\",\"klr3xdml\":\"1.10\",\"5lmygj8l\":\"1.10.1\",\"jq678gv1\":\"1.10.2\",\"0q54o3nl\":\"1.11\",\"zqo0rw5q\":\"1.11.1\",\"4lxgvw4q\":\"1.11.2\",\"xqkonxn1\":\"1.12\",\"5q8270kq\":\"1.12.1\",\"p12ongvl\":\"1.12.2\",\"8142pg0l\":\"1.13\",\"z19rz201\":\"1.13.1\",\"z19ryv41\":\"1.13.2\",\"9qj49koq\":\"1.14\",\"01305er1\":\"1.14.1\",\"814vn2v1\":\"1.14.2\",\"jqzx69m1\":\"1.14.3\",\"gq7zrdd1\":\"1.15\",\"21dor7gq\":\"1.15.1\",\"mln64j6q\":\"1.16\",\"21d7zo31\":\"1.16.2\",\"21d7evp1\":\"1.16.3\",\"21dgwkj1\":\"1.16.4\",\"21dzz0jl\":\"1.16.5\",\"5q8ojzr1\":\"1.17\",\"4qy93w3l\":\"1.17.1\"}";
        JsonElement versions = new JsonParser().parse(versionsString);
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : versions.getAsJsonObject().entrySet()) {
            if (Objects.equals(stringJsonElementEntry.getValue().getAsString(), clientVersion)) {
                return stringJsonElementEntry.getKey();
            }
        }
        throw new IllegalArgumentException("wrong client version");
    }

    public static String getExtensionVersionKey(String clientVersion) {
        String versionsString = "{\"21d7k251\":\"1.16.1\",\"81w5zz61\":\"1.0\",\"21d3wjpq\":\"1.1\",\"klr6xe0l\":\"1.2.1\",\"21do6j3q\":\"1.2.2\",\"5q8r4xr1\":\"1.2.3\",\"4qy87v31\":\"1.2.4\",\"mlnke46l\":\"1.2.5\",\"jq6gv9n1\":\"1.3.1\",\"5lmw2ryq\":\"1.3.2\",\"81wr7v9l\":\"1.4.2\",\"zqo2j8gl\":\"1.4.4\",\"0134xokl\":\"1.4.5\",\"rqvzxk51\":\"1.4.6\",\"5levn6p1\":\"1.4.7\",\"xqk6oe91\":\"1.5.1\",\"21g2v6ol\":\"1.5.2\",\"jqzygn4l\":\"1.6.1\",\"klr6x30l\":\"1.6.2\",\"gq7z25r1\":\"1.6.4\",\"rqveg7y1\":\"1.7.2\",\"p12nxx41\":\"1.7.3\",\"21go9noq\":\"1.7.4\",\"jqzygnkl\":\"1.7.5\",\"klr6x3wl\":\"1.7.6\",\"21do6kgq\":\"1.7.7\",\"5q8r4k61\":\"1.7.8\",\"4qyj9x7q\":\"1.7.9\",\"9qje2601\":\"1.7.10\",\"4qy87zd1\":\"1.8.1\",\"xqkm234l\":\"1.8\",\"mlnke8nl\":\"1.8.2\",\"81096vp1\":\"1.8.3\",\"9qje27o1\":\"1.8.4\",\"jq6gv5o1\":\"1.8.5\",\"5lmw2o0q\":\"1.8.6\",\"81wr746l\":\"1.8.7\",\"0q5gywvl\":\"1.8.8\",\"0q5gy6vl\":\"1.8.9\",\"0134x9yl\":\"1.9\",\"rqvzxvy1\":\"1.9.1\",\"5levnn61\":\"1.9.2\",\"0q5744vl\":\"1.9.3\",\"4lxdgggq\":\"1.9.4\",\"814499k1\":\"1.10\",\"z19dxx4l\":\"1.10.1\",\"p124992q\":\"1.10.2\",\"klrymeoq\":\"1.11\",\"81p6eenq\":\"1.11.1\",\"xqk6oo41\":\"1.11.2\",\"81w5zg61\":\"1.12\",\"gq7z22r1\":\"1.12.1\",\"4qyjdrdq\":\"1.12.2\",\"21go99oq\":\"1.13\",\"jqzyggkl\":\"1.13.1\",\"814786k1\":\"1.13.2\",\"gq7r0orl\":\"1.14\",\"klr6xxwl\":\"1.14.1\",\"mlnzpnnl\":\"1.14.2\",\"4lxerkg1\":\"1.14.3\",\"rqveg9y1\":\"1.14.4\",\"9qje4pg1\":\"1.15\",\"21go2jxq\":\"1.15.1\",\"5lmwp28q\":\"1.15.2\",\"81wo6g9l\":\"20w14∞\",\"4qyevg31\":\"1.16\",\"jq6krmjl\":\"1.16.2\",\"xqkkxv4q\":\"1.16.3\",\"81poj3kq\":\"1.16.4\",\"p12ddn4q\":\"1.16.5\",\"mlnpzj01\":\"15w14a\",\"gq7pv7yq\":\"1.17\",\"z19k460q\":\"alpha 1.0.4\",\"21g5ovml\":\"1.17.1\",\"jqz9ydm1\":\"1.17.2\",\"5le0zj5q\":\"1.6\",\"81pz9jnl\":\"1.17.41\"}";
        JsonElement versions = new JsonParser().parse(versionsString);
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : versions.getAsJsonObject().entrySet()) {
            if (Objects.equals(stringJsonElementEntry.getValue().getAsString(), clientVersion)) {
                return stringJsonElementEntry.getKey();
            }
        }
        throw new IllegalArgumentException("wrong client version");
    }

    public static String getExtensionVersionGroupKey(String clientVersion) {
        String[] vc = clientVersion.split("\\.");
        if (vc.length > 1) {
            if (Objects.equals(vc[1], "16")) {
                return "81p8o6el";
            }
            if (Objects.equals(vc[1], "17")) {
                return "810po8jl";
            }
        }
        throw new IllegalArgumentException("wrong client version");
    }

    public static String getAnyPDifficultyKey(Difficulty difficulty) {
        if (difficulty == Difficulty.EASY) {
            return "4lxg24q2";
        }
        if (difficulty == Difficulty.NORMAL) {
            return "8149mvqd";
        }
        if (difficulty == Difficulty.HARD) {
            return "z19xe814";
        }
        throw new IllegalArgumentException("wrong difficulty");
    }

    public static String getExtensionDifficultyKey(Difficulty difficulty) {
        if (difficulty == Difficulty.EASY) {
            return "21g22nnl";
        }
        if (difficulty == Difficulty.NORMAL) {
            return "jqzxxng1";
        }
        if (difficulty == Difficulty.HARD) {
            return "klryy3jq";
        }
        throw new IllegalArgumentException("wrong difficulty");
    }
}

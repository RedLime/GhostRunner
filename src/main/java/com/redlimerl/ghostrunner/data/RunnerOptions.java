package com.redlimerl.ghostrunner.data;

import com.redlimerl.ghostrunner.GhostRunner;
import com.redlimerl.ghostrunner.crypt.Crypto;
import com.redlimerl.speedrunigt.option.OptionArgument;
import net.minecraft.util.Identifier;

public class RunnerOptions {

    public static final OptionArgument<Boolean> TOGGLE_GHOST = new OptionArgument<Boolean>(new Identifier(GhostRunner.MOD_ID, "toggle_ghost"), true) {
        @Override
        public Boolean valueFromString(String s) {
            return Boolean.valueOf(s);
        }

        @Override
        public String valueToString(Boolean aBoolean) {
            return Boolean.toString(aBoolean);
        }
    };

    public static final OptionArgument<Float> GHOST_OPACITY = new OptionArgument<Float>(new Identifier(GhostRunner.MOD_ID, "ghost_opacity"), 0.3f) {
        @Override
        public Float valueFromString(String s) {
            return Float.parseFloat(s);
        }

        @Override
        public String valueToString(Float aFloat) {
            return Float.toString(aFloat);
        }
    };

    private static final String API_HASH_KEY = "ovKB3KpPNRoWJswBFTJSIiBYkvwtfPxG";

    public static final OptionArgument<String> SPEEDRUN_COM_API_KEY = new OptionArgument<String>(new Identifier(GhostRunner.MOD_ID, "speedrun_com_api_key"), "") {
        @Override
        public String valueFromString(String s) {
            return Crypto.decrypt(s, API_HASH_KEY);
        }

        @Override
        public String valueToString(String string) {
            return Crypto.encrypt(string, API_HASH_KEY);
        }
    };
}

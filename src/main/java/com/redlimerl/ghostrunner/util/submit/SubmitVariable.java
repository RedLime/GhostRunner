package com.redlimerl.ghostrunner.util.submit;

public class SubmitVariable {
    public final String key;
    public final String value;
    public final String type;

    public SubmitVariable(String key, String value) {
        this(key, value,"pre-defined");
    }

    public SubmitVariable(String key, String value, String type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }
}

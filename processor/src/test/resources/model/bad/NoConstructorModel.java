package com.bluelinelabs.logansquare.processor.bad;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class NoConstructorModel {

    @JsonField(name = "value")
    String value;

    public NoConstructorModel(String value) {
        this.value = value;
    }
}

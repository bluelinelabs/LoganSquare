package com.bluelinelabs.logansquare.processor.bad;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class PrivateConstructorModel {

    @JsonField(name = "value")
    String value;

    private PrivateConstructorModel() {
    }
}

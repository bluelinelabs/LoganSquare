package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.annotation.OnJsonParseComplete;
import com.bluelinelabs.logansquare.annotation.OnPreJsonSerialize;

@JsonObject
public class PrePostProcessingModel {

    @JsonField(name = "unformatted_string")
    public String unformattedString;

    public String formattedString;

    @OnPreJsonSerialize
    public void onPreSerialize() {
        unformattedString = formattedString.toLowerCase();
    }

    @OnJsonParseComplete
    public void onParseComplete() {
        formattedString = formattedString.toUpperCase();
    }
}

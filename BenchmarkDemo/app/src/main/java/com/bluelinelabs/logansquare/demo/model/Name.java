package com.bluelinelabs.logansquare.demo.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.google.api.client.util.Key;

@JsonObject
public class Name {

    @Key
    @JsonField
    public String first;

    @Key
    @JsonField
    public String last;
}

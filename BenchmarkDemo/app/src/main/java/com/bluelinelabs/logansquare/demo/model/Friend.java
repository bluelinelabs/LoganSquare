package com.bluelinelabs.logansquare.demo.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.google.api.client.util.Key;

@JsonObject
public class Friend {

    @Key
    @JsonField
    public int id;

    @Key
    @JsonField
    public String name;
}

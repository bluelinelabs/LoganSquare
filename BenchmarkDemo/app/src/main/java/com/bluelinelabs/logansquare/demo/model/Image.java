package com.bluelinelabs.logansquare.demo.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.google.api.client.util.Key;

@JsonObject
public class Image {

    @Key
    @JsonField
    public String id;

    @Key
    @JsonField
    public String format;

    @Key
    @JsonField
    public String url;

    @Key
    @JsonField
    public String description;

}

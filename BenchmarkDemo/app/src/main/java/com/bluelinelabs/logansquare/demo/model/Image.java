package com.bluelinelabs.logansquare.demo.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class Image {

    @JsonField
    public String id;

    @JsonField
    public String format;

    @JsonField
    public String url;

    @JsonField
    public String description;

}

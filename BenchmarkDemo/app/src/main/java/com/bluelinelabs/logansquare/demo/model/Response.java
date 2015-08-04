package com.bluelinelabs.logansquare.demo.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.util.Key;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@JsonObject
public class Response {

    @Key
    @JsonField
    public List<User> users;

    @Key
    @JsonField
    public String status;

    @Key("is_real_json") // Annotation needed for google http client
    @SerializedName("is_real_json") // Annotation needed for GSON
    @JsonProperty("is_real_json") // Annotation needed for Jackson Databind
    @JsonField(name = "is_real_json")
    public boolean isRealJson;
}

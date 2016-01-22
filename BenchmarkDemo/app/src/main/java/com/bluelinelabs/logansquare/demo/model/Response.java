package com.bluelinelabs.logansquare.demo.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.dslplatform.json.CompiledJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@JsonObject
@CompiledJson
public class Response {

    @JsonField
    public List<User> users;

    @JsonField
    public String status;

    @SerializedName("is_real_json") // Annotation needed for GSON
    @JsonProperty("is_real_json") // Annotation needed for Jackson Databind
    @JsonField(name = "is_real_json")
    public boolean isRealJson;
}

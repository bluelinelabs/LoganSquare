package com.bluelinelabs.logansquare.demo.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.util.Key;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@JsonObject
public class User {

    @Key("_id") // Annotation needed for google http client
    @SerializedName("_id") // Annotation needed for GSON
    @JsonProperty("_id")
    @JsonField(name = "_id")
    public String id;

    @Key
    @JsonField
    public int index;

    @Key
    @JsonField
    public String guid;

    @Key("is_active") // Annotation needed for google http client
    @SerializedName("is_active") // Annotation needed for GSON
    @JsonProperty("is_active") // Annotation needed for Jackson Databind
    @JsonField(name = "is_active")
    public boolean isActive;

    @Key
    @JsonField
    public String balance;

    @Key("picture") // Annotation needed for google http client
    @SerializedName("picture") // Annotation needed for GSON
    @JsonProperty("picture") // Annotation needed for Jackson Databind
    @JsonField(name = "picture")
    public String pictureUrl;

    @Key
    @JsonField
    public int age;

    @Key
    @JsonField
    public Name name;

    @Key
    @JsonField
    public String company;

    @Key
    @JsonField
    public String email;

    @Key
    @JsonField
    public String address;

    @Key
    @JsonField
    public String about;

    @Key
    @JsonField
    public String registered;

    @Key
    @JsonField
    public double latitude;

    @Key
    @JsonField
    public double longitude;

    @Key
    @JsonField
    public List<String> tags;

    @Key
    @JsonField
    public List<Integer> range;

    @Key
    @JsonField
    public List<Friend> friends;

    @Key
    @JsonField
    public List<Image> images;

    @Key
    @JsonField
    public String greeting;

    @Key("favorite_fruit") // Annotation needed for google http client
    @SerializedName("favorite_fruit") // Annotation needed for GSON
    @JsonProperty("favorite_fruit") // Annotation needed for Jackson Databind
    @JsonField(name = "favorite_fruit")
    public String favoriteFruit;

    @Key("eye_color") // Annotation needed for google http client
    @SerializedName("eye_color") // Annotation needed for GSON
    @JsonProperty("eye_color") // Annotation needed for Jackson Databind
    @JsonField(name = "eye_color")
    public String eyeColor;

    @Key
    @JsonField
    public String phone;
}

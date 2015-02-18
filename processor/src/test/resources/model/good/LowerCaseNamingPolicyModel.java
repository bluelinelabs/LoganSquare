package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.annotation.JsonObject.FieldNamingPolicy;
import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;

import java.util.List;

@JsonObject(fieldNamingPolicy = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
public class LowerCaseNamingPolicyModel {

    @JsonField
    public String camelCaseString;

    @JsonField
    public List<String> camelCaseList;

}

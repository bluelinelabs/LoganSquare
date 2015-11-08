package com.bluelinelabs.logansquare.processor.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.annotation.JsonObject.FieldNamingPolicy;

@JsonObject(serializeNullObjects = true, fieldNamingPolicy = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
public class SimpleModelWithGenericField {

    @JsonField
    public String string;

    @JsonField
    public SimpleGenericModel<String> genericModel;

}

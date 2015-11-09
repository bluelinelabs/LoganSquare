package com.bluelinelabs.logansquare.processor.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class SimpleGenericStringModel extends SimpleGenericModel<String> {

    @JsonField
    public String anotherString;

}

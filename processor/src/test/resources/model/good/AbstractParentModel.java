package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public abstract class AbstractParentModel {

    @JsonField
    public int parentTestInt;

}

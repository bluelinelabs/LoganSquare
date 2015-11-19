package com.bluelinelabs.logansquare.processor.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * @author eirnym
 */
@JsonObject
public class InheritanceParentModel {
    @JsonField
    public String stringInParent;
}

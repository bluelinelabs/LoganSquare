package com.bluelinelabs.logansquare.processor.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * @author eirnym
 */
@JsonObject
public class InheritanceChildModel extends InheritanceParentModel {
    @JsonField
    public String stringInChild;
}

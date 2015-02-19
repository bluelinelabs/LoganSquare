package com.bluelinelabs.logansquare.demo.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonIgnore;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy;

import java.util.List;

@JsonObject(fieldDetectionPolicy = FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class NonPrivateFieldsFieldDetectionPolicyModel {

    public String nonAnnotatedString;

    public List<String> nonAnnotatedList;

    private int nonAnnotatedPrivateInt;

    @JsonField(name = "annotated_string")
    public String annotatedString;

    @JsonIgnore
    public int intToIgnore;

    public transient int transientIntToIgnore;

    public static int staticIntToIgnore;

    @JsonField
    public transient int transientIntToInclude;

    @JsonField
    public static int staticIntToInclude;

    public int getNonAnnotatedPrivateInt() {
        return nonAnnotatedPrivateInt;
    }

    public void setNonAnnotatedPrivateInt(int nonAnnotatedPrivateInt) {
        this.nonAnnotatedPrivateInt = nonAnnotatedPrivateInt;
    }
}

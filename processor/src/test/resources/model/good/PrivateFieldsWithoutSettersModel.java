package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public final class PrivateFieldsWithoutSettersModel {
    @JsonField
    int integerField;

    @JsonField
    String[] arrayField;

    @JsonField
    private int anotherField;

    public PrivateFieldsWithoutSettersModel(int integerField, String[] whatever, int anotherField) {
        this.integerField = integerField;
        this.arrayField = whatever;
        this.anotherField = anotherField;
    }

    public void setArrayField(String[] arrayField) {
        this.arrayField = arrayField;
    }

    public void setIntegerField(int integerField) {
        this.integerField = integerField;
    }

    public String[] getArrayField() {
        return arrayField;
    }

    public int getAnotherField() {
        return anotherField;
    }

    public int getIntegerField() {
        return integerField;
    }
}

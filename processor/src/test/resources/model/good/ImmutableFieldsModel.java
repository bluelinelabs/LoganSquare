package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class ImmutableFieldsModel {
    @JsonField
    final int integerField;

    @JsonField
    final String[] arrayField;

    @JsonField
    int anotherField;

    public ImmutableFieldsModel(int integerField, String[] whatever, int anotherField) {
        this.integerField = integerField;
        this.arrayField = whatever;
        this.anotherField = anotherField;
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

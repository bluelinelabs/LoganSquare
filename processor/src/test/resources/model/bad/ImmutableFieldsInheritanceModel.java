package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public final class ImmutableFieldsInheritanceModel extends ImmutableFieldsModel {
    @JsonField
    int i;

    @JsonField
    private String[] j;

    @JsonField
    int k;

    public ImmutableFieldsInheritanceModel(int integerField, String[] whatever, int anotherField) {
        super(integerField, whatever, anotherField);
    }

    public String[] getJ() {
        return j;
    }
}

package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class MultipleFieldNamesModel {

    @JsonField(fieldName = {"possible_int_name_1", "possible_int_name_2"})
    public int testInt;

    @JsonField(fieldName = {"possible_long_name_1", "possible_long_name_2"})
    public long testLong;

    @JsonField(fieldName = {"possible_float_name_1", "possible_float_name_2"})
    public float testFloat;

    @JsonField(fieldName = {"possible_double_name_1", "possible_double_name_2"})
    public double testDouble;

    @JsonField(fieldName = {"possible_string_name_1", "possible_string_name_2"})
    public String testString;

}

package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.Date;

@JsonObject
public class SimpleModel {

    @JsonField
    public String string;

    @JsonField
    public Date date;

    @JsonField(fieldName = "test_int")
    public int testInt;

    @JsonField(fieldName = "test_long")
    public long testLong;

    @JsonField(fieldName = "test_float")
    public float testFloat;

    @JsonField(fieldName = "test_double")
    public double testDouble;

    @JsonField(fieldName = "test_string")
    public String testString;

    @JsonField(fieldName = "test_int_obj")
    public Integer testIntObj;

    @JsonField(fieldName = "test_long_obj")
    public Long testLongObj;

    @JsonField(fieldName = "test_float_obj")
    public Float testFloatObj;

    @JsonField(fieldName = "test_double_obj")
    public Double testDoubleObj;
}

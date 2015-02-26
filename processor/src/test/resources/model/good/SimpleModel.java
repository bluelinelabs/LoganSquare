package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonIgnore;
import com.bluelinelabs.logansquare.annotation.JsonIgnore.IgnorePolicy;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.Date;

@JsonObject
public class SimpleModel {

    @JsonField
    public String string;

    @JsonField
    public Date date;

    @JsonField(name = "test_int")
    public int testInt;

    @JsonField(name = "test_long")
    public long testLong;

    @JsonField(name = "test_float")
    public float testFloat;

    @JsonField(name = "test_double")
    public double testDouble;

    @JsonField(name = "test_string")
    public String testString;

    @JsonField(name = "test_int_obj")
    public Integer testIntObj;

    @JsonField(name = "test_long_obj")
    public Long testLongObj;

    @JsonField(name = "test_float_obj")
    public Float testFloatObj;

    @JsonField(name = "test_double_obj")
    public Double testDoubleObj;

    @JsonIgnore
    @JsonField
    public int intToIgnore;

    @JsonIgnore(ignorePolicy = IgnorePolicy.PARSE_AND_SERIALIZE)
    @JsonField
    public int intToIgnoreForBoth;

    @JsonIgnore(ignorePolicy = IgnorePolicy.PARSE_ONLY)
    @JsonField
    public int intToIgnoreForParse;

    @JsonIgnore(ignorePolicy = IgnorePolicy.SERIALIZE_ONLY)
    @JsonField
    public int intToIgnoreForSerialization;
}

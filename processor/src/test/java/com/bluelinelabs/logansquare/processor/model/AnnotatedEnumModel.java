package com.bluelinelabs.logansquare.processor.model;

import com.bluelinelabs.logansquare.annotation.JsonBooleanValue;
import com.bluelinelabs.logansquare.annotation.JsonEnum;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonNullValue;
import com.bluelinelabs.logansquare.annotation.JsonNumberValue;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.annotation.JsonStringValue;

@JsonObject
public class AnnotatedEnumModel {

    @JsonField(name = "default_naming_policy_enum")
    public DefaultNamingPolicyEnum defaultNamingPolicyEnum;
    @JsonField(name = "lower_case_naming_policy_enum")
    public LowerCaseNamingPolicyEnum lowerCaseNamingPolicyEnum;
    @JsonField(name = "string_enum")
    public StringEnum stringEnum;
    @JsonField(name = "number_enum")
    public NumberEnum numberEnum;
    @JsonField(name = "boolean_enum")
    public BooleanEnum booleanEnum;
    @JsonField(name = "nullable_enum")
    public NullableEnum nullableEnum;
    
    @JsonEnum
    public enum DefaultNamingPolicyEnum {
        ONE,
        TWO
    }

    @JsonEnum(valueNamingPolicy = JsonEnum.ValueNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    public enum LowerCaseNamingPolicyEnum {
        ONE,
        TWO
    }

    @JsonEnum
    public enum StringEnum {
        @JsonStringValue("one")
        ONE,
        @JsonStringValue("two")
        TWO
    }

    @JsonEnum
    public enum NumberEnum {
        @JsonNumberValue(1)
        ONE,
        @JsonNumberValue(2)
        TWO
    }

    @JsonEnum
    public enum BooleanEnum {
        @JsonBooleanValue(true)
        TRUE,
        @JsonBooleanValue(false)
        FALSE
    }

    @JsonEnum
    public enum NullableEnum {
        ONE,
        TWO,
        @JsonNullValue
        NULL
    }

}

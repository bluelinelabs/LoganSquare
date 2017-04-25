package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.annotation.JsonEnum;
import com.bluelinelabs.logansquare.annotation.JsonNullValue;

@JsonEnum
public enum NullableEnum {
    ONE,
    TWO,
    @JsonNullValue
    NULL
}
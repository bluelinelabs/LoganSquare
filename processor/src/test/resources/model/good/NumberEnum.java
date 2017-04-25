package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.annotation.JsonEnum;
import com.bluelinelabs.logansquare.annotation.JsonNumberValue;

@JsonEnum
public enum NumberEnum {
    @JsonNumberValue(1)
    ONE,
    @JsonNumberValue(2)
    TWO
}
package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.annotation.JsonEnum;
import com.bluelinelabs.logansquare.annotation.JsonBooleanValue;

@JsonEnum
public enum BooleanEnum {
    @JsonBooleanValue(true)
    TRUE,
    @JsonBooleanValue(false)
    FALSE
}
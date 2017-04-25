package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.annotation.JsonEnum;
import com.bluelinelabs.logansquare.annotation.JsonStringValue;

@JsonEnum
public enum StringEnum {
    @JsonStringValue("one")
    ONE,
    @JsonStringValue("two")
    TWO
}
package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.annotation.JsonEnum;

@JsonEnum(valueNamingPolicy = JsonEnum.ValueNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
public enum LowerCaseNamingPolicyEnum {
    ONE,
    TWO
}
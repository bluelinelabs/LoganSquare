package com.bluelinelabs.logansquare.processor.bad;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class InvalidTypeConverterModel {

    @JsonField(typeConverter = InvalidTypeConverter.class)
    public UnsupportedType unsupportedType;

    public static class UnsupportedType { }

    public static class InvalidTypeConverter {

    }
}

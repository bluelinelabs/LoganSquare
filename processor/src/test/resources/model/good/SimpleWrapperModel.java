package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class SimpleWrapperModel {

    @JsonField
    public WrappedClass wrappedObject;

    @JsonObject
    public static class WrappedClass {

        @JsonField
        public String wrappedString;
    }

}

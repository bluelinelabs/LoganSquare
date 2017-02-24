package com.bluelinelabs.logansquare.processor;

import com.squareup.javapoet.TypeName;

import java.util.Map;

public class JsonEnumHolder {

    public final String packageName;
    public final String injectedClassName;
    public final TypeName objectTypeName;
    public final ValueType valuesType;
    public final Map<String, Object> valuesMap;

    public boolean fileCreated;

    private JsonEnumHolder(JsonEnumHolderBuilder builder) {
        this.packageName = builder.packageName;
        this.injectedClassName = builder.injectedClassName;
        this.objectTypeName = builder.objectTypeName;
        this.valuesType = builder.valuesType;
        this.valuesMap = builder.valuesMap;
    }

    public enum ValueType {

        STRING(String.class, "getValueAsString", "writeStringField", "writeString"),
        NUMBER(long.class, "getValueAsLong", "writeNumberField", "writeNumber"),
        BOOLEAN(boolean.class, "getValueAsBoolean", "writeBooleanField", "writeBoolean");

        public final Class enumValueClass;
        public final String getFromMethodName;
        public final String writeToFieldMethodName;
        public final String writeMethodName;

        ValueType(Class enumValueClass, String getFromMethodName, String writeToFieldMethodName, String writeMethodName) {
            this.enumValueClass = enumValueClass;
            this.getFromMethodName = getFromMethodName;
            this.writeToFieldMethodName = writeToFieldMethodName;
            this.writeMethodName = writeMethodName;
        }

    }

    public static class JsonEnumHolderBuilder {
        private String packageName;
        private String injectedClassName;
        private TypeName objectTypeName;
        private ValueType valuesType;
        private Map<String, Object> valuesMap;

        public JsonEnumHolderBuilder setPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public JsonEnumHolderBuilder setInjectedClassName(String injectedClassName) {
            this.injectedClassName = injectedClassName;
            return this;
        }

        public JsonEnumHolderBuilder setObjectTypeName(TypeName objectTypeName) {
            this.objectTypeName = objectTypeName;
            return this;
        }

        public JsonEnumHolderBuilder setValuesMap(ValueType valuesType, Map<String, Object> valuesMap) {
            this.valuesType = valuesType;
            this.valuesMap = valuesMap;
            return this;
        }

        public JsonEnumHolder build() {
            return new JsonEnumHolder(this);
        }

    }

}

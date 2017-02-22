package com.bluelinelabs.logansquare.processor;

import com.squareup.javapoet.TypeName;

import java.util.Collections;
import java.util.Map;

public class JsonEnumHolder {

    public final String packageName;
    public final String injectedClassName;
    public final TypeName objectTypeName;
    public final Map<String, String> valuesMap;

    public boolean fileCreated;

    private JsonEnumHolder(JsonEnumHolderBuilder builder) {
        this.packageName = builder.packageName;
        this.injectedClassName = builder.injectedClassName;
        this.objectTypeName = builder.objectTypeName;
        this.valuesMap = builder.valuesMap;
    }

    public Map<String, String> getValuesMap() {
        return Collections.unmodifiableMap(valuesMap);
    }

    public static class JsonEnumHolderBuilder {
        private String packageName;
        private String injectedClassName;
        private TypeName objectTypeName;
        private Map<String, String> valuesMap;

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

        public JsonEnumHolderBuilder setValuesMap(Map<String, String> valuesMap) {
            this.valuesMap = valuesMap;
            return this;
        }

        public JsonEnumHolder build() {
            return new JsonEnumHolder(this);
        }

    }

}

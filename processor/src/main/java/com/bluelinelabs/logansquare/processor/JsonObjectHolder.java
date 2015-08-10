package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy;
import com.bluelinelabs.logansquare.annotation.JsonObject.FieldNamingPolicy;
import com.squareup.javapoet.TypeName;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.lang.model.element.TypeParameterElement;

public class JsonObjectHolder {

    public final String packageName;
    public final String injectedClassName;
    public final TypeName objectTypeName;
    public final boolean isAbstractClass;
    public final TypeName parentInjectedTypeName;
    public final FieldDetectionPolicy fieldDetectionPolicy;
    public final FieldNamingPolicy fieldNamingPolicy;
    public final boolean serializeNullObjects;
    public final boolean serializeNullCollectionElements;
    public final List<? extends TypeParameterElement> typeParameters;
    public String onCompleteCallback;
    public String preSerializeCallback;

    // Using a TreeMap now to keep the entries sorted. This ensures that code is
    // always written the exact same way, no matter which JDK you're using.
    public final Map<String, JsonFieldHolder> fieldMap = new TreeMap<>();
    public boolean fileCreated;

    private JsonObjectHolder(JsonObjectHolderBuilder builder) {
        packageName = builder.packageName;
        injectedClassName = builder.injectedClassName;
        objectTypeName = builder.objectTypeName;
        isAbstractClass = builder.isAbstractClass;
        parentInjectedTypeName = builder.parentInjectedTypeName;
        fieldDetectionPolicy = builder.fieldDetectionPolicy;
        fieldNamingPolicy = builder.fieldNamingPolicy;
        serializeNullObjects = builder.serializeNullObjects;
        serializeNullCollectionElements = builder.serializeNullCollectionElements;
        typeParameters = builder.typeParameters;
    }

    public static class JsonObjectHolderBuilder {
        private String packageName;
        private String injectedClassName;
        private TypeName objectTypeName;
        private boolean isAbstractClass;
        private TypeName parentInjectedTypeName;
        private FieldDetectionPolicy fieldDetectionPolicy;
        private FieldNamingPolicy fieldNamingPolicy;
        private boolean serializeNullObjects;
        private boolean serializeNullCollectionElements;
        private List<? extends TypeParameterElement> typeParameters;

        public JsonObjectHolderBuilder setPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public JsonObjectHolderBuilder setInjectedClassName(String injectedClassName) {
            this.injectedClassName = injectedClassName;
            return this;
        }

        public JsonObjectHolderBuilder setObjectTypeName(TypeName objectTypeName) {
            this.objectTypeName = objectTypeName;
            return this;
        }

        public JsonObjectHolderBuilder setIsAbstractClass(boolean isAbstractClass) {
            this.isAbstractClass = isAbstractClass;
            return this;
        }

        public JsonObjectHolderBuilder setParentInjectedTypeName(TypeName parentInjectedTypeName) {
            this.parentInjectedTypeName = parentInjectedTypeName;
            return this;
        }

        public JsonObjectHolderBuilder setFieldDetectionPolicy(FieldDetectionPolicy fieldDetectionPolicy) {
            this.fieldDetectionPolicy = fieldDetectionPolicy;
            return this;
        }

        public JsonObjectHolderBuilder setFieldNamingPolicy(FieldNamingPolicy fieldNamingPolicy) {
            this.fieldNamingPolicy = fieldNamingPolicy;
            return this;
        }

        public JsonObjectHolderBuilder setSerializeNullObjects(boolean serializeNullObjects) {
            this.serializeNullObjects = serializeNullObjects;
            return this;
        }

        public JsonObjectHolderBuilder setSerializeNullCollectionElements(boolean serializeNullCollectionElements) {
            this.serializeNullCollectionElements = serializeNullCollectionElements;
            return this;
        }

        public JsonObjectHolderBuilder setTypeParameters(List<? extends TypeParameterElement> typeParameters) {
            this.typeParameters = typeParameters;
            return this;
        }

        public JsonObjectHolder build() {
            return new JsonObjectHolder(this);
        }
    }
}

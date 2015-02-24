package com.bluelinelabs.logansquare.processor.type.field;

import com.squareup.javapoet.MethodSpec.Builder;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;

public abstract class NumberFieldType extends FieldType {

    protected boolean isPrimitive;

    public NumberFieldType(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public void serialize(Builder builder, int depth, String fieldName, String getter, boolean writeFieldNameForObject) {
        if (writeFieldNameForObject) {
            builder.addStatement("$L.writeNumberField($S, $L)", JSON_GENERATOR_VARIABLE_NAME, fieldName, getter);
        } else {
            builder.addStatement("$L.writeNumber($L)", JSON_GENERATOR_VARIABLE_NAME, getter);
        }
    }

}

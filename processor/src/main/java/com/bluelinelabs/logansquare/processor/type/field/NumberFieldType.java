package com.bluelinelabs.logansquare.processor.type.field;

import com.squareup.javapoet.MethodSpec.Builder;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;

public abstract class NumberFieldType extends FieldType {

    protected boolean isPrimitive;

    public NumberFieldType(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public void serialize(Builder builder, int depth, String fieldName, String getter, boolean isObjectProperty, boolean checkIfNull, boolean writeIfNull, boolean writeCollectionElementIfNull) {
        if (!isPrimitive && checkIfNull) {
            builder.beginControlFlow("if ($L != null)", getter);
        }

        if (isObjectProperty) {
            builder.addStatement("$L.writeNumberField($S, $L)", JSON_GENERATOR_VARIABLE_NAME, fieldName, getter);
        } else {
            builder.addStatement("$L.writeNumber($L)", JSON_GENERATOR_VARIABLE_NAME, getter);
        }

        if (!isPrimitive && checkIfNull) {
            if (writeIfNull) {
                builder.nextControlFlow("else");

                if (isObjectProperty) {
                    builder.addStatement("$L.writeFieldName($S)", JSON_GENERATOR_VARIABLE_NAME, fieldName);
                }
                builder.addStatement("$L.writeNull()", JSON_GENERATOR_VARIABLE_NAME);
            }

            builder.endControlFlow();
        }
    }

}

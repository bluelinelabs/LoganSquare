package com.bluelinelabs.logansquare.processor.fieldtype;

import com.bluelinelabs.logansquare.processor.JsonFieldHolder;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class FloatFieldType extends NumberFieldType {

    public FloatFieldType(boolean isPrimitive) {
        super(isPrimitive);
    }

    @Override
    public TypeName getTypeName() {
        return (isPrimitive ? TypeName.FLOAT : ClassName.get(Float.class));
    }

    @Override
    public TypeName getNonPrimitiveTypeName() {
        return ClassName.get(Float.class);
    }

    @Override
    public void parse(Builder builder, JsonFieldHolder fieldHolder) {
        if (isPrimitive) {
            builder.addCode("(float)$L.getValueAsDouble()", JSON_PARSER_VARIABLE_NAME);
        } else {
            builder.addCode("$L.getCurrentToken() == JsonToken.VALUE_NULL ? null : new Float($L.getValueAsDouble())", JSON_PARSER_VARIABLE_NAME, JSON_PARSER_VARIABLE_NAME);
        }
    }
}

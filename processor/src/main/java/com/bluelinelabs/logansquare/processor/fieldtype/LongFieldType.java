package com.bluelinelabs.logansquare.processor.fieldtype;

import com.bluelinelabs.logansquare.processor.JsonFieldHolder;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class LongFieldType extends NumberFieldType {

    public LongFieldType(boolean isPrimitive) {
        super(isPrimitive);
    }

    @Override
    public TypeName getTypeName() {
        return (isPrimitive ? TypeName.LONG : ClassName.get(Long.class));
    }

    @Override
    public TypeName getNonPrimitiveTypeName() {
        return ClassName.get(Long.class);
    }

    @Override
    public void parse(Builder builder, JsonFieldHolder fieldHolder) {
        if (isPrimitive) {
            builder.addCode("$L.getValueAsLong()", JSON_PARSER_VARIABLE_NAME);
        } else {
            builder.addCode("$L.getCurrentToken() == JsonToken.VALUE_NULL ? null : Long.valueOf($L.getValueAsLong())", JSON_PARSER_VARIABLE_NAME, JSON_PARSER_VARIABLE_NAME);
        }
    }
}

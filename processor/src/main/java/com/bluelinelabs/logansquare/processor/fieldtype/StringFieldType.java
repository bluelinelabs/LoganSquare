package com.bluelinelabs.logansquare.processor.fieldtype;

import com.bluelinelabs.logansquare.processor.JsonFieldHolder;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class StringFieldType extends FieldType {

    @Override
    public TypeName getTypeName() {
        return ClassName.get(String.class);
    }

    @Override
    public TypeName getNonPrimitiveTypeName() {
        return ClassName.get(String.class);
    }

    @Override
    public void parse(Builder builder, JsonFieldHolder fieldHolder) {
        builder.addCode("$L.getValueAsString(null)", JSON_PARSER_VARIABLE_NAME);
    }

    @Override
    public void serialize(Builder builder, JsonFieldHolder fieldHolder, String getter, boolean writeFieldNameForObject) {
        if (writeFieldNameForObject) {
            builder.addStatement("$L.writeStringField($S, $L)", JSON_GENERATOR_VARIABLE_NAME, fieldHolder.fieldName[0], getter);
        } else {
            builder.addStatement("$L.writeString($L)", JSON_GENERATOR_VARIABLE_NAME, getter);
        }
    }
}

package com.bluelinelabs.logansquare.processor.fieldtype;

import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.processor.JsonFieldHolder;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class DynamicFieldType extends FieldType {

    private TypeName mTypeName;

    public DynamicFieldType(TypeName typeName) {
        mTypeName = typeName;
    }

    @Override
    public TypeName getTypeName() {
        return mTypeName;
    }

    @Override
    public TypeName getNonPrimitiveTypeName() {
        return mTypeName;
    }

    @Override
    public void parse(Builder builder, JsonFieldHolder fieldHolder) {
        builder.addCode("LoganSquare.typeConverterFor($T.class).parse($L)", mTypeName, JSON_PARSER_VARIABLE_NAME);
    }

    @Override
    public void serialize(Builder builder, JsonFieldHolder fieldHolder, String getter, boolean writeFieldNameForObject) {
        if (!fieldHolder.fieldType.getTypeName().isPrimitive()) {
            builder.beginControlFlow("if ($L != null)", getter);
        }

        builder.addStatement("$T.typeConverterFor($T.class).serialize($L, $S, $L, $L)", LoganSquare.class, mTypeName, getter, fieldHolder.fieldName[0], writeFieldNameForObject, JSON_GENERATOR_VARIABLE_NAME);

        if (!fieldHolder.fieldType.getTypeName().isPrimitive()) {
            builder.endControlFlow();
        }
    }
}

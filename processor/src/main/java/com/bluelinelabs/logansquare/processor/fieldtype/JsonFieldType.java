package com.bluelinelabs.logansquare.processor.fieldtype;

import com.bluelinelabs.logansquare.processor.JsonFieldHolder;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class JsonFieldType extends FieldType {

    private ClassName mClassName;
    private ClassName mMapperClassName;

    public JsonFieldType(ClassName className, ClassName mapperClassName) {
        mClassName = className;
        mMapperClassName = mapperClassName;
    }

    @Override
    public TypeName getTypeName() {
        return mClassName;
    }

    @Override
    public TypeName getNonPrimitiveTypeName() {
        return mClassName;
    }

    @Override
    public String getJsonParserGetter(JsonFieldHolder fieldHolder) {
        return String.format("%s._parse(%s)", mMapperClassName.toString(), JSON_PARSER_VARIABLE_NAME);
    }

    @Override
    public void serialize(Builder builder, JsonFieldHolder fieldHolder, String getter, boolean writeFieldNameForObject) {
        builder.beginControlFlow("if ($L != null)", getter);

        if (writeFieldNameForObject) {
            builder.addStatement("$L.writeFieldName($S)", JSON_GENERATOR_VARIABLE_NAME, fieldHolder.fieldName[0]);
        }

        builder
                .addStatement("$T._serialize($L, $L, true)", mMapperClassName, getter, JSON_GENERATOR_VARIABLE_NAME)
                .endControlFlow();
    }
}

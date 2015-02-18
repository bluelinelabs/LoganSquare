package com.bluelinelabs.logansquare.processor.fieldtype;

import com.bluelinelabs.logansquare.processor.JsonFieldHolder;
import com.bluelinelabs.logansquare.processor.TextUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class TypeConverterFieldType extends FieldType {

    private TypeName mTypeName;
    private ClassName mTypeConverter;

    public TypeConverterFieldType(TypeName typeName, ClassName typeConverterClassName) {
        mTypeName = typeName;
        mTypeConverter = typeConverterClassName;
    }

    @Override
    public TypeName getTypeName() {
        return mTypeName;
    }

    public ClassName getTypeConverterClassName() {
        return mTypeConverter;
    }

    @Override
    public String getJsonParserGetter(JsonFieldHolder fieldHolder) {
        return String.format("%s.parse(%s)", TextUtils.toUpperCaseWithUnderscores(mTypeConverter.simpleName()), JSON_PARSER_VARIABLE_NAME);
    }

    @Override
    public void serialize(Builder builder, JsonFieldHolder fieldHolder, String getter, boolean writeFieldNameForObject) {
        if (!fieldHolder.fieldType.getTypeName().isPrimitive()) {
            builder.beginControlFlow("if ($L != null)", getter);
        }

        if (writeFieldNameForObject) {
            builder.addStatement("$L.writeFieldName($S)", JSON_GENERATOR_VARIABLE_NAME, fieldHolder.fieldName[0]);
        }

        builder.addStatement("$L.serialize($L, $S, $L)", TextUtils.toUpperCaseWithUnderscores(mTypeConverter.simpleName()), getter, fieldHolder.fieldName[0], JSON_GENERATOR_VARIABLE_NAME);

        if (!fieldHolder.fieldType.getTypeName().isPrimitive()) {
            builder.endControlFlow();
        }
    }
}

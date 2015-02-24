package com.bluelinelabs.logansquare.processor.type.field;

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

    @Override
    public TypeName getNonPrimitiveTypeName() {
        return mTypeName;
    }

    public ClassName getTypeConverterClassName() {
        return mTypeConverter;
    }

    @Override
    public void parse(Builder builder, int depth, String setter, Object... setterFormatArgs) {
        setter = replaceLastLiteral(setter, "$L.parse($L)");
        builder.addStatement(setter, addStringArgs(setterFormatArgs, TextUtils.toUpperCaseWithUnderscores(mTypeConverter.simpleName()), JSON_PARSER_VARIABLE_NAME));
    }

    @Override
    public void serialize(Builder builder, int depth, String fieldName, String getter, boolean writeFieldNameForObject) {
        if (!mTypeName.isPrimitive()) {
            builder.beginControlFlow("if ($L != null)", getter);
        }

        builder.addStatement("$L.serialize($L, $S, $L, $L)", TextUtils.toUpperCaseWithUnderscores(mTypeConverter.simpleName()), getter, fieldName, writeFieldNameForObject, JSON_GENERATOR_VARIABLE_NAME);

        if (!mTypeName.isPrimitive()) {
            builder.endControlFlow();
        }
    }
}

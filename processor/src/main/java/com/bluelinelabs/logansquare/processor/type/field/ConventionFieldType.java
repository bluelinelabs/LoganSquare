package com.bluelinelabs.logansquare.processor.type.field;

import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.ParameterizedType;
import com.bluelinelabs.logansquare.typeconverters.TypeConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.List;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class ConventionFieldType extends DynamicFieldType {
    private static final String CONVERTER_VAR = "typeConverter";

    private final TypeName mTypeName;
    private final TypeName mTypeConverterTypeName;

    public ConventionFieldType(TypeName typeName) {
        super(typeName);

        mTypeName = typeName;
        mTypeConverterTypeName = ParameterizedTypeName.get(ClassName.get(TypeConverter.class), mTypeName);
    }

    @Override
    public void parse(MethodSpec.Builder builder, int depth, String setter, Object... setterFormatArgs) {
        builder.addStatement("$T $N = $T.getConverter($T.class)", mTypeConverterTypeName, CONVERTER_VAR, LoganSquare.class, mTypeName);

        builder.beginControlFlow("if ($N == null)", CONVERTER_VAR);

        String setterConventional = replaceLastLiteral(setter, "$T.valueOf($L.getValueAsString(null))");
        builder.addStatement(setterConventional, expandStringArgs(setterFormatArgs, mTypeName, JSON_PARSER_VARIABLE_NAME));

        builder.nextControlFlow("else");

        String setterRuntime = replaceLastLiteral(setter, "$N.parse($L)");
        builder.addStatement(setterRuntime, expandStringArgs(setterFormatArgs, CONVERTER_VAR, JSON_PARSER_VARIABLE_NAME));

        builder.endControlFlow();
    }

    @Override
    public void serialize(MethodSpec.Builder builder, int depth, String fieldName, List<String> processedFieldNames, String getter, boolean isObjectProperty, boolean checkIfNull, boolean writeIfNull, boolean writeCollectionElementIfNull) {
        if (!mTypeName.isPrimitive() && checkIfNull) {
            builder.beginControlFlow("if ($L != null)", getter);
        }

        builder.addStatement("$T $N = $T.getConverter($T.class)", mTypeConverterTypeName, CONVERTER_VAR, LoganSquare.class, mTypeName);

        builder.beginControlFlow("if ($N == null)", CONVERTER_VAR);
        builder.addStatement("$N.serialize($L, $S, $L, $L)", CONVERTER_VAR, getter, isObjectProperty ? fieldName : null, isObjectProperty, JSON_GENERATOR_VARIABLE_NAME);
        builder.nextControlFlow("else");
        if (isObjectProperty) {
            builder.addStatement("$L.writeFieldName($S)", JSON_GENERATOR_VARIABLE_NAME, fieldName);
        }
        builder.addStatement("$L.writeString($L.toString())", JSON_GENERATOR_VARIABLE_NAME, getter);
        builder.endControlFlow();

        if (!mTypeName.isPrimitive() && checkIfNull) {
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

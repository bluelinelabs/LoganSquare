package com.bluelinelabs.logansquare.processor.type.collection;

import com.bluelinelabs.logansquare.processor.type.Type;
import com.fasterxml.jackson.core.JsonToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;

import java.util.Map;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public abstract class MapCollectionType extends CollectionType {

    private final ClassName mClassName;

    public MapCollectionType(ClassName className) {
        mClassName = className;
    }

    @Override
    public String getParameterizedTypeString() {
        return "$T<$T, " + parameterTypes.get(1).getParameterizedTypeString() + ">";
    }

    @Override
    public Object[] getParameterizedTypeStringArgs() {
        return expandStringArgs(mClassName, String.class, parameterTypes.get(1).getParameterizedTypeStringArgs());
    }

    @Override
    public void parse(Builder builder, int depth, String setter, Object... setterFormatArgs) {
        Type parameterType = parameterTypes.get(1);

        final String mapVariableName = "map" + depth;
        final String keyVariableName = "key" + depth;

        final String instanceCreator = String.format("$T<$T, %s> $L = new $T<$T, %s>()", parameterType.getParameterizedTypeString(), parameterType.getParameterizedTypeString());
        final Object[] instanceCreatorArgs = expandStringArgs(getTypeName(), String.class, parameterType.getParameterizedTypeStringArgs(), mapVariableName, getTypeName(), String.class, parameterType.getParameterizedTypeStringArgs());

        builder.beginControlFlow("if ($L.getCurrentToken() == $T.START_OBJECT)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement(instanceCreator, instanceCreatorArgs)
                .beginControlFlow("while ($L.nextToken() != $T.END_OBJECT)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement("$T $L = $L.getText()", String.class, keyVariableName, JSON_PARSER_VARIABLE_NAME)
                .addStatement("$L.nextToken()", JSON_PARSER_VARIABLE_NAME)
                .beginControlFlow("if ($L.getCurrentToken() == $T.VALUE_NULL)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement("$L.put($L, null)", mapVariableName, keyVariableName)
                .nextControlFlow("else");

        parameterType.parse(builder, depth + 1, "$L.put($L, $L)", mapVariableName, keyVariableName);

        builder
                .endControlFlow()
                .endControlFlow()
                .addStatement(setter, expandStringArgs(setterFormatArgs, mapVariableName))
                .nextControlFlow("else")
                .addStatement(setter, expandStringArgs(setterFormatArgs, "null"))
                .endControlFlow();
    }

    @Override
    public void serialize(MethodSpec.Builder builder, int depth, String fieldName, String getter, boolean isObjectProperty, boolean checkIfNull, boolean writeIfNull, boolean writeCollectionElementIfNull) {
        Type parameterType = parameterTypes.get(1);

        final String mapVariableName = "lslocal" + fieldName;
        final String entryVariableName = "entry" + depth;

        final String instanceCreator = String.format("final $T<$T, %s> $L = $L", parameterType.getParameterizedTypeString());
        final Object[] instanceCreatorArgs = expandStringArgs(Map.class, String.class, parameterType.getParameterizedTypeStringArgs(), mapVariableName, getter);

        final String forLine = String.format("for ($T<$T, %s> $L : $L.entrySet())", parameterType.getParameterizedTypeString());
        final Object[] forLineArgs = expandStringArgs(Map.Entry.class, String.class, parameterType.getParameterizedTypeStringArgs(), entryVariableName, mapVariableName);

        builder
                .addStatement(instanceCreator, instanceCreatorArgs)
                .beginControlFlow("if ($L != null)", mapVariableName);

        if (isObjectProperty) {
            builder.addStatement("$L.writeFieldName($S)", JSON_GENERATOR_VARIABLE_NAME, fieldName);
        }

        builder
                .addStatement("$L.writeStartObject()", JSON_GENERATOR_VARIABLE_NAME)
                .beginControlFlow(forLine, forLineArgs)
                .addStatement("$L.writeFieldName($L.getKey().toString())", JSON_GENERATOR_VARIABLE_NAME, entryVariableName)
                .beginControlFlow("if ($L.getValue() != null)", entryVariableName);

        parameterType.serialize(builder, depth + 1, mapVariableName + "Element", entryVariableName + ".getValue()", false, false, true, writeCollectionElementIfNull);

        if (writeCollectionElementIfNull) {
            builder
                    .nextControlFlow("else")
                    .addStatement("$L.writeNull()", JSON_GENERATOR_VARIABLE_NAME);
        }

        builder
                .endControlFlow()
                .endControlFlow()
                .addStatement("$L.writeEndObject()", JSON_GENERATOR_VARIABLE_NAME)
                .endControlFlow();
    }
}

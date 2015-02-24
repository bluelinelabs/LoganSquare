package com.bluelinelabs.logansquare.processor.type.container;

import com.fasterxml.jackson.core.JsonToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import java.util.HashMap;
import java.util.Map;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class HashMapCollectionType extends ContainerType {

    @Override
    public TypeName getTypeName() {
        return ClassName.get(HashMap.class);
    }

    @Override
    public void parse(Builder builder, int depth, String setter, Object... setterFormatArgs) {
        TypeName valueType = subType.getTypeName();

        String varName = "map" + depth;

        builder.beginControlFlow("if ($L.getCurrentToken() == $T.START_OBJECT)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement("$T<$T, $T> $L = new $T<$T, $T>()", HashMap.class, String.class, valueType, varName, HashMap.class, String.class, valueType)
                .beginControlFlow("while ($L.nextToken() != $T.END_OBJECT)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement("$T key = $L.getText()", String.class, JSON_PARSER_VARIABLE_NAME)
                .addStatement("$L.nextToken()", JSON_PARSER_VARIABLE_NAME)
                .beginControlFlow("if ($L.getCurrentToken() == $T.VALUE_NULL)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement("$L.put(key, null)", varName)
                .nextControlFlow("else");

        subType.parse(builder, depth + 1, "$L.put(key, $L)", varName);

        builder
                .endControlFlow()
                .endControlFlow()
                .addStatement(setter, addStringArgs(setterFormatArgs, varName))
                .endControlFlow();
    }

    @Override
    public void serialize(MethodSpec.Builder builder, int depth, String fieldName, String getter, boolean writeFieldName) {
        TypeName valueType = subType.getTypeName();

        final String mapVariableName = "lslocal" + fieldName;
        final String entryVariableName = "entry" + depth;

        builder
                .addStatement("final $T<$T, $T> $L = $L", Map.class, String.class, valueType, mapVariableName, getter)
                .beginControlFlow("if ($L != null)", mapVariableName)
                .addStatement("$L.writeFieldName($S)", JSON_GENERATOR_VARIABLE_NAME, fieldName)
                .addStatement("$L.writeStartObject()", JSON_GENERATOR_VARIABLE_NAME)
                .beginControlFlow("for ($T<$T, $T> $L : $L.entrySet())", Map.Entry.class, String.class, valueType, entryVariableName, mapVariableName)
                .addStatement("$L.writeFieldName($L.getKey().toString())", JSON_GENERATOR_VARIABLE_NAME, entryVariableName)
                .beginControlFlow("if ($L.getValue() == null)", entryVariableName)
                .addStatement("$L.writeNull()", JSON_GENERATOR_VARIABLE_NAME)
                .nextControlFlow("else");

        subType.serialize(builder, depth + 1, mapVariableName + "Element", entryVariableName + ".getValue()", false);

        builder
                .endControlFlow()
                .endControlFlow()
                .addStatement("$L.writeEndObject()", JSON_GENERATOR_VARIABLE_NAME)
                .endControlFlow();
    }
}

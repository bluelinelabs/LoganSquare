package com.bluelinelabs.logansquare.processor.type.container;

import com.bluelinelabs.logansquare.processor.TextUtils;
import com.fasterxml.jackson.core.JsonToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;

import java.util.List;
import java.util.Map;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public abstract class MapContainerType extends ContainerType {

    private final ClassName mClassName;

    public MapContainerType(ClassName className) {
        mClassName = className;
    }

    @Override
    public String getParameterizedTypeString() {
        return "$T<$T, " + subType.getParameterizedTypeString() + ">";
    }

    @Override
    public Object[] getParameterizedTypeStringArgs() {
        return expandStringArgs(mClassName, String.class, subType.getParameterizedTypeStringArgs());
    }

    @Override
    public void parse(Builder builder, int depth, String setter, Object... setterFormatArgs) {
        String mapVariableName = "map" + depth;
        String keyVariableName = "key" + depth;

        final String instanceCreator = String.format("$T<$T, %s> $L = new $T<$T, %s>()", subType.getParameterizedTypeString(), subType.getParameterizedTypeString());
        final Object[] instanceCreatorArgs = expandStringArgs(getTypeName(), String.class, subType.getParameterizedTypeStringArgs(), mapVariableName, getTypeName(), String.class, subType.getParameterizedTypeStringArgs());

        builder.beginControlFlow("if ($L.getCurrentToken() == $T.START_OBJECT)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement(instanceCreator, instanceCreatorArgs)
                .beginControlFlow("while ($L.nextToken() != $T.END_OBJECT)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement("$T $L = $L.getText()", String.class, keyVariableName, JSON_PARSER_VARIABLE_NAME)
                .addStatement("$L.nextToken()", JSON_PARSER_VARIABLE_NAME)
                .beginControlFlow("if ($L.getCurrentToken() == $T.VALUE_NULL)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement("$L.put($L, null)", mapVariableName, keyVariableName)
                .nextControlFlow("else");

        subType.parse(builder, depth + 1, "$L.put($L, $L)", mapVariableName, keyVariableName);

        builder
                .endControlFlow()
                .endControlFlow()
                .addStatement(setter, expandStringArgs(setterFormatArgs, mapVariableName))
                .nextControlFlow("else")
                .addStatement(setter, expandStringArgs(setterFormatArgs, "null"))
                .endControlFlow();
    }

    @Override
    public void serialize(MethodSpec.Builder builder, int depth, String fieldName, List<String> processedFieldNames, String getter, boolean isObjectProperty, boolean checkIfNull, boolean writeIfNull, boolean writeCollectionElementIfNull) {
        final String cleanFieldName = TextUtils.toUniqueFieldNameVariable(fieldName, processedFieldNames);
        processedFieldNames.add(cleanFieldName);
        final String mapVariableName = "lslocal" + cleanFieldName;
        final String entryVariableName = "entry" + depth;

        final String instanceCreator = String.format("final $T<$T, %s> $L = $L", subType.getParameterizedTypeString());
        final Object[] instanceCreatorArgs = expandStringArgs(Map.class, String.class, subType.getParameterizedTypeStringArgs(), mapVariableName, getter);

        final String forLine = String.format("for ($T<$T, %s> $L : $L.entrySet())", subType.getParameterizedTypeString());
        final Object[] forLineArgs = expandStringArgs(Map.Entry.class, String.class, subType.getParameterizedTypeStringArgs(), entryVariableName, mapVariableName);

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

        subType.serialize(builder, depth + 1, mapVariableName + "Element", processedFieldNames, entryVariableName + ".getValue()", false, false, true, writeCollectionElementIfNull);

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

package com.bluelinelabs.logansquare.processor.type.container;

import com.bluelinelabs.logansquare.processor.TextUtils;
import com.fasterxml.jackson.core.JsonToken;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;

import java.util.List;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public abstract class SingleParameterCollectionType extends ContainerType {

    public abstract Class getGenericClass();

    @Override
    public void parse(Builder builder, int depth, String setter, Object... setterFormatArgs) {
        final String collectionVarName = "collection" + depth;
        final String valueVarName = "value" + depth;

        final String instanceCreator = String.format("$T<%s> $L = new $T<%s>()", subType.getParameterizedTypeString(), subType.getParameterizedTypeString());
        final Object[] instanceCreatorArgs = expandStringArgs(getTypeName(), subType.getParameterizedTypeStringArgs(), collectionVarName, getTypeName(), subType.getParameterizedTypeStringArgs());

        builder.beginControlFlow("if ($L.getCurrentToken() == $T.START_ARRAY)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement(instanceCreator, instanceCreatorArgs)
                .beginControlFlow("while ($L.nextToken() != $T.END_ARRAY)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement("$T $L", subType.getTypeName(), valueVarName);

        subType.parse(builder, depth + 1, "$L = $L", valueVarName);

        builder
                .addStatement("$L.add($L)", collectionVarName, valueVarName)
                .endControlFlow();

        builder
                .addStatement(setter, expandStringArgs(setterFormatArgs, collectionVarName))
                .nextControlFlow("else")
                .addStatement(setter, expandStringArgs(setterFormatArgs, "null"))
                .endControlFlow();
    }

    @Override
    public void serialize(MethodSpec.Builder builder, int depth, String fieldName, List<String> processedFieldNames, String getter, boolean isObjectProperty, boolean checkIfNull, boolean writeIfNull, boolean writeCollectionElementIfNull) {
        final String cleanFieldName = TextUtils.toUniqueFieldNameVariable(fieldName, processedFieldNames);
        final String collectionVariableName = "lslocal" + cleanFieldName;
        final String elementVarName = "element" + depth;

        final String instanceCreator = String.format("final $T<%s> $L = $L", subType.getParameterizedTypeString());
        final Object[] instanceCreatorArgs = expandStringArgs(getGenericClass(), subType.getParameterizedTypeStringArgs(), collectionVariableName, getter);

        final String forLine = String.format("for (%s $L : $L)", subType.getParameterizedTypeString());
        final Object[] forLineArgs = expandStringArgs(subType.getParameterizedTypeStringArgs(), elementVarName, collectionVariableName);

        builder
                .addStatement(instanceCreator, instanceCreatorArgs)
                .beginControlFlow("if ($L != null)", collectionVariableName);

        if (isObjectProperty) {
            builder.addStatement("$L.writeFieldName($S)", JSON_GENERATOR_VARIABLE_NAME, fieldName);
        }

        builder
                .addStatement("$L.writeStartArray()", JSON_GENERATOR_VARIABLE_NAME)
                .beginControlFlow(forLine, forLineArgs)
                .beginControlFlow("if ($L != null)", elementVarName);

        subType.serialize(builder, depth + 1, collectionVariableName + "Element", processedFieldNames, elementVarName, false, false, false, writeCollectionElementIfNull);

        if (writeCollectionElementIfNull) {
            builder
                    .nextControlFlow("else")
                    .addStatement("$L.writeNull()", JSON_GENERATOR_VARIABLE_NAME);
        }

            builder
                .endControlFlow()
                .endControlFlow()
                .addStatement("$L.writeEndArray()", JSON_GENERATOR_VARIABLE_NAME)
                .endControlFlow();
    }

}

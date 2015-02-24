package com.bluelinelabs.logansquare.processor.type.container;

import com.fasterxml.jackson.core.JsonToken;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public abstract class SingleParameterCollectionType extends ContainerType {

    public abstract Class getGenericClass();

    @Override
    public void parse(Builder builder, int depth, String setter, Object... setterFormatArgs) {
        TypeName fieldType = subType.getTypeName();

        final String collectionVarName = "collection" + depth;
        final String valueVarName = "value" + depth;

        builder.beginControlFlow("if ($L.getCurrentToken() == $T.START_ARRAY)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement("$T<$T> $L = new $T<$T>()", getTypeName(), fieldType, collectionVarName, getTypeName(), fieldType)
                .beginControlFlow("while ($L.nextToken() != $T.END_ARRAY)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement("$T $L", fieldType, valueVarName);

        subType.parse(builder, depth + 1, "$L = $L", valueVarName);

        builder
                .beginControlFlow("if ($L != null)", valueVarName)
                .addStatement("$L.add($L)", collectionVarName, valueVarName)
                .endControlFlow()
                .endControlFlow();

        builder
                .addStatement(setter, addStringArgs(setterFormatArgs, collectionVarName))
                .endControlFlow();
    }

    @Override
    public void serialize(MethodSpec.Builder builder, int depth, String fieldName, String getter, boolean writeFieldName) {
        TypeName fieldType = subType.getTypeName();

        String collectionVariableName = "lslocal" + fieldName;
        final String elementVarName = "element" + depth;

        builder
                .addStatement("final $T<$T> $L = $L", getGenericClass(), fieldType, collectionVariableName, getter)
                .beginControlFlow("if ($L != null)", collectionVariableName)
                .addStatement("$L.writeFieldName($S)", JSON_GENERATOR_VARIABLE_NAME, fieldName)
                .addStatement("$L.writeStartArray()", JSON_GENERATOR_VARIABLE_NAME)
                .beginControlFlow("for ($T $L : $L)", fieldType, elementVarName, collectionVariableName);

        subType.serialize(builder, depth + 1, collectionVariableName + "Element", elementVarName, false);

        builder
                .endControlFlow()
                .addStatement("$L.writeEndArray()", JSON_GENERATOR_VARIABLE_NAME)
                .endControlFlow();
    }

}

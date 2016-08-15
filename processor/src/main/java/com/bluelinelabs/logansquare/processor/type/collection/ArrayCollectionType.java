package com.bluelinelabs.logansquare.processor.type.collection;

import com.bluelinelabs.logansquare.processor.TextUtils;
import com.bluelinelabs.logansquare.processor.type.Type;
import com.fasterxml.jackson.core.JsonToken;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class ArrayCollectionType extends CollectionType {

    private final Type arrayType;

    public ArrayCollectionType(Type arrayType) {
        this.arrayType = arrayType;
        addParameterType(arrayType);
    }

    @Override
    public TypeName getTypeName() {
        return ArrayTypeName.of(arrayType.getTypeName());
    }

    @Override
    public String getParameterizedTypeString() {
        return arrayType.getParameterizedTypeString() + "[]";
    }

    @Override
    public Object[] getParameterizedTypeStringArgs() {
        return arrayType.getParameterizedTypeStringArgs();
    }

    @Override
    public void parse(Builder builder, int depth, String setter, Object... setterFormatArgs) {
        TypeName fieldType = arrayType.getTypeName();
        final String collectionVarName = "collection" + depth;

        final String instanceCreator = String.format("$T<%s> $L = new $T<%s>()", arrayType.getParameterizedTypeString(), arrayType.getParameterizedTypeString());
        final Object[] instanceCreatorArgs = expandStringArgs(List.class, arrayType.getParameterizedTypeStringArgs(), collectionVarName, ArrayList.class, arrayType.getParameterizedTypeStringArgs());

        builder.beginControlFlow("if ($L.getCurrentToken() == $T.START_ARRAY)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement(instanceCreator, instanceCreatorArgs)
                .beginControlFlow("while ($L.nextToken() != $T.END_ARRAY)", JSON_PARSER_VARIABLE_NAME, JsonToken.class);

        if (!fieldType.isPrimitive()) {
            final String valueVarName = "value" + depth;

            builder.addStatement("$T $L", arrayType.getTypeName(), valueVarName);
            arrayType.parse(builder, depth + 1, "$L = $L", valueVarName);

            builder.addStatement("$L.add($L)", collectionVarName, valueVarName);
        } else {
            arrayType.parse(builder, depth + 1, "$L.add($L)", collectionVarName);
        }

        builder.endControlFlow();

        if (fieldType.isPrimitive()) {
            builder
                    .addStatement("$T[] array = new $T[$L.size()]", fieldType, fieldType, collectionVarName)
                    .addStatement("int i = 0")
                    .beginControlFlow("for ($T value : $L)", fieldType, collectionVarName)
                    .addStatement("array[i++] = value")
                    .endControlFlow();
        } else {
            builder.addStatement("$T[] array = $L.toArray(new $T[$L.size()])", fieldType, collectionVarName, fieldType, collectionVarName);
        }

        builder
                .addStatement(setter, expandStringArgs(setterFormatArgs, "array"))
                .nextControlFlow("else")
                .addStatement(setter, expandStringArgs(setterFormatArgs, "null"))
                .endControlFlow();
    }

    @Override
    public void serialize(MethodSpec.Builder builder, int depth, String fieldName, List<String> processedFieldNames, String getter, boolean isObjectProperty, boolean checkIfNull, boolean writeIfNull, boolean writeCollectionElementIfNull) {
        final String cleanFieldName = TextUtils.toUniqueFieldNameVariable(fieldName, processedFieldNames);
        final String collectionVariableName = "lslocal" + cleanFieldName;
        final String elementVarName = "element" + depth;

        final String instanceCreator;
        final Object[] instanceCreatorArgs;
        final String forLine;
        final Object[] forLineArgs;
        if (arrayType.getTypeName().isPrimitive()) {
            instanceCreator = "final $T[] $L = $L";
            instanceCreatorArgs = expandStringArgs(arrayType.getTypeName(), collectionVariableName, getter);
            forLine = "for ($T $L : $L)";
            forLineArgs = expandStringArgs(arrayType.getTypeName(), elementVarName, collectionVariableName);
        } else {
            instanceCreator = String.format("final %s[] $L = $L", arrayType.getParameterizedTypeString());
            instanceCreatorArgs = expandStringArgs(arrayType.getParameterizedTypeStringArgs(), collectionVariableName, getter);
            forLine = String.format("for (%s $L : $L)", arrayType.getParameterizedTypeString());
            forLineArgs = expandStringArgs(arrayType.getParameterizedTypeStringArgs(), elementVarName, collectionVariableName);
        }

        builder
                .addStatement(instanceCreator, instanceCreatorArgs)
                .beginControlFlow("if ($L != null)", collectionVariableName);

        if (isObjectProperty) {
            builder.addStatement("$L.writeFieldName($S)", JSON_GENERATOR_VARIABLE_NAME, fieldName);
        }

        builder
                .addStatement("$L.writeStartArray()", JSON_GENERATOR_VARIABLE_NAME)
                .beginControlFlow(forLine, forLineArgs);

        if (!arrayType.getTypeName().isPrimitive()) {
            builder.beginControlFlow("if ($L != null)", elementVarName);
        }

        arrayType.serialize(builder, depth + 1, collectionVariableName + "Element", processedFieldNames, elementVarName, false, false, false, writeCollectionElementIfNull);

        if (!arrayType.getTypeName().isPrimitive()) {
            if (writeCollectionElementIfNull) {
                builder
                        .nextControlFlow("else")
                        .addStatement("$L.writeNull()", JSON_GENERATOR_VARIABLE_NAME);
            }
            builder.endControlFlow();
        }

        builder
                .endControlFlow()
                .addStatement("$L.writeEndArray()", JSON_GENERATOR_VARIABLE_NAME)
                .endControlFlow();
    }

    @Override
    public Set<ClassNameObjectMapper> getUsedJsonObjectMappers() {
        Set<ClassNameObjectMapper> mappers = super.getUsedJsonObjectMappers();
        mappers.addAll(arrayType.getUsedJsonObjectMappers());
        return mappers;
    }
}

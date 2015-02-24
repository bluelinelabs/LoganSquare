package com.bluelinelabs.logansquare.processor.type.container;

import com.bluelinelabs.logansquare.processor.type.field.FieldType;
import com.fasterxml.jackson.core.JsonToken;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class ArrayCollectionType extends ContainerType {

    @Override
    public TypeName getTypeName() {
        return ArrayTypeName.of(subType.getTypeName());
    }

    @Override
    public String getParameterizedTypeString() {
        return subType.getParameterizedTypeString() + "[]";
    }

    @Override
    public Object[] getParameterizedTypeStringArgs() {
        return subType.getParameterizedTypeStringArgs();
    }

    @Override
    public void parse(Builder builder, int depth, String setter, Object... setterFormatArgs) {
        TypeName fieldType = subType.getTypeName();
        final String collectionVarName = "collection" + depth;

        final String instanceCreator = String.format("$T<%s> $L = new $T<%s>()", subType.getParameterizedTypeString(), subType.getParameterizedTypeString());
        final Object[] instanceCreatorArgs = expandStringArgs(List.class, subType.getParameterizedTypeStringArgs(), collectionVarName, ArrayList.class, subType.getParameterizedTypeStringArgs());

        builder.beginControlFlow("if ($L.getCurrentToken() == $T.START_ARRAY)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement(instanceCreator, instanceCreatorArgs)
                .beginControlFlow("while ($L.nextToken() != $T.END_ARRAY)", JSON_PARSER_VARIABLE_NAME, JsonToken.class);

        if (!fieldType.isPrimitive()) {
            final String valueVarName = "value" + depth;

            builder.addStatement("$T $L", subType.getTypeName(), valueVarName);
            subType.parse(builder, depth + 1, "$L = $L", valueVarName);

            builder
                    .beginControlFlow("if ($L != null)", valueVarName)
                    .addStatement("$L.add($L)", collectionVarName, valueVarName)
                    .endControlFlow();
        } else {
            subType.parse(builder, depth + 1, "$L.add($L)", collectionVarName);
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
    public void serialize(MethodSpec.Builder builder, int depth, String fieldName, String getter, boolean writeFieldName) {
        String collectionVariableName = "lslocal" + fieldName;
        final String elementVarName = "element" + depth;

        final String instanceCreator;
        final Object[] instanceCreatorArgs;
        final String forLine;
        final Object[] forLineArgs;
        if (subType.getTypeName().isPrimitive()) {
            instanceCreator = "final $T[] $L = $L";
            instanceCreatorArgs = expandStringArgs(subType.getTypeName(), collectionVariableName, getter);
            forLine = "for ($T $L : $L)";
            forLineArgs = expandStringArgs(subType.getTypeName(), elementVarName, collectionVariableName);
        } else {
            instanceCreator = String.format("final %s[] $L = $L", subType.getParameterizedTypeString());
            instanceCreatorArgs = expandStringArgs(subType.getParameterizedTypeStringArgs(), collectionVariableName, getter);
            forLine = String.format("for (%s $L : $L)", subType.getParameterizedTypeString());
            forLineArgs = expandStringArgs(subType.getParameterizedTypeStringArgs(), elementVarName, collectionVariableName);
        }

        builder
                .addStatement(instanceCreator, instanceCreatorArgs)
                .beginControlFlow("if ($L != null)", collectionVariableName);

        if (writeFieldName) {
            builder.addStatement("$L.writeFieldName($S)", JSON_GENERATOR_VARIABLE_NAME, fieldName);
        }

        builder
                .addStatement("$L.writeStartArray()", JSON_GENERATOR_VARIABLE_NAME)
                .beginControlFlow(forLine, forLineArgs);

        subType.serialize(builder, depth + 1, collectionVariableName + "Element", elementVarName, false);

        builder
                .endControlFlow()
                .addStatement("$L.writeEndArray()", JSON_GENERATOR_VARIABLE_NAME)
                .endControlFlow();
    }
}

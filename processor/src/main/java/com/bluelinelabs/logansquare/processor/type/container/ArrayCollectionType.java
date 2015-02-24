package com.bluelinelabs.logansquare.processor.type.container;

import com.bluelinelabs.logansquare.processor.type.field.FieldType;
import com.fasterxml.jackson.core.JsonToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class ArrayCollectionType extends ContainerType {

    @Override
    public TypeName getTypeName() {
        return ClassName.bestGuess(subType.toString() + "[]");
    }

    @Override
    public void parse(Builder builder, int depth, String setter, Object... setterFormatArgs) {
        TypeName fieldType = subType.getTypeName();
        TypeName nonPrimitiveFieldType;
        if (subType instanceof FieldType) {
            nonPrimitiveFieldType = ((FieldType)subType).getNonPrimitiveTypeName();
        } else {
            nonPrimitiveFieldType = subType.getTypeName();
        }

        final String collectionVarName = "collection" + depth;
        final String valueVarName = "value" + depth;

        builder.beginControlFlow("if ($L.getCurrentToken() == $T.START_ARRAY)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement("$T<$T> $L = new $T<$T>()", List.class, nonPrimitiveFieldType, collectionVarName, ArrayList.class, nonPrimitiveFieldType)
                .beginControlFlow("while ($L.nextToken() != $T.END_ARRAY)", JSON_PARSER_VARIABLE_NAME, JsonToken.class);

        if (!fieldType.isPrimitive()) {
            subType.parse(builder, depth + 1, "$T $L = $L", fieldType, valueVarName);

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
                .addStatement(setter, addStringArgs(setterFormatArgs, "array"))
                .endControlFlow();
    }

    @Override
    public void serialize(MethodSpec.Builder builder, int depth, String fieldName, String getter, boolean writeFieldName) {
        TypeName fieldType = subType.getTypeName();
        String collectionVariableName = "lslocal" + fieldName;
        final String elementVarName = "element" + depth;

        builder
                .addStatement("final $T[] $L = $L", fieldType, collectionVariableName, getter)
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

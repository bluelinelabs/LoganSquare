package com.bluelinelabs.logansquare.processor.collectiontype;

import com.bluelinelabs.logansquare.processor.JsonFieldHolder;
import com.fasterxml.jackson.core.JsonToken;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class ArrayCollectionType extends CollectionType {

    @Override
    public void serialize(Builder builder, JsonFieldHolder fieldHolder, String variableName) {
        String getter;
        if (fieldHolder.hasGetter()) {
            getter = "object." + fieldHolder.getterMethod + "()";
        } else {
            getter = "object." + variableName;
        }

        TypeName fieldType = fieldHolder.fieldType.getTypeName();
        String collectionVariableName = "lslocal" + variableName;
        builder
                .addStatement("final $T[] $L = $L", fieldType, collectionVariableName, getter)
                .beginControlFlow("if ($L != null)", collectionVariableName)
                .addStatement("$L.writeFieldName($S)", JSON_GENERATOR_VARIABLE_NAME, fieldHolder.fieldName[0])
                .addStatement("$L.writeStartArray()", JSON_GENERATOR_VARIABLE_NAME)
                .beginControlFlow("for ($T element : $L)", fieldType, collectionVariableName);

        fieldHolder.fieldType.serialize(builder, fieldHolder, "element", false);

        builder
                .endControlFlow()
                .addStatement("$L.writeEndArray()", JSON_GENERATOR_VARIABLE_NAME)
                .endControlFlow();
    }

    @Override
    public void parse(Builder builder, String objectName, String variableName, JsonFieldHolder fieldHolder) {
        TypeName fieldType = fieldHolder.fieldType.getTypeName();
        TypeName nonPrimitiveFieldType = fieldHolder.fieldType.getNonPrimitiveTypeName();

        builder.beginControlFlow("if ($L.getCurrentToken() == $T.START_ARRAY)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement("$T<$T> list = new $T<$T>()", List.class, nonPrimitiveFieldType, ArrayList.class, nonPrimitiveFieldType)
                .beginControlFlow("while ($L.nextToken() != $T.END_ARRAY)", JSON_PARSER_VARIABLE_NAME, JsonToken.class);

        if (!fieldType.isPrimitive()) {
            builder.addCode("$T value = ", fieldType);

            fieldHolder.fieldType.parse(builder, fieldHolder);

            builder
                    .addCode(";\n")
                    .beginControlFlow("if (value != null)")
                    .addStatement("list.add(value)")
                    .endControlFlow();
        } else {
            builder.addCode("list.add(");
            fieldHolder.fieldType.parse(builder, fieldHolder);
            builder.addCode(");\n");
        }

        builder.endControlFlow();

        if (fieldType.isPrimitive()) {
            builder
                    .addStatement("$T[] array = new $T[list.size()]", fieldType, fieldType)
                    .addStatement("int i = 0")
                    .beginControlFlow("for ($T value : list)", fieldType)
                    .addStatement("array[i++] = value")
                    .endControlFlow();
        } else {
            builder.addStatement("$T[] array = list.toArray(new $T[list.size()])", fieldType, fieldType);
        }

        if (fieldHolder.hasSetter()) {
            builder.addStatement("instance.$L(array)", fieldHolder.setterMethod);
        } else {
            builder.addStatement("instance.$L = array", variableName);
        }

        builder.endControlFlow();
    }
}

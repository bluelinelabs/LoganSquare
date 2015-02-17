package com.bluelinelabs.logansquare.processor.collectiontype;

import com.bluelinelabs.logansquare.processor.JsonFieldHolder;
import com.fasterxml.jackson.core.JsonToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.HashMap;
import java.util.Map;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class HashMapCollectionType extends CollectionType {

    @Override
    public void parse(MethodSpec.Builder builder, String objectName, String variableName, JsonFieldHolder fieldHolder) {
        TypeName valueType = fieldHolder.fieldType.getTypeName();

        builder.beginControlFlow("if ($L.getCurrentToken() == $T.START_OBJECT)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement("$T<$T, $T> map = new $T<$T, $T>()", HashMap.class, String.class, valueType, HashMap.class, String.class, valueType)
                .beginControlFlow("while ($L.nextToken() != $T.END_OBJECT)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement("$T key = $L.getText()", String.class, JSON_PARSER_VARIABLE_NAME)
                .addStatement("$L.nextToken()", JSON_PARSER_VARIABLE_NAME)
                .beginControlFlow("if ($L.getCurrentToken() == $T.VALUE_NULL)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement("map.put(key, null)")
                .nextControlFlow("else")
                .addStatement("map.put(key, $L)", fieldHolder.fieldType.getJsonParserGetter(fieldHolder))
                .endControlFlow()
                .endControlFlow();

        if (fieldHolder.hasSetter()) {
            builder.addStatement("instance.$L(map)", fieldHolder.setterMethod);
        } else {
            builder.addStatement("instance.$L = map", variableName);
        }

        builder.endControlFlow();
    }

    @Override
    public void serialize(MethodSpec.Builder builder, JsonFieldHolder fieldHolder, String variableName) {
        TypeName valueType = fieldHolder.fieldType.getTypeName();

        String getter;
        if (fieldHolder.hasGetter()) {
            getter = "object." + fieldHolder.getterMethod + "()";
        } else {
            getter = "object." + variableName;
        }

        builder
                .beginControlFlow("if (object.$L != null)", variableName)
                .addStatement("$L.writeFieldName($S)", JSON_GENERATOR_VARIABLE_NAME, fieldHolder.fieldName[0])
                .addStatement("$L.writeStartObject()", JSON_GENERATOR_VARIABLE_NAME)
                .beginControlFlow("for ($T<$T, $T> entry : $L.entrySet())", Map.Entry.class, String.class, valueType, getter)
                .addStatement("$L.writeFieldName(entry.getKey().toString())", JSON_GENERATOR_VARIABLE_NAME)
                .beginControlFlow("if (entry.getValue() == null)")
                .addStatement("$L.writeNull()", JSON_GENERATOR_VARIABLE_NAME)
                .nextControlFlow("else");

        fieldHolder.fieldType.serialize(builder, fieldHolder, variableName, "entry.getValue()", false);

        builder
                .endControlFlow()
                .endControlFlow()
                .addStatement("$L.writeEndObject()", JSON_GENERATOR_VARIABLE_NAME)
                .endControlFlow();
    }
}

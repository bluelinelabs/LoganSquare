package com.bluelinelabs.logansquare.processor.collectiontype;

import com.bluelinelabs.logansquare.processor.JsonFieldHolder;
import com.fasterxml.jackson.core.JsonToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public abstract class SingleParameterCollectionType extends CollectionType {

    public abstract Class getGenericClass();
    public abstract Class getConcreteClass();

    @Override
    public void serialize(Builder builder, JsonFieldHolder fieldHolder, String variableName) {
        String getter;
        if (fieldHolder.hasGetter()) {
            getter = "object." + fieldHolder.getterMethod + "()";
        } else {
            getter = "object." + variableName;
        }

        TypeName fieldType = fieldHolder.fieldType.getTypeName();
        builder
                .beginControlFlow("if (object.$L != null)", variableName)
                .addStatement("$L.writeFieldName($S)", JSON_GENERATOR_VARIABLE_NAME, fieldHolder.fieldName[0])
                .addStatement("$L.writeStartArray()", JSON_GENERATOR_VARIABLE_NAME)
                .beginControlFlow("for ($T element : ($T<$T>)$L)", fieldType, getGenericClass(), fieldType, getter);

        fieldHolder.fieldType.serialize(builder, fieldHolder, variableName, "element", false);

        builder
                .endControlFlow()
                .addStatement("$L.writeEndArray()", JSON_GENERATOR_VARIABLE_NAME)
                .endControlFlow();
    }

    @Override
    public void parse(Builder builder, String objectName, String variableName, JsonFieldHolder fieldHolder) {
        TypeName fieldType = fieldHolder.fieldType.getTypeName();

        builder.beginControlFlow("if ($L.getCurrentToken() == $T.START_ARRAY)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement("$T<$T> collection = new $T<$T>()", getConcreteClass(), fieldType, getConcreteClass(), fieldType)
                .beginControlFlow("while ($L.nextToken() != $T.END_ARRAY)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement("$T value = $L", fieldType, fieldHolder.fieldType.getJsonParserGetter(fieldHolder))
                .beginControlFlow("if (value != null)")
                .addStatement("collection.add(value)")
                .endControlFlow()
                .endControlFlow();

        if (fieldHolder.hasSetter()) {
            builder.addStatement("instance.$L(collection)", fieldHolder.setterMethod);
        } else {
            builder.addStatement("instance.$L = collection", variableName);
        }

        builder.endControlFlow();
    }

}

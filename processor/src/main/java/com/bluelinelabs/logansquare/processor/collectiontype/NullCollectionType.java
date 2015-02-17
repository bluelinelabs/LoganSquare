package com.bluelinelabs.logansquare.processor.collectiontype;

import com.bluelinelabs.logansquare.processor.JsonFieldHolder;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;

public class NullCollectionType extends CollectionType {

    @Override
    public void parse(MethodSpec.Builder builder, String objectName, String variableName, JsonFieldHolder fieldHolder) {
        if (fieldHolder.hasSetter()) {
            builder.addStatement("instance.$L($L)", fieldHolder.setterMethod, fieldHolder.fieldType.getJsonParserGetter(fieldHolder));
        } else {
            builder.addStatement("instance.$L = $L", variableName, fieldHolder.fieldType.getJsonParserGetter(fieldHolder));
        }
    }

    @Override
    public void serialize(Builder builder, JsonFieldHolder fieldHolder, String variableName) {
        String getter;
        if (fieldHolder.hasGetter()) {
            getter = "object." + fieldHolder.getterMethod + "()";
        } else {
            getter = "object." + variableName;
        }

        fieldHolder.fieldType.serialize(builder, fieldHolder, getter, true);
    }
}

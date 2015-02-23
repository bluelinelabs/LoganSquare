package com.bluelinelabs.logansquare.processor.collectiontype;

import com.bluelinelabs.logansquare.processor.JsonFieldHolder;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;

public class NullCollectionType extends CollectionType {

    @Override
    public void parse(MethodSpec.Builder builder, String objectName, String variableName, JsonFieldHolder fieldHolder) {
        if (fieldHolder.hasSetter()) {
            builder.addCode("instance.$L(", fieldHolder.setterMethod);
            fieldHolder.fieldType.parse(builder, fieldHolder);
            builder.addCode(");\n");
        } else {
            builder.addCode("instance.$L = ", variableName);
            fieldHolder.fieldType.parse(builder, fieldHolder);
            builder.addCode(";\n");
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

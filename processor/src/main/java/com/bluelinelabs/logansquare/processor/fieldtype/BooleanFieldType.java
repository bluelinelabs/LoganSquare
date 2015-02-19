package com.bluelinelabs.logansquare.processor.fieldtype;

import com.bluelinelabs.logansquare.processor.JsonFieldHolder;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class BooleanFieldType extends FieldType {

    protected boolean isPrimitive;

    public BooleanFieldType(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public TypeName getTypeName() {
        return (isPrimitive ? TypeName.BOOLEAN : ClassName.get(Boolean.class));
    }

    @Override
    public TypeName getNonPrimitiveTypeName() {
        return ClassName.get(Boolean.class);
    }

    @Override
    public String getJsonParserGetter(JsonFieldHolder fieldHolder) {
        if (isPrimitive) {
            return String.format("%s.getValueAsBoolean()", JSON_PARSER_VARIABLE_NAME);
        } else {
            return String.format("Boolean.valueOf(%s.getValueAsBoolean())", JSON_PARSER_VARIABLE_NAME);
        }
    }

    @Override
    public void serialize(Builder builder, JsonFieldHolder fieldHolder, String getter, boolean writeFieldNameForObject) {
        if (writeFieldNameForObject) {
            builder.addStatement("$L.writeBooleanField($S, $L)", JSON_GENERATOR_VARIABLE_NAME, fieldHolder.fieldName[0], getter);
        } else {
            builder.addStatement("$L.writeBoolean($L)", JSON_GENERATOR_VARIABLE_NAME, getter);
        }
    }
}

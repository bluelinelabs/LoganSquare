package com.bluelinelabs.logansquare.processor.type.field;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class FloatFieldType extends NumberFieldType {

    public FloatFieldType(boolean isPrimitive) {
        super(isPrimitive);
    }

    @Override
    public TypeName getTypeName() {
        return (isPrimitive ? TypeName.FLOAT : ClassName.get(Float.class));
    }

    @Override
    public TypeName getNonPrimitiveTypeName() {
        return ClassName.get(Float.class);
    }

    @Override
    public void parse(Builder builder, int depth, String setter, Object... setterFormatArgs) {
        if (isPrimitive) {
            setter = replaceLastLiteral(setter, "(float)$L.getValueAsDouble()");
            builder.addStatement(setter, expandStringArgs(setterFormatArgs, JSON_PARSER_VARIABLE_NAME));
        } else {
            setter = replaceLastLiteral(setter, "$L.getCurrentToken() == JsonToken.VALUE_NULL ? null : new Float($L.getValueAsDouble())");
            builder.addStatement(setter, expandStringArgs(setterFormatArgs, JSON_PARSER_VARIABLE_NAME, JSON_PARSER_VARIABLE_NAME));
        }
    }
}

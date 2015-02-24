package com.bluelinelabs.logansquare.processor.type.field;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class LongFieldType extends NumberFieldType {

    public LongFieldType(boolean isPrimitive) {
        super(isPrimitive);
    }

    @Override
    public TypeName getTypeName() {
        return (isPrimitive ? TypeName.LONG : ClassName.get(Long.class));
    }

    @Override
    public TypeName getNonPrimitiveTypeName() {
        return ClassName.get(Long.class);
    }

    @Override
    public void parse(Builder builder, int depth, String setter, Object... setterFormatArgs) {
        if (isPrimitive) {
            setter = replaceLastLiteral(setter, "$L.getValueAsLong()");
            builder.addStatement(setter, expandStringArgs(setterFormatArgs, JSON_PARSER_VARIABLE_NAME));
        } else {
            setter = replaceLastLiteral(setter, "$L.getCurrentToken() == JsonToken.VALUE_NULL ? null : Long.valueOf($L.getValueAsLong())");
            builder.addStatement(setter, expandStringArgs(setterFormatArgs, JSON_PARSER_VARIABLE_NAME, JSON_PARSER_VARIABLE_NAME));
        }
    }
}

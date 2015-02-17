package com.bluelinelabs.logansquare.processor.fieldtype;

import com.bluelinelabs.logansquare.processor.JsonFieldHolder;
import com.squareup.javapoet.ClassName;
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
    public String getJsonParserGetter(JsonFieldHolder fieldHolder) {
        if (isPrimitive) {
            return String.format("%s.getValueAsLong()", JSON_PARSER_VARIABLE_NAME);
        } else {
            return String.format("Long.valueOf(%s.getValueAsLong())", JSON_PARSER_VARIABLE_NAME);
        }
    }
}

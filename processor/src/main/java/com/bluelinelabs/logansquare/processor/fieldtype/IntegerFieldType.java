package com.bluelinelabs.logansquare.processor.fieldtype;

import com.bluelinelabs.logansquare.processor.JsonFieldHolder;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class IntegerFieldType extends NumberFieldType {

    public IntegerFieldType(boolean isPrimitive) {
        super(isPrimitive);
    }

    @Override
    public TypeName getTypeName() {
        return (isPrimitive ? TypeName.INT : ClassName.get(Integer.class));
    }

    @Override
    public TypeName getNonPrimitiveTypeName() {
        return ClassName.get(Integer.class);
    }

    @Override
    public String getJsonParserGetter(JsonFieldHolder fieldHolder) {
        if (isPrimitive) {
            return String.format("%s.getValueAsInt()", JSON_PARSER_VARIABLE_NAME);
        } else {
            return String.format("Integer.valueOf(%s.getValueAsInt())", JSON_PARSER_VARIABLE_NAME);
        }
    }
}

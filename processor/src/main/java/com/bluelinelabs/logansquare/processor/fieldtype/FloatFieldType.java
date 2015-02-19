package com.bluelinelabs.logansquare.processor.fieldtype;

import com.bluelinelabs.logansquare.processor.JsonFieldHolder;
import com.squareup.javapoet.ClassName;
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
    public String getJsonParserGetter(JsonFieldHolder fieldHolder) {
        if (isPrimitive) {
            return String.format("(float)%s.getValueAsDouble()", JSON_PARSER_VARIABLE_NAME);
        } else {
            return String.format("new Float(%s.getValueAsDouble())", JSON_PARSER_VARIABLE_NAME);
        }
    }
}

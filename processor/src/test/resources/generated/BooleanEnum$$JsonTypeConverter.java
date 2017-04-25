package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.typeconverters.TypeConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;

public final class BooleanEnum$$JsonTypeConverter implements TypeConverter<BooleanEnum> {
    @Override
    public BooleanEnum parse(JsonParser jsonParser) throws IOException {
        if (jsonParser.getCurrentToken() == JsonToken.VALUE_NULL) {
            return null;
        }
        boolean parsedValue = jsonParser.getValueAsBoolean();
        if(parsedValue == true) {
            return BooleanEnum.TRUE;
        } else if(parsedValue == false) {
            return BooleanEnum.FALSE;
        }
        throw new IllegalArgumentException(jsonParser.toString());
    }

    @Override
    public void serialize(BooleanEnum value, String fieldName, boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {
        if (fieldName != null) {
            if (value == null) {
                jsonGenerator.writeNullField(fieldName);
                return;
            }
            switch (value) {
                case TRUE: {
                    jsonGenerator.writeBooleanField(fieldName, true);
                    break;
                }
                case FALSE: {
                    jsonGenerator.writeBooleanField(fieldName, false);
                    break;
                }
                default: throw new IllegalArgumentException(value.name());
            }
        } else {
            if (value == null) {
                jsonGenerator.writeNull();
                return;
            }
            switch (value) {
                case TRUE: {
                    jsonGenerator.writeBoolean(true);
                    break;
                }
                case FALSE: {
                    jsonGenerator.writeBoolean(false);
                    break;
                }
                default: throw new IllegalArgumentException(value.name());
            }
        }
    }
}

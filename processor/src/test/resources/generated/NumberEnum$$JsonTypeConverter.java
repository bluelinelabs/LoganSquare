package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.typeconverters.TypeConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;

public final class NumberEnum$$JsonTypeConverter implements TypeConverter<NumberEnum> {
    @Override
    public NumberEnum parse(JsonParser jsonParser) throws IOException {
        if (jsonParser.getCurrentToken() == JsonToken.VALUE_NULL) {
            return null;
        }
        long parsedValue = jsonParser.getValueAsLong();
        if(parsedValue == 1L) {
            return NumberEnum.ONE;
        } else if(parsedValue == 2L) {
            return NumberEnum.TWO;
        }
        throw new IllegalArgumentException(jsonParser.toString());
    }

    @Override
    public void serialize(NumberEnum value, String fieldName, boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {
        if (fieldName != null) {
            if (value == null) {
                jsonGenerator.writeNullField(fieldName);
                return;
            }
            switch (value) {
                case ONE: {
                    jsonGenerator.writeNumberField(fieldName, 1L);
                    break;
                }
                case TWO: {
                    jsonGenerator.writeNumberField(fieldName, 2L);
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
                case ONE: {
                    jsonGenerator.writeNumber(1L);
                    break;
                }
                case TWO: {
                    jsonGenerator.writeNumber(2L);
                    break;
                }
                default: throw new IllegalArgumentException(value.name());
            }
        }
    }
}

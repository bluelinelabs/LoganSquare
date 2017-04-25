package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.typeconverters.TypeConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;

public final class DefaultNamingPolicyEnum$$JsonTypeConverter implements TypeConverter<DefaultNamingPolicyEnum> {
    @Override
    public DefaultNamingPolicyEnum parse(JsonParser jsonParser) throws IOException {
        if (jsonParser.getCurrentToken() == JsonToken.VALUE_NULL) {
            return null;
        }
        String parsedValue = jsonParser.getValueAsString();
        if(parsedValue.equals("ONE")) {
            return DefaultNamingPolicyEnum.ONE;
        } else if(parsedValue.equals("TWO")) {
            return DefaultNamingPolicyEnum.TWO;
        }
        throw new IllegalArgumentException(jsonParser.toString());
    }

    @Override
    public void serialize(DefaultNamingPolicyEnum value, String fieldName, boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {
        if (fieldName != null) {
            if (value == null) {
                jsonGenerator.writeNullField(fieldName);
                return;
            }
            switch (value) {
                case ONE: {
                    jsonGenerator.writeStringField(fieldName, "ONE");
                    break;
                }
                case TWO: {
                    jsonGenerator.writeStringField(fieldName, "TWO");
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
                    jsonGenerator.writeString("ONE");
                    break;
                }
                case TWO: {
                    jsonGenerator.writeString("TWO");
                    break;
                }
                default: throw new IllegalArgumentException(value.name());
            }
        }
    }
}

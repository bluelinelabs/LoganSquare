package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.typeconverters.TypeConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;

public final class LowerCaseNamingPolicyEnum$$JsonTypeConverter implements TypeConverter<LowerCaseNamingPolicyEnum> {
    @Override
    public LowerCaseNamingPolicyEnum parse(JsonParser jsonParser) throws IOException {
        if (jsonParser.getCurrentToken() == JsonToken.VALUE_NULL) {
            return null;
        }
        String parsedValue = jsonParser.getValueAsString();
        if(parsedValue.equals("one")) {
            return LowerCaseNamingPolicyEnum.ONE;
        } else if(parsedValue.equals("two")) {
            return LowerCaseNamingPolicyEnum.TWO;
        }
        throw new IllegalArgumentException(jsonParser.toString());
    }

    @Override
    public void serialize(LowerCaseNamingPolicyEnum value, String fieldName, boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {
        if (fieldName != null) {
            if (value == null) {
                jsonGenerator.writeNullField(fieldName);
                return;
            }
            switch (value) {
                case ONE: {
                    jsonGenerator.writeStringField(fieldName, "one");
                    break;
                }
                case TWO: {
                    jsonGenerator.writeStringField(fieldName, "two");
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
                    jsonGenerator.writeString("one");
                    break;
                }
                case TWO: {
                    jsonGenerator.writeString("two");
                    break;
                }
                default: throw new IllegalArgumentException(value.name());
            }
        }
    }
}
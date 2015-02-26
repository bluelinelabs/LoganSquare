package com.bluelinelabs.logansquare.processor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import java.lang.String;
import java.lang.SuppressWarnings;

@SuppressWarnings("unsafe")
public final class AbstractParentModel$$JsonObjectMapper {
    public static void parseField(AbstractParentModel instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("parentTestInt".equals(fieldName)) {
            instance.parentTestInt = jsonParser.getValueAsInt();
        }
    }

    public static void _serialize(AbstractParentModel object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        jsonGenerator.writeNumberField("parentTestInt", object.parentTestInt);
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}

package com.bluelinelabs.logansquare.processor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import java.lang.String;

public final class DateModel$$JsonObjectMapper {
    protected static final DateModel.MyDateTypeConverter MY_DATE_TYPE_CONVERTER = new DateModel.MyDateTypeConverter();

    public static void parseField(DateModel instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("date".equals(fieldName)) {
            instance.date = MY_DATE_TYPE_CONVERTER.parse(jsonParser);
        }
    }

    public static void _serialize(DateModel object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.date != null) {
            MY_DATE_TYPE_CONVERTER.serialize(object.date, "date", true, jsonGenerator);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
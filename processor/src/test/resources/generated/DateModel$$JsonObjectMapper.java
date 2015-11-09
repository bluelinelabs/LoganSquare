package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;

@SuppressWarnings("unsafe,unchecked")
public final class DateModel$$JsonObjectMapper extends JsonMapper<DateModel> {
    protected static final DateModel.MyDateTypeConverter MY_DATE_TYPE_CONVERTER = new DateModel.MyDateTypeConverter();

    @Override
    public DateModel parse(JsonParser jsonParser) throws IOException {
        return null;
    }

    @Override
    public void parseField(DateModel instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("date".equals(fieldName)) {
            instance.date = MY_DATE_TYPE_CONVERTER.parse(jsonParser);
        }
    }

    @Override
    public void serialize(DateModel object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        MY_DATE_TYPE_CONVERTER.serialize(object.date, "date", true, jsonGenerator);
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }

    public void ensureParent() {
    }
}
package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;

@SuppressWarnings("unsafe,unchecked")
public final class SimpleWrapperModel$$JsonObjectMapper extends JsonMapper<SimpleWrapperModel> {
    @Override
    public SimpleWrapperModel parse(JsonParser jsonParser) throws IOException {
        SimpleWrapperModel instance = new SimpleWrapperModel();
        if (jsonParser.getCurrentToken() == null) {
            jsonParser.nextToken();
        }
        if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            jsonParser.skipChildren();
            return null;
        }
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            parseField(instance, fieldName, jsonParser);
            jsonParser.skipChildren();
        }
        return instance;
    }

    @Override
    public void parseField(SimpleWrapperModel instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("wrappedObject".equals(fieldName)) {
            instance.wrappedObject = SimpleWrapperModel$WrappedClass$$JsonObjectMapper._parse(jsonParser);
        }
    }

    @Override
    public void serialize(SimpleWrapperModel object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.wrappedObject != null) {
            jsonGenerator.writeFieldName("wrappedObject");
            SimpleWrapperModel$WrappedClass$$JsonObjectMapper._serialize(object.wrappedObject, jsonGenerator, true);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}

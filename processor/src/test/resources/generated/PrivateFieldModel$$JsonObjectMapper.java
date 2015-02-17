package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;

public final class PrivateFieldModel$$JsonObjectMapper extends JsonMapper<PrivateFieldModel> {
    @Override
    public PrivateFieldModel parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static PrivateFieldModel _parse(JsonParser jsonParser) throws IOException {
        PrivateFieldModel instance = new PrivateFieldModel();
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

    public static void parseField(PrivateFieldModel instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("privateBoolean".equals(fieldName)) {
            instance.setPrivateBoolean(jsonParser.getValueAsBoolean());
        } else if ("privateString".equals(fieldName)){
            instance.setPrivateString(jsonParser.getValueAsString(null));
        } else if ("private_named_string".equals(fieldName)){
            instance.setPrivateNamedString(jsonParser.getValueAsString(null));
        }
    }

    @Override
    public void serialize(PrivateFieldModel object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(PrivateFieldModel object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        jsonGenerator.writeBooleanField("privateBoolean", object.isPrivateBoolean());
        jsonGenerator.writeStringField("privateString", object.getPrivateString());
        jsonGenerator.writeStringField("private_named_string", object.getPrivateNamedString());
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
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
public final class PrePostProcessingModel$$JsonObjectMapper extends JsonMapper<PrePostProcessingModel> {
    @Override
    public PrePostProcessingModel parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static PrePostProcessingModel _parse(JsonParser jsonParser) throws IOException {
        PrePostProcessingModel instance = new PrePostProcessingModel();
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
        instance.onParseComplete();
        return instance;
    }

    public static void parseField(PrePostProcessingModel instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("unformatted_string".equals(fieldName)) {
            instance.unformattedString = jsonParser.getValueAsString(null);
        }
    }

    @Override
    public void serialize(PrePostProcessingModel object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(PrePostProcessingModel object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        object.onPreSerialize();
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.unformattedString != null) {
            jsonGenerator.writeStringField("unformatted_string", object.unformattedString);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}

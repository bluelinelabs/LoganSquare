package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if ("string_to_test_m_vars".equals(fieldName)) {
            instance.setStringThatStartsWithM(jsonParser.getValueAsString(null));
        } else if ("privateBoolean".equals(fieldName)){
            instance.setPrivateBoolean(jsonParser.getValueAsBoolean());
        } else if ("privateList".equals(fieldName)){
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<String> collection = new ArrayList<String>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    String value = jsonParser.getValueAsString(null);
                    if (value != null) {
                        collection.add(value);
                    }
                }
                instance.setPrivateList(collection);
            }
        } else if ("privateMap".equals(fieldName)){
            if (jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {
                HashMap<String, String> map = new HashMap<String, String>();
                while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                    String key = jsonParser.getText();
                    jsonParser.nextToken();
                    if (jsonParser.getCurrentToken() == JsonToken.VALUE_NULL) {
                        map.put(key, null);
                    } else{
                        map.put(key, jsonParser.getValueAsString(null));
                    }
                }
                instance.setPrivateMap(map);
            }
        } else if ("private_named_string".equals(fieldName)){
            instance.setPrivateNamedString(jsonParser.getValueAsString(null));
        } else if ("privateString".equals(fieldName)){
            instance.setPrivateString(jsonParser.getValueAsString(null));
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
        jsonGenerator.writeStringField("string_to_test_m_vars", object.getStringThatStartsWithM());
        jsonGenerator.writeBooleanField("privateBoolean", object.isPrivateBoolean());
        final List<String> lslocalprivateList = object.getPrivateList();
        if (lslocalprivateList != null) {
            jsonGenerator.writeFieldName("privateList");
            jsonGenerator.writeStartArray();
            for (String element : lslocalprivateList) {
                jsonGenerator.writeString(element);
            }
            jsonGenerator.writeEndArray();
        }
        final Map<String, String> lslocalprivateMap = object.getPrivateMap();
        if (lslocalprivateMap != null) {
            jsonGenerator.writeFieldName("privateMap");
            jsonGenerator.writeStartObject();
            for (Map.Entry<String, String> entry : lslocalprivateMap.entrySet()) {
                jsonGenerator.writeFieldName(entry.getKey().toString());
                if (entry.getValue() == null) {
                    jsonGenerator.writeNull();
                } else{
                    jsonGenerator.writeString(entry.getValue());
                }
            }
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeStringField("private_named_string", object.getPrivateNamedString());
        jsonGenerator.writeStringField("privateString", object.getPrivateString());
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
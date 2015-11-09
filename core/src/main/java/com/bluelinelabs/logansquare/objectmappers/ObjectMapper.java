package com.bluelinelabs.logansquare.objectmappers;

import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Built-in mapper for unknown types
 */
public class ObjectMapper extends JsonMapper<Object> {

    @Override
    public Object parse(JsonParser jsonParser) throws IOException {
        switch (jsonParser.getCurrentToken()) {
            case VALUE_NULL:
                return null;
            case VALUE_FALSE:
                return false;
            case VALUE_TRUE:
                return true;
            case VALUE_NUMBER_FLOAT:
                return jsonParser.getDoubleValue();
            case VALUE_NUMBER_INT:
                return jsonParser.getLongValue();
            case VALUE_STRING:
                return jsonParser.getText();
            case VALUE_EMBEDDED_OBJECT:
                return LoganSquare.mapperFor(Map.class).parse(jsonParser);
            case START_OBJECT:
                return LoganSquare.mapperFor(Map.class).parse(jsonParser);
            case START_ARRAY:
                return LoganSquare.mapperFor(List.class).parse(jsonParser);
            default:
                throw new RuntimeException("Invalid json token encountered: " + jsonParser.getCurrentToken());
        }
    }

    @Override
    public void serialize(Object value, JsonGenerator generator, boolean writeStartAndEnd) throws IOException {
        if (value == null) {
            generator.writeNull();
        } else if (value instanceof String) {
            generator.writeString((String)value);
        } else if (value instanceof Integer) {
            generator.writeNumber((Integer)value);
        } else if (value instanceof Long) {
            generator.writeNumber((Long)value);
        } else if (value instanceof Float) {
            generator.writeNumber((Float)value);
        } else if (value instanceof Double) {
            generator.writeNumber((Double)value);
        } else if (value instanceof Boolean) {
            generator.writeBoolean((Boolean)value);
        } else if (value instanceof List) {
            LoganSquare.mapperFor(List.class).serialize((List<Object>)value, generator, writeStartAndEnd);
        } else if (value instanceof Map) {
            LoganSquare.mapperFor(Map.class).serialize((Map<String, Object>)value, generator, writeStartAndEnd);
        } else {
            Class valueClass = value.getClass();
            JsonMapper jsonMapper = LoganSquare.mapperFor(valueClass);

            if (jsonMapper != null) {
                if (writeStartAndEnd) {
                    generator.writeStartObject();
                }

                jsonMapper.serialize(value, generator, false);

                if (writeStartAndEnd) {
                    generator.writeEndObject();
                }
            }
        }
    }
}

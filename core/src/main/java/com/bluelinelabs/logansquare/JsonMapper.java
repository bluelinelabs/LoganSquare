package com.bluelinelabs.logansquare;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** The class that handles all parsing and serialization of @JsonObject's */
public abstract class JsonMapper<T> {

    /**
     * Parse an object from a pre-configured JsonParser object.
     *
     * @param jsonParser The pre-configured JsonParser
     */
    public abstract T parse(JsonParser jsonParser) throws IOException;

    /**
     * Serialize an object to a pre-configured JsonGenerator object.
     *
     * @param object The object to serialize.
     * @param generator The pre-configured JsonGenerator being written to.
     * @param writeStartAndEnd True if writeStartObject() should be called before and writeEndObject() should be called after serializing. False if not.
     */
    public abstract void serialize(T object, JsonGenerator generator, boolean writeStartAndEnd) throws IOException;

    /**
     * Parse an object from an InputStream.
     *
     * @param is The InputStream, most likely from your networking library.
     */
    public T parse(InputStream is) throws IOException {
        JsonParser jsonParser = LoganSquare.JSON_FACTORY.createParser(is);
        jsonParser.nextToken();
        return parse(jsonParser);
    }

    /**
     * Parse an object from a String. Note: parsing from an InputStream should be preferred over parsing from a String if possible.
     *
     * @param jsonString The JSON string being parsed.
     */
    public T parse(String jsonString) throws IOException {
        JsonParser jsonParser = LoganSquare.JSON_FACTORY.createParser(jsonString);
        jsonParser.nextToken();
        return parse(jsonParser);
    }

    /**
     * Parse a list of objects from an InputStream.
     *
     * @param is The inputStream, most likely from your networking library.
     */
    public List<T> parseList(InputStream is) throws IOException {
        JsonParser jsonParser = LoganSquare.JSON_FACTORY.createParser(is);
        jsonParser.nextToken();
        return parseList(jsonParser);
    }

    /**
     * Parse a list of objects from a String. Note: parsing from an InputStream should be preferred over parsing from a String if possible.
     *
     * @param jsonString The JSON string being parsed.
     */
    public List<T> parseList(String jsonString) throws IOException {
        JsonParser jsonParser = LoganSquare.JSON_FACTORY.createParser(jsonString);
        jsonParser.nextToken();
        return parseList(jsonParser);
    }

    /**
     * Parse a list of objects from a JsonParser.
     *
     * @param jsonParser The JsonParser, preconfigured to be at the START_ARRAY token.
     */
    public List<T> parseList(JsonParser jsonParser) throws IOException {
        List<T> list = new ArrayList<>();
        if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                T object = parse(jsonParser);
                if (object != null) {
                    list.add(object);
                }
            }
        }
        return list;
    }

    /**
     * Parse a map of objects from an InputStream.
     *
     * @param is The inputStream, most likely from your networking library.
     */
    public Map<String, T> parseMap(InputStream is) throws IOException {
        JsonParser jsonParser = LoganSquare.JSON_FACTORY.createParser(is);
        jsonParser.nextToken();
        return parseMap(jsonParser);
    }

    /**
     * Parse a map of objects from a String. Note: parsing from an InputStream should be preferred over parsing from a String if possible.
     *
     * @param jsonString The JSON string being parsed.
     */
    public Map<String, T> parseMap(String jsonString) throws IOException {
        JsonParser jsonParser = LoganSquare.JSON_FACTORY.createParser(jsonString);
        jsonParser.nextToken();
        return parseMap(jsonParser);
    }

    /**
     * Parse a map of objects from a JsonParser.
     *
     * @param jsonParser The JsonParser, preconfigured to be at the START_ARRAY token.
     */
    public Map<String, T> parseMap(JsonParser jsonParser) throws IOException {
        HashMap<String, T> map = new HashMap<String, T>();
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String key = jsonParser.getText();
            jsonParser.nextToken();
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_NULL) {
                map.put(key, null);
            } else{
                map.put(key, parse(jsonParser));
            }
        }
        return map;
    }

    /**
     * Serialize an object to a JSON String.
     *
     * @param object The object to serialize.
     */
    public String serialize(T object) throws IOException {
        StringWriter sw = new StringWriter();
        JsonGenerator jsonGenerator = LoganSquare.JSON_FACTORY.createGenerator(sw);
        serialize(object, jsonGenerator, true);
        jsonGenerator.close();
        return sw.toString();
    }

    /**
     * Serialize an object to an OutputStream.
     *
     * @param object The object to serialize.
     * @param os The OutputStream being written to.
     */
    public void serialize(T object, OutputStream os) throws IOException {
        JsonGenerator jsonGenerator = LoganSquare.JSON_FACTORY.createGenerator(os);
        serialize(object, jsonGenerator, true);
        jsonGenerator.close();
    }

    /**
     * Serialize a list of objects to a JSON String.
     *
     * @param list The list of objects to serialize.
     */
    public String serialize(List<T> list) throws IOException {
        StringWriter sw = new StringWriter();
        JsonGenerator jsonGenerator = LoganSquare.JSON_FACTORY.createGenerator(sw);
        serialize(list, jsonGenerator);
        jsonGenerator.close();
        return sw.toString();
    }

    /**
     * Serialize a list of objects to an OutputStream.
     *
     * @param list The list of objects to serialize.
     * @param os The OutputStream to which the list should be serialized
     */
    public void serialize(List<T> list, OutputStream os) throws IOException {
        JsonGenerator jsonGenerator = LoganSquare.JSON_FACTORY.createGenerator(os);
        serialize(list, jsonGenerator);
        jsonGenerator.close();
    }

    /**
     * Serialize a list of objects to a JsonGenerator.
     *
     * @param list The list of objects to serialize.
     * @param jsonGenerator The JsonGenerator to which the list should be serialized
     */
    public void serialize(List<T> list, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeStartArray();
        for (T object : list) {
            serialize(object, jsonGenerator, true);
        }
        jsonGenerator.writeEndArray();
    }

    /**
     * Serialize a list of objects to a JSON String.
     *
     * @param map The map of objects to serialize.
     */
    public String serialize(Map<String, T> map) throws IOException {
        StringWriter sw = new StringWriter();
        JsonGenerator jsonGenerator = LoganSquare.JSON_FACTORY.createGenerator(sw);
        serialize(map, jsonGenerator);
        jsonGenerator.close();
        return sw.toString();
    }

    /**
     * Serialize a list of objects to an OutputStream.
     *
     * @param map The map of objects to serialize.
     * @param os The OutputStream to which the list should be serialized
     */
    public void serialize(Map<String, T> map, OutputStream os) throws IOException {
        JsonGenerator jsonGenerator = LoganSquare.JSON_FACTORY.createGenerator(os);
        serialize(map, jsonGenerator);
        jsonGenerator.close();
    }

    /**
     * Serialize a list of objects to a JsonGenerator.
     *
     * @param map The map of objects to serialize.
     * @param jsonGenerator The JsonGenerator to which the list should be serialized
     */
    public void serialize(Map<String, T> map, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeStartObject();
        for (Map.Entry<String, T> entry : map.entrySet()) {
            jsonGenerator.writeFieldName(entry.getKey());
            if (entry.getValue() == null) {
                jsonGenerator.writeNull();
            } else {
                serialize(entry.getValue(), jsonGenerator, true);
            }
        }
        jsonGenerator.writeEndObject();
    }
}
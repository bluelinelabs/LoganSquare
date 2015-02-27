package com.bluelinelabs.logansquare.demo.serializetasks;

import com.bluelinelabs.logansquare.demo.model.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CachedJacksonDatabindSerializer extends Serializer {

    private final ObjectMapper objectMapper;

    public CachedJacksonDatabindSerializer(SerializeListener parseListener, Response response, ObjectMapper objectMapper) {
        super(parseListener, response);
        this.objectMapper = objectMapper;
    }

    @Override
    protected String serialize(Response response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            return null;
        } finally {
            System.gc();
        }
    }
}

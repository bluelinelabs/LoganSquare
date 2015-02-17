package com.bluelinelabs.logansquare.demo.serializetasks;

import com.bluelinelabs.logansquare.demo.model.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonDatabindSerializer extends Serializer {

    public JacksonDatabindSerializer(SerializeListener parseListener, Response response) {
        super(parseListener, response);
    }

    @Override
    protected String serialize(Response response) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            return null;
        } finally {
            objectMapper = null;
            System.gc();
        }
    }
}

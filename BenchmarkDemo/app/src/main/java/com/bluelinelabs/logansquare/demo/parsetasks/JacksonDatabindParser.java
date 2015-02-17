package com.bluelinelabs.logansquare.demo.parsetasks;

import com.bluelinelabs.logansquare.demo.model.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonDatabindParser extends Parser {

    public JacksonDatabindParser(ParseListener parseListener, String jsonString) {
        super(parseListener, jsonString);
    }

    @Override
    protected int parse(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Response response = objectMapper.readValue(json, Response.class);
            return response.users.size();
        } catch (Exception e) {
            return 0;
        } finally {
            objectMapper = null;
            System.gc();
        }
    }

}

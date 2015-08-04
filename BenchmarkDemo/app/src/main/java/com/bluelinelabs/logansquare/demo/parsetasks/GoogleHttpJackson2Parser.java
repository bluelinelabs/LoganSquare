package com.bluelinelabs.logansquare.demo.parsetasks;

import com.bluelinelabs.logansquare.demo.model.Response;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.jackson2.JacksonFactory;

public class GoogleHttpJackson2Parser extends Parser {

    private JacksonFactory jacksonFactory;

    public GoogleHttpJackson2Parser(ParseListener parseListener, String jsonString) {
        super(parseListener, jsonString);

        jacksonFactory = new JacksonFactory();
    }

    @Override
    protected int parse(String json) {
        try {
            JsonParser jsonParser = jacksonFactory.createJsonParser(json);
            Response response = jsonParser.parse(Response.class);
            jsonParser.close();
            return response.users.size();
        } catch (Exception e) {
            return 0;
        } finally {
            System.gc();
        }
    }

}

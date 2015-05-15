package com.bluelinelabs.logansquare.demo.parsetasks;

import com.bluelinelabs.logansquare.demo.model.Response;
import com.google.gson.Gson;

public class GsonParser extends Parser {

    private final Gson gson;

    public GsonParser(ParseListener parseListener, String jsonString, Gson gson) {
        super(parseListener, jsonString);
        this.gson = gson;
    }

    @Override
    protected int parse(String json) {
        try {
            Response response = gson.fromJson(json, Response.class);
            return response.users.size();
        } catch (Exception e) {
            return 0;
        } finally {
            System.gc();
        }
    }

}

package com.bluelinelabs.logansquare.demo.parsetasks;

import com.bluelinelabs.logansquare.demo.model.Response;
import com.google.gson.Gson;

public class GsonParser extends Parser {

    public GsonParser(ParseListener parseListener, String jsonString) {
        super(parseListener, jsonString);
    }

    @Override
    protected int parse(String json) {
        Gson gson = new Gson();
        try {
            Response response = gson.fromJson(json, Response.class);
            return response.users.size();
        } catch (Exception e) {
            return 0;
        } finally {
            gson = null;
            System.gc();
        }
    }

}

package com.bluelinelabs.logansquare.demo.serializetasks;

import com.bluelinelabs.logansquare.demo.model.Response;
import com.google.gson.Gson;

public class GsonSerializer extends Serializer {

    private final Gson gson;

    public GsonSerializer(SerializeListener parseListener, Response response, Gson gson) {
        super(parseListener, response);
        this.gson = gson;
    }

    @Override
    protected String serialize(Response response) {
        try {
            return gson.toJson(response);
        } catch (Exception e) {
            return null;
        } finally {
            System.gc();
        }
    }
}

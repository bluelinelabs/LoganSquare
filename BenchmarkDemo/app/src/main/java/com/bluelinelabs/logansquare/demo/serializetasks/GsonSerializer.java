package com.bluelinelabs.logansquare.demo.serializetasks;

import com.bluelinelabs.logansquare.demo.model.Response;
import com.google.gson.Gson;

public class GsonSerializer extends Serializer {

    public GsonSerializer(SerializeListener parseListener, Response response) {
        super(parseListener, response);
    }

    @Override
    protected String serialize(Response response) {
        Gson gson = new Gson();
        try {
            return gson.toJson(response);
        } catch (Exception e) {
            return null;
        } finally {
            gson = null;
            System.gc();
        }
    }
}

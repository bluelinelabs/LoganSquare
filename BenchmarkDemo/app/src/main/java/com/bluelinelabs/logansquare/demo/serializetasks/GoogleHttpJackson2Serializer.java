package com.bluelinelabs.logansquare.demo.serializetasks;

import com.bluelinelabs.logansquare.demo.model.Response;
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.StringWriter;

public class GoogleHttpJackson2Serializer extends Serializer {

    private JacksonFactory jacksonFactory;

    public GoogleHttpJackson2Serializer(SerializeListener parseListener, Response response) {
        super(parseListener, response);

        jacksonFactory = new JacksonFactory();
    }

    @Override
    protected String serialize(Response response) {
        try {
            StringWriter writer = new StringWriter();
            JsonGenerator jsonGenerator = jacksonFactory.createJsonGenerator(writer);
            jsonGenerator.serialize(response);
            jsonGenerator.flush();
            jsonGenerator.close();
            return writer.toString();
        } catch (Exception e) {
            return null;
        } finally {
            System.gc();
        }
    }
}

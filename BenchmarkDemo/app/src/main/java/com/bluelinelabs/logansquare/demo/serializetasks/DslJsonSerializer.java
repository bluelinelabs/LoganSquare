package com.bluelinelabs.logansquare.demo.serializetasks;

import com.bluelinelabs.logansquare.demo.model.Response;
import com.dslplatform.json.DslJson;
import com.dslplatform.json.JsonWriter;

public class DslJsonSerializer extends Serializer {

    private final DslJson<Object> dsl;
    private final JsonWriter writer = new JsonWriter();

    public DslJsonSerializer(SerializeListener parseListener, Response response, DslJson<Object> dsl) {
        super(parseListener, response);
        this.dsl = dsl;
    }

    @Override
    protected String serialize(Response response) {
        try {
            writer.reset();
            dsl.serialize(writer, response);
            return writer.toString();
        } catch (Exception e) {
            return null;
        } finally {
            System.gc();
        }
    }
}

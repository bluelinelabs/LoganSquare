package com.bluelinelabs.logansquare.demo.parsetasks;

import com.bluelinelabs.logansquare.demo.model.Response;
import com.dslplatform.json.DslJson;

public class DslJsonParser extends Parser {

    private final DslJson<Object> dsl;

    public DslJsonParser(ParseListener parseListener, String jsonString, DslJson<Object> dsl) {
        super(parseListener, jsonString);
        this.dsl = dsl;
    }

    @Override
    protected int parse(String json) {
        try {
            byte[] bytes = json.getBytes("UTF-8");
            Response response = dsl.deserialize(Response.class, bytes, bytes.length);
            return response.users.size();
        } catch (Exception e) {
            return 0;
        } finally {
            System.gc();
        }
    }

}

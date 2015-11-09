package com.bluelinelabs.logansquare.demo.parsetasks;

import com.bluelinelabs.logansquare.demo.model.Response;
import com.squareup.moshi.Moshi;

public class MoshiParser extends Parser {

    private final Moshi moshi;

    public MoshiParser(Parser.ParseListener parseListener, String jsonString, Moshi moshi) {
        super(parseListener, jsonString);
        this.moshi = moshi;
    }

    @Override
    protected int parse(String json) {
        try {
            return moshi.adapter(Response.class).fromJson(json).users.size();
        } catch (Exception e) {
            return 0;
        } finally {
            System.gc();
        }
    }

}

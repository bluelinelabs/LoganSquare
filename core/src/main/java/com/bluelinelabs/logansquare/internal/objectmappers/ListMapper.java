package com.bluelinelabs.logansquare.internal.objectmappers;

import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.util.List;

/**
 * Built-in mapper for List objects of an unknown type
 */
public class ListMapper extends JsonMapper<List<Object>> {

    @Override
    public List<Object> parse(JsonParser jsonParser) throws IOException {
        return LoganSquare.mapperFor(Object.class).parseList(jsonParser);
    }

    @Override
    public void serialize(List<Object> list, JsonGenerator generator, boolean writeStartAndEnd) throws IOException {
        LoganSquare.mapperFor(Object.class).serialize(list, generator);
    }

}

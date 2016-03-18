package com.bluelinelabs.logansquare.internal.objectmappers;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

public class EnumMapper<E extends Enum<E>> extends JsonMapper<E> {
    private final Class<E> enumCls;

    public EnumMapper(Class<E> enumCls) {
        this.enumCls = enumCls;
    }

    @Override
    public E parse(JsonParser jsonParser) throws IOException {
        return Enum.valueOf(enumCls, jsonParser.getText());
    }

    @Override
    public void parseField(E instance, String fieldName, JsonParser jsonParser) throws IOException { }

    @Override
    public void serialize(E object, JsonGenerator generator, boolean writeStartAndEnd) throws IOException {
        generator.writeString(object.name());
    }
}

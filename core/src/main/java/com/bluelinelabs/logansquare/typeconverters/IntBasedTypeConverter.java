package com.bluelinelabs.logansquare.typeconverters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

public abstract class IntBasedTypeConverter<T> implements TypeConverter<T> {

    /**
     * Called to convert an int into an object of type T.
     *
     * @param i The int parsed from JSON.
     */
    public abstract T getFromInt(int i);

    /**
     * Called to convert a an object of type T into an int.
     *
     * @param object The object being converted.
     */
    public abstract int convertToInt(T object);

    @Override
    public T parse(JsonParser jsonParser) throws IOException {
        return getFromInt(jsonParser.getValueAsInt());
    }

    @Override
    public void serialize(T object, String fieldName, boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {
        if (fieldName != null) {
            jsonGenerator.writeNumberField(fieldName, convertToInt(object));
        } else {
            jsonGenerator.writeNumber(convertToInt(object));
        }
    }

}

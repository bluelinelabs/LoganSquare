package com.bluelinelabs.logansquare.typeconverters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

public abstract class BooleanBasedTypeConverter<T> implements TypeConverter<T> {

    /**
     * Called to convert a boolean into an object of type T.
     *
     * @param b The boolean parsed from JSON.
     */
    public abstract T getFromBoolean(boolean b);

    /**
     * Called to convert a an object of type T into a boolean.
     *
     * @param object The object being converted.
     */
    public abstract boolean convertToBoolean(T object);

    @Override
    public T parse(JsonParser jsonParser) throws IOException {
        return getFromBoolean(jsonParser.getValueAsBoolean());
    }

    @Override
    public void serialize(T object, String fieldName, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeBooleanField(fieldName, convertToBoolean(object));
    }

}

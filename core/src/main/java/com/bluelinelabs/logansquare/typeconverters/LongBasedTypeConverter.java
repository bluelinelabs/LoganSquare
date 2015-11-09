package com.bluelinelabs.logansquare.typeconverters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

public abstract class LongBasedTypeConverter<T> implements TypeConverter<T> {

    /**
     * Called to convert a long into an object of type T.
     *
     * @param l The long parsed from JSON.
     */
    public abstract T getFromLong(long l);

    /**
     * Called to convert a an object of type T into a long.
     *
     * @param object The object being converted.
     */
    public abstract long convertToLong(T object);

    @Override
    public T parse(JsonParser jsonParser) throws IOException {
        return getFromLong(jsonParser.getValueAsLong());
    }

    @Override
    public void serialize(T object, String fieldName, boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {
        if (fieldName != null) {
            jsonGenerator.writeNumberField(fieldName, convertToLong(object));
        } else {
            jsonGenerator.writeNumber(convertToLong(object));
        }
    }

}

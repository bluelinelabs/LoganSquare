package com.bluelinelabs.logansquare.typeconverters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

public abstract class FloatBasedTypeConverter<T> implements TypeConverter<T> {

    /**
     * Called to convert a float into an object of type T.
     *
     * @param f The float parsed from JSON.
     */
    public abstract T getFromFloat(float f);

    /**
     * Called to convert a an object of type T into a float.
     *
     * @param object The object being converted.
     */
    public abstract float convertToFloat(T object);

    @Override
    public T parse(JsonParser jsonParser) throws IOException {
        return getFromFloat((float) jsonParser.getValueAsDouble());
    }

    @Override
    public void serialize(T object, String fieldName, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeNumberField(fieldName, convertToFloat(object));
    }

}

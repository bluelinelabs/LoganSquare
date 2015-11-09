package com.bluelinelabs.logansquare.typeconverters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

public abstract class DoubleBasedTypeConverter<T> implements TypeConverter<T> {

    /**
     * Called to convert a double into an object of type T.
     *
     * @param d The double parsed from JSON.
     */
    public abstract T getFromDouble(double d);

    /**
     * Called to convert a an object of type T into a double.
     *
     * @param object The object being converted.
     */
    public abstract double convertToDouble(T object);

    @Override
    public T parse(JsonParser jsonParser) throws IOException {
        return getFromDouble(jsonParser.getValueAsDouble());
    }

    @Override
    public void serialize(T object, String fieldName, boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {
        if (fieldName != null) {
            jsonGenerator.writeNumberField(fieldName, convertToDouble(object));
        } else {
            jsonGenerator.writeNumber(convertToDouble(object));
        }
    }

}

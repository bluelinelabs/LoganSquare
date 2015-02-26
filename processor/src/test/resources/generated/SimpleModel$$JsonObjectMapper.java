package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Date;

@SuppressWarnings("unsafe")
public final class SimpleModel$$JsonObjectMapper extends JsonMapper<SimpleModel> {
    @Override
    public SimpleModel parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static SimpleModel _parse(JsonParser jsonParser) throws IOException {
        SimpleModel instance = new SimpleModel();
        if (jsonParser.getCurrentToken() == null) {
            jsonParser.nextToken();
        }
        if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            jsonParser.skipChildren();
            return null;
        }
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            parseField(instance, fieldName, jsonParser);
            jsonParser.skipChildren();
        }
        return instance;
    }

    public static void parseField(SimpleModel instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("date".equals(fieldName)) {
            instance.date = LoganSquare.typeConverterFor(Date.class).parse(jsonParser);
        } else if ("intToIgnoreForSerialization".equals(fieldName)){
            instance.intToIgnoreForSerialization = jsonParser.getValueAsInt();
        } else if ("string".equals(fieldName)){
            instance.string = jsonParser.getValueAsString(null);
        } else if ("test_double".equals(fieldName)){
            instance.testDouble = jsonParser.getValueAsDouble();
        } else if ("test_double_obj".equals(fieldName)){
            instance.testDoubleObj = jsonParser.getCurrentToken() == JsonToken.VALUE_NULL ? null : Double.valueOf(jsonParser.getValueAsDouble());
        } else if ("test_float".equals(fieldName)){
            instance.testFloat = (float)jsonParser.getValueAsDouble();
        } else if ("test_float_obj".equals(fieldName)){
            instance.testFloatObj = jsonParser.getCurrentToken() == JsonToken.VALUE_NULL ? null : new Float(jsonParser.getValueAsDouble());
        } else if ("test_int".equals(fieldName)){
            instance.testInt = jsonParser.getValueAsInt();
        } else if ("test_int_obj".equals(fieldName)){
            instance.testIntObj = jsonParser.getCurrentToken() == JsonToken.VALUE_NULL ? null : Integer.valueOf(jsonParser.getValueAsInt());
        } else if ("test_long".equals(fieldName)){
            instance.testLong = jsonParser.getValueAsLong();
        } else if ("test_long_obj".equals(fieldName)){
            instance.testLongObj = jsonParser.getCurrentToken() == JsonToken.VALUE_NULL ? null : Long.valueOf(jsonParser.getValueAsLong());
        } else if ("test_string".equals(fieldName)){
            instance.testString = jsonParser.getValueAsString(null);
        }
    }

    @Override
    public void serialize(SimpleModel object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(SimpleModel object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.date != null) {
            LoganSquare.typeConverterFor(Date.class).serialize(object.date, "date", true, jsonGenerator);
        }
        jsonGenerator.writeNumberField("intToIgnoreForParse", object.intToIgnoreForParse);
        jsonGenerator.writeStringField("string", object.string);
        jsonGenerator.writeNumberField("test_double", object.testDouble);
        jsonGenerator.writeNumberField("test_double_obj", object.testDoubleObj);
        jsonGenerator.writeNumberField("test_float", object.testFloat);
        jsonGenerator.writeNumberField("test_float_obj", object.testFloatObj);
        jsonGenerator.writeNumberField("test_int", object.testInt);
        jsonGenerator.writeNumberField("test_int_obj", object.testIntObj);
        jsonGenerator.writeNumberField("test_long", object.testLong);
        jsonGenerator.writeNumberField("test_long_obj", object.testLongObj);
        jsonGenerator.writeStringField("test_string", object.testString);
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
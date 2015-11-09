package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.ParameterizedType;
import com.bluelinelabs.logansquare.util.SimpleArrayMap;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Date;

@SuppressWarnings("unsafe,unchecked")
public final class SimpleGenericModelWithExtends$$JsonObjectMapper<T extends String> extends JsonMapper<SimpleGenericModelWithExtends<T>> {
  private final JsonMapper<T> m84ClassJsonMapper;

  public SimpleGenericModelWithExtends$$JsonObjectMapper(ParameterizedType type, ParameterizedType TType, SimpleArrayMap<ParameterizedType, JsonMapper> partialMappers) {
    partialMappers.put(type, this);
    m84ClassJsonMapper = LoganSquare.mapperFor(TType, partialMappers);
  }

  @Override
  public SimpleGenericModelWithExtends<T> parse(JsonParser jsonParser) throws IOException {
    SimpleGenericModelWithExtends<T> instance = new SimpleGenericModelWithExtends<T>();
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

  @Override
  public void parseField(SimpleGenericModelWithExtends<T> instance, String fieldName, JsonParser jsonParser) throws IOException {
    if ("date".equals(fieldName)) {
      instance.date = LoganSquare.typeConverterFor(Date.class).parse(jsonParser);
    } else if ("string".equals(fieldName)) {
      instance.string = jsonParser.getValueAsString(null);
    } else if ("test_double".equals(fieldName)) {
      instance.testDouble = jsonParser.getValueAsDouble();
    } else if ("test_double_obj".equals(fieldName)) {
      instance.testDoubleObj = jsonParser.getCurrentToken() == JsonToken.VALUE_NULL ? null : Double.valueOf(jsonParser.getValueAsDouble());
    } else if ("test_float".equals(fieldName)) {
      instance.testFloat = (float)jsonParser.getValueAsDouble();
    } else if ("test_float_obj".equals(fieldName)) {
      instance.testFloatObj = jsonParser.getCurrentToken() == JsonToken.VALUE_NULL ? null : new Float(jsonParser.getValueAsDouble());
    } else if ("test_int".equals(fieldName)) {
      instance.testInt = jsonParser.getValueAsInt();
    } else if ("test_int_obj".equals(fieldName)) {
      instance.testIntObj = jsonParser.getCurrentToken() == JsonToken.VALUE_NULL ? null : Integer.valueOf(jsonParser.getValueAsInt());
    } else if ("test_long".equals(fieldName)) {
      instance.testLong = jsonParser.getValueAsLong();
    } else if ("test_long_obj".equals(fieldName)) {
      instance.testLongObj = jsonParser.getCurrentToken() == JsonToken.VALUE_NULL ? null : Long.valueOf(jsonParser.getValueAsLong());
    } else if ("test_string".equals(fieldName)) {
      instance.testString = jsonParser.getValueAsString(null);
    } else if ("test_t".equals(fieldName)) {
      instance.testT = m84ClassJsonMapper.parse(jsonParser);
    }
  }

  @Override
  public void serialize(SimpleGenericModelWithExtends<T> object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
    if (writeStartAndEnd) {
      jsonGenerator.writeStartObject();
    }
    if (object.date != null) {
      LoganSquare.typeConverterFor(Date.class).serialize(object.date, "date", true, jsonGenerator);
    }
    if (object.string != null) {
      jsonGenerator.writeStringField("string", object.string);
    }
    jsonGenerator.writeNumberField("test_double", object.testDouble);
    if (object.testDoubleObj != null) {
      jsonGenerator.writeNumberField("test_double_obj", object.testDoubleObj);
    }
    jsonGenerator.writeNumberField("test_float", object.testFloat);
    if (object.testFloatObj != null) {
      jsonGenerator.writeNumberField("test_float_obj", object.testFloatObj);
    }
    jsonGenerator.writeNumberField("test_int", object.testInt);
    if (object.testIntObj != null) {
      jsonGenerator.writeNumberField("test_int_obj", object.testIntObj);
    }
    jsonGenerator.writeNumberField("test_long", object.testLong);
    if (object.testLongObj != null) {
      jsonGenerator.writeNumberField("test_long_obj", object.testLongObj);
    }
    if (object.testString != null) {
      jsonGenerator.writeStringField("test_string", object.testString);
    }
    if (object.testT != null) {
      jsonGenerator.writeFieldName("test_t");
      m84ClassJsonMapper.serialize(object.testT, jsonGenerator, true);
    }
    if (writeStartAndEnd) {
      jsonGenerator.writeEndObject();
    }
  }

  public void ensureParent() {
  }
}
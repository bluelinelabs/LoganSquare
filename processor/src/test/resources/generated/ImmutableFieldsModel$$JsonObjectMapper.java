package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.lang.UnsupportedOperationException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unsafe,unchecked")
public final class ImmutableFieldsModel$$JsonObjectMapper extends JsonMapper<ImmutableFieldsModel> {
  @Override
  public ImmutableFieldsModel parse(JsonParser jsonParser) throws IOException {
    Integer integerField = null;
    String[] arrayField = null;
    Integer anotherField = null;
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
      if ("integerField".equals(fieldName)) {
        integerField = jsonParser.getValueAsInt();
      } else if ("arrayField".equals(fieldName)) {
        if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
          List<String> collection1 = new ArrayList<String>();
          while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            String value1;
            value1 = jsonParser.getValueAsString(null);
            collection1.add(value1);
          }
          String[] array = collection1.toArray(new String[collection1.size()]);
          arrayField = array;
        } else {
          arrayField = null;
        }
      } else if ("anotherField".equals(fieldName)) {
        anotherField = jsonParser.getValueAsInt();
      }
      jsonParser.skipChildren();
    }
    return new ImmutableFieldsModel(integerField, arrayField, anotherField);
  }

  @Override
  public void parseField(ImmutableFieldsModel instance, String fieldName, JsonParser jsonParser) throws IOException {
    throw new UnsupportedOperationException("The class is using constructor injection, individual fields can not be parsed");
  }

  @Override
  public void serialize(ImmutableFieldsModel object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
    if (writeStartAndEnd) {
      jsonGenerator.writeStartObject();
    }
    jsonGenerator.writeNumberField("integerField", object.getIntegerField());
    final String[] lslocalarrayField = object.getArrayField();
    if (lslocalarrayField != null) {
      jsonGenerator.writeFieldName("arrayField");
      jsonGenerator.writeStartArray();
      for (String element1 : lslocalarrayField) {
        if (element1 != null) {
          jsonGenerator.writeString(element1);
        }
      }
      jsonGenerator.writeEndArray();
    }
    jsonGenerator.writeNumberField("anotherField", object.getAnotherField());
    if (writeStartAndEnd) {
      jsonGenerator.writeEndObject();
    }
  }
}

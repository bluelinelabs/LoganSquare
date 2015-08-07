package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unsafe")
public final class WhitespaceFieldNameModel$$JsonObjectMapper extends JsonMapper<WhitespaceFieldNameModel> {
  @Override
  public WhitespaceFieldNameModel parse(JsonParser jsonParser) throws IOException {
    return _parse(jsonParser);
  }

  public static WhitespaceFieldNameModel _parse(JsonParser jsonParser) throws IOException {
    WhitespaceFieldNameModel instance = new WhitespaceFieldNameModel();
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

  public static void parseField(WhitespaceFieldNameModel instance, String fieldName, JsonParser jsonParser) throws IOException {
    if ("Address Lines".equals(fieldName)) {
      if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
        ArrayList<String> collection1 = new ArrayList<String>();
        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
          String value1;
          value1 = jsonParser.getValueAsString(null);
          collection1.add(value1);
        }
        instance.addressLines = collection1;
      } else{
        instance.addressLines = null;
      }
    } else if ("All Contacts".equals(fieldName)){
      if (jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {
        HashMap<String, String> map1 = new HashMap<String, String>();
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
          String key1 = jsonParser.getText();
          jsonParser.nextToken();
          if (jsonParser.getCurrentToken() == JsonToken.VALUE_NULL) {
            map1.put(key1, null);
          } else{
            map1.put(key1, jsonParser.getValueAsString(null));
          }
        }
        instance.allContacts = map1;
      } else{
        instance.allContacts = null;
      }
    } else if ("Full Name".equals(fieldName)){
      instance.fullName = jsonParser.getValueAsString(null);
    }
  }

  @Override
  public void serialize(WhitespaceFieldNameModel object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
    _serialize(object, jsonGenerator, writeStartAndEnd);
  }

  public static void _serialize(WhitespaceFieldNameModel object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
    if (writeStartAndEnd) {
      jsonGenerator.writeStartObject();
    }
    final List<String> lslocalAddressLines = object.addressLines;
    if (lslocalAddressLines != null) {
      jsonGenerator.writeFieldName("Address Lines");
      jsonGenerator.writeStartArray();
      for (String element1 : lslocalAddressLines) {
        if (element1 != null) {
          jsonGenerator.writeString(element1);
        }
      }
      jsonGenerator.writeEndArray();
    }
    final Map<String, String> lslocalAllContacts = object.allContacts;
    if (lslocalAllContacts != null) {
      jsonGenerator.writeFieldName("All Contacts");
      jsonGenerator.writeStartObject();
      for (Map.Entry<String, String> entry1 : lslocalAllContacts.entrySet()) {
        jsonGenerator.writeFieldName(entry1.getKey().toString());
        if (entry1.getValue() != null) {
          jsonGenerator.writeString(entry1.getValue());
        }
      }
      jsonGenerator.writeEndObject();
    }
    if (object.fullName != null) {
      jsonGenerator.writeStringField("Full Name", object.fullName);
    }
    if (writeStartAndEnd) {
      jsonGenerator.writeEndObject();
    }
  }
}

package com.bluelinelabs.logansquare.processor.model;

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

@SuppressWarnings("unsafe,unchecked")
public final class WhitespaceFieldNameModel$$JsonObjectMapper extends JsonMapper<WhitespaceFieldNameModel> {
  @Override
  public WhitespaceFieldNameModel parse(JsonParser jsonParser) throws IOException {
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

  @Override
  public void parseField(WhitespaceFieldNameModel instance, String fieldName, JsonParser jsonParser) throws IOException {
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
    } else if ("AddressLines".equals(fieldName)){
      if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
        ArrayList<String> collection1 = new ArrayList<String>();
        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
          String value1;
          value1 = jsonParser.getValueAsString(null);
          collection1.add(value1);
        }
        instance.addressLinesDuplicate = collection1;
      } else{
        instance.addressLinesDuplicate = null;
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
    } else if ("Pet Names".equals(fieldName)){
      if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
        List<String> collection1 = new ArrayList<String>();
        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
          String value1;
          value1 = jsonParser.getValueAsString(null);
          collection1.add(value1);
        }
        String[] array = collection1.toArray(new String[collection1.size()]);
        instance.petNames = array;
      } else{
        instance.petNames = null;
      }
    }
  }

  @Override
  public void serialize(WhitespaceFieldNameModel object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
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
    final List<String> lslocalAddressLines1 = object.addressLinesDuplicate;
    if (lslocalAddressLines1 != null) {
      jsonGenerator.writeFieldName("AddressLines");
      jsonGenerator.writeStartArray();
      for (String element1 : lslocalAddressLines1) {
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
    final String[] lslocalPetNames = object.petNames;
    if (lslocalPetNames != null) {
      jsonGenerator.writeFieldName("Pet Names");
      jsonGenerator.writeStartArray();
      for (String element1 : lslocalPetNames) {
        if (element1 != null) {
          jsonGenerator.writeString(element1);
        }
      }
      jsonGenerator.writeEndArray();
    }
    if (writeStartAndEnd) {
      jsonGenerator.writeEndObject();
    }
  }

  public void ensureParent() {
  }
}

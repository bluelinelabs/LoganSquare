package com.bluelinelabs.logansquare.demo.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unsafe")
public final class NonPrivateFieldsAndAccessorsFieldDetectionPolicyModel$$JsonObjectMapper extends JsonMapper<NonPrivateFieldsAndAccessorsFieldDetectionPolicyModel> {
    @Override
    public NonPrivateFieldsAndAccessorsFieldDetectionPolicyModel parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static NonPrivateFieldsAndAccessorsFieldDetectionPolicyModel _parse(JsonParser jsonParser) throws IOException {
        NonPrivateFieldsAndAccessorsFieldDetectionPolicyModel instance = new NonPrivateFieldsAndAccessorsFieldDetectionPolicyModel();
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

    public static void parseField(NonPrivateFieldsAndAccessorsFieldDetectionPolicyModel instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("annotated_string".equals(fieldName)) {
            instance.annotatedString = jsonParser.getValueAsString(null);
        } else if ("intToIgnoreForSerialization".equals(fieldName)){
            instance.intToIgnoreForSerialization = jsonParser.getValueAsInt();
        } else if ("nonAnnotatedList".equals(fieldName)){
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<String> collection1 = new ArrayList<String>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    String value1;
                    value1 = jsonParser.getValueAsString(null);
                    collection1.add(value1);
                }
                instance.nonAnnotatedList = collection1;
            } else {
                instance.nonAnnotatedList = null;
            }
        } else if ("nonAnnotatedPrivateInt".equals(fieldName)){
            instance.setNonAnnotatedPrivateInt(jsonParser.getValueAsInt());
        } else if ("nonAnnotatedString".equals(fieldName)){
            instance.nonAnnotatedString = jsonParser.getValueAsString(null);
        } else if ("privateStaticIntToInclude".equals(fieldName)){
            instance.setPrivateStaticIntToInclude(jsonParser.getValueAsInt());
        } else if ("privateTransientIntToInclude".equals(fieldName)){
            instance.setPrivateTransientIntToInclude(jsonParser.getValueAsInt());
        } else if ("staticIntToInclude".equals(fieldName)){
            instance.staticIntToInclude = jsonParser.getValueAsInt();
        } else if ("transientIntToInclude".equals(fieldName)){
            instance.transientIntToInclude = jsonParser.getValueAsInt();
        }
    }

    @Override
    public void serialize(NonPrivateFieldsAndAccessorsFieldDetectionPolicyModel object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(NonPrivateFieldsAndAccessorsFieldDetectionPolicyModel object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.annotatedString != null) {
            jsonGenerator.writeStringField("annotated_string", object.annotatedString);
        }
        jsonGenerator.writeNumberField("intToIgnoreForParse", object.intToIgnoreForParse);
        final List<String> lslocalnonAnnotatedList = object.nonAnnotatedList;
        if (lslocalnonAnnotatedList != null) {
            jsonGenerator.writeFieldName("nonAnnotatedList");
            jsonGenerator.writeStartArray();
            for (String element1 : lslocalnonAnnotatedList) {
                if (element1 != null) {
                    jsonGenerator.writeString(element1);
                }
            }
            jsonGenerator.writeEndArray();
        }
        jsonGenerator.writeNumberField("nonAnnotatedPrivateInt", object.getNonAnnotatedPrivateInt());
        if (object.nonAnnotatedString != null) {
            jsonGenerator.writeStringField("nonAnnotatedString", object.nonAnnotatedString);
        }
        jsonGenerator.writeNumberField("privateStaticIntToInclude", object.getPrivateStaticIntToInclude());
        jsonGenerator.writeNumberField("privateTransientIntToInclude", object.getPrivateTransientIntToInclude());
        jsonGenerator.writeNumberField("staticIntToInclude", object.staticIntToInclude);
        jsonGenerator.writeNumberField("transientIntToInclude", object.transientIntToInclude);
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
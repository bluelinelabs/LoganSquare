package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public final class SimpleCollectionModel$$JsonObjectMapper extends JsonMapper<SimpleCollectionModel> {
    @Override
    public SimpleCollectionModel parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static SimpleCollectionModel _parse(JsonParser jsonParser) throws IOException {
        SimpleCollectionModel instance = new SimpleCollectionModel();
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

    public static void parseField(SimpleCollectionModel instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("primitive_array".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                List<Integer> list = new ArrayList<Integer>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    list.add(jsonParser.getValueAsInt());
                }
                int[] array = new int[list.size()];
                int i = 0;
                for (int value : list) {
                    array[i++] = value;
                }
                instance.primitiveArray = array;
            }
        } else if ("model_array".equals(fieldName)){
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                List<SimpleCollectionModel.ModelForCollection> list = new ArrayList<SimpleCollectionModel.ModelForCollection>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    SimpleCollectionModel.ModelForCollection value = com.bluelinelabs.logansquare.processor.SimpleCollectionModel$ModelForCollection$$JsonObjectMapper._parse(jsonParser);
                    if (value != null) {
                        list.add(value);
                    }
                }
                SimpleCollectionModel.ModelForCollection[] array = list.toArray(new SimpleCollectionModel.ModelForCollection[list.size()]);
                instance.modelForCollectionArray = array;
            }
        } else if ("model_map".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {
                HashMap<String, SimpleCollectionModel.ModelForCollection> map = new HashMap<String, SimpleCollectionModel.ModelForCollection>();
                while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                    String key = jsonParser.getText();
                    jsonParser.nextToken();
                    if (jsonParser.getCurrentToken() == JsonToken.VALUE_NULL) {
                        map.put(key, null);
                    } else{
                        map.put(key, com.bluelinelabs.logansquare.processor.SimpleCollectionModel$ModelForCollection$$JsonObjectMapper._parse(jsonParser));
                    }
                }
                instance.modelForCollectionMap = map;
            }
        } else if ("model_deque".equals(fieldName)){
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayDeque<SimpleCollectionModel.ModelForCollection> collection = new ArrayDeque<SimpleCollectionModel.ModelForCollection>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    SimpleCollectionModel.ModelForCollection value = com.bluelinelabs.logansquare.processor.SimpleCollectionModel$ModelForCollection$$JsonObjectMapper._parse(jsonParser);
                    if (value != null) {
                        collection.add(value);
                    }
                }
                instance.modelForCollectionDeque = collection;
            }
        } else if ("model_list".equals(fieldName)){
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<SimpleCollectionModel.ModelForCollection> collection = new ArrayList<SimpleCollectionModel.ModelForCollection>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    SimpleCollectionModel.ModelForCollection value = com.bluelinelabs.logansquare.processor.SimpleCollectionModel$ModelForCollection$$JsonObjectMapper._parse(jsonParser);
                    if (value != null) {
                        collection.add(value);
                    }
                }
                instance.modelForCollectionList = collection;
            }
        } else if ("model_set".equals(fieldName)){
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                HashSet<SimpleCollectionModel.ModelForCollection> collection = new HashSet<SimpleCollectionModel.ModelForCollection>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    SimpleCollectionModel.ModelForCollection value = com.bluelinelabs.logansquare.processor.SimpleCollectionModel$ModelForCollection$$JsonObjectMapper._parse(jsonParser);
                    if (value != null) {
                        collection.add(value);
                    }
                }
                instance.modelForCollectionSet = collection;
            }
        } else if ("model_queue".equals(fieldName)){
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayDeque<SimpleCollectionModel.ModelForCollection> collection = new ArrayDeque<SimpleCollectionModel.ModelForCollection>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    SimpleCollectionModel.ModelForCollection value = com.bluelinelabs.logansquare.processor.SimpleCollectionModel$ModelForCollection$$JsonObjectMapper._parse(jsonParser);
                    if (value != null) {
                        collection.add(value);
                    }
                }
                instance.modelForCollectionQueue = collection;
            }
        }
    }

    @Override
    public void serialize(SimpleCollectionModel object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(SimpleCollectionModel object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        final int[] lslocalprimitiveArray = object.primitiveArray;
        if (lslocalprimitiveArray != null) {
            jsonGenerator.writeFieldName("primitive_array");
            jsonGenerator.writeStartArray();
            for (int element : lslocalprimitiveArray) {
                jsonGenerator.writeNumber(element);
            }
            jsonGenerator.writeEndArray();
        }
        final SimpleCollectionModel.ModelForCollection[] lslocalmodelForCollectionArray = object.modelForCollectionArray;
        if (lslocalmodelForCollectionArray != null) {
            jsonGenerator.writeFieldName("model_array");
            jsonGenerator.writeStartArray();
            for (SimpleCollectionModel.ModelForCollection element : lslocalmodelForCollectionArray) {
                if (element != null) {
                    SimpleCollectionModel$ModelForCollection$$JsonObjectMapper._serialize(element, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        final Map<String, SimpleCollectionModel.ModelForCollection> lslocalmodelForCollectionMap = object.modelForCollectionMap;
        if (lslocalmodelForCollectionMap != null) {
            jsonGenerator.writeFieldName("model_map");
            jsonGenerator.writeStartObject();
            for (Map.Entry<String, SimpleCollectionModel.ModelForCollection> entry : lslocalmodelForCollectionMap.entrySet()) {
                jsonGenerator.writeFieldName(entry.getKey().toString());
                if (entry.getValue() == null) {
                    jsonGenerator.writeNull();
                } else{
                    if (entry.getValue() != null) {
                        SimpleCollectionModel$ModelForCollection$$JsonObjectMapper._serialize(entry.getValue(), jsonGenerator, true);
                    }
                }
            }
            jsonGenerator.writeEndObject();
        }
        final Queue<SimpleCollectionModel.ModelForCollection> lslocalmodelForCollectionDeque = object.modelForCollectionDeque;
        if (lslocalmodelForCollectionDeque != null) {
            jsonGenerator.writeFieldName("model_deque");
            jsonGenerator.writeStartArray();
            for (SimpleCollectionModel.ModelForCollection element : lslocalmodelForCollectionDeque) {
                if (element != null) {
                    SimpleCollectionModel$ModelForCollection$$JsonObjectMapper._serialize(element, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        final List<SimpleCollectionModel.ModelForCollection> lslocalmodelForCollectionList = object.modelForCollectionList;
        if (lslocalmodelForCollectionList != null) {
            jsonGenerator.writeFieldName("model_list");
            jsonGenerator.writeStartArray();
            for (SimpleCollectionModel.ModelForCollection element : lslocalmodelForCollectionList) {
                if (element != null) {
                    SimpleCollectionModel$ModelForCollection$$JsonObjectMapper._serialize(element, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        final Set<SimpleCollectionModel.ModelForCollection> lslocalmodelForCollectionSet = object.modelForCollectionSet;
        if (lslocalmodelForCollectionSet != null) {
            jsonGenerator.writeFieldName("model_set");
            jsonGenerator.writeStartArray();
            for (SimpleCollectionModel.ModelForCollection element : lslocalmodelForCollectionSet) {
                if (element != null) {
                    SimpleCollectionModel$ModelForCollection$$JsonObjectMapper._serialize(element, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        final Queue<SimpleCollectionModel.ModelForCollection> lslocalmodelForCollectionQueue = object.modelForCollectionQueue;
        if (lslocalmodelForCollectionQueue != null) {
            jsonGenerator.writeFieldName("model_queue");
            jsonGenerator.writeStartArray();
            for (SimpleCollectionModel.ModelForCollection element : lslocalmodelForCollectionQueue) {
                if (element != null) {
                    SimpleCollectionModel$ModelForCollection$$JsonObjectMapper._serialize(element, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }

}
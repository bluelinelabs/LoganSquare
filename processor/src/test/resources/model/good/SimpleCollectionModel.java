package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

@JsonObject
public class SimpleCollectionModel {

    @JsonField(fieldName = "model_list")
    public List<ModelForCollection> ModelForCollectionList;

    @JsonField(fieldName = "model_set")
    public Set<ModelForCollection> ModelForCollectionSet;

    @JsonField(fieldName = "model_queue")
    public Queue<ModelForCollection> ModelForCollectionQueue;

    @JsonField(fieldName = "model_deque")
    public Deque<ModelForCollection> ModelForCollectionDeque;

    @JsonField(fieldName = "model_map")
    public Map<String, ModelForCollection> ModelForCollectionMap;

    @JsonObject
    public static class ModelForCollection {

        @JsonField
        public String name;

    }
}

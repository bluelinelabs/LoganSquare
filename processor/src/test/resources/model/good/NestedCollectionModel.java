package model.good;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

@JsonObject
public class NestedCollectionModel {

    @JsonField(name = "primitive_array")
    public int[][] primitiveTwoDimensionalArray;

    @JsonField(name = "model_list")
    public List<List<ModelForCollection>> modelForCollectionList;

    @JsonField(name = "model_set")
    public Set<List<ModelForCollection>> modelForCollectionSet;

    @JsonField(name = "model_queue")
    public Queue<List<ModelForCollection>> modelForCollectionQueue;

    @JsonField(name = "model_deque")
    public Deque<List<ModelForCollection>> modelForCollectionDeque;

    @JsonField(name = "model_map")
    public Map<String, List<ModelForCollection>> modelForCollectionMap;

    @JsonField(name = "model_array")
    public ModelForCollection[][] modelForCollectionTwoDimensionalArray;

    public List<List<List<List<ModelForCollection>>>> dontKnowWhatImDoingList;

    @JsonObject
    public static class ModelForCollection {

        @JsonField
        public String name;

    }
}
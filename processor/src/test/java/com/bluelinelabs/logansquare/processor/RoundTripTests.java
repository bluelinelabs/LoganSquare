package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.processor.model.NestedCollectionModel;
import com.bluelinelabs.logansquare.processor.model.SimpleModel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.google.common.truth.Truth.ASSERT;

public class RoundTripTests {

    @Test
    public void simpleObject() {
        String json = "{\"date\":\"2015-02-21T18:45:50.748+0000\",\"string\":\"testString\",\"test_double\":342.0,\"test_double_obj\":345.0,\"test_float\":898.0,\"test_float_obj\":382.0,\"test_int\":32,\"test_int_obj\":323,\"test_long\":932,\"test_long_obj\":3920,\"test_string\":\"anotherTestString\"}";

        String reserialized = null;
        try {
            SimpleModel simpleModel = LoganSquare.parse(json, SimpleModel.class);
            reserialized = LoganSquare.serialize(simpleModel);
        } catch (Exception ignored) { }

        ASSERT.that(json.equals(reserialized)).isTrue();
    }

    @Test
    public void simpleObjectList() {
        String json = "[{\"date\":\"2015-02-21T18:45:50.748+0000\",\"string\":\"testString\",\"test_double\":342.0,\"test_double_obj\":345.0,\"test_float\":898.0,\"test_float_obj\":382.0,\"test_int\":32,\"test_int_obj\":323,\"test_long\":932,\"test_long_obj\":3920,\"test_string\":\"anotherTestString\"}," +
                "{\"date\":\"2015-02-22T18:45:50.748+0000\",\"string\":\"testString2\",\"test_double\":342.0,\"test_double_obj\":345.0,\"test_float\":898.0,\"test_float_obj\":382.0,\"test_int\":32,\"test_int_obj\":323,\"test_long\":932,\"test_long_obj\":3920,\"test_string\":\"anotherTestString\"}]";

        String reserialized = null;
        try {
            List<SimpleModel> simpleModels = LoganSquare.parseList(json, SimpleModel.class);
            reserialized = LoganSquare.serialize(simpleModels, SimpleModel.class);
        } catch (Exception ignored) { }

        ASSERT.that(json.equals(reserialized)).isTrue();
    }

    @Test
    public void simpleObjectMap() {
        String json = "{\"obj1\":{\"date\":\"2015-02-21T18:45:50.748+0000\",\"string\":\"testString\",\"test_double\":342.0,\"test_double_obj\":345.0,\"test_float\":898.0,\"test_float_obj\":382.0,\"test_int\":32,\"test_int_obj\":323,\"test_long\":932,\"test_long_obj\":3920,\"test_string\":\"anotherTestString\"}," +
                "\"obj2\":{\"date\":\"2015-02-22T18:45:50.748+0000\",\"string\":\"testString2\",\"test_double\":342.0,\"test_double_obj\":345.0,\"test_float\":898.0,\"test_float_obj\":382.0,\"test_int\":32,\"test_int_obj\":323,\"test_long\":932,\"test_long_obj\":3920,\"test_string\":\"anotherTestString\"}}";

        String reserialized = null;
        try {
            Map<String, SimpleModel> simpleModelMap = LoganSquare.parseMap(json, SimpleModel.class);
            reserialized = LoganSquare.serialize(simpleModelMap, SimpleModel.class);
        } catch (Exception ignored) { }

        ASSERT.that(json.equals(reserialized)).isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void nestedCollection() {

        List<ArrayList<Set<Map<String, List<String>>>>>[] array = (List<ArrayList<Set<Map<String, List<String>>>>>[])new List<?>[] {getStringListMapSetArrayListList() };

        NestedCollectionModel model = new NestedCollectionModel();
        model.crazyCollection = array;

        String json = null;
        try {
            json = LoganSquare.serialize(model);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ASSERT.that(json).isNotNull();

        NestedCollectionModel reconstructedModel = null;
        try {
            reconstructedModel = LoganSquare.parse(json, NestedCollectionModel.class);
        } catch (Exception ignored) { }

        // Comparing the json doesn't work so well on arrays, since they could be reordered. We'll compare the output instead.
        ASSERT.that(model.equals(reconstructedModel)).isTrue();
    }

    private List<String> getStringList() {
        List<String> list = new ArrayList<>();
        list.add(UUID.randomUUID().toString());
        list.add(UUID.randomUUID().toString());
        return list;
    }

    private Map<String, List<String>> getStringListMap() {
        Map<String, List<String>> map = new HashMap<>();
        map.put(UUID.randomUUID().toString(), getStringList());
        map.put(UUID.randomUUID().toString(), getStringList());
        return map;
    }

    private Set<Map<String, List<String>>> getStringListMapSet() {
        Set<Map<String, List<String>>> set = new HashSet<>();
        set.add(getStringListMap());
        set.add(getStringListMap());
        set.add(getStringListMap());
        return set;
    }

    private ArrayList<Set<Map<String, List<String>>>> getStringListMapSetArrayList() {
        ArrayList<Set<Map<String, List<String>>>> list = new ArrayList<>();
        list.add(getStringListMapSet());
        list.add(getStringListMapSet());
        return list;
    }

    private List<ArrayList<Set<Map<String, List<String>>>>> getStringListMapSetArrayListList() {
        List<ArrayList<Set<Map<String, List<String>>>>> list = new ArrayList<>();
        list.add(getStringListMapSetArrayList());
        list.add(getStringListMapSetArrayList());
        return list;
    }
}

package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.ParameterizedType;
import com.bluelinelabs.logansquare.processor.model.NestedCollectionModel;
import com.bluelinelabs.logansquare.processor.model.SimpleGenericModel;
import com.bluelinelabs.logansquare.processor.model.SimpleModel;
import com.bluelinelabs.logansquare.processor.model.SimpleModelWithoutNullObjects;
import com.bluelinelabs.logansquare.processor.model.TwoParamGenericModel;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import static com.google.common.truth.Truth.ASSERT;

public class RoundTripTests {

    @Test
    public void stringList() {
        String json = "[\"test1\",\"test2\",\"test3\",\"test4\"]";

        String reserialized = null;
        try {
            List<String> list = LoganSquare.parseList(json, String.class);
            reserialized = LoganSquare.serialize(list, String.class);
        } catch (Exception ignored) { }

        ASSERT.that(json.equals(reserialized)).isTrue();
    }

    @Test
    public void intList() {
        String json = "[1,2,3,4,5,6]";

        String reserialized = null;
        try {
            List<Integer> list = LoganSquare.parseList(json, Integer.class);
            reserialized = LoganSquare.serialize(list, Integer.class);
        } catch (Exception ignored) { }

        ASSERT.that(json.equals(reserialized)).isTrue();
    }

    @Test
    public void floatList() {
        String json = "[1.4,2.0,3.0,4.0,5.03,6.2]";

        String reserialized = null;
        try {
            List<Float> list = LoganSquare.parseList(json, Float.class);
            reserialized = LoganSquare.serialize(list, Float.class);
        } catch (Exception ignored) { }

        ASSERT.that(json.equals(reserialized)).isTrue();
    }

    @Test
    public void booleanList() {
        String json = "[true,false,false,false,true]";

        String reserialized = null;
        try {
            List<Boolean> list = LoganSquare.parseList(json, Boolean.class);
            reserialized = LoganSquare.serialize(list, Boolean.class);
        } catch (Exception ignored) { }

        ASSERT.that(json.equals(reserialized)).isTrue();
    }

    @Test
    public void mixedObjectList() {
        String json = "[true,1,1.0,\"test1\",false,1000,1000000000,\"test2\",{\"subkey1\":1,\"subkey2\":\"string\"},[1,2,3]]";

        String reserialized = null;
        try {
            List<Object> list = LoganSquare.parseList(json, Object.class);
            reserialized = LoganSquare.serialize(list, Object.class);
        } catch (Exception ignored) { }

        ASSERT.that(json.equals(reserialized)).isTrue();
    }

    @Test
    public void stringMap() {
        String json = "{\"key1\":\"val1\",\"key2\":\"val2\",\"key3\":\"val3\"}";

        String reserialized = null;
        try {
            Map<String, String> map = new TreeMap<>(LoganSquare.parseMap(json, String.class));
            reserialized = LoganSquare.serialize(map, String.class);
        } catch (Exception ignored) { }

        ASSERT.that(json.equals(reserialized)).isTrue();
    }

    @Test
    public void intMap() {
        String json = "{\"key1\":1,\"key2\":2,\"key3\":3}";

        String reserialized = null;
        try {
            Map<String, Integer> map = new TreeMap<>(LoganSquare.parseMap(json, Integer.class));
            reserialized = LoganSquare.serialize(map, Integer.class);
        } catch (Exception ignored) { }

        ASSERT.that(json.equals(reserialized)).isTrue();
    }

    @Test
    public void floatMap() {
        String json = "{\"key1\":1.4,\"key2\":2.0,\"key3\":3.224}";

        String reserialized = null;
        try {
            Map<String, Float> map = new TreeMap<>(LoganSquare.parseMap(json, Float.class));
            reserialized = LoganSquare.serialize(map, Float.class);
        } catch (Exception ignored) { }

        ASSERT.that(json.equals(reserialized)).isTrue();
    }

    @Test
    public void booleanMap() {
        String json = "{\"key1\":true,\"key2\":true,\"key3\":false}";

        String reserialized = null;
        try {
            Map<String, Boolean> map = new TreeMap<>(LoganSquare.parseMap(json, Boolean.class));
            reserialized = LoganSquare.serialize(map, Boolean.class);
        } catch (Exception ignored) { }

        ASSERT.that(json.equals(reserialized)).isTrue();
    }

    @Test
    public void mixedObjectMap() {
        String json = "{\"key1\":true,\"key2\":1,\"key3\":1.02,\"key4\":1002020,\"key5\":\"test2\",\"key6\":{\"subkey1\":1,\"subkey2\":\"string\"},\"key7\":[1,2,3]}";

        String reserialized = null;
        try {
            Map<String, Object> map = new TreeMap<>(LoganSquare.parseMap(json, Object.class));
            reserialized = LoganSquare.serialize(map, Object.class);
        } catch (Exception ignored) { }

        ASSERT.that(json.equals(reserialized)).isTrue();
    }

    @Test
    public void simpleGenericObject() {
        String json = "{\"date\":\"2015-02-21T18:45:50.748+0000\",\"string\":\"testString\",\"test_double\":342.0,\"test_double_obj\":345.0,\"test_float\":898.0,\"test_float_obj\":382.0,\"test_int\":32,\"test_int_obj\":323,\"test_long\":932,\"test_long_obj\":3920,\"test_nested_generic\":{\"test_double\":0.0,\"test_float\":0.2,\"test_int\":10,\"test_long\":0},\"test_string\":\"anotherTestString\",\"test_t\":\"generic string!\"}";

        String reserialized = null;
        try {
            ParameterizedType<SimpleGenericModel<String>> parameterizedType = new ParameterizedType<SimpleGenericModel<String>>() { };
            SimpleGenericModel<String> simpleModel = LoganSquare.parse(json, parameterizedType);
            reserialized = LoganSquare.serialize(simpleModel, parameterizedType);
        } catch (Exception ignored) { }

        ASSERT.that(json.equals(reserialized)).isTrue();
    }

    @Test
    public void simpleGenericGenericObject() {
        String json = "{\"date\":\"2015-02-21T18:45:50.748+0000\",\"string\":\"testString\",\"test_double\":342.0,\"test_double_obj\":345.0,\"test_float\":898.0,\"test_float_obj\":382.0,\"test_int\":32,\"test_int_obj\":323,\"test_long\":932,\"test_long_obj\":3920,\"test_string\":\"anotherTestString\",\"test_t\":{\"date\":\"2015-02-21T18:45:50.748+0000\",\"string\":\"testString\",\"test_double\":342.0,\"test_double_obj\":345.0,\"test_float\":898.0,\"test_float_obj\":382.0,\"test_int\":32,\"test_int_obj\":323,\"test_long\":932,\"test_long_obj\":3920,\"test_string\":\"anotherTestString\",\"test_t\":\"generic string!\"}}";

        String reserialized = null;
        try {
            ParameterizedType<SimpleGenericModel<SimpleGenericModel<String>>> parameterizedType = new ParameterizedType<SimpleGenericModel<SimpleGenericModel<String>>>() { };
            SimpleGenericModel<SimpleGenericModel<String>> simpleModel = LoganSquare.parse(json, parameterizedType);
            reserialized = LoganSquare.serialize(simpleModel, parameterizedType);
        } catch (Exception ignored) { }

        ASSERT.that(json.equals(reserialized)).isTrue();
    }

    @Test
    public void twoParamGenericModelObject() {
        String json = "{\"t_list\":[\"a\",\"b\"],\"test_k\":1,\"test_t\":\"hello\"}";

        String reserialized = null;
        try {
            ParameterizedType<TwoParamGenericModel<String, Integer>> parameterizedType = new ParameterizedType<TwoParamGenericModel<String, Integer>>() { };
            TwoParamGenericModel<String, Integer> model = LoganSquare.parse(json, parameterizedType);
            reserialized = LoganSquare.serialize(model, parameterizedType);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        ASSERT.that(json.equals(reserialized)).isTrue();
    }

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
    public void simpleObjectListWithNulls() {
        String json = "[{\"date\":\"2015-02-21T18:45:50.748+0000\",\"string\":\"testString\",\"test_double\":342.0,\"test_double_obj\":345.0,\"test_float\":898.0,\"test_float_obj\":382.0,\"test_int\":32,\"test_int_obj\":323,\"test_long\":932,\"test_long_obj\":3920,\"test_string\":\"anotherTestString\"}," +
                "{\"date\":\"2015-02-22T18:45:50.748+0000\",\"string\":\"testString2\",\"test_double\":342.0,\"test_double_obj\":345.0,\"test_float\":898.0,\"test_float_obj\":382.0,\"test_int\":32,\"test_int_obj\":323,\"test_long\":932,\"test_long_obj\":3920,\"test_string\":\"anotherTestString\"}," +
                "{\"date\":null,\"string\":\"testString2\",\"test_double\":342.0,\"test_double_obj\":null,\"test_float\":898.0,\"test_float_obj\":null,\"test_int\":32,\"test_int_obj\":null,\"test_long\":932,\"test_long_obj\":null,\"test_string\":null}," +
                "null," +
                "null]";

        String reserialized = null;
        try {
            List<SimpleModel> simpleModels = LoganSquare.parseList(json, SimpleModel.class);
            reserialized = LoganSquare.serialize(simpleModels, SimpleModel.class);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        ASSERT.that(json.equals(reserialized)).isTrue();
    }

    @Test
    public void simpleObjectMap() {
        String json = "{\"obj1\":{\"date\":\"2015-02-21T18:45:50.748+0000\",\"string\":\"testString\",\"test_double\":342.0,\"test_double_obj\":345.0,\"test_float\":898.0,\"test_float_obj\":382.0,\"test_int\":32,\"test_int_obj\":323,\"test_long\":932,\"test_long_obj\":3920,\"test_string\":\"anotherTestString\"}," +
                "\"obj2\":{\"date\":\"2015-02-22T18:45:50.748+0000\",\"string\":\"testString2\",\"test_double\":342.0,\"test_double_obj\":345.0,\"test_float\":898.0,\"test_float_obj\":382.0,\"test_int\":32,\"test_int_obj\":323,\"test_long\":932,\"test_long_obj\":3920,\"test_string\":\"anotherTestString\"}}";

        String reserialized = null;
        try {
            Map<String, SimpleModel> simpleModelMap = new TreeMap<>(LoganSquare.parseMap(json, SimpleModel.class));
            reserialized = LoganSquare.serialize(simpleModelMap, SimpleModel.class);
        } catch (Exception ignored) { }

        ASSERT.that(json.equals(reserialized)).isTrue();
    }

    @Test
    public void simpleObjectMapWithNulls() {
        String json = "{\"obj1\":{\"date\":\"2015-02-21T18:45:50.748+0000\",\"string\":\"testString\",\"test_double\":342.0,\"test_double_obj\":345.0,\"test_float\":898.0,\"test_float_obj\":382.0,\"test_int\":32,\"test_int_obj\":323,\"test_long\":932,\"test_long_obj\":3920,\"test_string\":\"anotherTestString\"}," +
                "\"obj2\":{\"date\":\"2015-02-22T18:45:50.748+0000\",\"string\":\"testString2\",\"test_double\":342.0,\"test_double_obj\":345.0,\"test_float\":898.0,\"test_float_obj\":382.0,\"test_int\":32,\"test_int_obj\":323,\"test_long\":932,\"test_long_obj\":3920,\"test_string\":\"anotherTestString\"}," +
                "\"obj3\":{\"date\":null,\"string\":\"testString2\",\"test_double\":342.0,\"test_double_obj\":null,\"test_float\":898.0,\"test_float_obj\":null,\"test_int\":32,\"test_int_obj\":null,\"test_long\":932,\"test_long_obj\":null,\"test_string\":null}," +
                "\"obj4\":null}";

        String reserialized = null;
        try {
            Map<String, SimpleModel> simpleModelMap = LoganSquare.parseMap(json, SimpleModel.class);
            TreeMap<String, SimpleModel> sortedMap = new TreeMap<>();
            sortedMap.put("obj1", simpleModelMap.get("obj1"));
            sortedMap.put("obj2", simpleModelMap.get("obj2"));
            sortedMap.put("obj3", simpleModelMap.get("obj3"));
            sortedMap.put("obj4", simpleModelMap.get("obj4"));

            reserialized = LoganSquare.serialize(sortedMap, SimpleModel.class);
        } catch (Exception ignored) { }

        ASSERT.that(json.equals(reserialized)).isTrue();
    }

    @Test
    public void simpleObjectMapWithoutNulls() {
        String json = "{\"obj1\":{\"date\":\"2015-02-21T18:45:50.748+0000\",\"string\":\"testString\",\"test_double\":342.0,\"test_double_obj\":345.0,\"test_float\":898.0,\"test_float_obj\":382.0,\"test_int\":32,\"test_int_obj\":323,\"test_long\":932,\"test_long_obj\":3920,\"test_string\":\"anotherTestString\"}," +
                "\"obj2\":{\"date\":\"2015-02-22T18:45:50.748+0000\",\"string\":\"testString2\",\"test_double\":342.0,\"test_double_obj\":345.0,\"test_float\":898.0,\"test_float_obj\":382.0,\"test_int\":32,\"test_int_obj\":323,\"test_long\":932,\"test_long_obj\":3920,\"test_string\":\"anotherTestString\"}," +
                "\"obj3\":{\"string\":\"testString2\",\"test_double\":342.0,\"test_float\":898.0,\"test_int\":32,\"test_long\":932}," +
                "\"obj4\":null}";

        String reserialized = null;
        try {
            Map<String, SimpleModelWithoutNullObjects> simpleModelMap = LoganSquare.parseMap(json, SimpleModelWithoutNullObjects.class);
            TreeMap<String, SimpleModelWithoutNullObjects> sortedMap = new TreeMap<>();
            sortedMap.put("obj1", simpleModelMap.get("obj1"));
            sortedMap.put("obj2", simpleModelMap.get("obj2"));
            sortedMap.put("obj3", simpleModelMap.get("obj3"));
            sortedMap.put("obj4", simpleModelMap.get("obj4"));

            reserialized = LoganSquare.serialize(sortedMap, SimpleModelWithoutNullObjects.class);
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

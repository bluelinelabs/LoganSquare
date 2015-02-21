package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.processor.model.SimpleModel;
import org.junit.Test;

import java.util.List;
import java.util.Map;

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
}

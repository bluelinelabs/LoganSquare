package com.bluelinelabs.logansquare.processor.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class TwoParamGenericModel<T, K> {

    @JsonField(name = "test_t")
    public T testT;

    @JsonField(name = "test_k")
    public K testK;

    @JsonField(name = "t_list")
    public List<T> tList;

    @JsonField(name = "k_list")
    public List<K> kList;

    @JsonField(name = "test_nested_generic")
    public TwoParamGenericModel<String, Integer> testNestedGeneric;

    @JsonField(name = "test_nested_generic_integer")
    public TwoParamGenericModel<T, Integer> testNestedGenericInteger;

    @JsonField(name = "test_nested_string_generic")
    public TwoParamGenericModel<String, T> testNestedStringGeneric;

}

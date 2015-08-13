package com.bluelinelabs.logansquare.internal;

import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.ParameterizedType;
import com.bluelinelabs.logansquare.util.SimpleArrayMap;

public interface JsonMapperLoader {

    void putAllJsonMappers(SimpleArrayMap<Class, JsonMapper> map);
    <T> JsonMapper<T> mapperFor(ParameterizedType<T> type);

}

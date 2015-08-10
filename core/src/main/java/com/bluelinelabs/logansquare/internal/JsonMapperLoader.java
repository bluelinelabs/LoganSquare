package com.bluelinelabs.logansquare.internal;

import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.util.SimpleArrayMap;

public interface JsonMapperLoader {

    void putAllJsonMappers(SimpleArrayMap<Class, JsonMapper> map);

}

package com.bluelinelabs.logansquare.processor;

import java.util.Collections;
import java.util.Map;

public class JsonEnumHolder {

    private final Map<String, String> valuesMap;

    private JsonEnumHolder(final Map<String, String> valuesMap) {
        this.valuesMap = valuesMap;
    }

    public Map<String, String> getValuesMap() {
        return Collections.unmodifiableMap(valuesMap);
    }

}

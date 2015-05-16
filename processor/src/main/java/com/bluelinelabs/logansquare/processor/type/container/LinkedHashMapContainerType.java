package com.bluelinelabs.logansquare.processor.type.container;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.LinkedHashMap;
import java.util.TreeMap;

public class LinkedHashMapContainerType extends MapContainerType {

    public LinkedHashMapContainerType(ClassName className) {
        super(className);
    }

    @Override
    public TypeName getTypeName() {
        return ClassName.get(LinkedHashMap.class);
    }

}

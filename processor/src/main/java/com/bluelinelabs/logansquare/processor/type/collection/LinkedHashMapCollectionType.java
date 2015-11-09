package com.bluelinelabs.logansquare.processor.type.collection;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.LinkedHashMap;

public class LinkedHashMapCollectionType extends MapCollectionType {

    public LinkedHashMapCollectionType(ClassName className) {
        super(className);
    }

    @Override
    public TypeName getTypeName() {
        return ClassName.get(LinkedHashMap.class);
    }

}

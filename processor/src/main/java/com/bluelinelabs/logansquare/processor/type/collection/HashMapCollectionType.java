package com.bluelinelabs.logansquare.processor.type.collection;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.HashMap;

public class HashMapCollectionType extends MapCollectionType {

    public HashMapCollectionType(ClassName className) {
        super(className);
    }

    @Override
    public TypeName getTypeName() {
        return ClassName.get(HashMap.class);
    }

}

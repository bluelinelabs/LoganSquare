package com.bluelinelabs.logansquare.processor.type.collection;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.TreeMap;

public class TreeMapCollectionType extends MapCollectionType {

    public TreeMapCollectionType(ClassName className) {
        super(className);
    }

    @Override
    public TypeName getTypeName() {
        return ClassName.get(TreeMap.class);
    }

}

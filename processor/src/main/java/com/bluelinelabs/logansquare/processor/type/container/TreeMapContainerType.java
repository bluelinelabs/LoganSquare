package com.bluelinelabs.logansquare.processor.type.container;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.HashMap;
import java.util.TreeMap;

public class TreeMapContainerType extends MapContainerType {

    public TreeMapContainerType(ClassName className) {
        super(className);
    }

    @Override
    public TypeName getTypeName() {
        return ClassName.get(TreeMap.class);
    }

}

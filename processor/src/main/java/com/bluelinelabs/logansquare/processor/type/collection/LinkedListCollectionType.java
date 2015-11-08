package com.bluelinelabs.logansquare.processor.type.collection;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.LinkedList;

public class LinkedListCollectionType extends ListCollectionType {

    public LinkedListCollectionType(ClassName className) {
        super(className);
    }

    @Override
    public TypeName getTypeName() {
        return ClassName.get(LinkedList.class);
    }
}

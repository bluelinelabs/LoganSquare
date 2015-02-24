package com.bluelinelabs.logansquare.processor.type.container;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayDeque;
import java.util.Queue;

public class ArrayDequeCollectionType extends SingleParameterCollectionType {

    @Override
    public TypeName getTypeName() {
        return ClassName.get(ArrayDeque.class);
    }

    @Override
    public Class getGenericClass() {
        return Queue.class;
    }
}

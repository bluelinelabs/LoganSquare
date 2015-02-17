package com.bluelinelabs.logansquare.processor.collectiontype;

import java.util.ArrayDeque;
import java.util.Queue;

public class ArrayDequeCollectionType extends SingleParameterCollectionType {

    @Override
    public Class getGenericClass() {
        return Queue.class;
    }

    @Override
    public Class getConcreteClass() {
        return ArrayDeque.class;
    }
}

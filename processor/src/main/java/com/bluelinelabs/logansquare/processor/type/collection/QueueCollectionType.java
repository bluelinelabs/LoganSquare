package com.bluelinelabs.logansquare.processor.type.collection;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayDeque;
import java.util.Queue;

public class QueueCollectionType extends SingleParameterCollectionType {

    private final ClassName mClassName;

    public QueueCollectionType(ClassName className) {
        mClassName = className;
    }

    @Override
    public TypeName getTypeName() {
        return ClassName.get(ArrayDeque.class);
    }

    @Override
    public String getParameterizedTypeString() {
        return "$T<" + parameterTypes.get(0).getParameterizedTypeString() + ">";
    }

    @Override
    public Object[] getParameterizedTypeStringArgs() {
        return expandStringArgs(mClassName, parameterTypes.get(0).getParameterizedTypeStringArgs());
    }

    @Override
    public Class getGenericClass() {
        return Queue.class;
    }
}

package com.bluelinelabs.logansquare.processor.type.container;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayDeque;
import java.util.Queue;

public class QueueContainerType extends SingleParameterCollectionType {

    private final ClassName mClassName;

    public QueueContainerType(ClassName className) {
        mClassName = className;
    }

    @Override
    public TypeName getTypeName() {
        return ClassName.get(ArrayDeque.class);
    }

    @Override
    public String getParameterizedTypeString() {
        return "$T<" + subType.getParameterizedTypeString() + ">";
    }

    @Override
    public Object[] getParameterizedTypeStringArgs() {
        return expandStringArgs(mClassName, subType.getParameterizedTypeStringArgs());
    }

    @Override
    public Class getGenericClass() {
        return Queue.class;
    }
}

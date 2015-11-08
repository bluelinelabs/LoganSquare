package com.bluelinelabs.logansquare.processor.type.collection;

import com.squareup.javapoet.ClassName;

import java.util.List;

public abstract class ListCollectionType extends SingleParameterCollectionType {

    private final ClassName mClassName;

    public ListCollectionType(ClassName className) {
        mClassName = className;
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
        return List.class;
    }
}
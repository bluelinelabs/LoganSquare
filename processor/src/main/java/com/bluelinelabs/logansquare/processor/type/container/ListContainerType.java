package com.bluelinelabs.logansquare.processor.type.container;

import com.squareup.javapoet.ClassName;

import java.util.List;

public abstract class ListContainerType extends SingleParameterCollectionType {

    private final ClassName mClassName;

    public ListContainerType(ClassName className) {
        mClassName = className;
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
        return List.class;
    }
}
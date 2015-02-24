package com.bluelinelabs.logansquare.processor.type.container;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ListContainerType extends SingleParameterCollectionType {

    private final ClassName mClassName;

    public ListContainerType(ClassName className) {
        mClassName = className;
    }

    @Override
    public TypeName getTypeName() {
        return ClassName.get(ArrayList.class);
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

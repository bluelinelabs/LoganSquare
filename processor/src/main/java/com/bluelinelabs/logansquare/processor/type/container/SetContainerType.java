package com.bluelinelabs.logansquare.processor.type.container;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class SetContainerType extends SingleParameterCollectionType {

    private final ClassName mClassName;

    public SetContainerType(ClassName className) {
        mClassName = className;
    }

    @Override
    public TypeName getTypeName() {
        return ClassName.get(HashSet.class);
    }

    @Override
    public String getParameterizedTypeString() {
        return "$T<" + subType.getParameterizedTypeString() + ">";
    }

    @Override
    public Object[] getParameterizedTypeStringArgs() {
        return expandStringArgs(Set.class, subType.getParameterizedTypeStringArgs());
    }

    @Override
    public Class getGenericClass() {
        return Set.class;
    }

}

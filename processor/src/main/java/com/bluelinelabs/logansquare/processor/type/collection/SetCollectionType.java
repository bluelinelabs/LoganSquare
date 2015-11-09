package com.bluelinelabs.logansquare.processor.type.collection;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.HashSet;
import java.util.Set;

public class SetCollectionType extends SingleParameterCollectionType {

    private final ClassName mClassName;

    public SetCollectionType(ClassName className) {
        mClassName = className;
    }

    @Override
    public TypeName getTypeName() {
        return ClassName.get(HashSet.class);
    }

    @Override
    public String getParameterizedTypeString() {
        return "$T<" + parameterTypes.get(0).getParameterizedTypeString() + ">";
    }

    @Override
    public Object[] getParameterizedTypeStringArgs() {
        return expandStringArgs(Set.class, parameterTypes.get(0).getParameterizedTypeStringArgs());
    }

    @Override
    public Class getGenericClass() {
        return Set.class;
    }

}

package com.bluelinelabs.logansquare.processor.type.container;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.HashSet;
import java.util.Set;

public class HashSetCollectionType extends SingleParameterCollectionType {

    @Override
    public TypeName getTypeName() {
        return ClassName.get(HashSet.class);
    }

    @Override
    public Class getGenericClass() {
        return Set.class;
    }

}

package com.bluelinelabs.logansquare.processor.type.container;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

public class ArrayListCollectionType extends SingleParameterCollectionType {

    @Override
    public TypeName getTypeName() {
        return ClassName.get(ArrayList.class);
    }

    @Override
    public Class getGenericClass() {
        return List.class;
    }
}

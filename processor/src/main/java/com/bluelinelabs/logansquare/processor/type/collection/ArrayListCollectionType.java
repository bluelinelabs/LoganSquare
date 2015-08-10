package com.bluelinelabs.logansquare.processor.type.collection;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;

public class ArrayListCollectionType extends ListCollectionType {

    public ArrayListCollectionType(ClassName className) {
        super(className);
    }

    @Override
    public TypeName getTypeName() {
        return ClassName.get(ArrayList.class);
    }

}

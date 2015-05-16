package com.bluelinelabs.logansquare.processor.type.container;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ArrayListContainerType extends ListContainerType {

    public ArrayListContainerType(ClassName className) {
        super(className);
    }

    @Override
    public TypeName getTypeName() {
        return ClassName.get(ArrayList.class);
    }

}

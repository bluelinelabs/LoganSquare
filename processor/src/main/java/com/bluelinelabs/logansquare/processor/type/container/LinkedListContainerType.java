package com.bluelinelabs.logansquare.processor.type.container;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LinkedListContainerType extends ListContainerType {

    public LinkedListContainerType(ClassName className) {
        super(className);
    }

    @Override
    public TypeName getTypeName() {
        return ClassName.get(LinkedList.class);
    }
}

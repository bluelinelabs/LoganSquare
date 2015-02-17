package com.bluelinelabs.logansquare.processor.collectiontype;

import java.util.ArrayList;
import java.util.List;

public class ArrayListCollectionType extends SingleParameterCollectionType {

    @Override
    public Class getGenericClass() {
        return List.class;
    }

    @Override
    public Class getConcreteClass() {
        return ArrayList.class;
    }
}

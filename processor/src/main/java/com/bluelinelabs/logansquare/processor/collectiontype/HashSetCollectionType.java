package com.bluelinelabs.logansquare.processor.collectiontype;

import java.util.HashSet;
import java.util.Set;

public class HashSetCollectionType extends SingleParameterCollectionType {

    @Override
    public Class getGenericClass() {
        return Set.class;
    }

    @Override
    public Class getConcreteClass() {
        return HashSet.class;
    }
}

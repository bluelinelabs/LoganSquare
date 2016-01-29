package com.bluelinelabs.logansquare.processor.type.collection;

import com.bluelinelabs.logansquare.processor.TypeUtils;
import com.bluelinelabs.logansquare.processor.type.Type;
import com.squareup.javapoet.ClassName;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public abstract class CollectionType extends Type {

    public static CollectionType collectionTypeFor(TypeMirror typeMirror, TypeMirror genericClassTypeMirror, Elements elements, Types types) {
        CollectionType collectionType = null;
        
        String typeMirrorInfo = genericClassTypeMirror.toString().replaceAll("<.*>", "");
        
        switch (typeMirrorInfo) {
            case "java.util.List":
            case "java.util.ArrayList":
                collectionType = new ArrayListCollectionType(ClassName.bestGuess(typeMirrorInfo));
                break;
            case "java.util.LinkedList":
                collectionType = new LinkedListCollectionType(ClassName.bestGuess(typeMirrorInfo));
                break;
            case "java.util.Map":
            case "java.util.HashMap":
                collectionType = new HashMapCollectionType(ClassName.bestGuess(typeMirrorInfo));
                break;
            case "java.util.TreeMap":
                collectionType = new TreeMapCollectionType(ClassName.bestGuess(typeMirrorInfo));
                break;
            case "java.util.LinkedHashMap":
                collectionType = new LinkedHashMapCollectionType(ClassName.bestGuess(typeMirrorInfo));
                break;
            case "java.util.Set":
            case "java.util.HashSet":
                collectionType = new SetCollectionType(ClassName.bestGuess(typeMirrorInfo));
                break;
            case "java.util.Queue":
            case "java.util.Deque":
            case "java.util.ArrayDeque":
                collectionType = new QueueCollectionType(ClassName.bestGuess(typeMirrorInfo));
                break;
        }

        if (collectionType != null) {
            collectionType.addParameterTypes(TypeUtils.getParameterizedTypes(typeMirror), elements, types);
        }

        return collectionType;
    }

}

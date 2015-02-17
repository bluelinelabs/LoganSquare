package com.bluelinelabs.logansquare.processor.collectiontype;

import com.bluelinelabs.logansquare.processor.JsonFieldHolder;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.type.TypeMirror;

public abstract class CollectionType {

    public abstract void parse(MethodSpec.Builder builder, String objectName, String variableName, JsonFieldHolder fieldHolder);
    public abstract void serialize(MethodSpec.Builder builder, JsonFieldHolder fieldHolder, String variableName);

    public static CollectionType typeFor(TypeMirror typeMirror) {
        String typeString = typeMirror.toString();
        switch (typeString) {
            case "java.util.List":
            case "java.util.ArrayList":
                return new ArrayListCollectionType();
            case "java.util.Map":
            case "java.util.HashMap":
                return new HashMapCollectionType();
            case "java.util.Set":
            case "java.util.HashSet":
                return new HashSetCollectionType();
            case "java.util.Queue":
            case "java.util.Deque":
            case "java.util.ArrayDeque":
                return new ArrayDequeCollectionType();
            default:
                return new NullCollectionType();
        }
    }
}

package com.bluelinelabs.logansquare.processor.type.container;

import com.bluelinelabs.logansquare.processor.TypeUtils;
import com.bluelinelabs.logansquare.processor.type.Type;
import com.squareup.javapoet.ClassName;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public abstract class ContainerType extends Type {

    public Type subType;

    public static ContainerType containerTypeFor(TypeMirror typeMirror, TypeMirror typeConverterType, TypeMirror genericClassTypeMirror, Elements elements, Types types) {
        ContainerType containerType = null;
        switch (genericClassTypeMirror.toString()) {
            case "java.util.List":
            case "java.util.ArrayList":
                containerType = new ArrayListContainerType(ClassName.bestGuess(genericClassTypeMirror.toString()));
                break;
            case "java.util.LinkedList":
                containerType = new LinkedListContainerType(ClassName.bestGuess(genericClassTypeMirror.toString()));
                break;
            case "java.util.Map":
            case "java.util.HashMap":
                containerType = new HashMapContainerType(ClassName.bestGuess(genericClassTypeMirror.toString()));
                break;
            case "java.util.TreeMap":
                containerType = new TreeMapContainerType(ClassName.bestGuess(genericClassTypeMirror.toString()));
                break;
            case "java.util.LinkedHashMap":
                containerType = new LinkedHashMapContainerType(ClassName.bestGuess(genericClassTypeMirror.toString()));
                break;
            case "java.util.Set":
            case "java.util.HashSet":
                containerType = new SetContainerType(ClassName.bestGuess(genericClassTypeMirror.toString()));
                break;
            case "java.util.Queue":
            case "java.util.Deque":
            case "java.util.ArrayDeque":
                containerType = new QueueContainerType(ClassName.bestGuess(genericClassTypeMirror.toString()));
                break;
        }

        if (containerType == null) {
            if (typeMirror instanceof ArrayType) {
                typeMirror = ((ArrayType)typeMirror).getComponentType();
                containerType = new ArrayCollectionType();
            }
        } else {
            typeMirror = TypeUtils.getTypeFromCollection(typeMirror);
        }

        if (containerType != null) {
            containerType.subType = Type.typeFor(typeMirror, typeConverterType, elements, types);
        }

        return containerType;
    }
}

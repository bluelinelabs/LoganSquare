package com.bluelinelabs.logansquare.processor.type;

import com.bluelinelabs.logansquare.processor.type.container.ContainerType;
import com.bluelinelabs.logansquare.processor.type.field.FieldType;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Type {

    public abstract TypeName getTypeName();
    public abstract String getParameterizedTypeString();
    public abstract Object[] getParameterizedTypeStringArgs();
    public abstract void parse(MethodSpec.Builder builder, int depth, String setter, Object... setterFormatArgs);
    public abstract void serialize(MethodSpec.Builder builder, int depth, String fieldName, String getter, boolean isObjectProperty, boolean checkIfNull, boolean writeIfNull, boolean writeCollectionElementIfNull);

    public static Type typeFor(TypeMirror typeMirror, TypeMirror typeConverterType, Elements elements, Types types) {
        TypeMirror genericClassTypeMirror = types.erasure(typeMirror);

        boolean isCollection = !genericClassTypeMirror.toString().equals(typeMirror.toString()) || (typeMirror instanceof ArrayType);

        if (isCollection) {
            return ContainerType.containerTypeFor(typeMirror, typeConverterType, genericClassTypeMirror, elements, types);
        } else {
            return FieldType.fieldTypeFor(typeMirror, typeConverterType, elements, types);
        }
    }

    protected Object[] expandStringArgs(Object... args) {
        List<Object> argList = new ArrayList<>();
        for (Object arg : args) {
            if (arg instanceof Object[]) {
                Collections.addAll(argList, (Object[])arg);
            } else {
                argList.add(arg);
            }
        }

        return argList.toArray(new Object[argList.size()]);
    }
}

package com.bluelinelabs.logansquare.processor.type;

import com.bluelinelabs.logansquare.processor.type.collection.ArrayCollectionType;
import com.bluelinelabs.logansquare.processor.type.collection.CollectionType;
import com.bluelinelabs.logansquare.processor.type.field.FieldType;
import com.bluelinelabs.logansquare.processor.type.field.ParameterizedTypeField;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public abstract class Type {

    public final List<Type> parameterTypes;

    public abstract TypeName getTypeName();
    public abstract String getParameterizedTypeString();
    public abstract Object[] getParameterizedTypeStringArgs();
    public abstract void parse(MethodSpec.Builder builder, int depth, String setter, Object... setterFormatArgs);
    public abstract void serialize(MethodSpec.Builder builder, int depth, String fieldName, List<String> processedFieldNames, String getter, boolean isObjectProperty, boolean checkIfNull, boolean writeIfNull, boolean writeCollectionElementIfNull);

    public Type() {
        parameterTypes = new ArrayList<>();
    }

    public static Type typeFor(TypeMirror typeMirror, TypeMirror typeConverterType, ProcessingEnvironment env, Elements elements, Types types) {
        TypeMirror genericClassTypeMirror = types.erasure(typeMirror);
        boolean hasTypeConverter = typeConverterType != null && !typeConverterType.toString().equals("void");

        Type type;
        if (!hasTypeConverter && typeMirror instanceof ArrayType) {
            TypeMirror arrayTypeMirror = ((ArrayType)typeMirror).getComponentType();
            type = new ArrayCollectionType(Type.typeFor(arrayTypeMirror, null, env, elements, types));
        } else if (!hasTypeConverter && !genericClassTypeMirror.toString().equals(typeMirror.toString())) {
            type = CollectionType.collectionTypeFor(typeMirror, genericClassTypeMirror, env, elements, types);

            if (type == null) {
                if (typeMirror.toString().contains("?")) {
                    throw new RuntimeException("Generic types with wildcards are currently not supported by LoganSquare.");
                }
                try {
                    type = new ParameterizedTypeField(TypeName.get(typeMirror));
                } catch (Exception ignored) { }
            }
        } else {
            type = FieldType.fieldTypeFor(typeMirror, typeConverterType, env, elements, types);
        }

        return type;
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

    public void addParameterTypes(List<TypeMirror> parameterTypes, ProcessingEnvironment env, Elements elements, Types types) {
        for (TypeMirror typeMirror : parameterTypes) {
            addParameterType(typeMirror, env, elements, types);
        }
    }

    public void addParameterType(TypeMirror parameterType, ProcessingEnvironment env, Elements elements, Types types) {
        parameterTypes.add(Type.typeFor(parameterType, null, env, elements, types));
    }
}

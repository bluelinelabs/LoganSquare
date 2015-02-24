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

public abstract class Type {

    public abstract TypeName getTypeName();
    public abstract void parse(MethodSpec.Builder builder, int depth, String setter, Object... setterFormatArgs);
    public abstract void serialize(MethodSpec.Builder builder, int depth, String fieldName, String getter, boolean writeFieldName);

    public static Type typeFor(TypeMirror typeMirror, TypeMirror typeConverterType, Elements elements, Types types) {
        TypeMirror genericClassTypeMirror = types.erasure(typeMirror);

        boolean hasTypeConverter = typeConverterType != null && !typeConverterType.toString().equals("void");
        boolean isCollection = !genericClassTypeMirror.toString().equals(typeMirror.toString()) || (typeMirror instanceof ArrayType);

        if (!hasTypeConverter && isCollection) {
            return ContainerType.containerTypeFor(typeMirror, genericClassTypeMirror, elements, types);
        } else {
            return FieldType.fieldTypeFor(typeMirror, typeConverterType, elements, types);
        }
    }

    protected Object[] addStringArgs(Object[] initialArgs, Object... newArgs) {
        Object[] args = new Object[initialArgs.length + newArgs.length];
        System.arraycopy(initialArgs, 0, args, 0, initialArgs.length);
        System.arraycopy(newArgs, 0, args, initialArgs.length, newArgs.length);
        return args;
    }
}

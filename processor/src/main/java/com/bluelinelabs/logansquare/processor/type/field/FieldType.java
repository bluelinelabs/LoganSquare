package com.bluelinelabs.logansquare.processor.type.field;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.processor.type.Type;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.lang.annotation.Annotation;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public abstract class FieldType extends Type {

    public abstract TypeName getNonPrimitiveTypeName();

    @Override
    public String getParameterizedTypeString() {
        return "$T";
    }

    @Override
    public Object[] getParameterizedTypeStringArgs() {
        return new Object[] { getNonPrimitiveTypeName() };
    }

    public static FieldType fieldTypeFor(TypeMirror typeMirror, TypeMirror typeConverterType, ProcessingEnvironment env, Elements elements, Types types) {
        if (typeMirror != null) {
            if (typeConverterType != null && !"void".equals(typeConverterType.toString())) {
                return new TypeConverterFieldType(TypeName.get(typeMirror), ClassName.bestGuess(typeConverterType.toString()));
            } else if (typeMirror.getKind() == TypeKind.BOOLEAN) {
                return new BooleanFieldType(true);
            } else if (Boolean.class.getCanonicalName().equals(typeMirror.toString())) {
                return new BooleanFieldType(false);
            } else if (typeMirror.getKind() == TypeKind.BYTE) {
                return new ByteFieldType(true);
            } else if (Byte.class.getCanonicalName().equals(typeMirror.toString())) {
                return new ByteFieldType(false);
            } else if (typeMirror.getKind() == TypeKind.INT) {
                return new IntegerFieldType(true);
            } else if (Integer.class.getCanonicalName().equals(typeMirror.toString())) {
                return new IntegerFieldType(false);
            } else if (typeMirror.getKind() == TypeKind.LONG) {
                return new LongFieldType(true);
            } else if (Long.class.getCanonicalName().equals(typeMirror.toString())) {
                return new LongFieldType(false);
            } else if (typeMirror.getKind() == TypeKind.FLOAT) {
                return new FloatFieldType(true);
            } else if (Float.class.getCanonicalName().equals(typeMirror.toString())) {
                return new FloatFieldType(false);
            } else if (typeMirror.getKind() == TypeKind.DOUBLE) {
                return new DoubleFieldType(true);
            } else if (Double.class.getCanonicalName().equals(typeMirror.toString())) {
                return new DoubleFieldType(false);
            } else if (String.class.getCanonicalName().equals(typeMirror.toString())) {
                return new StringFieldType();
            } else if (Object.class.getCanonicalName().equals(typeMirror.toString())) {
                return new UnknownFieldType(env);
            } else if (typeMirror instanceof DeclaredType) {
                Annotation annotation = ((DeclaredType) typeMirror).asElement().getAnnotation(JsonObject.class);
                if (annotation != null) {
                    return new JsonFieldType(env, ClassName.bestGuess(typeMirror.toString()));
                }
            }

            return new DynamicFieldType(TypeName.get(typeMirror));
        } else {
            return null;
        }
    }

    protected static String replaceLastLiteral(String string, String replacement) {
        int pos = string.lastIndexOf("$L");
        if (pos > -1) {
            return string.substring(0, pos)
                    + replacement
                    + string.substring(pos + 2, string.length());
        } else {
            return string;
        }
    }
}

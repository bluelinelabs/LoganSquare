package com.bluelinelabs.logansquare.processor.fieldtype;

import com.bluelinelabs.logansquare.Constants;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.processor.JsonFieldHolder;
import com.bluelinelabs.logansquare.processor.TypeUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;

public abstract class FieldType {

    public abstract String getJsonParserGetter(JsonFieldHolder fieldHolder);
    public abstract void serialize(MethodSpec.Builder builder, JsonFieldHolder fieldHolder, String getter, boolean writeFieldNameForObject);
    public abstract TypeName getTypeName();
    public abstract TypeName getNonPrimitiveTypeName();

    public static FieldType typeFor(TypeMirror typeMirror, TypeMirror typeConverterType, Elements elements, Types types) {
        if (typeMirror != null) {
            if (typeConverterType != null && !"void".equals(typeConverterType.toString())) {
                return new TypeConverterFieldType(TypeName.get(typeMirror), ClassName.bestGuess(typeConverterType.toString()));
            } else if (typeMirror.getKind() == TypeKind.BOOLEAN) {
                return new BooleanFieldType(true);
            } else if ("java.lang.Boolean".equals(typeMirror.toString())) {
                return new BooleanFieldType(false);
            } else if (typeMirror.getKind() == TypeKind.INT) {
                return new IntegerFieldType(true);
            } else if ("java.lang.Integer".equals(typeMirror.toString())) {
                return new IntegerFieldType(false);
            } else if (typeMirror.getKind() == TypeKind.LONG) {
                return new LongFieldType(true);
            } else if ("java.lang.Long".equals(typeMirror.toString())) {
                return new LongFieldType(false);
            } else if (typeMirror.getKind() == TypeKind.FLOAT) {
                return new FloatFieldType(true);
            } else if ("java.lang.Float".equals(typeMirror.toString())) {
                return new FloatFieldType(false);
            } else if (typeMirror.getKind() == TypeKind.DOUBLE) {
                return new DoubleFieldType(true);
            } else if ("java.lang.Double".equals(typeMirror.toString())) {
                return new DoubleFieldType(false);
            } else if ("java.lang.String".equals(typeMirror.toString())) {
                return new StringFieldType();
            } else if (typeMirror instanceof DeclaredType) {
                Annotation annotation = ((DeclaredType) typeMirror).asElement().getAnnotation(JsonObject.class);
                if (annotation != null) {
                    TypeMirror erasedType = types.erasure(typeMirror);
                    DeclaredType declaredType = (DeclaredType) erasedType;
                    TypeElement typeElement = (TypeElement) declaredType.asElement();

                    String packageName = elements.getPackageOf(typeElement).getQualifiedName().toString();
                    ClassName fieldClass = ClassName.bestGuess(typeMirror.toString());
                    ClassName mapperClass = ClassName.get(packageName, TypeUtils.getSimpleClassName(typeElement, packageName) + Constants.MAPPER_CLASS_SUFFIX);

                    return new JsonFieldType(fieldClass, mapperClass);
                }
            }

            return new DynamicFieldType(TypeName.get(typeMirror));
        } else {
            return null;
        }
    }
}

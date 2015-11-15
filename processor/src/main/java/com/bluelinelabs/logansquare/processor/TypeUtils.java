package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.Constants;
import com.squareup.javapoet.ClassName;

import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

public class TypeUtils {

    public static String getSimpleClassName(TypeElement type, String packageName) {
        return type.getQualifiedName().toString().substring(packageName.length() + 1).replace('.', '$');
    }

    public static String getInjectedFQCN(TypeElement type, Elements elements) {
        String packageName = elements.getPackageOf(type).getQualifiedName().toString();
        return packageName + "." + getSimpleClassName(type, packageName) + Constants.MAPPER_CLASS_SUFFIX;
    }

    public static String getInjectedFQCN(ClassName className) {
        StringBuilder name = new StringBuilder();
        for (String part : className.simpleNames()) {
            if (name.length() > 0) {
                name.append("$");
            }
            name.append(part);
        }
        return className.packageName() + "." + name.toString() + Constants.MAPPER_CLASS_SUFFIX;
    }

    @SuppressWarnings("unchecked")
    public static List<TypeMirror> getParameterizedTypes(TypeMirror typeMirror) {
        if (!(typeMirror instanceof DeclaredType)) {
            return null;
        }

        DeclaredType declaredType = (DeclaredType)typeMirror;
        return (List<TypeMirror>)declaredType.getTypeArguments();
    }
}

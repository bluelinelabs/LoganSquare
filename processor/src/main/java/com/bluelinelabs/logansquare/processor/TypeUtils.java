package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.Constants;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import java.util.List;

public class TypeUtils {

    public static String getSimpleClassName(TypeElement type, String packageName) {
        return type.getQualifiedName().toString().substring(packageName.length() + 1).replace('.', '$');
    }

    public static String getInjectedFQCN(TypeElement type, Elements elements) {
        String packageName = elements.getPackageOf(type).getQualifiedName().toString();
        return packageName + "." + getSimpleClassName(type, packageName) + Constants.MAPPER_CLASS_SUFFIX;
    }

    @SuppressWarnings("unchecked")
    public static TypeMirror getTypeFromCollection(TypeMirror typeMirror) {
        if (!(typeMirror instanceof DeclaredType)) {
            return null;
        }

        DeclaredType declaredType = (DeclaredType)typeMirror;
        List<TypeMirror> genericTypes = (List<TypeMirror>)declaredType.getTypeArguments();
        if (genericTypes.size() > 0) {
            String genericClassName = declaredType.toString().substring(0, declaredType.toString().indexOf('<'));

            switch (genericClassName) {
                case "java.util.List":
                case "java.util.ArrayList":
                case "java.util.Set":
                case "java.util.HashSet":
                case "java.util.Deque":
                case "java.util.Queue":
                case "java.util.ArrayDeque":
                    return genericTypes.get(0);
                case "java.util.Map":
                case "java.util.HashMap":
                    if (!"java.lang.String".equals(genericTypes.get(0).toString())) {
                        throw new IllegalStateException("JsonField Map collections must use Strings as keys");
                    }
                    return genericTypes.get(1);
            }
        }

        return null;
    }
}

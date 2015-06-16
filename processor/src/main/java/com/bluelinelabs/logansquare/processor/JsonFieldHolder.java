package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.processor.type.Type;
import com.bluelinelabs.logansquare.processor.type.container.ContainerType;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.List;

public class JsonFieldHolder {

    public String[] fieldName;
    public String setterMethod;
    public String getterMethod;
    public boolean shouldParse;
    public boolean shouldSerialize;
    public Type type;
    public Type subType;

    public String fill(Element element, Elements elements, Types types, String[] fieldNames, TypeMirror typeConverterType, JsonObjectHolder objectHolder, boolean shouldParse, boolean shouldSerialize) {
        if (fieldNames == null || fieldNames.length == 0) {
            String defaultFieldName = element.getSimpleName().toString();

            switch (objectHolder.fieldNamingPolicy) {
                case LOWER_CASE_WITH_UNDERSCORES:
                    defaultFieldName = TextUtils.toLowerCaseWithUnderscores(defaultFieldName);
                    break;
            }

            fieldNames = new String[] { defaultFieldName };
        }
        fieldName = fieldNames;

        this.shouldParse = shouldParse;
        this.shouldSerialize = shouldSerialize;

        setterMethod = getSetter(element, elements);
        getterMethod = getGetter(element, elements);

        type = Type.typeFor(element.asType(), typeConverterType, elements, types);
        subType = type;

        if (type instanceof ContainerType) {
            subType = ((ContainerType) type).subType;
        }

        Type typeToCheck = type;
        boolean hasSubtypes = true;
        do {
            if (typeToCheck == null) {
                return "Type could not be determined for " + element.toString();
            } else {
                if (typeToCheck instanceof ContainerType) {
                    typeToCheck = ((ContainerType)typeToCheck).subType;
                } else {
                    hasSubtypes = false;
                }
            }
        } while (hasSubtypes);

        return null;
    }

    public static String getGetter(Element element, Elements elements) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        TypeKind elementTypeKind = element.asType().getKind();

        String elementName = element.getSimpleName().toString();
        String elementNameLowerCase = elementName.toLowerCase();

        List<String> possibleMethodNames = new ArrayList<>();
        possibleMethodNames.add("get" + elementNameLowerCase);
        if (elementTypeKind == TypeKind.BOOLEAN) {
            possibleMethodNames.add("is" + elementNameLowerCase);
        }

        // Handle the case where variables are named in the form mVariableName instead of just variableName
        if (elementName.length() > 1 && elementName.charAt(0) == 'm' && (elementName.charAt(1) >= 'A' && elementName.charAt(1) <= 'Z')) {
            possibleMethodNames.add("get" + elementNameLowerCase.substring(1));
            if (elementTypeKind == TypeKind.BOOLEAN) {
                possibleMethodNames.add("is" + elementNameLowerCase.substring(1));
            }
        }

        List<? extends Element> elementMembers = elements.getAllMembers(enclosingElement);
        List<ExecutableElement> elementMethods = ElementFilter.methodsIn(elementMembers);
        for (ExecutableElement methodElement : elementMethods) {
            if (methodElement.getParameters().size() == 0) {
                String methodNameString = methodElement.getSimpleName().toString();
                String methodNameLowerCase = methodNameString.toLowerCase();

                if (possibleMethodNames.contains(methodNameLowerCase)) {
                    if (methodElement.getParameters().size() == 0) {
                        if (methodElement.getReturnType().toString().equals(element.asType().toString())) {
                            return methodNameString;
                        }
                    }
                }
            }
        }

        return null;
    }

    public static String getSetter(Element element, Elements elements) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        String elementName = element.getSimpleName().toString();
        String elementNameLowerCase = elementName.toLowerCase();

        List<String> possibleMethodNames = new ArrayList<>();
        possibleMethodNames.add("set" + elementNameLowerCase);

        // Handle the case where variables are named in the form mVariableName instead of just variableName
        if (elementName.length() > 1 && elementName.charAt(0) == 'm' && (elementName.charAt(1) >= 'A' && elementName.charAt(1) <= 'Z')) {
            possibleMethodNames.add("set" + elementNameLowerCase.substring(1));
        }

        List<? extends Element> elementMembers = elements.getAllMembers(enclosingElement);
        List<ExecutableElement> elementMethods = ElementFilter.methodsIn(elementMembers);
        for (ExecutableElement methodElement : elementMethods) {
            String methodNameString = methodElement.getSimpleName().toString();
            String methodNameLowerCase = methodNameString.toLowerCase();

            if (possibleMethodNames.contains(methodNameLowerCase)) {
                if (methodElement.getParameters().size() == 1) {
                    if (methodElement.getParameters().get(0).asType().toString().equals(element.asType().toString())) {
                        return methodNameString;
                    }
                }
            }
        }

        return null;
    }

    public boolean hasSetter() {
        return !TextUtils.isEmpty(setterMethod);
    }

    public boolean hasGetter() {
        return !TextUtils.isEmpty(getterMethod);
    }
}

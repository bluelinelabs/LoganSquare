package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.processor.collectiontype.ArrayCollectionType;
import com.bluelinelabs.logansquare.processor.collectiontype.CollectionType;
import com.bluelinelabs.logansquare.processor.collectiontype.NullCollectionType;
import com.bluelinelabs.logansquare.processor.fieldtype.FieldType;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.MirroredTypeException;
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
    public CollectionType collectionType;
    public FieldType fieldType;

    public String fill(Element element, Elements elements, Types types, String[] fieldNames, TypeMirror typeConverterType, JsonObjectHolder objectHolder) {
        TypeElement enclosingElement = (TypeElement)element.getEnclosingElement();

        TypeMirror fieldTypeMirror;
        TypeMirror genericClassTypeMirror;

        fieldTypeMirror = element.asType();
        genericClassTypeMirror = types.erasure(fieldTypeMirror);

        collectionType = CollectionType.typeFor(genericClassTypeMirror);
        if (!(collectionType instanceof NullCollectionType)) {
            fieldTypeMirror = TypeUtils.getTypeFromCollection(fieldTypeMirror);
        } else if (fieldTypeMirror instanceof ArrayType) {
            fieldTypeMirror = ((ArrayType)fieldTypeMirror).getComponentType();
            collectionType = new ArrayCollectionType();
        }

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

        setterMethod = getSetter(element, elements);
        getterMethod = getGetter(element, elements);

        fieldType = FieldType.typeFor(fieldTypeMirror, typeConverterType, elements, types);
        if (fieldType == null) {
            if (!hasGetter() || !hasSetter()) {
                return enclosingElement + ": unsupported classes must have a type converter specified";
            }
        }

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

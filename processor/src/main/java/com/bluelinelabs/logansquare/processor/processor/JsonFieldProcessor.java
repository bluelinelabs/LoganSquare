package com.bluelinelabs.logansquare.processor.processor;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.typeconverters.TypeConverter;
import com.bluelinelabs.logansquare.processor.JsonFieldHolder;
import com.bluelinelabs.logansquare.processor.JsonObjectHolder;
import com.bluelinelabs.logansquare.processor.TextUtils;
import com.bluelinelabs.logansquare.processor.TypeUtils;
import com.bluelinelabs.logansquare.processor.collectiontype.CollectionType;
import com.bluelinelabs.logansquare.processor.collectiontype.NullCollectionType;
import com.bluelinelabs.logansquare.processor.fieldtype.FieldType;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import static javax.lang.model.element.Modifier.PRIVATE;

public class JsonFieldProcessor extends Processor {

    public JsonFieldProcessor(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    @Override
    public Class getAnnotation() {
        return JsonField.class;
    }

    @Override
    public void findAndParseObjects(RoundEnvironment env, Map<String, JsonObjectHolder> jsonObjectMap, Elements elements, Types types) {
        for (Element element : env.getElementsAnnotatedWith(JsonField.class)) {
            try {
                processJsonFieldAnnotation(element, jsonObjectMap, elements, types);
            } catch (Exception e) {
                StringWriter stackTrace = new StringWriter();
                e.printStackTrace(new PrintWriter(stackTrace));

                error(element, "Unable to generate injector for %s. Stack trace incoming:\n%s", JsonField.class, stackTrace.toString());
            }
        }
    }

    private void processJsonFieldAnnotation(Element element, Map<String, JsonObjectHolder> jsonObjectMap, Elements elements, Types types) {
        if (!isJsonFieldFieldAnnotationValid(element, elements)) {
            return;
        }

        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        JsonObjectHolder objectHolder = jsonObjectMap.get(TypeUtils.getInjectedFQCN(enclosingElement, elements));
        JsonFieldHolder fieldHolder = objectHolder.fieldMap.get(element.getSimpleName().toString());

        if (fieldHolder == null) {
            fieldHolder = new JsonFieldHolder();
            objectHolder.fieldMap.put(element.getSimpleName().toString(), fieldHolder);
        }

        JsonField annotation = element.getAnnotation(JsonField.class);

        TypeMirror fieldTypeMirror;
        TypeMirror genericClassTypeMirror;

        TypeMirror typeConverterType;
        try {
            typeConverterType = mProcessingEnv.getElementUtils().getTypeElement(annotation.typeConverter().getCanonicalName()).asType();
        } catch (MirroredTypeException mte) {
            typeConverterType = mte.getTypeMirror();
        }
        if (!isTypeConverterClassValid(typeConverterType, elements, types)) {
            return;
        }

        fieldTypeMirror = element.asType();
        genericClassTypeMirror = types.erasure(fieldTypeMirror);

        if (!(CollectionType.typeFor(genericClassTypeMirror) instanceof NullCollectionType)) {
            fieldTypeMirror = TypeUtils.getTypeFromCollection(fieldTypeMirror);
        }

        String[] fieldName = annotation.name();
        if (fieldName.length == 0) {
            fieldName = new String[] { element.getSimpleName().toString() };
        }

        fieldHolder.fieldName = fieldName;
        fieldHolder.setterMethod = getSetter(element, elements);
        fieldHolder.getterMethod = getGetter(element, elements);
        fieldHolder.collectionType = CollectionType.typeFor(genericClassTypeMirror);
        fieldHolder.fieldType = FieldType.typeFor(fieldTypeMirror, typeConverterType, elements, types);

        if (fieldHolder.fieldType == null) {
            if (!fieldHolder.hasGetter() || !fieldHolder.hasSetter()) {
                error(element, "%s: unsupported classes must have a type converter specified", enclosingElement);
            }
        }
    }

    private boolean isJsonFieldFieldAnnotationValid(Element element, Elements elements) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        Annotation objectAnnotation = enclosingElement.getAnnotation(JsonObject.class);
        if (objectAnnotation == null) {
            error(enclosingElement, "%s: %s fields can only be in classes annotated with %s.", enclosingElement.getQualifiedName(), JsonField.class.getSimpleName(), JsonObject.class.getSimpleName());
            return false;
        }

        if (element.getModifiers().contains(PRIVATE) && (TextUtils.isEmpty(getGetter(element, elements)) || TextUtils.isEmpty(getSetter(element, elements)))) {
            error(element, "%s annotation can only be used on private fields if both getter and setter are present.", JsonField.class.getSimpleName());
            return false;
        }

        return true;
    }

    private boolean isTypeConverterClassValid(TypeMirror typeConverterClassMirror, Elements elements, Types types) {
        TypeElement typeConverterElement = elements.getTypeElement(typeConverterClassMirror.toString());

        if (typeConverterElement != null) {
            boolean isTypeConverterType = false;
            TypeElement element = typeConverterElement;

            while (!isTypeConverterType && element != null) {
                for (TypeMirror iface : element.getInterfaces()) {
                    if (iface.toString().equals(TypeConverter.class.getCanonicalName() + "<T>")) {
                        isTypeConverterType = true;
                    }
                }

                TypeMirror superClassMirror = element.getSuperclass();
                if (superClassMirror != null) {
                    superClassMirror = types.erasure(superClassMirror);

                    element = elements.getTypeElement(superClassMirror.toString());
                } else {
                    element = null;
                }
            }

            if (!isTypeConverterType) {
                error(element, "typeConverter elements must implement the TypeConverter interface or extend from one of the convenience helpers (ie StringBasedTypeConverter or DateTypeConverter).");
                return false;
            }

            boolean constructorIsDeclared = false;
            boolean hasAccessibleConstructor = false;
            List<? extends Element> enclosedElements = typeConverterElement.getEnclosedElements();
            for (Element enclosedElement : enclosedElements) {
                ElementKind enclosedElementKind = enclosedElement.getKind();
                if (enclosedElementKind == ElementKind.CONSTRUCTOR) {
                    constructorIsDeclared = true;
                    if (!enclosedElement.getModifiers().contains(Modifier.PRIVATE)) {
                        ExecutableElement executableElement = (ExecutableElement)enclosedElement;

                        if (executableElement.getParameters().size() == 0) {
                            hasAccessibleConstructor = true;
                        }
                    }
                }
            }

            if (constructorIsDeclared && !hasAccessibleConstructor) {
                error(element, "TypeConverter classes must have a non-private zero argument constructor.");
                return false;
            }
        }

        return true;
    }

    private String getGetter(Element element, Elements elements) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        String elementNameLowerCase = element.getSimpleName().toString().toLowerCase();
        String expectedMethodName = "get" + elementNameLowerCase;
        String expectedBooleanMethodName = "is" + elementNameLowerCase;

        TypeKind elementTypeKind = element.asType().getKind();

        List<? extends Element> elementMembers = elements.getAllMembers(enclosingElement);
        List<ExecutableElement> elementMethods = ElementFilter.methodsIn(elementMembers);
        for (ExecutableElement methodElement : elementMethods) {
            if (methodElement.getParameters().size() == 0) {
                String methodNameString = methodElement.getSimpleName().toString();
                String methodNameLowerCase = methodNameString.toLowerCase();

                if (methodNameLowerCase.equals(expectedMethodName)) {
                    return methodNameString;
                }
                if (elementTypeKind == TypeKind.BOOLEAN && methodNameLowerCase.equals(expectedBooleanMethodName)) {
                    return methodNameString;
                }
            }
        }

        return null;
    }

    private String getSetter(Element element, Elements elements) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        String expectedMethodName = "set" + element.getSimpleName().toString().toLowerCase();

        List<? extends Element> elementMembers = elements.getAllMembers(enclosingElement);
        List<ExecutableElement> elementMethods = ElementFilter.methodsIn(elementMembers);
        for (ExecutableElement methodElement : elementMethods) {
            if (methodElement.getParameters().size() == 1 && methodElement.getSimpleName().toString().toLowerCase().equals(expectedMethodName)) {
                if (methodElement.getParameters().get(0).getSimpleName().toString().equals(element.getSimpleName().toString())) {
                    return methodElement.getSimpleName().toString();
                }
            }
        }

        return null;
    }
}

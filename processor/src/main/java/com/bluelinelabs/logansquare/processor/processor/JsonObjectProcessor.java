package com.bluelinelabs.logansquare.processor.processor;

import com.bluelinelabs.logansquare.Constants;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.processor.JsonObjectHolder;
import com.bluelinelabs.logansquare.processor.TypeUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.PRIVATE;

public class JsonObjectProcessor extends Processor {

    public JsonObjectProcessor(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    @Override
    public Class getAnnotation() {
        return JsonObject.class;
    }

    @Override
    public void findAndParseObjects(RoundEnvironment env, Map<String, JsonObjectHolder> jsonObjectMap, Elements elements, Types types) {
        for (Element element : env.getElementsAnnotatedWith(JsonObject.class)) {
            try {
                processJsonObjectAnnotation(element, jsonObjectMap, elements, types);
            } catch (Exception e) {
                StringWriter stackTrace = new StringWriter();
                e.printStackTrace(new PrintWriter(stackTrace));

                error(element, "Unable to generate injector for %s. Stack trace incoming:\n%s", JsonObject.class, stackTrace.toString());
            }
        }
    }

    private void processJsonObjectAnnotation(Element element, Map<String, JsonObjectHolder> jsonObjectMap, Elements elements, Types types) {
        TypeElement typeElement = (TypeElement) element;

        if (element.getModifiers().contains(PRIVATE)) {
            error(element, "%s: %s annotation can't be used on private classes.", typeElement.getQualifiedName(), JsonObject.class.getSimpleName());
            return;
        }

        JsonObjectHolder holder = jsonObjectMap.get(TypeUtils.getInjectedFQCN(typeElement, elements));
        if (holder == null) {
            String packageName = elements.getPackageOf(typeElement).getQualifiedName().toString();
            String objectClassName = TypeUtils.getSimpleClassName(typeElement, packageName);
            String injectedSimpleClassName = objectClassName + Constants.MAPPER_CLASS_SUFFIX;
            boolean abstractClass = element.getModifiers().contains(ABSTRACT);
            TypeName parentInjectedClassName = null;

            TypeMirror superclass = typeElement.getSuperclass();
            while (superclass.getKind() != TypeKind.NONE) {
                TypeElement superclassElement = (TypeElement)types.asElement(superclass);

                if (superclassElement.getAnnotation(JsonObject.class) != null) {
                    String superclassPackageName = elements.getPackageOf(superclassElement).getQualifiedName().toString();
                    parentInjectedClassName = ClassName.get(superclassPackageName, TypeUtils.getSimpleClassName(superclassElement, superclassPackageName) + Constants.MAPPER_CLASS_SUFFIX);
                    break;
                }

                superclass = superclassElement.getSuperclass();
            }

            holder = new JsonObjectHolder(packageName, injectedSimpleClassName, TypeName.get(typeElement.asType()), abstractClass, parentInjectedClassName);

            jsonObjectMap.put(TypeUtils.getInjectedFQCN(typeElement, elements), holder);
        }
    }
}

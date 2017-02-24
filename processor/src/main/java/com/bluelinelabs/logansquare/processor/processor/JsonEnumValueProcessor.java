package com.bluelinelabs.logansquare.processor.processor;

import com.bluelinelabs.logansquare.annotation.JsonEnum;
import com.bluelinelabs.logansquare.processor.JsonEnumHolder;
import com.bluelinelabs.logansquare.processor.JsonObjectHolder;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class JsonEnumValueProcessor<T extends Annotation> extends Processor {

    private final Class<T> annotationClass;

    protected JsonEnumValueProcessor(Class<T> annotationClass, ProcessingEnvironment processingEnv) {
        super(processingEnv);
        this.annotationClass=annotationClass;
    }

    @Override
    public Class<T> getAnnotation() {
        return annotationClass;
    }

    @Override
    public void findAndParseObjects(RoundEnvironment env, Map<String, JsonObjectHolder> jsonObjectMap, Map<String, JsonEnumHolder> jsonEnumMap, Elements elements, Types types) {
        for (Element element : env.getElementsAnnotatedWith(annotationClass)) {
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

            Annotation objectAnnotation = enclosingElement.getAnnotation(JsonEnum.class);
            if (objectAnnotation == null) {
                error(enclosingElement, "%s: @%s values can only be in classes annotated with @%s.", enclosingElement.getQualifiedName(), annotationClass.getSimpleName(), JsonEnum.class.getSimpleName());
            }
            if(element.getKind() != ElementKind.ENUM_CONSTANT){
                error(element, "%s: @%s annotation can only be used on enum values.", enclosingElement.getQualifiedName(), annotationClass.getSimpleName());
            }
        }
    }

}

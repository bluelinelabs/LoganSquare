package com.bluelinelabs.logansquare.processor.processor;

import com.bluelinelabs.logansquare.annotation.OnPreJsonSerialize;
import com.bluelinelabs.logansquare.processor.JsonObjectHolder;
import com.bluelinelabs.logansquare.processor.TypeUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

public class OnPreSerializeProcessor extends MethodProcessor {

    public OnPreSerializeProcessor(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    @Override
    public Class getAnnotation() {
        return OnPreJsonSerialize.class;
    }

    @Override
    public void findAndParseObjects(RoundEnvironment env, Map<String, JsonObjectHolder> jsonObjectMap, Elements elements, Types types) {
        for (Element element : env.getElementsAnnotatedWith(OnPreJsonSerialize.class)) {
            try {
                processOnPreJsonSerializeMethodAnnotation(element, jsonObjectMap, elements);
            } catch (Exception e) {
                StringWriter stackTrace = new StringWriter();
                e.printStackTrace(new PrintWriter(stackTrace));

                error(element, "Unable to generate injector for %s. Stack trace incoming:\n%s", OnPreJsonSerialize.class, stackTrace.toString());
            }
        }
    }

    private void processOnPreJsonSerializeMethodAnnotation(Element element, Map<String, JsonObjectHolder> jsonObjectMap, Elements elements) throws Exception {
        if (!isCallbackMethodAnnotationValid(element, OnPreJsonSerialize.class.getSimpleName())) {
            return;
        }

        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        ExecutableElement executableElement = (ExecutableElement)element;
        JsonObjectHolder objectHolder = jsonObjectMap.get(TypeUtils.getInjectedFQCN(enclosingElement, elements));
        objectHolder.preSerializeCallback = executableElement.getSimpleName().toString();
    }
}

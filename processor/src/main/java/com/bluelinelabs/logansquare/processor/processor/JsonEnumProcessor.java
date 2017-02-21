package com.bluelinelabs.logansquare.processor.processor;

import com.bluelinelabs.logansquare.annotation.JsonEnum;
import com.bluelinelabs.logansquare.processor.JsonEnumHolder;
import com.bluelinelabs.logansquare.processor.JsonObjectHolder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static javax.lang.model.element.Modifier.PRIVATE;

public class JsonEnumProcessor extends Processor {

    public JsonEnumProcessor(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    @Override
    public Class getAnnotation() {
        return JsonEnum.class;
    }

    @Override
    public void findAndParseObjects(RoundEnvironment env, Map<String, JsonObjectHolder> jsonObjectMap, Map<String, JsonEnumHolder> jsonEnumMap, Elements elements, Types types) {
        for (Element element : env.getElementsAnnotatedWith(JsonEnum.class)) {
            try {
                processJsonEnumAnnotation(element, jsonEnumMap, elements, types);
            } catch (Exception e) {
                StringWriter stackTrace = new StringWriter();
                e.printStackTrace(new PrintWriter(stackTrace));

                error(element, "Unable to generate injector for %s. Stack trace incoming:\n%s", JsonEnum.class, stackTrace.toString());
            }
        }
    }

    private void processJsonEnumAnnotation(Element element, Map<String, JsonEnumHolder> jsonEnumMap, Elements elements, Types types) {
        TypeElement typeElement = (TypeElement)element;

        if (element.getModifiers().contains(PRIVATE)) {
            error(element, "%s: %s annotation can't be used on private enums.", typeElement.getQualifiedName(), JsonEnum.class.getSimpleName());
        }
    }

}

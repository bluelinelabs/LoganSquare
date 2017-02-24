package com.bluelinelabs.logansquare.processor.processor;

import com.bluelinelabs.logansquare.annotation.JsonBooleanValue;
import com.bluelinelabs.logansquare.annotation.JsonNullValue;
import com.bluelinelabs.logansquare.annotation.JsonNumberValue;
import com.bluelinelabs.logansquare.annotation.JsonStringValue;
import com.bluelinelabs.logansquare.processor.JsonEnumHolder;
import com.bluelinelabs.logansquare.processor.JsonObjectHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static javax.tools.Diagnostic.Kind.ERROR;

public abstract class Processor {

    protected ProcessingEnvironment mProcessingEnv;

    protected Processor(ProcessingEnvironment processingEnv) {
        mProcessingEnv = processingEnv;
    }

    public abstract Class getAnnotation();
    public abstract void findAndParseObjects(RoundEnvironment env, Map<String, JsonObjectHolder> jsonObjectMap, Map<String, JsonEnumHolder> jsonEnumMap, Elements elements, Types types);

    public static List<Processor> allProcessors(ProcessingEnvironment processingEnvironment) {
        List<Processor> list = new ArrayList<>();

        list.add(new JsonEnumProcessor(processingEnvironment));
        list.add(new JsonEnumValueProcessor<>(JsonStringValue.class, processingEnvironment));
        list.add(new JsonEnumValueProcessor<>(JsonNumberValue.class, processingEnvironment));
        list.add(new JsonEnumValueProcessor<>(JsonBooleanValue.class, processingEnvironment));
        list.add(new JsonEnumValueProcessor<>(JsonNullValue.class, processingEnvironment));

        list.add(new JsonObjectProcessor(processingEnvironment));
        list.add(new OnJsonParseCompleteProcessor(processingEnvironment));
        list.add(new OnPreSerializeProcessor(processingEnvironment));
        list.add(new JsonFieldProcessor(processingEnvironment));

        return list;
    }

    public void error(Element element, String message, Object... args) {
        mProcessingEnv.getMessager().printMessage(ERROR, String.format(message, args), element);
    }
}

package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.processor.processor.Processor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import static javax.tools.Diagnostic.Kind.ERROR;

public class JsonAnnotationProcessor extends AbstractProcessor {
    private Elements mElementUtils;
    private Types mTypeUtils;
    private Filer mFiler;
    private List<Processor> mProcessors;
    private Map<String, JsonObjectHolder> mJsonObjectMap;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        mElementUtils = env.getElementUtils();
        mTypeUtils = env.getTypeUtils();
        mFiler = env.getFiler();
        mJsonObjectMap = new HashMap<>();
        mProcessors = Processor.allProcessors(processingEnv);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportTypes = new LinkedHashSet<>();
        for (Processor processor : mProcessors) {
            supportTypes.add(processor.getAnnotation().getCanonicalName());
        }
        return supportTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> elements, RoundEnvironment env) {
        try {
            for (Processor processor : mProcessors) {
                processor.findAndParseObjects(env, mJsonObjectMap, mElementUtils, mTypeUtils);
            }

            for (Map.Entry<String, JsonObjectHolder> entry : mJsonObjectMap.entrySet()) {
                String fqcn = entry.getKey();
                JsonObjectHolder jsonObjectHolder = entry.getValue();

                if (!jsonObjectHolder.fileCreated) {
                    jsonObjectHolder.fileCreated = true;

                    if (jsonObjectHolder.hasParentClass() && hasUnsupportedInheritance(jsonObjectHolder)) {
                        // The interactions between models with constructor injection and
                        // and their subclasses can get really weird. For now let's simply
                        // disallow inheritance from them
                        continue;
                    }

                    if (jsonObjectHolder.needConstructorInjection) {
                        // our constructor injection requires proper argument order, ensure it here
                        sortAccordingToFieldOrder(jsonObjectHolder);
                    }

                    try {
                        JavaFileObject jfo = mFiler.createSourceFile(fqcn);
                        Writer writer = jfo.openWriter();
                        writer.write(new ObjectMapperInjector(jsonObjectHolder).getJavaClassFile());
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        error(fqcn, "Exception occurred while attempting to write injector for type %s. Exception message: %s", fqcn, e.getMessage());
                    }
                }
            }

            return true;
        } catch (Throwable e) {
            StringWriter stackTrace = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTrace));
            error("Exception while processing Json classes. Stack trace incoming:\n%s", stackTrace.toString());
            return false;
        }
    }

    @SuppressWarnings("all")
    private void sortAccordingToFieldOrder(JsonObjectHolder jsonObjectHolder) {
        TypeName jsonModel = jsonObjectHolder.objectTypeName;

        TypeName jsonModelType = jsonModel instanceof ClassName
                ? (ClassName) jsonModel
                : ((ParameterizedTypeName) jsonModel).rawType;

        TypeElement jsonModelElement = mElementUtils.getTypeElement(jsonModelType.toString());

        final List<VariableElement> fields = ElementFilter.fieldsIn(jsonModelElement.getEnclosedElements());

        for (int i = 0; i < fields.size(); ++i) {
            ((List) fields).set(i, fields.get(i).getSimpleName().toString());
        }

        final Map<String, JsonFieldHolder> oldMap = jsonObjectHolder.fieldMap;

        final TreeMap<String, JsonFieldHolder> newMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.compare(fields.indexOf(o1), fields.indexOf(o2));
            }
        });

        newMap.putAll(oldMap);

        jsonObjectHolder.fieldMap = newMap;
    }

    private boolean hasUnsupportedInheritance(JsonObjectHolder objectHolder) {
        TypeName parentType = objectHolder.parentTypeName;

        ClassName parentClass = parentType instanceof ClassName
                        ? (ClassName) parentType
                        : ((ParameterizedTypeName) parentType).rawType;

        JsonObjectHolder parentHolder =
                mJsonObjectMap.get(TypeUtils.getInjectedFQCN(parentClass));

        if (parentHolder == null || !parentHolder.needConstructorInjection) {
            return false;
        }

        TypeName badType = objectHolder.objectTypeName;

        TypeName perpetratorType = badType instanceof ClassName
                ? (ClassName) badType
                : ((ParameterizedTypeName) badType).rawType;

        TypeElement badTypeElement = mElementUtils.getTypeElement(perpetratorType.toString());

        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                "Subclassing from models with constructor injection is not supported", badTypeElement);

        return true;
    }

    private void error(String message, Object... args) {
        processingEnv.getMessager().printMessage(ERROR, String.format(message, args));
    }
}

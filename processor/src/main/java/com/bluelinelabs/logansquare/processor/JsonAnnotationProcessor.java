package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.Constants;
import com.bluelinelabs.logansquare.processor.processor.Processor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

import static javax.tools.Diagnostic.Kind.ERROR;

public class JsonAnnotationProcessor extends AbstractProcessor {
    private Elements mElementUtils;
    private Types mTypeUtils;
    private Filer mFiler;
    private List<Processor> mProcessors;
    private Map<String, JsonObjectHolder> mJsonObjectMap;
    private boolean mLoaderWritten;

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
    public Set<String> getSupportedOptions() {
        Set<String> supportOptions = new LinkedHashSet<>();
        supportOptions.add("jsonMapperLoaderSuffix");
        return supportOptions;
    }

    @Override
    public boolean process(Set<? extends TypeElement> elements, RoundEnvironment env) {
        try {
            for (Processor processor : mProcessors) {
                processor.findAndParseObjects(env, mJsonObjectMap, mElementUtils, mTypeUtils);
            }

            if (!mLoaderWritten) {
                mLoaderWritten = true;

                final JsonMapperLoaderInjector loaderInjector = new JsonMapperLoaderInjector(mJsonObjectMap.values(), processingEnv);
                try {
                    JavaFileObject jfo = mFiler.createSourceFile(Constants.LOADER_PACKAGE_NAME + "." + getLoaderClassName(processingEnv));
                    Writer writer = jfo.openWriter();
                    writer.write(loaderInjector.getJavaClassFile());
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    error(Constants.LOADER_CLASS_NAME, "Exception occurred while attempting to write injector for the mapper loader. Exception message: %s", e.getMessage());
                }
            }

            for (Map.Entry<String, JsonObjectHolder> entry : mJsonObjectMap.entrySet()) {
                String fqcn = entry.getKey();
                JsonObjectHolder jsonObjectHolder = entry.getValue();

                if (!jsonObjectHolder.fileCreated) {
                    jsonObjectHolder.fileCreated = true;

                    try {
                        JavaFileObject jfo = mFiler.createSourceFile(fqcn);
                        Writer writer = jfo.openWriter();
                        writer.write(new ObjectMapperInjector(jsonObjectHolder, processingEnv).getJavaClassFile());
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

    public static String getLoaderClassName(ProcessingEnvironment env) {
        final String suffix = env.getOptions().get("jsonMapperLoaderSuffix");
        if (suffix == null) return Constants.LOADER_CLASS_NAME;
        return Constants.LOADER_CLASS_NAME + suffix;
    }

    private void error(String message, Object... args) {
        processingEnv.getMessager().printMessage(ERROR, String.format(message, args));
    }
}

package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.processor.processor.Processor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import static javax.tools.Diagnostic.Kind.ERROR;

@SupportedOptions(value = {"loganSquareIndex", "verbose"})
public class JsonAnnotationProcessor extends AbstractProcessor {
    private Elements mElementUtils;
    private Types mTypeUtils;
    private Filer mFiler;
    private List<Processor> mProcessors;
    private Map<String, JsonObjectHolder> mJsonObjectMap;
    private Messager messager;

    private List<String> generatedIndex = new ArrayList<>();

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        mElementUtils = env.getElementUtils();
        mTypeUtils = env.getTypeUtils();
        mFiler = env.getFiler();
        mJsonObjectMap = new HashMap<>();
        mProcessors = Processor.allProcessors(processingEnv);
        messager = processingEnv.getMessager();
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

            //start creating JsonMapperIndex
            String indexPath = processingEnv.getOptions().get("loganSquareIndex");

            if (!generatedIndex.contains(indexPath)) {
                generatedIndex.add(indexPath);

                CodeBlock.Builder staticImportBlockBuilder = CodeBlock.builder();
                staticImportBlockBuilder.add("CLASSES = new $T<$T, $T>();\n", ConcurrentHashMap.class, Class.class, Class.class);

                for (Map.Entry<String, JsonObjectHolder> entry : mJsonObjectMap.entrySet()) {
                    JsonObjectHolder holder = entry.getValue();

                    ClassName mapperClassName = ClassName.get(holder.packageName, holder.injectedClassName);

                    staticImportBlockBuilder.add("CLASSES.put($T.class, $T.class);\n", holder.objectTypeName, mapperClassName);

                    messager.printMessage(Diagnostic.Kind.NOTE, "JsonObjectMapper Class Name: " + holder.objectTypeName.toString() + " " + mapperClassName.simpleName());
                }

                MethodSpec.Builder mainBuilder = MethodSpec.methodBuilder("getJsonMapper")
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(Class.class, "objClass")
                        .returns(ClassName.get("com.bluelinelabs.logansquare", "JsonMapper"))
                        .addStatement("$T mapper = null", JsonMapper.class)
                        .addCode("try {\n")
                        .addCode("if (CLASSES.containsKey(objClass)) {\n")
                        .addStatement("Class mapperClass = CLASSES.get(objClass)")
                        .addStatement("mapper = ($T) mapperClass.newInstance()", JsonMapper.class)
                        .addCode("}\n")
                        .addCode("} catch ($T e) {e.printStackTrace();}\n", Throwable.class)
                        .addStatement("return mapper");

                MethodSpec main = mainBuilder.build();

                int lastPeriod = indexPath.lastIndexOf(".");

                String indexClassPackageName = indexPath.substring(0, lastPeriod);
                String indexClassName = indexPath.substring(lastPeriod + 1);

                TypeSpec objectMapperIndexClass = TypeSpec.classBuilder(indexClassName)
                        .addSuperinterface(LoganSquare.JsonMapperIndex.class)
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addField(FieldSpec.builder(
                                ParameterizedTypeName.get(
                                        ClassName.get("java.util", "Map"),
                                        ClassName.get("java.lang", "Class"),
                                        ClassName.get("java.lang", "Class")),
                                "CLASSES")
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                                .build())
                        .addStaticBlock(staticImportBlockBuilder.build())
                        .addMethod(main)
                        .build();

                JavaFile javaFile = JavaFile.builder(indexClassPackageName, objectMapperIndexClass)
                        .build();

                javaFile.toJavaFileObject().delete();

                javaFile.writeTo(mFiler);
            }

            return true;
        } catch (Throwable e) {
            StringWriter stackTrace = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTrace));
            error("Exception while processing Json classes. Stack trace incoming:\n%s", stackTrace.toString());
            return false;
        }
    }

    private void error(String message, Object... args) {
        processingEnv.getMessager().printMessage(ERROR, String.format(message, args));
    }
}

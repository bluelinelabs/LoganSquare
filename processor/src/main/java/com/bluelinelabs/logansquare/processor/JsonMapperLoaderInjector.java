package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.Constants;
import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.ParameterizedType;
import com.bluelinelabs.logansquare.internal.JsonMapperLoader;
import com.bluelinelabs.logansquare.internal.objectmappers.BooleanMapper;
import com.bluelinelabs.logansquare.internal.objectmappers.DoubleMapper;
import com.bluelinelabs.logansquare.internal.objectmappers.FloatMapper;
import com.bluelinelabs.logansquare.internal.objectmappers.IntegerMapper;
import com.bluelinelabs.logansquare.internal.objectmappers.ListMapper;
import com.bluelinelabs.logansquare.internal.objectmappers.LongMapper;
import com.bluelinelabs.logansquare.internal.objectmappers.MapMapper;
import com.bluelinelabs.logansquare.internal.objectmappers.ObjectMapper;
import com.bluelinelabs.logansquare.internal.objectmappers.StringMapper;
import com.bluelinelabs.logansquare.util.SimpleArrayMap;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

public class JsonMapperLoaderInjector {

    private static final String PARAMETERIZED_MAPPERS_VARIABLE_NAME = "PARAMETERIZED_OBJECT_MAPPERS";

    private final Collection<JsonObjectHolder> mJsonObjectHolders;
    private final Map<Class, Class> mBuiltInMapperMap;
    private final ProcessingEnvironment mProcessingEnv;

    public JsonMapperLoaderInjector(Collection<JsonObjectHolder> jsonObjectHolders, ProcessingEnvironment processingEnv) {
        mJsonObjectHolders = jsonObjectHolders;
        mProcessingEnv = processingEnv;

        mBuiltInMapperMap = new HashMap<>();
        mBuiltInMapperMap.put(String.class, StringMapper.class);
        mBuiltInMapperMap.put(Integer.class, IntegerMapper.class);
        mBuiltInMapperMap.put(Long.class, LongMapper.class);
        mBuiltInMapperMap.put(Float.class, FloatMapper.class);
        mBuiltInMapperMap.put(Double.class, DoubleMapper.class);
        mBuiltInMapperMap.put(Boolean.class, BooleanMapper.class);
        mBuiltInMapperMap.put(Object.class, ObjectMapper.class);
        mBuiltInMapperMap.put(List.class, ListMapper.class);
        mBuiltInMapperMap.put(ArrayList.class, ListMapper.class);
        mBuiltInMapperMap.put(Map.class, MapMapper.class);
        mBuiltInMapperMap.put(HashMap.class, MapMapper.class);
    }

    public String getJavaClassFile() {
        try {
            return JavaFile.builder(Constants.LOADER_PACKAGE_NAME, getTypeSpec()).build().toString();
        } catch (Exception e) {
            return null;
        }
    }

    private TypeSpec getTypeSpec() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(JsonAnnotationProcessor.getLoaderClassName(mProcessingEnv)).addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        builder.addSuperinterface(ClassName.get(JsonMapperLoader.class));

        builder.addField(FieldSpec.builder(ParameterizedTypeName.get(ConcurrentHashMap.class, ParameterizedType.class, JsonMapper.class), PARAMETERIZED_MAPPERS_VARIABLE_NAME)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new $T()", ParameterizedTypeName.get(ConcurrentHashMap.class, ParameterizedType.class, JsonMapper.class))
                .build());

        addAllBuiltInMappers(builder);
        addAllProjectMappers(builder);
        builder.addMethod(getPutAllJsonMappersMethod());
        builder.addMethod(getParameterizedMethodGetterMethod());
        builder.addMethod(getStaticParameterizedMapperGetterMethod());
        builder.addMethod(getStaticParameterizedMapperWithPartialGetterMethod());
        builder.addMethod(getGetClassMapperMethod());

        return builder.build();
    }

    private MethodSpec getPutAllJsonMappersMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("putAllJsonMappers")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(ClassName.get(SimpleArrayMap.class), ClassName.get(Class.class), ClassName.get(JsonMapper.class)), "map");

        for (Class cls : mBuiltInMapperMap.keySet()) {
            builder.addStatement("map.put($T.class, $L)", cls, getMapperVariableName(mBuiltInMapperMap.get(cls)));
        }

        List<String> createdMappers = new ArrayList<>();
        for (JsonObjectHolder jsonObjectHolder : mJsonObjectHolders) {
            if (jsonObjectHolder.typeParameters.size() == 0) {
                String variableName = getMapperVariableName(jsonObjectHolder.packageName + "." + jsonObjectHolder.injectedClassName);

                createdMappers.add(variableName);

                if (!jsonObjectHolder.isAbstractClass) {
                    builder.addStatement("map.put($T.class, $L)", jsonObjectHolder.objectTypeName, variableName);
                }
            }
        }

        return builder.build();
    }

    private void addAllBuiltInMappers(TypeSpec.Builder typeSpecBuilder) {
        addBuiltInMapper(typeSpecBuilder, StringMapper.class);
        addBuiltInMapper(typeSpecBuilder, IntegerMapper.class);
        addBuiltInMapper(typeSpecBuilder, LongMapper.class);
        addBuiltInMapper(typeSpecBuilder, FloatMapper.class);
        addBuiltInMapper(typeSpecBuilder, DoubleMapper.class);
        addBuiltInMapper(typeSpecBuilder, BooleanMapper.class);
        addBuiltInMapper(typeSpecBuilder, ObjectMapper.class);
        addBuiltInMapper(typeSpecBuilder, ListMapper.class);
        addBuiltInMapper(typeSpecBuilder, MapMapper.class);
    }

    private void addBuiltInMapper(TypeSpec.Builder typeSpecBuilder, Class mapperClass) {
        typeSpecBuilder.addField(FieldSpec.builder(mapperClass, getMapperVariableName(mapperClass))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("new $T()", mapperClass)
                .build()
        );
    }

    private void addAllProjectMappers(TypeSpec.Builder typeSpecBuilder) {
        List<JsonObjectHolder> sortedHolders = new ArrayList<>(mJsonObjectHolders);
        Collections.sort(sortedHolders, new Comparator<JsonObjectHolder>() {
            @Override
            public int compare(JsonObjectHolder o1, JsonObjectHolder o2) {
                if (o2.objectTypeName.equals(o1.parentTypeName)) {
                    return -1;
                } else if (o1.objectTypeName.equals(o2.parentTypeName)) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        for (JsonObjectHolder jsonObjectHolder : sortedHolders) {
            if (jsonObjectHolder.typeParameters.size() == 0) {
                TypeName mapperTypeName = ClassName.get(jsonObjectHolder.packageName, jsonObjectHolder.injectedClassName);
                String variableName = getMapperVariableName(jsonObjectHolder.packageName + "." + jsonObjectHolder.injectedClassName);

                typeSpecBuilder.addField(FieldSpec.builder(mapperTypeName, variableName)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                        .initializer("$T.INSTANCE", mapperTypeName)
                        .build()
                );
            }
        }
    }

    private MethodSpec getParameterizedMethodGetterMethod() {
        return MethodSpec.methodBuilder("mapperFor")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(TypeVariableName.get("T"))
                .returns(ParameterizedTypeName.get(ClassName.get(JsonMapper.class), TypeVariableName.get("T")))
                .addParameter(ParameterizedTypeName.get(ClassName.get(ParameterizedType.class), TypeVariableName.get("T")), "type")
                .addParameter(ParameterizedTypeName.get(ClassName.get(SimpleArrayMap.class), ClassName.get(ParameterizedType.class), ClassName.get(JsonMapper.class)), "partialMappers")
                .addStatement("return _mapperFor(type, partialMappers)")
                .build();
    }

    private MethodSpec getStaticParameterizedMapperGetterMethod() {
        return MethodSpec.methodBuilder("_mapperFor")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addTypeVariable(TypeVariableName.get("T"))
                .returns(ParameterizedTypeName.get(ClassName.get(JsonMapper.class), TypeVariableName.get("T")))
                .addParameter(ParameterizedTypeName.get(ClassName.get(ParameterizedType.class), TypeVariableName.get("T")), "type")
                .addStatement("return _mapperFor(type, null)")
                .build();

    }

    private MethodSpec getStaticParameterizedMapperWithPartialGetterMethod() {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("_mapperFor")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addTypeVariable(TypeVariableName.get("T"))
                .returns(ParameterizedTypeName.get(ClassName.get(JsonMapper.class), TypeVariableName.get("T")))
                .addParameter(ParameterizedTypeName.get(ClassName.get(ParameterizedType.class), TypeVariableName.get("T")), "type")
                .addParameter(ParameterizedTypeName.get(ClassName.get(SimpleArrayMap.class), ClassName.get(ParameterizedType.class), ClassName.get(JsonMapper.class)), "partialMappers");

        methodBuilder
                .beginControlFlow("if (type.typeParameters.size() == 0)")
                .addStatement("return getMapper((Class<T>)type.rawType)")
                .endControlFlow()
                .beginControlFlow("if (partialMappers == null)")
                .addStatement("partialMappers = new $T()", ParameterizedTypeName.get(ClassName.get(SimpleArrayMap.class), ClassName.get(ParameterizedType.class), ClassName.get(JsonMapper.class)))
                .endControlFlow()
                .addStatement("$T mapper", ParameterizedTypeName.get(ClassName.get(JsonMapper.class), TypeVariableName.get("T")))
                .beginControlFlow("if (partialMappers.containsKey(type))")
                .addStatement("mapper = partialMappers.get(type)")
                .nextControlFlow("else if ($L.containsKey(type))", PARAMETERIZED_MAPPERS_VARIABLE_NAME)
                .addStatement("mapper = $L.get(type)", PARAMETERIZED_MAPPERS_VARIABLE_NAME)
                .nextControlFlow("else");

        boolean conditionalStarted = false;
        for (JsonObjectHolder jsonObjectHolder : mJsonObjectHolders) {
            if (jsonObjectHolder.typeParameters.size() > 0) {
                String conditional = String.format("if (type.rawType == %s.class)", jsonObjectHolder.objectTypeName.toString().replaceAll("<(.*?)>", ""));
                if (conditionalStarted) {
                    methodBuilder.nextControlFlow("else " + conditional);
                } else {
                    conditionalStarted = true;
                    methodBuilder.beginControlFlow(conditional);
                }

                methodBuilder.beginControlFlow("if (type.typeParameters.size() == $L)", jsonObjectHolder.typeParameters.size());

                StringBuilder constructorArgs = new StringBuilder();
                for (int i = 0; i < jsonObjectHolder.typeParameters.size(); i++) {
                    constructorArgs.append(", type.typeParameters.get(").append(i).append(")");
                }
                methodBuilder.addStatement("mapper = new $T(type" + constructorArgs.toString() + ", partialMappers)", ClassName.get(jsonObjectHolder.packageName, jsonObjectHolder.injectedClassName));

                methodBuilder.nextControlFlow("else");
                methodBuilder.addStatement(
                        "throw new $T(\"Invalid number of parameter types. Type $T expects $L parameter types, received \" + type.typeParameters.size())",
                        RuntimeException.class, jsonObjectHolder.objectTypeName, jsonObjectHolder.typeParameters.size()
                );
                methodBuilder.endControlFlow();
            }
        }

        if (conditionalStarted) {
            methodBuilder.nextControlFlow("else")
                    .addStatement("mapper = null")
                    .endControlFlow();
        } else {
            methodBuilder.addStatement("mapper = null");
        }

        methodBuilder.beginControlFlow("if (mapper != null)")
                .addStatement("$L.put(type, mapper)", PARAMETERIZED_MAPPERS_VARIABLE_NAME)
                .endControlFlow();
        methodBuilder.endControlFlow();

        methodBuilder.addStatement("System.out.println(\"type = \" + type + \"rawType = \" + type.rawType + \"; mapper = \" + mapper)");

        methodBuilder.addStatement("return mapper");

        return methodBuilder.build();
    }

    private MethodSpec getGetClassMapperMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getMapper")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .addTypeVariable(TypeVariableName.get("T"))
                .returns(ParameterizedTypeName.get(ClassName.get(JsonMapper.class), TypeVariableName.get("T")))
                .addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), TypeVariableName.get("T")), "cls")
                .addStatement("$T mapper = $T.getMapper(cls)", JsonMapper.class, LoganSquare.class)
                .beginControlFlow("if (mapper == null)");

        boolean ifStatementStarted = false;
        for (Class cls : mBuiltInMapperMap.keySet()) {
            if (!ifStatementStarted) {
                builder.beginControlFlow("if (cls == $T.class)", cls);
                ifStatementStarted = true;
            } else {
                builder.nextControlFlow(" else if (cls == $T.class)", cls);
            }
            builder.addStatement("mapper = $L", getMapperVariableName(mBuiltInMapperMap.get(cls)));
        }

        for (JsonObjectHolder jsonObjectHolder : mJsonObjectHolders) {
            if (jsonObjectHolder.typeParameters.size() == 0) {
                if (!ifStatementStarted) {
                    builder.beginControlFlow("if (cls == $T.class)", jsonObjectHolder.objectTypeName);
                    ifStatementStarted = true;
                } else {
                    builder.nextControlFlow("else if (cls == $T.class)", jsonObjectHolder.objectTypeName);
                }
                builder.addStatement("mapper = $L", getMapperVariableName(jsonObjectHolder.packageName + "." + jsonObjectHolder.injectedClassName));
            }
        }

        builder.endControlFlow();
        builder.endControlFlow();

        builder.addStatement("return mapper");

        return builder.build();
    }

    public static String getMapperVariableName(Class cls) {
        return getMapperVariableName(cls.getCanonicalName());
    }

    public static String getMapperVariableName(String fullyQualifiedClassName) {
        return fullyQualifiedClassName.replaceAll("\\.", "_").replaceAll("\\$", "_").toUpperCase();
    }
}

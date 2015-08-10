package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.Constants;
import com.bluelinelabs.logansquare.JsonMapper;
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
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;

public class JsonMapperLoaderInjector {

    private final Collection<JsonObjectHolder> mJsonObjectHolders;

    public JsonMapperLoaderInjector(Collection<JsonObjectHolder> jsonObjectHolders) {
        mJsonObjectHolders = jsonObjectHolders;
    }

    public String getJavaClassFile() {
        try {
            return JavaFile.builder(Constants.LOADER_PACKAGE_NAME, getTypeSpec()).build().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private TypeSpec getTypeSpec() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(Constants.LOADER_CLASS_NAME).addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        builder.addSuperinterface(ClassName.get(JsonMapperLoader.class));

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("putAllJsonMappers")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(ClassName.get(SimpleArrayMap.class), ClassName.get(Class.class), ClassName.get(JsonMapper.class)), "map")
                .addStatement("map.put($T.class, new $T())", String.class, StringMapper.class)
                .addStatement("map.put($T.class, new $T())", Integer.class, IntegerMapper.class)
                .addStatement("map.put($T.class, new $T())", Long.class, LongMapper.class)
                .addStatement("map.put($T.class, new $T())", Float.class, FloatMapper.class)
                .addStatement("map.put($T.class, new $T())", Double.class, DoubleMapper.class)
                .addStatement("map.put($T.class, new $T())", Boolean.class, BooleanMapper.class)
                .addStatement("map.put($T.class, new $T())", Object.class, ObjectMapper.class)
                .addStatement("map.put($T.class, new $T())", List.class, ListMapper.class)
                .addStatement("map.put($T.class, new $T())", ArrayList.class, ListMapper.class)
                .addStatement("map.put($T.class, new $T())", Map.class, MapMapper.class)
                .addStatement("map.put($T.class, new $T())", HashMap.class, MapMapper.class);

        for (JsonObjectHolder jsonObjectHolder : mJsonObjectHolders) {
            if (!jsonObjectHolder.isAbstractClass) {
                methodBuilder.addStatement("map.put($T.class, new $T())", jsonObjectHolder.objectTypeName, ClassName.get(jsonObjectHolder.packageName, jsonObjectHolder.injectedClassName));
            }
        }

        builder.addMethod(methodBuilder.build());
        return builder.build();
    }
}

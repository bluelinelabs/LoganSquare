package com.bluelinelabs.logansquare.processor;


import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Map;

import javax.lang.model.element.Modifier;

public class EnumConverterInjector implements Injector {

    private static final String CONVERTER_FROM_VARIABLE_NAME = "string";
    private static final String CONVERTER_TO_VARIABLE_NAME = "value";

    private final JsonEnumHolder mJsonEnumHolder;

    public EnumConverterInjector(JsonEnumHolder jsonEnumHolder) {
        this.mJsonEnumHolder = jsonEnumHolder;
    }

    public String getJavaClassFile() {
        try {
            return JavaFile.builder(mJsonEnumHolder.packageName, getTypeSpec()).build().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private TypeSpec getTypeSpec() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(mJsonEnumHolder.injectedClassName).addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        builder.superclass(ParameterizedTypeName.get(ClassName.get(StringBasedTypeConverter.class), mJsonEnumHolder.objectTypeName));

        builder.addMethod(createGetFromMethod(mJsonEnumHolder.getValuesMap()));
        builder.addMethod(createConvertToMethod(mJsonEnumHolder.getValuesMap()));

        return builder.build();
    }

    private MethodSpec createGetFromMethod(Map<String, String> valuesMap) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getFromString")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(mJsonEnumHolder.objectTypeName)
                .addParameter(String.class, CONVERTER_FROM_VARIABLE_NAME);

        builder.beginControlFlow("if ($L == null)", CONVERTER_FROM_VARIABLE_NAME);
        boolean nullEntryFound = false;
        for (Map.Entry<String, String> entry : valuesMap.entrySet()) {
            if (entry.getValue() == null) {
                builder.addStatement("return $T.$L", mJsonEnumHolder.objectTypeName, entry.getKey());
                nullEntryFound = true;
            }
        }
        if (!nullEntryFound) {
            builder.addStatement("return null");
        }
        builder.endControlFlow();

        builder.beginControlFlow("switch ($L)", CONVERTER_FROM_VARIABLE_NAME);
        for (Map.Entry<String, String> entry : valuesMap.entrySet()) {
            if (entry.getValue() != null) {
                builder.addStatement("case \"$L\": return $T.$L", entry.getValue(), mJsonEnumHolder.objectTypeName, entry.getKey());
            }
        }
        builder.addStatement("default: throw new IllegalArgumentException($L)", CONVERTER_FROM_VARIABLE_NAME);
        builder.endControlFlow();

        return builder.build();
    }

    private MethodSpec createConvertToMethod(Map<String, String> valuesMap) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("convertToString")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addParameter(mJsonEnumHolder.objectTypeName, CONVERTER_TO_VARIABLE_NAME);

        String nullValue = null;
        for (Map.Entry<String, String> entry : valuesMap.entrySet()) {
            if (entry.getValue() == null) {
                nullValue = entry.getKey();
            }
        }
        if (nullValue == null) {
            builder.beginControlFlow("if ($L == null)", CONVERTER_TO_VARIABLE_NAME);
        } else {
            builder.beginControlFlow("if ($L == null || $L == $T.$L)",
                    CONVERTER_TO_VARIABLE_NAME, CONVERTER_TO_VARIABLE_NAME, mJsonEnumHolder.objectTypeName, nullValue);
        }
        builder.addStatement("return null");
        builder.endControlFlow();

        builder.beginControlFlow("switch ($L)", CONVERTER_TO_VARIABLE_NAME);
        for (Map.Entry<String, String> entry : valuesMap.entrySet()) {
            if (entry.getValue() != null) {
                builder.addStatement("case $L: return \"$L\"", entry.getKey(), entry.getValue());
            }
        }
        builder.addStatement("default: throw new IllegalArgumentException($L.name())", CONVERTER_TO_VARIABLE_NAME);
        builder.endControlFlow();

        return builder.build();
    }

    /*TODO: add like used types
    private CodeBlock createStaticConstructor() {
        CodeBlock.Builder builder = CodeBlock.builder();
        builder.addStatement("$T.registerTypeConverter($T.class, new $L())", LoganSquare.class, mJsonEnumHolder.objectTypeName, mJsonEnumHolder.injectedClassName);
        return builder.build();
    }
    */

}

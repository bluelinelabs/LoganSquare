package com.bluelinelabs.logansquare.processor;


import com.bluelinelabs.logansquare.typeconverters.TypeConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Map;

import javax.lang.model.element.Modifier;

public class EnumConverterInjector implements Injector {

    private static final String CONVERTER_PARSER_PARAMETER_NAME = "jsonParser";
    private static final String CONVERTER_PARSED_VALUE_VARIABLE_NAME = "parsedValue";

    private static final String CONVERTER_VALUE_PARAMETER_NAME = "value";
    private static final String CONVERTER_FIELD_NAME_PARAMETER_NAME = "fieldName";
    private static final String CONVERTER_JSON_GENERATOR_PARAMETER_NAME = "jsonGenerator";

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

        builder.addSuperinterface(ParameterizedTypeName.get(ClassName.get(TypeConverter.class), mJsonEnumHolder.objectTypeName));

        builder.addMethod(createParseMethod(mJsonEnumHolder.valuesType.enumValueClass, mJsonEnumHolder.valuesType.getFromMethodName, mJsonEnumHolder.valuesMap));
        builder.addMethod(createSerializeMethod(mJsonEnumHolder.valuesMap, mJsonEnumHolder.valuesType.writeToFieldMethodName, mJsonEnumHolder.valuesType.writeMethodName));

        return builder.build();
    }

    private MethodSpec createParseMethod(Class enumValueClass, String getFromMethodName, Map<String, Object> valuesMap) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("parse")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(mJsonEnumHolder.objectTypeName)
                .addParameter(JsonParser.class, CONVERTER_PARSER_PARAMETER_NAME)
                .addException(IOException.class);

        builder.beginControlFlow("if ($L.getCurrentToken() == $T.VALUE_NULL)", CONVERTER_PARSER_PARAMETER_NAME, JsonToken.class);
        boolean nullEntryFound = false;
        for (Map.Entry<String, Object> entry : valuesMap.entrySet()) {
            if (entry.getValue() == null) {
                builder.addStatement("return $T.$L", mJsonEnumHolder.objectTypeName, entry.getKey());
                nullEntryFound = true;
                break;
            }
        }
        if (!nullEntryFound) {
            builder.addStatement("return null");
        }
        builder.endControlFlow();

        builder.addStatement("$T $L = $L.$L()", enumValueClass, CONVERTER_PARSED_VALUE_VARIABLE_NAME, CONVERTER_PARSER_PARAMETER_NAME, getFromMethodName);
        boolean firstStatement = true;
        for (Map.Entry<String, Object> entry : valuesMap.entrySet()) {
            if (entry.getValue() != null) {
                String equalsString = (entry.getValue() instanceof Number || entry.getValue() instanceof Boolean) ? "$L == $L" : "$L.equals($L)";
                if (firstStatement) {
                    builder.beginControlFlow("if(" + equalsString + ")", CONVERTER_PARSED_VALUE_VARIABLE_NAME, toGoodFormat(entry.getValue()));
                } else {
                    builder.nextControlFlow("else if(" + equalsString + ")", CONVERTER_PARSED_VALUE_VARIABLE_NAME, toGoodFormat(entry.getValue()));
                }
                builder.addStatement("return $T.$L", mJsonEnumHolder.objectTypeName, entry.getKey());
                firstStatement = false;
            }
        }
        builder.endControlFlow();
        builder.addStatement("throw new IllegalArgumentException($L.toString())", CONVERTER_PARSER_PARAMETER_NAME);

        return builder.build();
    }

    private MethodSpec createSerializeMethod(Map<String, Object> valuesMap, String writeToFieldMethodName, String writeMethodName) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("serialize")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mJsonEnumHolder.objectTypeName, CONVERTER_VALUE_PARAMETER_NAME)
                .addParameter(String.class, CONVERTER_FIELD_NAME_PARAMETER_NAME)
                .addParameter(boolean.class, "writeFieldNameForObject")
                .addParameter(JsonGenerator.class, CONVERTER_JSON_GENERATOR_PARAMETER_NAME)
                .addException(IOException.class);

        builder.beginControlFlow("if ($L != null)", CONVERTER_FIELD_NAME_PARAMETER_NAME);
        addSerializeMethodPart(builder, valuesMap, writeToFieldMethodName, true);
        builder.nextControlFlow("else");
        addSerializeMethodPart(builder, valuesMap, writeMethodName, false);
        builder.endControlFlow();

        return builder.build();
    }

    private void addSerializeMethodPart(MethodSpec.Builder builder, Map<String, Object> valuesMap, String writeMethodName, boolean writeToField) {
        String nullValue = null;
        for (Map.Entry<String, Object> entry : valuesMap.entrySet()) {
            if (entry.getValue() == null) {
                nullValue = entry.getKey();
            }
        }
        if (nullValue == null) {
            builder.beginControlFlow("if ($L == null)", CONVERTER_VALUE_PARAMETER_NAME);
        } else {
            builder.beginControlFlow("if ($L == null || $L == $T.$L)",
                    CONVERTER_VALUE_PARAMETER_NAME, CONVERTER_VALUE_PARAMETER_NAME, mJsonEnumHolder.objectTypeName, nullValue);
        }
        if (writeToField) {
            builder.addStatement("$L.writeNullField($L)", CONVERTER_JSON_GENERATOR_PARAMETER_NAME, CONVERTER_FIELD_NAME_PARAMETER_NAME);
        } else {
            builder.addStatement("$L.writeNull()", CONVERTER_JSON_GENERATOR_PARAMETER_NAME);
        }
        builder.addStatement("return");
        builder.endControlFlow();

        builder.beginControlFlow("switch ($L)", CONVERTER_VALUE_PARAMETER_NAME);
        for (Map.Entry<String, Object> entry : valuesMap.entrySet()) {
            if (entry.getValue() != null) {
                builder.beginControlFlow("case $L:", entry.getKey());
                if (writeToField) {
                    builder.addStatement("$L.$L($L, $L)", CONVERTER_JSON_GENERATOR_PARAMETER_NAME, writeMethodName, CONVERTER_FIELD_NAME_PARAMETER_NAME, toGoodFormat(entry.getValue()));
                } else {
                    builder.addStatement("$L.$L($L)", CONVERTER_JSON_GENERATOR_PARAMETER_NAME, writeMethodName, toGoodFormat(entry.getValue()));
                }
                builder.addStatement("break");
                builder.endControlFlow();
            }
        }
        builder.addStatement("default: throw new IllegalArgumentException($L.name())", CONVERTER_VALUE_PARAMETER_NAME);
        builder.endControlFlow();
    }

    private String toGoodFormat(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            return "\"" + object + "\"";
        }
        if (object instanceof Long) {
            return object + "L";
        }
        return object.toString();
    }

}

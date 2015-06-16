package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.processor.type.field.TypeConverterFieldType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ObjectMapperInjector {

    public static final String JSON_PARSER_VARIABLE_NAME = "jsonParser";
    public static final String JSON_GENERATOR_VARIABLE_NAME = "jsonGenerator";

    private final JsonObjectHolder mJsonObjectHolder;

    public ObjectMapperInjector(JsonObjectHolder jsonObjectHolder) {
        mJsonObjectHolder = jsonObjectHolder;
    }

    public String getJavaClassFile() {
        try {
            return JavaFile.builder(mJsonObjectHolder.packageName, getTypeSpec()).build().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private TypeSpec getTypeSpec() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(mJsonObjectHolder.injectedClassName).addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        builder.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "\"unsafe\"").build());

        if (!mJsonObjectHolder.isAbstractClass) {
            builder.superclass(ParameterizedTypeName.get(ClassName.get(JsonMapper.class), mJsonObjectHolder.objectTypeName));
        }

        // TypeConverters could be expensive to create, so just use one per class
        Set<ClassName> typeConvertersUsed = new HashSet<>();
        for (JsonFieldHolder fieldHolder : mJsonObjectHolder.fieldMap.values()) {
            if (fieldHolder.subType instanceof TypeConverterFieldType) {
                typeConvertersUsed.add(((TypeConverterFieldType)fieldHolder.subType).getTypeConverterClassName());
            }
        }
        for (ClassName typeConverter : typeConvertersUsed) {
            builder.addField(FieldSpec.builder(typeConverter, TextUtils.toUpperCaseWithUnderscores(typeConverter.simpleName()))
                    .addModifiers(Modifier.PROTECTED, Modifier.STATIC, Modifier.FINAL)
                    .initializer("new $T()", typeConverter)
                    .build());
        }

        if (!mJsonObjectHolder.isAbstractClass) {
            builder.addMethod(getParseMethod());
            builder.addMethod(getStaticParseMethod());
        }

        builder.addMethod(getParseFieldMethod());

        if (!mJsonObjectHolder.isAbstractClass) {
            builder.addMethod(getSerializeMethod());
        }

        builder.addMethod(getStaticSerializeMethod());

        return builder.build();
    }

    private MethodSpec getParseMethod() {
        return MethodSpec.methodBuilder("parse")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(mJsonObjectHolder.objectTypeName)
                .addParameter(JsonParser.class, JSON_PARSER_VARIABLE_NAME)
                .addException(IOException.class)
                .addStatement("return _parse($L)", JSON_PARSER_VARIABLE_NAME)
                .build();
    }

    private MethodSpec getStaticParseMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("_parse")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(mJsonObjectHolder.objectTypeName)
                .addParameter(JsonParser.class, JSON_PARSER_VARIABLE_NAME)
                .addException(IOException.class)
                .addStatement("$T instance = new $T()", mJsonObjectHolder.objectTypeName, mJsonObjectHolder.objectTypeName)
                .beginControlFlow("if ($L.getCurrentToken() == null)", JSON_PARSER_VARIABLE_NAME)
                .addStatement("$L.nextToken()", JSON_PARSER_VARIABLE_NAME)
                .endControlFlow()
                .beginControlFlow("if ($L.getCurrentToken() != $T.START_OBJECT)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement("$L.skipChildren()", JSON_PARSER_VARIABLE_NAME)
                .addStatement("return null")
                .endControlFlow()
                .beginControlFlow("while ($L.nextToken() != $T.END_OBJECT)", JSON_PARSER_VARIABLE_NAME, JsonToken.class)
                .addStatement("String fieldName = $L.getCurrentName()", JSON_PARSER_VARIABLE_NAME)
                .addStatement("$L.nextToken()", JSON_PARSER_VARIABLE_NAME)
                .addStatement("parseField(instance, fieldName, $L)", JSON_PARSER_VARIABLE_NAME)
                .addStatement("$L.skipChildren()", JSON_PARSER_VARIABLE_NAME)
                .endControlFlow();

        if (!TextUtils.isEmpty(mJsonObjectHolder.onCompleteCallback)) {
            builder.addStatement("instance.$L()", mJsonObjectHolder.onCompleteCallback);
        }

        return builder
                .addStatement("return instance")
                .build();
    }

    private MethodSpec getParseFieldMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("parseField")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(mJsonObjectHolder.objectTypeName, "instance")
                .addParameter(String.class, "fieldName")
                .addParameter(JsonParser.class, JSON_PARSER_VARIABLE_NAME)
                .addException(IOException.class);

        int parseFieldLines = addParseFieldLines(builder);

        if (mJsonObjectHolder.parentInjectedTypeName != null) {
            if (parseFieldLines > 0) {
                builder.nextControlFlow("else");
            }

            builder.addStatement("$T.parseField(instance, fieldName, $L)", mJsonObjectHolder.parentInjectedTypeName, JSON_PARSER_VARIABLE_NAME);
        }

        if (parseFieldLines > 0) {
            builder.endControlFlow();
        }

        return builder.build();
    }

    private MethodSpec getSerializeMethod() {
        return MethodSpec.methodBuilder("serialize")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mJsonObjectHolder.objectTypeName, "object")
                .addParameter(JsonGenerator.class, JSON_GENERATOR_VARIABLE_NAME)
                .addParameter(boolean.class, "writeStartAndEnd")
                .addException(IOException.class)
                .addStatement("_serialize(object, $L, writeStartAndEnd)", JSON_GENERATOR_VARIABLE_NAME)
                .build();
    }

    private MethodSpec getStaticSerializeMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("_serialize")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(mJsonObjectHolder.objectTypeName, "object")
                .addParameter(JsonGenerator.class, JSON_GENERATOR_VARIABLE_NAME)
                .addParameter(boolean.class, "writeStartAndEnd")
                .addException(IOException.class);

        if (!TextUtils.isEmpty(mJsonObjectHolder.preSerializeCallback)) {
            builder.addStatement("object.$L()", mJsonObjectHolder.preSerializeCallback);
        }

        builder
                .beginControlFlow("if (writeStartAndEnd)")
                .addStatement("$L.writeStartObject()", JSON_GENERATOR_VARIABLE_NAME)
                .endControlFlow();

        for (Map.Entry<String, JsonFieldHolder> entry : mJsonObjectHolder.fieldMap.entrySet()) {
            JsonFieldHolder fieldHolder = entry.getValue();

            if (fieldHolder.shouldSerialize) {
                String getter;
                if (fieldHolder.hasGetter()) {
                    getter = "object." + fieldHolder.getterMethod + "()";
                } else {
                    getter = "object." + entry.getKey();
                }

                fieldHolder.type.serialize(builder, 1, fieldHolder.fieldName[0], getter, true, true, mJsonObjectHolder.serializeNullObjects, mJsonObjectHolder.serializeNullCollectionElements);
            }
        }

        if (mJsonObjectHolder.parentInjectedTypeName != null) {
            builder.addStatement("$T._serialize(object, $L, false)", mJsonObjectHolder.parentInjectedTypeName, JSON_GENERATOR_VARIABLE_NAME);
        }

        builder
                .beginControlFlow("if (writeStartAndEnd)")
                .addStatement("$L.writeEndObject()", JSON_GENERATOR_VARIABLE_NAME)
                .endControlFlow();

        return builder.build();
    }

    private int addParseFieldLines(MethodSpec.Builder builder) {
        int entryCount = 0;
        for (Map.Entry<String, JsonFieldHolder> entry : mJsonObjectHolder.fieldMap.entrySet()) {
            JsonFieldHolder fieldHolder = entry.getValue();

            if (fieldHolder.shouldParse) {
                if (entryCount == 0) {
                    builder.beginControlFlow("if (" + getParseFieldIfStatement(fieldHolder) + ")");
                } else {
                    builder.nextControlFlow("else if (" + getParseFieldIfStatement(fieldHolder) + ")");
                }

                String setter;
                Object[] stringFormatArgs;
                if (fieldHolder.hasSetter()) {
                    setter = "instance.$L($L)";
                    stringFormatArgs = new Object[] { fieldHolder.setterMethod };
                } else {
                    setter = "instance.$L = $L";
                    stringFormatArgs = new Object[] { entry.getKey() };
                }

                fieldHolder.type.parse(builder, 1, setter, stringFormatArgs);

                entryCount++;
            }
        }
        return entryCount;
    }

    private String getParseFieldIfStatement(JsonFieldHolder fieldHolder) {
        StringBuilder ifStatement = new StringBuilder();
        boolean isFirst = true;
        for (String fieldName : fieldHolder.fieldName) {
            if (isFirst) {
                isFirst = false;
            } else {
                ifStatement.append(" || ");
            }
            ifStatement.append('\"').append(fieldName).append("\".equals(fieldName)");
        }
        return ifStatement.toString();
    }
}

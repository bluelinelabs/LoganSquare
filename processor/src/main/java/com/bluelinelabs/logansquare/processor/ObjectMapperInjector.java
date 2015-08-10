package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.processor.type.field.ParameterizedType;
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
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeParameterElement;

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

        for (TypeParameterElement typeParameterElement : mJsonObjectHolder.typeParameters) {
            builder.addTypeVariable(TypeVariableName.get(typeParameterElement.getSimpleName().toString()));
        }

        // TypeConverters could be expensive to create, so just use one per class
        Set<ClassName> typeConvertersUsed = new HashSet<>();
        for (JsonFieldHolder fieldHolder : mJsonObjectHolder.fieldMap.values()) {
            if (fieldHolder.type instanceof TypeConverterFieldType) {
                typeConvertersUsed.add(((TypeConverterFieldType)fieldHolder.type).getTypeConverterClassName());
            }
        }
        for (ClassName typeConverter : typeConvertersUsed) {
            builder.addField(FieldSpec.builder(typeConverter, TextUtils.toUpperCaseWithUnderscores(typeConverter.simpleName()))
                    .addModifiers(Modifier.PROTECTED, Modifier.STATIC, Modifier.FINAL)
                    .initializer("new $T()", typeConverter)
                    .build());
        }

        if (mJsonObjectHolder.typeParameters.size() > 0) {
            MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);
            for (TypeParameterElement typeParameterElement : mJsonObjectHolder.typeParameters) {
                final String typeName = typeParameterElement.getSimpleName().toString();
                final String classArgumentName = typeName + "Class";
                final String jsonMapperVariableName = getJsonMapperVariableNameForTypeParameter(typeName);

                // Add a JsonMapper reference
                builder.addField(FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(JsonMapper.class), TypeVariableName.get(typeName)), jsonMapperVariableName)
                        .addModifiers(Modifier.PRIVATE, Modifier.FINAL)

                        .build());

                constructorBuilder.addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), TypeVariableName.get(typeName)), classArgumentName);
                constructorBuilder.addStatement("$L = $T.mapperFor($L)", jsonMapperVariableName, LoganSquare.class, classArgumentName);
            }
            builder.addMethod(constructorBuilder.build());
        }

        final boolean includeStaticMethods = mJsonObjectHolder.typeParameters.size() == 0;
        if (!mJsonObjectHolder.isAbstractClass) {
            builder.addMethod(getParseMethod(includeStaticMethods));

            if (includeStaticMethods) {
                builder.addMethod(getStaticParseMethod());
            }
        }

        builder.addMethod(getParseFieldMethod(includeStaticMethods));

        if (!mJsonObjectHolder.isAbstractClass) {
            builder.addMethod(getSerializeMethod(includeStaticMethods));
        }

        if (includeStaticMethods) {
            builder.addMethod(getStaticSerializeMethod());
        }

        return builder.build();
    }

    private MethodSpec getParseMethod(boolean referToStatic) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("parse")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(mJsonObjectHolder.objectTypeName)
                .addParameter(JsonParser.class, JSON_PARSER_VARIABLE_NAME)
                .addException(IOException.class);

        if (referToStatic) {
            builder.addStatement("return _parse($L)", JSON_PARSER_VARIABLE_NAME);
        } else {
            insertParseStatements(builder);
        }

        return builder.build();
    }

    private MethodSpec getStaticParseMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("_parse")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(mJsonObjectHolder.objectTypeName)
                .addParameter(JsonParser.class, JSON_PARSER_VARIABLE_NAME)
                .addException(IOException.class);

        insertParseStatements(builder);

        for (TypeParameterElement typeParameterElement : mJsonObjectHolder.typeParameters) {
            builder.addTypeVariable(TypeVariableName.get(typeParameterElement.getSimpleName().toString()));
        }

        return builder.build();
    }

    private void insertParseStatements(MethodSpec.Builder builder) {
        builder.addStatement("$T instance = new $T()", mJsonObjectHolder.objectTypeName, mJsonObjectHolder.objectTypeName)
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

        builder.addStatement("return instance");
    }

    private MethodSpec getParseFieldMethod(boolean makeStatic) {
        Modifier[] modifiers = makeStatic ? new Modifier[] {Modifier.PUBLIC, Modifier.STATIC} : new Modifier[] {Modifier.PUBLIC};

        MethodSpec.Builder builder = MethodSpec.methodBuilder("parseField")
                .addModifiers(modifiers)
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

        if (makeStatic) {
            for (TypeParameterElement typeParameterElement : mJsonObjectHolder.typeParameters) {
                builder.addTypeVariable(TypeVariableName.get(typeParameterElement.getSimpleName().toString()));
            }
        }

        return builder.build();
    }

    private MethodSpec getSerializeMethod(boolean referToStatic) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("serialize")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mJsonObjectHolder.objectTypeName, "object")
                .addParameter(JsonGenerator.class, JSON_GENERATOR_VARIABLE_NAME)
                .addParameter(boolean.class, "writeStartAndEnd")
                .addException(IOException.class);

        if (referToStatic) {
            builder.addStatement("_serialize(object, $L, writeStartAndEnd)", JSON_GENERATOR_VARIABLE_NAME);
        } else {
            insertSerializeStatements(builder);
        }

        return builder.build();
    }

    private MethodSpec getStaticSerializeMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("_serialize")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(mJsonObjectHolder.objectTypeName, "object")
                .addParameter(JsonGenerator.class, JSON_GENERATOR_VARIABLE_NAME)
                .addParameter(boolean.class, "writeStartAndEnd")
                .addException(IOException.class);

        insertSerializeStatements(builder);

        for (TypeParameterElement typeParameterElement : mJsonObjectHolder.typeParameters) {
            builder.addTypeVariable(TypeVariableName.get(typeParameterElement.getSimpleName().toString()));
        }

        return builder.build();
    }

    private void insertSerializeStatements(MethodSpec.Builder builder) {
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

                if (fieldHolder.type != null) {
                    if (fieldHolder.type instanceof ParameterizedType) {
                        ParameterizedType parameterizedType = (ParameterizedType)fieldHolder.type;
                        parameterizedType.setJsonMapperVariableName(getJsonMapperVariableNameForTypeParameter(parameterizedType.getParameterName()));
                    }
                    fieldHolder.type.parse(builder, 1, setter, stringFormatArgs);
                }

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

    private String getJsonMapperVariableNameForTypeParameter(String typeName) {
        return "m" + typeName + "ClassJsonMapper";
    }
}

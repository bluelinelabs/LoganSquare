package com.bluelinelabs.logansquare.processor.type.field;

import com.bluelinelabs.logansquare.Constants;
import com.bluelinelabs.logansquare.processor.JsonAnnotationProcessor;
import com.bluelinelabs.logansquare.processor.JsonMapperLoaderInjector;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class JsonFieldType extends FieldType {

    private final ClassName mClassName;

    public JsonFieldType(ProcessingEnvironment env, ClassName className) {
        mClassName = className;
    }

    @Override
    public TypeName getTypeName() {
        return mClassName;
    }

    @Override
    public TypeName getNonPrimitiveTypeName() {
        return mClassName;
    }

    @Override
    public void parse(Builder builder, int depth, String setter, Object... setterFormatArgs) {
        setter = replaceLastLiteral(setter, "$T.INSTANCE.parse($L)");
        builder.addStatement(setter, expandStringArgs(setterFormatArgs, generateMapperClassName(mClassName), JSON_PARSER_VARIABLE_NAME));
    }

    @Override
    public void serialize(Builder builder, int depth, String fieldName, List<String> processedFieldNames, String getter, boolean isObjectProperty, boolean checkIfNull, boolean writeIfNull, boolean writeCollectionElementIfNull) {

        if (checkIfNull) {
            builder.beginControlFlow("if ($L != null)", getter);
        }

        if (isObjectProperty) {
            builder.addStatement("$L.writeFieldName($S)", JSON_GENERATOR_VARIABLE_NAME, fieldName);
        }

        builder.addStatement("$T.INSTANCE.serialize($L, $L, true)", generateMapperClassName(mClassName), getter, JSON_GENERATOR_VARIABLE_NAME);

        if (checkIfNull) {
            if (writeIfNull) {
                builder.nextControlFlow("else");

                if (isObjectProperty) {
                    builder.addStatement("$L.writeFieldName($S)", JSON_GENERATOR_VARIABLE_NAME, fieldName);
                }
                builder.addStatement("$L.writeNull()", JSON_GENERATOR_VARIABLE_NAME);
            }

            builder.endControlFlow();
        }
    }

    public static ClassName generateMapperClassName(ClassName className) {
        final String packageName = className.packageName();
        final StringBuilder nameBuilder = new StringBuilder();
        for (String simpleName : className.simpleNames()) {
            if (nameBuilder.length() > 0) {
                nameBuilder.append('$');
            }
            nameBuilder.append(simpleName);
        }
        nameBuilder.append(Constants.MAPPER_CLASS_SUFFIX);
        return ClassName.get(packageName, nameBuilder.toString());
    }
}

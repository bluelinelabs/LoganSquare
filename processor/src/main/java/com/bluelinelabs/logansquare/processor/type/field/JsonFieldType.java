package com.bluelinelabs.logansquare.processor.type.field;

import com.bluelinelabs.logansquare.Constants;
import com.bluelinelabs.logansquare.processor.JsonMapperLoaderInjector;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class JsonFieldType extends FieldType {

    private final ClassName mClassName;
    private final ClassName mLoaderClassName;
    private final String mMapperVariableName;

    public JsonFieldType(ClassName className) {
        mClassName = className;
        mLoaderClassName = ClassName.get(Constants.LOADER_PACKAGE_NAME, Constants.LOADER_CLASS_NAME);
        mMapperVariableName = JsonMapperLoaderInjector.getMapperVariableName(mClassName.toString() + Constants.MAPPER_CLASS_SUFFIX);
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
        setter = replaceLastLiteral(setter, "$T.$L.parse($L)");
        builder.addStatement(setter, expandStringArgs(setterFormatArgs, mLoaderClassName, mMapperVariableName, JSON_PARSER_VARIABLE_NAME));
    }

    @Override
    public void serialize(Builder builder, int depth, String fieldName, String getter, boolean isObjectProperty, boolean checkIfNull, boolean writeIfNull, boolean writeCollectionElementIfNull) {

        if (checkIfNull) {
            builder.beginControlFlow("if ($L != null)", getter);
        }

        if (isObjectProperty) {
            builder.addStatement("$L.writeFieldName($S)", JSON_GENERATOR_VARIABLE_NAME, fieldName);
        }

        builder.addStatement("$T.$L.serialize($L, $L, true)", mLoaderClassName, mMapperVariableName, getter, JSON_GENERATOR_VARIABLE_NAME);

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
}

package com.bluelinelabs.logansquare.processor.type.field;

import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class ParameterizedType extends FieldType {

    private final TypeName mTypeName;
    private String mJsonMapperVariableName;

    public ParameterizedType(TypeMirror typeMirror) {
        mTypeName = TypeName.get(typeMirror);
    }

    @Override
    public void parse(Builder builder, int depth, String setter, Object... setterFormatArgs) {
        setter = replaceLastLiteral(setter, String.format("%s.parse($L)", mJsonMapperVariableName));
        builder.addStatement(setter, expandStringArgs(setterFormatArgs, JSON_PARSER_VARIABLE_NAME));
    }

    @Override
    public void serialize(Builder builder, int depth, String fieldName, String getter, boolean isObjectProperty, boolean checkIfNull, boolean writeIfNull, boolean writeCollectionElementIfNull) {
        if (checkIfNull) {
            builder.beginControlFlow("if ($L != null)", getter);
        }

        builder.addStatement("$L.writeFieldName($S)", JSON_GENERATOR_VARIABLE_NAME, fieldName);
        builder.addStatement("$L.serialize($L, $L, true)", mJsonMapperVariableName, getter, JSON_GENERATOR_VARIABLE_NAME);

        if (checkIfNull) {
            if (writeIfNull) {
                builder.nextControlFlow("else");

                builder.addStatement("$L.writeFieldName($S)", JSON_GENERATOR_VARIABLE_NAME, fieldName);
                builder.addStatement("$L.writeNull()", JSON_GENERATOR_VARIABLE_NAME);
            }
            builder.endControlFlow();
        }
    }

    @Override
    public String getParameterizedTypeString() {
        StringBuilder string = new StringBuilder("$T<");
        for (int i = 0; i < parameterTypes.size(); i++) {
            if (i > 0) {
                string.append(", ");
            }
            string.append(parameterTypes.get(i).getParameterizedTypeString());
        }
        string.append('>');
        return string.toString();
    }

    @Override
    public Object[] getParameterizedTypeStringArgs() {
        return null;
    }

    @Override
    public TypeName getNonPrimitiveTypeName() {
        return mTypeName;
    }

    @Override
    public TypeName getTypeName() {
        return mTypeName;
    }

    public void setJsonMapperVariableName(String jsonMapperVariableName) {
        mJsonMapperVariableName = jsonMapperVariableName;
    }

    public String getParameterName() {
        return mTypeName.toString();
    }
}

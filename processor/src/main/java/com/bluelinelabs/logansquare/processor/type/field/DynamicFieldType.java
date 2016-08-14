package com.bluelinelabs.logansquare.processor.type.field;

import com.bluelinelabs.logansquare.processor.ObjectMapperInjector;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import java.util.List;
import java.util.Set;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class DynamicFieldType extends FieldType {

    private TypeName mTypeName;

    public DynamicFieldType(TypeName typeName) {
        mTypeName = typeName;
    }

    @Override
    public TypeName getTypeName() {
        return mTypeName;
    }

    @Override
    public TypeName getNonPrimitiveTypeName() {
        return mTypeName;
    }

    @Override
    public void parse(Builder builder, int depth, String setter, Object... setterFormatArgs) {
        setter = replaceLastLiteral(setter, ObjectMapperInjector.getTypeConverterGetter(mTypeName) + "().parse($L)");
        builder.addStatement(setter, expandStringArgs(setterFormatArgs, JSON_PARSER_VARIABLE_NAME));
    }

    @Override
    public void serialize(Builder builder, int depth, String fieldName, List<String> processedFieldNames, String getter, boolean isObjectProperty, boolean checkIfNull, boolean writeIfNull, boolean writeCollectionElementIfNull) {
        if (!mTypeName.isPrimitive() && checkIfNull) {
            builder.beginControlFlow("if ($L != null)", getter);
        }

        builder.addStatement(ObjectMapperInjector.getTypeConverterGetter(mTypeName) + "().serialize($L, $S, $L, $L)", getter, isObjectProperty ? fieldName : null, isObjectProperty, JSON_GENERATOR_VARIABLE_NAME);
        if (!mTypeName.isPrimitive() && checkIfNull) {
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

    @Override
    public Set<TypeName> getUsedTypeConverters() {
        Set<TypeName> set = super.getUsedTypeConverters();
        set.add(mTypeName);
        return set;
    }
}

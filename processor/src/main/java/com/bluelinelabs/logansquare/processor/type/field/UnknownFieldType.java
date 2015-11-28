package com.bluelinelabs.logansquare.processor.type.field;

import com.bluelinelabs.logansquare.internal.objectmappers.ObjectMapper;
import com.bluelinelabs.logansquare.processor.ObjectMapperInjector;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import java.util.List;
import java.util.Set;

import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_GENERATOR_VARIABLE_NAME;
import static com.bluelinelabs.logansquare.processor.ObjectMapperInjector.JSON_PARSER_VARIABLE_NAME;

public class UnknownFieldType extends FieldType {

    @Override
    public TypeName getTypeName() {
        return ClassName.get(Object.class);
    }

    @Override
    public TypeName getNonPrimitiveTypeName() {
        return ClassName.get(Object.class);
    }

    @Override
    public void parse(Builder builder, int depth, String setter, Object... setterFormatArgs) {
        setter = replaceLastLiteral(setter, "$L.parse($L)");
        builder.addStatement(setter, expandStringArgs(setterFormatArgs, ObjectMapperInjector.getMapperVariableName(ObjectMapper.class), JSON_PARSER_VARIABLE_NAME));
    }

    @Override
    public void serialize(Builder builder, int depth, String fieldName, List<String> processedFieldNames, String getter, boolean isObjectProperty, boolean checkIfNull, boolean writeIfNull, boolean writeCollectionElementIfNull) {
        if (checkIfNull) {
            builder.beginControlFlow("if ($L != null)", getter);
        }

        builder.addStatement("$L.serialize($L, $L, $L)", ObjectMapperInjector.getMapperVariableName(ObjectMapper.class), getter, JSON_GENERATOR_VARIABLE_NAME, isObjectProperty);

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

    @Override
    public Set<ClassNameObjectMapper> getUsedJsonObjectMappers() {
        Set<ClassNameObjectMapper> set = super.getUsedJsonObjectMappers();
        set.add(new ClassNameObjectMapper(ClassName.get(Object.class), ObjectMapper.class.getCanonicalName()));
        return set;
    }
}

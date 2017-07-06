package com.bluelinelabs.logansquare.processor;

import com.google.testing.compile.JavaFileObjects;
import org.junit.Test;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class ConstructorInjectionTest {
    @Test
    public void testPrivateFields() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/good/PrivateFieldsWithoutSettersModel.java"))
                .processedWith(new JsonAnnotationProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forResource("generated/PrivateFieldsWithoutSettersModel$$JsonObjectMapper.java"));
    }

    @Test
    public void testImmutableFields() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/good/ImmutableFieldsModel.java"))
                .processedWith(new JsonAnnotationProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forResource("generated/ImmutableFieldsModel$$JsonObjectMapper.java"));
    }
}

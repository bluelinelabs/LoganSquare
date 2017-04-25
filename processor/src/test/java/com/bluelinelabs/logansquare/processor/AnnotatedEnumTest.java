package com.bluelinelabs.logansquare.processor;

import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class AnnotatedEnumTest {

    @Test
    public void generatedBooleanEnumSource() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/good/BooleanEnum.java"))
                .processedWith(new JsonAnnotationProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forResource("generated/BooleanEnum$$JsonTypeConverter.java"));
    }

    @Test
    public void generatedNumberEnumSource() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/good/NumberEnum.java"))
                .processedWith(new JsonAnnotationProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forResource("generated/NumberEnum$$JsonTypeConverter.java"));
    }

    @Test
    public void generatedStringEnumSource() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/good/StringEnum.java"))
                .processedWith(new JsonAnnotationProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forResource("generated/StringEnum$$JsonTypeConverter.java"));
    }

    @Test
    public void generatedNullableEnumSource() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/good/NullableEnum.java"))
                .processedWith(new JsonAnnotationProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forResource("generated/NullableEnum$$JsonTypeConverter.java"));
    }

    @Test
    public void generatedLowerCaseNamingPolicyEnumSource() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/good/LowerCaseNamingPolicyEnum.java"))
                .processedWith(new JsonAnnotationProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forResource("generated/LowerCaseNamingPolicyEnum$$JsonTypeConverter.java"));
    }

    @Test
    public void generatedDefaultNamingPolicyEnumSource() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/good/DefaultNamingPolicyEnum.java"))
                .processedWith(new JsonAnnotationProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forResource("generated/DefaultNamingPolicyEnum$$JsonTypeConverter.java"));
    }

}

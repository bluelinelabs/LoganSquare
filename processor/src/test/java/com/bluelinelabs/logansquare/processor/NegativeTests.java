package com.bluelinelabs.logansquare.processor;

import com.google.testing.compile.JavaFileObjects;
import org.junit.Test;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class NegativeTests {

    @Test
    public void fieldWithoutObject() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/bad/FieldWithoutObjectModel.java"))
                .processedWith(new JsonAnnotationProcessor())
                .failsToCompile();
    }

    @Test
    public void privateField() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/bad/PrivateFieldModel.java"))
                .processedWith(new JsonAnnotationProcessor())
                .failsToCompile();
    }

    @Test
    public void invalidTypeConverter() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/bad/InvalidTypeConverterModel.java"))
                .processedWith(new JsonAnnotationProcessor())
                .failsToCompile();
    }

    @Test
    public void methodObject() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/bad/MethodWithoutObjectModel.java"))
                .processedWith(new JsonAnnotationProcessor())
                .failsToCompile();
    }

    @Test
    public void methodWithArgs() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/bad/MethodWithArgsModel.java"))
                .processedWith(new JsonAnnotationProcessor())
                .failsToCompile();
    }

    @Test
    public void multipleMethods() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/bad/MultipleMethodsModel.java"))
                .processedWith(new JsonAnnotationProcessor())
                .failsToCompile();
    }
}

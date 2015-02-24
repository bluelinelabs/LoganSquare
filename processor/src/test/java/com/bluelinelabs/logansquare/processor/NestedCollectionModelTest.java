package com.bluelinelabs.logansquare.processor;

import com.google.testing.compile.JavaFileObjects;
import org.junit.Test;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class NestedCollectionModelTest {

    @Test
    public void generatedSource() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/good/NestedCollectionModel.java"))
                .processedWith(new JsonAnnotationProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forResource("generated/NestedCollectionModel$$JsonObjectMapper.java"));
    }
}

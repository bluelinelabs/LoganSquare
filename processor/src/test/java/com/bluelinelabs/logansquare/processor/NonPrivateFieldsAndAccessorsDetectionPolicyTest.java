package com.bluelinelabs.logansquare.processor;

import com.google.testing.compile.JavaFileObjects;
import org.junit.Test;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class NonPrivateFieldsAndAccessorsDetectionPolicyTest {

    @Test
    public void generatedSource() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/good/NonPrivateFieldsAndAccessorsFieldDetectionPolicyModel.java"))
                .processedWith(new JsonAnnotationProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forResource("generated/NonPrivateFieldsAndAccessorsFieldDetectionPolicyModel$$JsonObjectMapper.java"));
    }
}

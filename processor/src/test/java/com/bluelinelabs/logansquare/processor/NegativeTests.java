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
                .failsToCompile()
                .withErrorContaining("@JsonField fields can only be in classes annotated with @JsonObject.");
    }

    @Test
    public void genericObject() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/bad/GenericModel.java"))
                .processedWith(new JsonAnnotationProcessor())
                .failsToCompile()
                .withErrorContaining("@JsonObject annotation can't be used on generic classes.");
    }

    @Test
    public void privateField() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/bad/PrivateFieldModelWithoutAccessors.java"))
                .processedWith(new JsonAnnotationProcessor())
                .failsToCompile()
                .withErrorContaining("@JsonField annotation can only be used on private fields if both getter and setter are present.");
    }

    @Test
    public void invalidTypeConverter() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/bad/InvalidTypeConverterModel.java"))
                .processedWith(new JsonAnnotationProcessor())
                .failsToCompile()
                .withErrorContaining("TypeConverter elements must implement the TypeConverter interface or extend from one of the convenience helpers");
    }

    @Test
    public void methodObject() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/bad/MethodWithoutObjectModel.java"))
                .processedWith(new JsonAnnotationProcessor())
                .failsToCompile()
                .withErrorContaining("@OnJsonParseComplete methods can only be in classes annotated with @JsonObject.");
    }

    @Test
    public void methodWithArgs() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/bad/MethodWithArgsModel.java"))
                .processedWith(new JsonAnnotationProcessor())
                .failsToCompile()
                .withErrorContaining("@OnJsonParseComplete methods must not take any parameters.");
    }

    @Test
    public void multipleMethods() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/bad/MultipleMethodsModel.java"))
                .processedWith(new JsonAnnotationProcessor())
                .failsToCompile()
                .withErrorContaining("There can only be one @OnJsonParseComplete method per class");
    }
}

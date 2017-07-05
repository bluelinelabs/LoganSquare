package com.bluelinelabs.logansquare.processor;

import com.google.testing.compile.JavaFileObjects;
import org.junit.Test;

import javax.tools.JavaFileObject;
import java.util.Arrays;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

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
    public void privateField() {
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forResource("model/bad/PrivateFieldModelWithoutAccessors.java"))
                .processedWith(new JsonAnnotationProcessor())
                .failsToCompile()
                .withErrorContaining("@JsonField annotation can only be used on private fields if a getter is present.");
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
    public void inheritFromModelWithConstructorInjection() {
        JavaFileObject badDescendant = JavaFileObjects.forResource("model/bad/ImmutableFieldsInheritanceModel.java");
        JavaFileObject okParent = JavaFileObjects.forResource("model/good/ImmutableFieldsModel.java");

        ASSERT.about(javaSources())
                .that(Arrays.asList(okParent, badDescendant))
                .processedWith(new JsonAnnotationProcessor())
                .failsToCompile()
                .withErrorContaining("Subclassing from models with constructor injection is not supported")
                .in(badDescendant);
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

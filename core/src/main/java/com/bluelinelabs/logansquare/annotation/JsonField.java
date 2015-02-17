package com.bluelinelabs.logansquare.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Declare that a variable should be parsed/serialized.
 * <pre><code>
 * {@literal @}JsonField(name = "random_variable_name")
 * public String randomVariableName;
 * </code></pre>
 */
@Target(FIELD)
@Retention(CLASS)
public @interface JsonField {

    /** The name(s) of this field in JSON. Use an array if this could be represented by multiple names. */
    String[] name() default {};

    /** The TypeConverter that will be used to parse/serialize this variable. */
    Class typeConverter() default void.class;

}

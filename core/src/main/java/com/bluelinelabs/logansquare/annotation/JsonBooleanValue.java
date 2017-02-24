package com.bluelinelabs.logansquare.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Declare that a enum value should be parsed/serialized.
 * <pre><code>
 * {@literal @}JsonEnum
 * public enum MyEnum {
 *
 *     {@literal @}@JsonBooleanValue(true)
 *     VALUE_FOR_TRUE,
 *     {@literal @}@JsonBooleanValue(false)
 *     VALUE_FOR_FALSE
 *
 * }
 * </code></pre>
 */
@Target(FIELD)
@Retention(CLASS)
public @interface JsonBooleanValue {

    /**
     * The boolean representation of this enum value in JSON.
     */
    boolean value();

}

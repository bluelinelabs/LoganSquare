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
 *     {@literal @}@JsonStringValue("value_1")
 *     VALUE_1,
 *     {@literal @}@JsonStringValue("value_2")
 *     VALUE_2
 *
 * }
 * </code></pre>
 */
@Target(FIELD)
@Retention(CLASS)
public @interface JsonStringValue {

    /**
     * The string representation of this enum value in JSON.
     */
    String value();

}

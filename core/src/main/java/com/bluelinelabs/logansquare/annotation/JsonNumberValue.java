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
 *     {@literal @}@JsonNumberValue(1)
 *     VALUE_ONE,
 *     {@literal @}@JsonNumberValue(2)
 *     VALUE_TWO
 *
 * }
 * </code></pre>
 */
@Target(FIELD)
@Retention(CLASS)
public @interface JsonNumberValue {

    /**
     * The number representation of this enum value in JSON.
     */
    long value();

}

package com.bluelinelabs.logansquare.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Declare that a enum value should be parsed/serialized as null.
 * So if enum contains value annotated with it
 * then parser fill fields with that value instead of null
 * and serializer will write null if field contains that value.
 * <pre><code>
 * {@literal @}JsonEnum
 * public enum MyEnum {
 *
 *     {@literal @}@JsonNullValue
 *     VALUE_EMPTY,
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
public @interface JsonNullValue {
}

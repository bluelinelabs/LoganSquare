package com.bluelinelabs.logansquare.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Declare that a Java enum is parsable and serializable.
 * <pre><code>
 * {@literal @}JsonEnum
 * public enum MyEnum {
 *
 *     {@literal @}@JsonStringValue("value_1")
 *     VALUE_1,
 *     {@literal @}@JsonStringValue("value_2")
 *     VALUE_2,
 *     {@literal @}@JsonNullValue
 *     VALUE_NULL
 *
 * }
 * </code></pre>
 */
@Target(TYPE)
@Retention(CLASS)
public @interface JsonEnum {

    public enum ValueNamingPolicy {
        /**
         * Use the Java value's name, unless the 'value' parameter is
         * passed into the @JsonStringValue, @JsonNumberValue or @jsonBooleanValue annotation
         */
        VALUE_NAME,

        /**
         * Use the Java value's name converted to lower case separated by
         * underscores, unless the 'value' parameter is
         * passed into the @JsonStringValue, @JsonNumberValue or @jsonBooleanValue annotation
         */
        LOWER_CASE_WITH_UNDERSCORES
    }

    /**
     * Allows control over what value names LoganSquare expects in the JSON when parsing
     * and how the values are named while serializing. By default, value names match
     * the name of the Java value unless the 'value' parameter is
     * passed into the value's @JsonStringValue, @JsonNumberValue or @jsonBooleanValue annotation
     */
    ValueNamingPolicy valueNamingPolicy() default ValueNamingPolicy.VALUE_NAME;

}

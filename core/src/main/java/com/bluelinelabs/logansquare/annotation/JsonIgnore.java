package com.bluelinelabs.logansquare.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Declare that a variable should NOT be parsed/serialized when using a fieldDetectionPolicy other than
 * ANNOTATIONS_ONLY.
 * <pre><code>
 * {@literal @}JsonIgnore
 * public String variableIDontWantParsedOrSerialized;
 * </code></pre>
 */
@Target(FIELD)
@Retention(CLASS)
public @interface JsonIgnore {

    public enum IgnorePolicy {
        /** This field will be ignored for both parsing and serializing. */
        PARSE_AND_SERIALIZE,

        /** This field will be ignored for parsing, but will still be serialized. */
        PARSE_ONLY,

        /** This field will be ignored for serializing, but will still be parsed. */
        SERIALIZE_ONLY
    }

    /**
     * Allows control over whether a field should be parsed and/or serialized or not
     */
    IgnorePolicy ignorePolicy() default IgnorePolicy.PARSE_AND_SERIALIZE;
}

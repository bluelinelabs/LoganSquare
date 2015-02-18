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
public @interface JsonIgnore { }

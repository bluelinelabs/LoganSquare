package com.bluelinelabs.logansquare.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Declare that a Java object is parsable and serializable.
 * <pre><code>
 * {@literal @}JsonObject
 * public class MyClass {
 *     ...
 * }
 * </code></pre>
 */
@Target(TYPE)
@Retention(CLASS)
public @interface JsonObject { }

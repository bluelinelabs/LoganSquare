package com.bluelinelabs.logansquare.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Declare that a method should be called once a class has been parsed from JSON.
 * <pre><code>
 * {@literal @}OnJsonParseComplete
 * public void postParseMethod() {
 *     ...
 * }
 * </code></pre>
 */
@Target(METHOD)
@Retention(CLASS)
public @interface OnJsonParseComplete { }
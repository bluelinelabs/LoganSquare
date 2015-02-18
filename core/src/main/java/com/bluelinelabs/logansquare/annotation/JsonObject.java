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
public @interface JsonObject {

    public enum FieldDetectionPolicy {
        /** Only parse and serialize fields annotated with @JsonField */
        ANNOTATIONS_ONLY,

        /**
         * Parse and serialize all non-private fields that haven't been
         * annotated with @JsonIgnore.
         */
        NONPRIVATE_FIELDS,

        /**
         * Parse and serialize all non-private fields and accessors that
         * haven't been annotated with @JsonIgnore.
         */
        NONPRIVATE_FIELDS_AND_ACCESSORS
    }

    public enum FieldNamingPolicy {
        /**
         * Use the Java variable's name, unless the 'name' parameter is
         * passed into the @JsonField annotation
         */
        FIELD_NAME,

        /**
         * Use the Java variable's name converted to lower case separated by
         * underscores, unless the 'name' parameter is passed into the @JsonField
         * annotation
         */
        LOWER_CASE_WITH_UNDERSCORES
    }

    /**
     * Allows control over which fields will be used by LoganSquare for parsing and
     * serializing. By default, only fields annotated with @JsonField will be used.
     */
    FieldDetectionPolicy fieldDetectionPolicy() default FieldDetectionPolicy.ANNOTATIONS_ONLY;

    /**
     * Allows control over what field names LoganSquare expects in the JSON when parsing
     * and how the fields are named while serializing. By default, field names match
     * the name of the Java variable unless the 'name' parameter is passed into a
     * field's @JsonField annotation.
     */
    FieldNamingPolicy fieldNamingPolicy() default FieldNamingPolicy.FIELD_NAME;
}

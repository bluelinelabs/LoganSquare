package com.bluelinelabs.logansquare;

/**
 * The exception that will be thrown in the event that LoganSquare.mapperFor() is
 * called with a class that hasn't been declared as a @JsonObject.
 */
public class NoSuchMapperException extends RuntimeException {

    public NoSuchMapperException(Class cls, Exception e) {
        super("Class " + cls.getCanonicalName() + " could not be mapped to a JSON object. Perhaps it hasn't been annotated with @JsonObject?", e);
    }

}

package com.bluelinelabs.logansquare;

/**
 * The exception that will be thrown in the event that LoganSquare.typeConverterFor() is
 * called with a class that doesn't have a defined TypeConverter.
 */
public class NoSuchTypeConverterException extends RuntimeException {

    public NoSuchTypeConverterException(Class cls) {
        super("Class " + cls.getCanonicalName() + " does not have a TypeConverter defined. TypeConverters can be added using LoganSquare.registerTypeConverter().");
    }

}

package com.bluelinelabs.logansquare.typeconverters;

import java.text.DateFormat;

/** The default DateTypeConverter implementation. Attempts to parse ISO8601-formatted dates. */
public class DefaultDateConverter extends DateTypeConverter {

    public DateFormat getDateFormat() {
        return new DefaultDateFormatter();
    }

}

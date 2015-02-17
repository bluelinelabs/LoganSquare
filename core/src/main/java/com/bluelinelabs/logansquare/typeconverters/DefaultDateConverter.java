package com.bluelinelabs.logansquare.typeconverters;

import java.text.DateFormat;

/** The default DateTypeConverter implementation. Attempts to parse ISO8601-formatted dates. */
public class DefaultDateConverter extends DateTypeConverter {

    private DateFormat mDateFormat;

    public DefaultDateConverter() {
        mDateFormat = new DefaultDateFormatter();
    }

    public DateFormat getDateFormat() {
        return mDateFormat;
    }

}

package com.bluelinelabs.logansquare.typeconverters;

import java.text.DateFormat;

/** The default CalendarTypeConverter implementation. Attempts to parse ISO8601-formatted dates. */
public class DefaultCalendarConverter extends CalendarTypeConverter {

    private DateFormat mDateFormat;

    public DefaultCalendarConverter() {
        mDateFormat = new DefaultDateFormatter();
    }

    public DateFormat getDateFormat() {
        return mDateFormat;
    }

}

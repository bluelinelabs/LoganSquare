package com.bluelinelabs.logansquare.typeconverters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

public abstract class CalendarTypeConverter implements TypeConverter<Calendar> {

    /** Called to get the DateFormat used to parse and serialize objects */
    public abstract DateFormat getDateFormat();

    private static final Object FORMATTER_LOCK = new Object();

    @Override
    public Calendar parse(JsonParser jsonParser) throws IOException {
        String dateString = jsonParser.getValueAsString(null);
        try {
            // DateFormat is not thread-safe, so this has to be synchronized
            synchronized (FORMATTER_LOCK) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(getDateFormat().parse(dateString));
                return calendar;
            }
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public void serialize(Calendar object, String fieldName, boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {
        // DateFormat is not thread-safe, so this has to be synchronized
        synchronized (FORMATTER_LOCK) {
            jsonGenerator.writeStringField(fieldName, getDateFormat().format(object.getTimeInMillis()));
        }
    }

}

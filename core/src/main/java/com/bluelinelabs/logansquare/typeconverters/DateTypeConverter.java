package com.bluelinelabs.logansquare.typeconverters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public abstract class DateTypeConverter implements TypeConverter<Date> {

    /** Called to get the DateFormat used to parse and serialize objects */
    public abstract DateFormat getDateFormat();

    private static final Object FORMATTER_LOCK = new Object();

    @Override
    public Date parse(JsonParser jsonParser) throws IOException {
        String dateString = jsonParser.getValueAsString(null);
        try {
            // DateFormat is not thread-safe, so this has to be synchronized
            synchronized (FORMATTER_LOCK) {
                return getDateFormat().parse(dateString);
            }
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public void serialize(Date object, String fieldName, JsonGenerator jsonGenerator) throws IOException {
        // DateFormat is not thread-safe, so this has to be synchronized
        synchronized (FORMATTER_LOCK) {
            jsonGenerator.writeStringField(fieldName, getDateFormat().format(object));
        }
    }

}

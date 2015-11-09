package com.bluelinelabs.logansquare.typeconverters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

public abstract class CalendarTypeConverter implements TypeConverter<Calendar> {

    // DateFormat is not thread-safe, so wrap it in a ThreadLocal
    private final ThreadLocal<DateFormat> mDateFormat = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return getDateFormat();
        }
    };

    @Override
    public Calendar parse(JsonParser jsonParser) throws IOException {
        String dateString = jsonParser.getValueAsString(null);
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mDateFormat.get().parse(dateString));
            return calendar;
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public void serialize(Calendar object, String fieldName, boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {
        if (fieldName != null) {
            jsonGenerator.writeStringField(fieldName, mDateFormat.get().format(object.getTimeInMillis()));
        } else {
            jsonGenerator.writeString(mDateFormat.get().format(object.getTimeInMillis()));
        }
    }

    /** Called to get the DateFormat used to parse and serialize objects */
    public abstract DateFormat getDateFormat();

}

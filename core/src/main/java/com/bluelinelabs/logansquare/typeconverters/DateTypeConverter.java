package com.bluelinelabs.logansquare.typeconverters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public abstract class DateTypeConverter implements TypeConverter<Date> {

    // DateFormat is not thread-safe, so wrap it in a ThreadLocal
    private final ThreadLocal<DateFormat> mDateFormat = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return getDateFormat();
        }
    };

    @Override
    public Date parse(JsonParser jsonParser) throws IOException {
        String dateString = jsonParser.getValueAsString(null);
        if (dateString != null) {
            try {
                return mDateFormat.get().parse(dateString);
            } catch (ParseException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void serialize(Date object, String fieldName, boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {
        if (fieldName != null && object != null) {
            jsonGenerator.writeStringField(fieldName, mDateFormat.get().format(object));
        } else if (object != null) {
            jsonGenerator.writeString(mDateFormat.get().format(object));
        } else {
            if (fieldName != null) {
                jsonGenerator.writeFieldName(fieldName);
            }
            jsonGenerator.writeNull();
        }
    }

    /** Called to get the DateFormat used to parse and serialize objects */
    public abstract DateFormat getDateFormat();

}

package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.typeconverters.DateTypeConverter;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@JsonObject
public abstract class DateModel {

    @JsonField(typeConverter = MyDateTypeConverter.class)
    public Date date;

    public static class MyDateTypeConverter extends DateTypeConverter {
        private DateFormat mDateFormat;

        public MyDateTypeConverter(){
            mDateFormat = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSZ");
        }

        @Override
        public DateFormat getDateFormat() {
            return mDateFormat;
        }

    }
}
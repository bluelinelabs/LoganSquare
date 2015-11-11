package com.bluelinelabs.logansquare.typeconverters;

public class EnumValueTypeConverter<T extends Enum<T>> extends StringBasedTypeConverter<T> {

    private Class<T> mClass;

    public EnumValueTypeConverter(Class<T> cls) {
        mClass = cls;
    }

    @Override
    public T getFromString(String string) {
        return Enum.valueOf(mClass, convertString(string, true));
    }

    @Override
    public String convertToString(T object) {
        return convertString(object.toString(), false);
    }

    public String convertString(String string, boolean forParse) {
        return string;
    }

}

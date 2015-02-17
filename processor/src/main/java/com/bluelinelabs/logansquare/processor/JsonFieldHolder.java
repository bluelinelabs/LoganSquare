package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.processor.collectiontype.CollectionType;
import com.bluelinelabs.logansquare.processor.fieldtype.FieldType;

public class JsonFieldHolder {

    public String[] fieldName;
    public String setterMethod;
    public String getterMethod;
    public CollectionType collectionType;
    public FieldType fieldType;

    public boolean hasSetter() {
        return !TextUtils.isEmpty(setterMethod);
    }

    public boolean hasGetter() {
        return !TextUtils.isEmpty(getterMethod);
    }
}

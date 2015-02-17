package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.Date;

@JsonObject
public class PrivateFieldModel {

    @JsonField
    private String privateString;

    @JsonField(name = "private_named_string")
    private String privateNamedString;

    public String getPrivateString() {
        return privateString;
    }

    public void setPrivateString(String privateString) {
        this.privateString = privateString;
    }

    public String getPrivateNamedString() {
        return privateNamedString;
    }

    public void setPrivateNamedString(String privateNamedString) {
        this.privateNamedString = privateNamedString;
    }
}

package com.bluelinelabs.logansquare.processor;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;
import java.util.Map;

@JsonObject
public class WhitespaceFieldNameModel {

    @JsonField(name = "Full Name")
    public String fullName;

    @JsonField(name = "Address Lines")
    public List<String> addressLines;

    @JsonField(name = "All Contacts")
    public Map<String, String> allContacts;
}

package com.bluelinelabs.logansquare.processor.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;

import java.util.List;
import java.util.Map;

@JsonObject
public class EnumListModel {

    public enum TestEnum {
        ONE, TWO
    }

    @JsonField(name = "enum_obj")
    TestEnum enumObj;

    @JsonField(name = "enum_list")
    List<TestEnum> enumList;

    @JsonField(name = "enum_array")
    TestEnum[] enumArray;

    @JsonField(name = "enum_map")
    Map<String, TestEnum> enumMap;

    public static class LsEnumTestConverter extends StringBasedTypeConverter<TestEnum> {
        @Override
        public TestEnum getFromString(String string) {
            return TestEnum.valueOf(string);
        }

        @Override
        public String convertToString(TestEnum testEnum) {
            return testEnum.toString();
        }
    }
}
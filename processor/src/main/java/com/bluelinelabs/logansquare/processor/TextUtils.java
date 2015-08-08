package com.bluelinelabs.logansquare.processor;

import java.util.Collections;
import java.util.List;

public class TextUtils {

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static String toUniqueFieldNameVariable(String fieldName, List<String> processedFieldNames) {
        fieldName = fieldName.replaceAll(" ", "");
        int frequency = Collections.frequency(processedFieldNames, fieldName);
        processedFieldNames.add(fieldName);
        return (frequency > 0) ? fieldName + frequency : fieldName;
    }

    public static String toUpperCaseWithUnderscores(String className) {
        StringBuilder sb = new StringBuilder();
        for (char c : className.toCharArray()) {
            if (c >= 'A' && c <= 'Z' && sb.length() > 0) {
                sb.append('_').append(c);
            } else {
                sb.append(Character.toUpperCase(c));
            }
        }

        return sb.toString();
    }

    public static String toLowerCaseWithUnderscores(String className) {
        StringBuilder sb = new StringBuilder();
        for (char c : className.toCharArray()) {
            if (c >= 'A' && c <= 'Z' && sb.length() > 0) {
                sb.append('_');
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

}

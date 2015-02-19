##Type Support

###Types with Built-in Support

By default, the following types are supported by LoganSquare:

* any object using the `@JsonObject` annotation
* int and Integer
* long and Long
* float and Float
* double and Double
* boolean and Boolean
* String
* Date (if formatted using the ISO 8601 standard: `yyyy-MM-dd'T'HH:mm:ss.SSSZ`)

Additionally, the following collections are supported:

* List
* Set
* Queue
* Deque
* Map (with Strings as keys)

###Support for Additional Types

Any Java object can be supported by LoganSquare, even if they don't fall into the above categories. To add support for your own types, you'll need to extend one of the built-in `TypeConverter` classes. You can register your custom type converters using one of the two following ways:

#### For global `TypeConverter`s (used in many places)

```java
LoganSquare.registerTypeConverter(Date.class, YourConverter.class)
```

#### For `TypeConverter`s that should only be used for certain variables

Hint: This method is especially useful for handling multiple Date formats!

```java
@JsonObject
public class ModelObject {
    @JsonField(typeConverter = YourConverter.class)
    public Date speciallyConvertedDate;
}
```

###Example `TypeConverter`s

####`TypeConverter` for a custom date format

```java
public class TimeOnlyDateConverter extends DateTypeConverter {

    private DateFormat mDateFormat;

    public DefaultDateConverter() {
        mDateFormat = new SimpleDateFormat("HH:mm");
    }

    public DateFormat getDateFormat() {
        return mDateFormat;
    }

}
```

####`TypeConverter` for an enum, where the JSON contains an int

```java
public enum TestEnum {
    VALUE_1, VALUE_2, VALUE_3
}
    
public class TimeOnlyDateConverter extends IntBasedTypeConverter<TestEnum> {
    @Override
    public TestEnum getFromInt(int i) {
        return TestEnum.values()[i];
    }
    
    public int convertToInt(TestEnum object) {
        return Arrays.asList(TestEnum.values()).indexOf(TestEnum.VALUE_1);
    }

}
```

####`TypeConverter` for an enum, where the JSON contains a String

```java
public enum TestEnum {
    VALUE_1, VALUE_2, VALUE_3
}

public class EnumConverter extends StringBasedTypeConverter<TestEnum> {
    @Override
    public TestEnum getFromString(String s) {
        TestEnum.valueOf(s);
    }
    
    public String convertToString(TestEnum object) {
        return object.toString();
    }

}
```
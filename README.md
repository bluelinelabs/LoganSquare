[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-LoganSquare-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1550)

#LoganSquare

The fastest JSON parsing and serializing library available for Android. Based on Jackson's streaming API, LoganSquare is able to consistently outperform GSON and Jackson's Databind library by 400% or more. By relying on compile-time annotation processing to generate code, you know that your JSON will parse and serialize faster than any other method available.

By using this library, you'll be able to utilize the power of Jackson's streaming API without having to code tedius, low-level code involving `JsonParser`s or `JsonGenerator`s. Instead, just annotate your model objects as a `@JsonObject` and your fields as `@JsonField`s and we'll do the heavy lifting for you.

Don't believe it could improve upon Jackson Databind's or GSON's performance that much? Well, then check out the nifty graphs below for yourself. Not convinced? Feel free to build and run the BenchmarkDemo app included in this repository.

![Benchmarks](images/benchmarks.jpg)

##Download

Note that Gradle is the only supported build configuration for LoganSquare. To add the library to your app's build.gradle file.

```groovy
  apply plugin: 'com.neenbedankt.android-apt'

  dependencies {
    apt 'com.bluelinelabs:logansquare-compiler:1.0.1'
    compile 'com.bluelinelabs:logansquare:1.0.1'
  }

```
For the curious, the first line adds the [apt plugin](https://bitbucket.org/hvisser/android-apt), which is what allows us to do compile-time annotation processing. The first dependency is what tells Gradle to process your JSON annotations, and the second dependency is our tiny 19kb runtime library that interfaces with the generated code for you.

##Usage

Using LoganSquare is about as easy as it gets. Here are a few examples to get you started:

###Sample Model

```java
@JsonObject
public class Image {

    @JsonField(name = "_id")
    public int imageId;

    @JsonField
    public String format;

    @JsonField
    public String url;

    @JsonField
    public String description;
    
    @JsonField(name = "similar_images")
    List<Image> similarImages;
    
}

```
The first thing to note is that your JSON model class has to be annotated with the `@JsonObject` annotation. Next, any fields that you want parsed or serialized have to be annotated as `@JsonField`s. If you pass a `name` into the `@JsonField` annotation, that's the name that LoganSquare will use when parsing and serializing your JSON. If you don't pass in a `name`, it will use the name of your variable. For code clarity, we recommend always passing a `name`, even if it isn't required.

###Sample Model with Callbacks

LoganSquare can notify you when your model is done parsing or when it's about to serialize. This can be useful if you need to ensure some variables are in the correct state. Here's an example:

```java
@JsonObject
public class ModelWithCallbacks {

    //@JsonField annotated variables here...
    
    @OnJsonParseComplete void onParseComplete() {
        // Do some fancy post-processing stuff after parsing here
    }
    
    @OnPreJsonSerialize void onPreSerialize() {
        // Do some fancy pre-processing stuff before serializing here
    }
}
```

###Parsing JSON

JSON can be parsed from an `InputStream` or a `String`. If you're getting your JSON directly from your networking library, you should be able to use an `InputStream` to avoid converting it to a String for no reason. Here's a sample:

```java
    // Parse from an InputStream
    InputStream is = ...;
    Image imageFromInputStream = LoganSquare.parse(is, Image.class);
    
    // Parse from a String
    String jsonString = ...;
    Image imageFromString = LoganSquare.parse(jsonString, Image.class); 
```

###Serializing JSON

LoganSquare can serialize objects to an `OutputStream` or a `String`. Here's a sample:

```java
    // Serialize it to an OutputStream
    OutputStream os = ...;
    LoganSquare.serialize(image, os);
    
    // Serialize it to a String
    String jsonString = LoganSquare.serialize(image);
```

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

```java
// If your TypeConverter should be used globally, register 
// your type converter when your app starts up like so:

LoganSquare.registerTypeConverter(Date.class, YourConverter.class)
```

```java
// If your TypeConverter should only be used for certain 
// variables (for example, if you need to handle multiple 
// date formats), only declare it for individual fields

@JsonObject
public class ModelObject {
    @JsonField(typeConverter = YourConverter.class)
    public Date speciallyConvertedDate;
}
```

Here are a few examples of common type converters:

####TypeConverter for a custom date format

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

####TypeConverter for an Enum, where the JSON contains an int

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

####TypeConverter for an Enum, where the JSON contains a String

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

##Why LoganSquare?

We're BlueLine Labs, a mobile app development company based in Chicago. We love this city so much that we named our company after the blue line of the iconic 'L.' And what's one of the most popular stops on the blue line? Well, that would be Logan Square of course. Does it have anything to do with JSON? Nope, but we're okay with that.

##Props

 * [Jackson's streaming API](https://github.com/FasterXML/jackson-core) for being a super-fast, awesome base for this project.
 * [Instagram's ig-json-parser](https://github.com/Instagram/ig-json-parser) for giving us the idea for this project.
 * [Jake Wharton's Butterknife](https://github.com/JakeWharton/butterknife) for being a great reference for annotation processing and code generation.

##License

    Copyright 2015 BlueLine Labs, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



## Parsing JSON

JSON can be parsed from an `InputStream` or a `String`. If you're getting your JSON directly from your networking library, you should be able to use an `InputStream` to avoid converting it to a String for no reason. Here's a sample:

```java
    // Parse from an InputStream
    InputStream is = ...;
    Image imageFromInputStream = LoganSquare.parse(is, Image.class);
    
    // Parse from a String
    String jsonString = ...;
    Image imageFromString = LoganSquare.parse(jsonString, Image.class); 
```

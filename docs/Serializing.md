## Serializing JSON

LoganSquare can serialize objects to an `OutputStream` or a `String`. Here's a sample:

```java
    // Serialize it to an OutputStream
    OutputStream os = ...;
    LoganSquare.serialize(image, os);
    
    // Serialize it to a String
    String jsonString = LoganSquare.serialize(image);
```
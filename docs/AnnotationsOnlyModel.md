## Annotating Every Field (Recommended)

### Sample Model

```java
@JsonObject
public class Image {

    /*
     * Standard field declaration.
     */
    @JsonField
    public String format;
    
    /*
     * Note: passing the name parameter into @JsonField will cause
     * LoganSquare to use "_id" in JSON parsing and processing instead 
     * of "imageId".
     */
    @JsonField(name = "_id")
    public int imageId;

    @JsonField
    public String url;

    @JsonField
    public String description;
    
    /*
     * Note that even though this is a package-local field,
     * LoganSquare can parse and serialize it without issue.
     */
    @JsonField(name = "similar_images")
    List<Image> similarImages;
    
    /*
     * Note that since this field isn't annotated as a
     * @JsonField, LoganSquare will ignore it when parsing
     * and serializing this class.
     */
    public int nonJsonField;
    
    /*
     * Since this is a private field, LoganSquare will use 
     * the getter and setter to parse and serialize this one.
     * If the either the getter or setter are not present, 
     * you're gonna have a bad time.
     */
    @JsonField
    private int privateInt;
    
    public int getPrivateInt() {
        return privateInt;
    }
    
    public void setPrivateInt(int i) {
        privateInt = i;
    }
    
    /*
     * Optional callback method to do something when your
     * object is done parsing.
     */
    @OnJsonParseComplete void onParseComplete() {
        // Do some fancy post-processing stuff after parsing here
    }
    
    /*
     * Optional callback method to do something before your
     * object serializes.
     */
    @OnPreJsonSerialize void onPreSerialize() {
        // Do some fancy pre-processing stuff before serializing here
    }
    
}

```
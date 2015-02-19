##Non-private Field Detection Policy

###Sample Model

```java
/*
 * To have LoganSquare detect all non-private fields, even if they
 * aren't annotated as @JsonFields, set the fieldDetectionPolicy of
 * the @JsonObject.
 */
@JsonObject(fieldDetectionPolicy = FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class Image {

    /*
     * Standard field declaration.
     */
    public String format;
    
    /*
     * If you want to have a variable with a different name
     * than the JSON field, you'll still have to annotate it like so.
     */
    @JsonField(name = "_id")
    public int imageId;

    public String url;

    public String description;
    
    /*
     * Note that even though this is a package-local field,
     * LoganSquare can parse and serialize it without issue.
     */
    List<Image> similarImages;
    
    /*
     * Note that since this field is annotated with
     * @JsonIgnore, LoganSquare will ignore it when parsing
     * and serializing this class.
     */
    @JsonIgnore
    public int nonJsonField;
    
    /*
     * Since this is a private field, LoganSquare will ignore it
     * due to the field detection policy.
     */
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
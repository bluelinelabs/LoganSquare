package model.bad;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public abstract class GenericModel<T> {

    @JsonField
    public String string;

}

package com.bluelinelabs.logansquare.processor.bad;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.annotation.OnJsonParseComplete;

@JsonObject
public class MultipleMethodsModel {

    @OnJsonParseComplete
    public void onParseComplete() {

    }

    @OnJsonParseComplete
    public void secondOnParseComplete() {

    }

}

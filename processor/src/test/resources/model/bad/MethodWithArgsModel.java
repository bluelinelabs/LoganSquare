package com.bluelinelabs.logansquare.processor.bad;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.annotation.OnJsonParseComplete;

@JsonObject
public class MethodWithArgsModel {

    @OnJsonParseComplete
    public void onParseComplete(String someArg) {

    }

}

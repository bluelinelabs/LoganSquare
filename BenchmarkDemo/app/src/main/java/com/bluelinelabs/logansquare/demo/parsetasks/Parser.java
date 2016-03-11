package com.bluelinelabs.logansquare.demo.parsetasks;

import android.os.AsyncTask;

import java.util.concurrent.TimeUnit;

public abstract class Parser extends AsyncTask<Void, Void, ParseResult> {

    public interface ParseListener {
        void onComplete(Parser parser, ParseResult parseResult);
    }

    private final ParseListener mParseListener;
    private final String mJsonString;

    protected Parser(ParseListener parseListener, String jsonString) {
        mParseListener = parseListener;
        mJsonString = jsonString;
    }

    @Override
    protected ParseResult doInBackground(Void... params) {
        System.gc();
        long startTime = System.nanoTime();
        int objectCount = parse(mJsonString);
        if (objectCount == 0) return null;
        long endTime = System.nanoTime();
        long duration = TimeUnit.NANOSECONDS.toMicros(endTime - startTime);

        return new ParseResult(duration, objectCount);
    }

    @Override
    protected void onPostExecute(ParseResult parseResult) {
        mParseListener.onComplete(this, parseResult);
    }

    protected abstract int parse(String json);
}

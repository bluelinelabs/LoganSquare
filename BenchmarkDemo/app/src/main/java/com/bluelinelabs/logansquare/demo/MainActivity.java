package com.bluelinelabs.logansquare.demo;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.demo.model.Response;
import com.bluelinelabs.logansquare.demo.parsetasks.GsonParser;
import com.bluelinelabs.logansquare.demo.parsetasks.JacksonDatabindParser;
import com.bluelinelabs.logansquare.demo.parsetasks.LoganSquareParser;
import com.bluelinelabs.logansquare.demo.parsetasks.ParseResult;
import com.bluelinelabs.logansquare.demo.parsetasks.Parser;
import com.bluelinelabs.logansquare.demo.parsetasks.Parser.ParseListener;
import com.bluelinelabs.logansquare.demo.serializetasks.GsonSerializer;
import com.bluelinelabs.logansquare.demo.serializetasks.JacksonDatabindSerializer;
import com.bluelinelabs.logansquare.demo.serializetasks.LoganSquareSerializer;
import com.bluelinelabs.logansquare.demo.serializetasks.SerializeResult;
import com.bluelinelabs.logansquare.demo.serializetasks.Serializer;
import com.bluelinelabs.logansquare.demo.serializetasks.Serializer.SerializeListener;
import com.bluelinelabs.logansquare.demo.widget.BarChart;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a test app we wrote in 10 minutes. Please do not write code like this, kiddos.
 */
public class MainActivity extends ActionBarActivity {

    private static final int ITERATIONS = 20;

    private BarChart mBarChart;
    private List<String> mJsonStringsToParse;
    private List<Response> mResponsesToSerialize;

    private final ParseListener mParseListener = new ParseListener() {
        @Override
        public void onComplete(Parser parser, ParseResult parseResult) {
            addBarData(parser, parseResult);
        }
    };
    private final SerializeListener mSerializeListener = new SerializeListener() {
        @Override
        public void onComplete(Serializer serializer, SerializeResult serializeResult) {
            addBarData(serializer, serializeResult);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mJsonStringsToParse = readJsonFromFile();
        mResponsesToSerialize = getResponsesToParse();

        mBarChart = (BarChart)findViewById(R.id.bar_chart);
        mBarChart.setColumnTitles(new String[] {"GSON", "Jackson", "LoganSquare"});

        findViewById(R.id.btn_parse_tests).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                performParseTests();
            }
        });

        findViewById(R.id.btn_serialize_tests).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                performSerializeTests();
            }
        });
    }

    private void performParseTests() {
        mBarChart.clear();
        mBarChart.setSections(new String[] {"Parse 60 items", "Parse 20 items", "Parse 7 items", "Parse 2 items"});

        Gson gson = new Gson();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Parser> parsers = new ArrayList<>();
        for (String jsonString : mJsonStringsToParse) {
            for (int iteration = 0; iteration < ITERATIONS; iteration++) {
                parsers.add(new GsonParser(mParseListener, jsonString, gson));
                parsers.add(new JacksonDatabindParser(mParseListener, jsonString, objectMapper));
                parsers.add(new LoganSquareParser(mParseListener, jsonString));
            }
        }

        for (Parser parser : parsers) {
            parser.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
    }

    private void performSerializeTests() {
        mBarChart.clear();
        mBarChart.setSections(new String[] {"Serialize 60 items", "Serialize 20 items", "Serialize 7 items", "Serialize 2 items"});

        Gson gson = new Gson();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Serializer> serializers = new ArrayList<>();
        for (Response response : mResponsesToSerialize) {
            for (int iteration = 0; iteration < ITERATIONS; iteration++) {
                serializers.add(new GsonSerializer(mSerializeListener, response, gson));
                serializers.add(new JacksonDatabindSerializer(mSerializeListener, response, objectMapper));
                serializers.add(new LoganSquareSerializer(mSerializeListener, response));
            }
        }

        for (Serializer serializer : serializers) {
            serializer.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
    }

    private void addBarData(Parser parser, ParseResult parseResult) {
        int section;
        switch (parseResult.objectsParsed) {
            case 60:
                section = 0;
                break;
            case 20:
                section = 1;
                break;
            case 7:
                section = 2;
                break;
            case 2:
                section = 3;
                break;
            default:
                section = -1;
                break;
        }

        if (parser instanceof GsonParser) {
            mBarChart.addTiming(section, 0, parseResult.runDuration / 1000f);
        } else if (parser instanceof JacksonDatabindParser) {
            mBarChart.addTiming(section, 1, parseResult.runDuration / 1000f);
        } else if (parser instanceof LoganSquareParser) {
            mBarChart.addTiming(section, 2, parseResult.runDuration / 1000f);
        }
    }

    private void addBarData(Serializer serializer, SerializeResult serializeResult) {
        int section;
        switch (serializeResult.objectsParsed) {
            case 60:
                section = 0;
                break;
            case 20:
                section = 1;
                break;
            case 7:
                section = 2;
                break;
            case 2:
                section = 3;
                break;
            default:
                section = -1;
                break;
        }

        if (serializer instanceof GsonSerializer) {
            mBarChart.addTiming(section, 0, serializeResult.runDuration / 1000f);
        } else if (serializer instanceof JacksonDatabindSerializer) {
            mBarChart.addTiming(section, 1, serializeResult.runDuration / 1000f);
        } else if (serializer instanceof LoganSquareSerializer) {
            mBarChart.addTiming(section, 2, serializeResult.runDuration / 1000f);
        }
    }

    private List<Response> getResponsesToParse() {
        List<Response> responses = new ArrayList<>();

        try {
            for (String jsonString : mJsonStringsToParse) {
                responses.add(LoganSquare.parse(jsonString, Response.class));
            }
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("The serializable objects were not able to load properly. These tests won't work until you completely kill this demo app and restart it.")
                    .setPositiveButton("OK", null)
                    .show();
        }

        return responses;
    }

    private List<String> readJsonFromFile() {
        List<String> strings = new ArrayList<>();

        strings.add(readFile("largesample.json"));
        strings.add(readFile("mediumsample.json"));
        strings.add(readFile("smallsample.json"));
        strings.add(readFile("tinysample.json"));

        return strings;
    }

    private String readFile(String filename) {
        StringBuilder sb = new StringBuilder();

        try {
            InputStream json = getAssets().open(filename);
            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));

            String str;
            while ((str = in.readLine()) != null) {
                sb.append(str);
            }

            in.close();
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("The JSON file was not able to load properly. These tests won't work until you completely kill this demo app and restart it.")
                    .setPositiveButton("OK", null)
                    .show();
        }

        return sb.toString();
    }
}

package com.bluelinelabs.logansquare.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/** Hacked together barchart view for this demo. Please do not take this as an example of a good way to do things. */
public class BarChart extends View {

    private static final int SECTION_COUNT = 4;
    private static final int COLUMNS_PER_SECTION = 4;

    private final Section[] mSections = new Section[SECTION_COUNT];
    private final Rect mTextBounds = new Rect();

    private final Paint[] mPaints = new Paint[COLUMNS_PER_SECTION];
    private TextPaint mTextPaint;
    private int mColumnHeight;
    private int mColumnPadding;
    private int mSectionPadding;
    private int mDividerHeight;
    private int mTextPadding;

    public BarChart(Context context) {
        super(context);
        init(context);
    }

    public BarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BarChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        final float density = context.getResources().getDisplayMetrics().density;

        mPaints[0] = new Paint();
        mPaints[0].setColor(0xfff5d391);

        mPaints[1] = new Paint();
        mPaints[1].setColor(0xffa9e8fe);

        mPaints[2] = new Paint();
        mPaints[2].setColor(0xffe9969c);

        mPaints[3] = new Paint();
        mPaints[3].setColor(0xffb5d951);

        mTextPaint = new TextPaint();
        mTextPaint.setColor(0xff555555);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(density * 12);

        mColumnHeight = (int)(density * 20);
        mColumnPadding = (int)(density * 2);
        mSectionPadding = (int)(density * 24);
        mDividerHeight = (int)(density * 2);
        mTextPadding = (int)(density * 4);

        for (int i = 0; i < SECTION_COUNT; i++) {
            mSections[i] = new Section();
        }
    }

    public void setSections(String[] titles) {
        for (int i = 0; i < titles.length; i++) {
            mSections[i].title = titles[i];
        }
    }

    public void setColumnTitles(String[] titles) {
        for (Section section : mSections) {
            for (int i = 0; i < titles.length; i++) {
                section.columns[i].title = titles[i];
            }
        }
    }

    public void clear() {
        for (Section section : mSections) {
            section.title = "";

            for (Column column : section.columns) {
                column.timings.clear();
            }
        }
        invalidate();
    }

    public void addTiming(int section, int column, float timing) {
        mSections[section].columns[column].addTiming(timing);
        invalidate();
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = SECTION_COUNT * mSectionPadding + SECTION_COUNT * COLUMNS_PER_SECTION * (mColumnPadding + mColumnHeight);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int height = getMeasuredHeight();
        final int width = getMeasuredWidth();

        final int heightAfterPadding = height  - mSectionPadding * SECTION_COUNT - (SECTION_COUNT * COLUMNS_PER_SECTION - 1) * mColumnPadding;
        final int columnHeight = heightAfterPadding / (SECTION_COUNT * COLUMNS_PER_SECTION);

        float[] maxTimings = new float[SECTION_COUNT];
        for (int sectionIndex = 0; sectionIndex < SECTION_COUNT; sectionIndex++) {
            Section section = mSections[sectionIndex];
            float maxMs = 0;
            for (Column column : section.columns) {
                maxMs = Math.max(maxMs, column.getMedianTiming());
            }
            maxTimings[sectionIndex] = maxMs;
        }

        int currentY = mSectionPadding;
        for (int sectionIndex = 0; sectionIndex < SECTION_COUNT; sectionIndex++) {
            Section section = mSections[sectionIndex];

            canvas.drawLine(0, currentY - mDividerHeight, width, currentY, mTextPaint);
            canvas.drawText(section.title, mTextPadding, currentY - mTextPaint.descent(), mTextPaint);

            float maxMs = maxTimings[sectionIndex];
            if (maxMs > 0) {
                for (int columnIndex = 0; columnIndex < COLUMNS_PER_SECTION; columnIndex++) {
                    Column column = section.columns[columnIndex];
                    int columnWidth = (int)(width * column.getMedianTiming() / maxMs);
                    canvas.drawRect(0, currentY, columnWidth, currentY + columnHeight, mPaints[columnIndex]);

                    String columnTitle = column.getTitle();
                    if (!TextUtils.isEmpty(columnTitle)) {
                        mTextPaint.getTextBounds(columnTitle, 0, columnTitle.length(), mTextBounds);
                        canvas.drawText(columnTitle, mTextPadding, currentY + columnHeight / 2 - mTextBounds.exactCenterY(), mTextPaint);
                    }

                    currentY += columnHeight + mColumnPadding;
                }
                currentY += mSectionPadding;
            }
        }
    }

    public static class Section {

        public String title;
        private final Column[] columns;

        public Section() {
            title = "";
            columns = new Column[] {new Column(), new Column(), new Column(),  new Column()};
        }
    }

    public static class Column {

        public String title;
        private List<Float> timings = new ArrayList<>();

        public String getTitle() {
            if (timings.size() > 0) {
                float minTime = timings.get(0);
                float medianTime = timings.get(timings.size() / 2);
                float maxTime = timings.get(timings.size() - 1);

                return String.format(Locale.getDefault(), "%s (median: %.2fms, min: %.2fms, max: %.2fms", title, medianTime, minTime, maxTime);
            } else {
                return title;
            }
        }

        public float getMedianTiming() {
            if (timings.size() > 0) {
                return timings.get(timings.size() / 2);
            } else {
                return 0;
            }
        }

        public void addTiming(float timing) {
            timings.add(timing);
            Collections.sort(timings);
        }
    }

}

package ru.bstu.it41.service;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

/**
 * Created by Герман on 13.11.2017.
 */

public class CustomTypefaceSpan extends TypefaceSpan {
    private final Typeface newType;
    private int textSize = 0;

    public CustomTypefaceSpan(String family, Typeface type) {
        super(family);
        newType = type;
    }
    public CustomTypefaceSpan(String family, Typeface type, int textSize) {
        super(family);
        newType = type;
        this.textSize = textSize;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeFace(ds, newType,textSize);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, newType,textSize);
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf, int textSize) {
        int oldStyle;
        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }
        int fake = oldStyle & ~tf.getStyle();
        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }
        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }
        if(textSize != 0)
            paint.setTextSize(textSize);
        paint.setFakeBoldText(true);
        paint.setTypeface(tf);
    }
}

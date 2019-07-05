package com.example.maxcalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.orhanobut.logger.Logger;

public class WeekBarView extends AppCompatTextView {

    private static String[] mWeekChars = {"日", "一", "二", "三", "四", "五", "六"};
    private TextPaint mTextPaint;

    public WeekBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTextPaint = getPaint();
        // 居中对齐
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int width = getMeasuredWidth() - paddingLeft - paddingRight;
        int height = getMeasuredHeight() - paddingTop - paddingBottom;

        for (int i = 0; i < mWeekChars.length; i++) {

            // 每个字体的矩形
            Rect rect = new Rect(paddingLeft + (width / mWeekChars.length * i), paddingTop, paddingLeft + (width / mWeekChars.length * (i + 1)), paddingBottom + height);

            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();

            Logger.d("rect.centerY() : " + rect.centerY());
            Logger.d("rect.centerX() : " + rect.centerX());

            // 解释 ：https://www.jianshu.com/p/8b97627b21c4
            float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;   // 基于基线
            float baseLineY = rect.centerY() + distance;

            // y 对应的是文字基准线baseLine
            canvas.drawText(mWeekChars[i], rect.centerX(), baseLineY, mTextPaint);
        }
    }
}

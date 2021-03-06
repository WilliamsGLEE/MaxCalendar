package com.example.maxcalendar.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.example.maxcalendar.R;
import com.orhanobut.logger.Logger;

import butterknife.internal.Utils;

public class AttrsUtil {

    public static com.example.maxcalendar.util.Attrs getAttrs(Context context, AttributeSet attributeSet) {
        com.example.maxcalendar.util.Attrs attrs = new com.example.maxcalendar.util.Attrs();
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.MaxCalendar);

        String startDate = typedArray.getString(R.styleable.MaxCalendar_startDate);
        String endDate = typedArray.getString(R.styleable.MaxCalendar_endDate);

        attrs.startDateString = TextUtils.isEmpty(startDate) ?  "1901-01-01" : startDate;
        attrs.endDateString = TextUtils.isEmpty(endDate) ? "2099-12-31" : endDate;

        attrs.mItemHeight = typedArray.getInt(R.styleable.MaxCalendar_mItemHeight, OtherUtil.dpToPx(context, 60));
        attrs.numberStrokeWidth = typedArray.getFloat(R.styleable.MaxCalendar_numberStrokeWidth, 0.9f);
        attrs.lunarStrokeWidth = typedArray.getFloat(R.styleable.MaxCalendar_lunarStrokeWidth, 0.2f);
        attrs.lunarDistance = typedArray.getFloat(R.styleable.MaxCalendar_lunarDistance, 45f);
        attrs.itemRectPadding = typedArray.getInt(R.styleable.MaxCalendar_itemRectPadding, OtherUtil.dpToPx(context, 5));
        attrs.yearViewDayStrokeWidth = typedArray.getFloat(R.styleable.MaxCalendar_yearViewDayStrokeWidth, 0.3f);
        attrs.schemeRectPaddingWidth = typedArray.getInt(R.styleable.MaxCalendar_schemeRectPaddingWidth, OtherUtil.dpToPx(context, 14));
        attrs.schemeRectPaddingTop = typedArray.getInt(R.styleable.MaxCalendar_schemeRectPaddingTop, OtherUtil.dpToPx(context, 48));
        attrs.schemeRectPaddingBottom = typedArray.getInt(R.styleable.MaxCalendar_schemeRectPaddingBottom, OtherUtil.dpToPx(context, 5));

        attrs.todayTextColor = typedArray.getInt(R.styleable.MaxCalendar_todayTextColor, context.getResources().getColor(R.color.red));  // 直接用R.color.red得到的值是负数
        attrs.thisMonthTextColor = typedArray.getInt(R.styleable.MaxCalendar_thisMonthTextColor, context.getResources().getColor(R.color.black));
        attrs.notThisMonthTextColor = typedArray.getInt(R.styleable.MaxCalendar_notThisMonthTextColor, context.getResources().getColor(R.color.lightgray));
        attrs.selectBackgroundColor = typedArray.getInt(R.styleable.MaxCalendar_selectBackgroundColor, context.getResources().getColor(R.color.lightlightgray));
        attrs.yearViewDayTextColor = typedArray.getInt(R.styleable.MaxCalendar_yearViewDayTextColor, context.getResources().getColor(R.color.black));
        attrs.yearViewMonthTextColor = typedArray.getInt(R.styleable.MaxCalendar_yearViewMonthTextColor, context.getResources().getColor(R.color.red));
        attrs.yearViewDaySchemeTextColor = typedArray.getInt(R.styleable.MaxCalendar_yearViewDaySchemeTextColor, context.getResources().getColor(R.color.orange));

        attrs.calendarDateNumberSize = typedArray.getInt(R.styleable.MaxCalendar_calendarDateNumberSize, OtherUtil.dpToPx(context, 17));
        attrs.calendarDateLunarTextSize = typedArray.getInt(R.styleable.MaxCalendar_calendarDateLunarTextSize, OtherUtil.dpToPx(context, 8));
        attrs.yearViewDayTextSize = typedArray.getInt(R.styleable.MaxCalendar_yearViewDayTextSize, OtherUtil.dpToPx(context, 6));

        attrs.animatorDuration = typedArray.getInt(R.styleable.MaxCalendar_animatorDuration, 240);

        attrs.yearViewRvColumns = typedArray.getInt(R.styleable.MaxCalendar_yearViewRvColumns, 3);
        attrs.yearViewRvRows = typedArray.getInt(R.styleable.MaxCalendar_yearViewRvRows, 4);

        typedArray.recycle();
        return attrs;
    }
}

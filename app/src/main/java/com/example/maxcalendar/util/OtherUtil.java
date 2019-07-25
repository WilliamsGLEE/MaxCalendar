package com.example.maxcalendar.util;

import android.content.Context;
import android.widget.Toast;

import com.example.maxcalendar.R;
import com.example.maxcalendar.app.MyApplication;
import com.example.maxcalendar.constant.Constant;

public class OtherUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dpToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int pxToDp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     */
    public static int spToPx(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     */
    public static int pxToSp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据日程优先级选择对应颜色
     */
    public static int eventTypeToColor(int type) {
        if (type == Constant.RED) {
            return R.color.red;
        } else if (type == Constant.ORANGE) {
            return R.color.orange;
        } else if (type == Constant.BLUE) {
            return R.color.blue;
        } else if (type == Constant.GREEN) {
            return R.color.green;
        } else if (type == Constant.YELLOW) {
            return R.color.yellow;
        }
        return R.color.yellow;
    }

    public static void showToastMessage(String msg) {
        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

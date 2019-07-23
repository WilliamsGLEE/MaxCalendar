package com.example.maxcalendar.listener;

public interface OnClickRvListener {

    // 点击item
    void onItemClick(int position);

    // 长按item
    boolean onItemLongClick(int position);

    // 点击 footerView
    void onFooterViewClick();
}

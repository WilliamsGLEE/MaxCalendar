package com.example.maxcalendar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maxcalendar.bean.Months;
import com.example.maxcalendar.util.Attrs;
import com.example.maxcalendar.view.YearView;

public class YearRvAdapter extends BaseRvAdapter<Months> {

    private int mItemHeight, mItemWidth;
    private Attrs mAttrs;

    public YearRvAdapter(Context context, Attrs attrs) {
//        super(context);
        this.mAttrs = attrs;
    }

    public void setItemSize(int height, int width) {
        this.mItemHeight = height;
        this.mItemWidth = width;
    }

//    @Override
//    public RecyclerView.ViewHolder createDefViewHolder(ViewGroup parent, int viewType) {
//        YearView yearView = new YearView(mContext, mAttrs);
//        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
//        yearView.setLayoutParams(params);
//
//        return new YearViewRvHolder(yearView);
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    static class YearViewRvHolder extends RecyclerView.ViewHolder {

        YearView mYearView;

        YearViewRvHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

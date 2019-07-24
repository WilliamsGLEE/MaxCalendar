package com.example.maxcalendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maxcalendar.R;
import com.example.maxcalendar.bean.Months;
import com.example.maxcalendar.painter.IPainter;
import com.example.maxcalendar.painter.YCalendarPainter;
import com.example.maxcalendar.util.Attrs;
import com.example.maxcalendar.view.YearView;
import com.orhanobut.logger.Logger;

import butterknife.BindView;

public class YearRvAdapter extends BaseRvAdapter<Months> {

    private int mItemHeight, mItemWidth;
    private Attrs mAttrs;
    private YCalendarPainter mYCalendarPainter;

    public YearRvAdapter(Context context, Attrs attrs, YCalendarPainter painter) {
//        super(context);
        this.mAttrs = attrs;
        this.mYCalendarPainter = painter;
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
        return new YearViewRvHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_yearpager, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof YearViewRvHolder) {
            Logger.d("TTTTTTTESTTTTTTT : " + position);
            ((YearViewRvHolder) holder).bindView(mDataList.get(position));
        }
    }

    class YearViewRvHolder extends BaseRvHolder {

        @BindView(R.id.tv_item_rv_yearpager)
        TextView mTextView;
        @BindView(R.id.yearview_item_rv_yearpager)
        YearView mYearView;

        YearViewRvHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void bindView(Months month) {


//            mYearView.measureSize(mItemHeight, mItemWidth);
            mYearView.getLayoutParams().height = mItemHeight;
            mYearView.getLayoutParams().width = mItemWidth;     // ?
            mYearView.setYearViewPainter(mYCalendarPainter);

            mYearView.init(mAttrs, month);
            mTextView.setText("一月");

            Logger.d("TTTTTTTESTTTTTTT : " + mDataList.size() + " , " + month.getMonth() + " , " + mItemHeight);
        }
    }

    public void setItemHeight(int height) {
        this.mItemHeight = height;
    }
}

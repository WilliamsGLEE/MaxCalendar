package com.example.maxcalendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maxcalendar.R;
import com.example.maxcalendar.bean.DailyTask;
import com.example.maxcalendar.util.OtherUtil;

import butterknife.BindView;

public class EventRvAdapter extends com.example.maxcalendar.adapter.BaseRvAdapter<DailyTask> {

    public EventRvAdapter(Context context) {

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventRvHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_activity_calendar, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventRvHolder) {
            ((EventRvHolder) holder).bindView(mDataList.get(position));
        }
    }

    class EventRvHolder extends BaseRvHolder {

        @BindView(R.id.tv_color_item_rv_activity_calendar) TextView colorText;
        @BindView(R.id.tv_time_item_rv_activity_calendar) TextView timeText;
        @BindView(R.id.tv_title_item_rv_activity_calendar) TextView titleText;


        public EventRvHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bindView(DailyTask dailyTask) {
//            colorText.setBackground(colorText.getResources().getString());
//            colorText.setBackgroundResource(colorText.getResources().getDrawable(OtherUtil.eventTypeToColor(dailyTask.getType())));
            colorText.setBackgroundResource(OtherUtil.eventTypeToColor(dailyTask.getType()));
            timeText.setText(dailyTask.getTime());
            titleText.setText(dailyTask.getTitle());
        }
    }
}



//public class EventRvAdapter extends RecyclerView.Adapter<EventRvAdapter.MyViewHolder> {
//
//    private Context context;
//
//    public EventRvAdapter(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_activity_calendar, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//        TextView textView = holder.textView;
//        textView.setText("-----" + position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return 20;
//    }
//
//    class MyViewHolder extends RecyclerView.ViewHolder {
//        TextView textView;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            textView = (TextView) itemView.findViewById(R.id.textView);
//        }
//    }
//
//}
//

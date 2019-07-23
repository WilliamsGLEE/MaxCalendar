package com.example.maxcalendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.maxcalendar.R;

public class EventRvAdapter extends RecyclerView.Adapter<EventRvAdapter.MyViewHolder> {

    private Context context;

    public EventRvAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_activity_calendar, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TextView textView = holder.textView;
        textView.setText("-----" + position);
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }

}


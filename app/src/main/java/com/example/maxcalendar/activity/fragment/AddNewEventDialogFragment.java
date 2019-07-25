package com.example.maxcalendar.activity.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.maxcalendar.R;
import com.example.maxcalendar.bean.DailyTask;
import com.example.maxcalendar.util.OtherUtil;

import org.joda.time.LocalDate;

/**
 * Created by Wachsbeere on 2019/7/26.
 */
public class AddNewEventDialogFragment extends DialogFragment {

    private onClickListener mOnClickListener;
    private LocalDate mCurrDate;

    public interface onClickListener {
        void onPositiveClick(DailyTask dailyTask);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_new_event, null);

//        Button confirm = view.findViewById(R.id.btn_confirm);
//        final Button cancel = view.findViewById(R.id.btn_cancel);
//        TextView partType = view.findViewById(R.id.tv_part_type);
//        TextView partName = view.findViewById(R.id.tv_part_name);
//        TextView textView = view.findViewById(R.id.tv_part_count);
//        final EditText count = view.findViewById(R.id.et_count);

        TextView currDate = view.findViewById(R.id.tv_currentDate_dialog_add_new_event);
        EditText title = view.findViewById(R.id.et_title_dialog_add_new_event);
        EditText priority = view.findViewById(R.id.et_priority_dialog_add_new_event);
        EditText startTime = view.findViewById(R.id.et_startTime_dialog_add_new_event);
        EditText endTime = view.findViewById(R.id.et_endTime_dialog_add_new_event);
        ImageView cancel = view.findViewById(R.id.iv_cancel_dialog_add_new_event);
        ImageView confirm = view.findViewById(R.id.iv_confirm_dialog_add_new_event);

        currDate.setText("设置" + mCurrDate.getYear() + "年" + mCurrDate.getMonthOfYear() + "月" + mCurrDate.getDayOfMonth() + "日的日程");     // TODO english


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(title.getText().toString()) || TextUtils.isEmpty(priority.getText().toString()) ||
                        TextUtils.isEmpty(startTime.getText().toString()) || TextUtils.isEmpty(endTime.getText().toString())){
                    OtherUtil.showToastMessage("请填写完整的数据！");        // TODO english
                    return;
                }

                DailyTask dailyTask = new DailyTask();
                dailyTask.setTitle(title.getText().toString());
                // TODO add a selection
                dailyTask.setType(Integer.parseInt(priority.getText().toString()));
                dailyTask.setTime(startTime.getText().toString() + " ~ " + endTime.getText().toString());
                dailyTask.setYear(mCurrDate.getYear());
                dailyTask.setMonth(mCurrDate.getMonthOfYear());
                dailyTask.setDay(mCurrDate.getDayOfMonth());

                if (mOnClickListener != null) {
                    mOnClickListener.onPositiveClick(dailyTask);
                }
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }

    public void setOnClickListener(onClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setCurrentDate(LocalDate localDate) {
        this.mCurrDate = localDate;
    }
}

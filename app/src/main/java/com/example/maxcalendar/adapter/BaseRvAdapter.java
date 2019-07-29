package com.example.maxcalendar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maxcalendar.listener.OnClickRvListener;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseRvAdapter<T> extends RecyclerView.Adapter {

    protected static final int TYPE_FOOTER = 0;  // 说明是带有Footer的
    protected static final int TYPE_NORMAL = 1;  // 说明是不带有header和footer的
    protected static final int TYPE_NONE = 2;   // 没有数据

    protected List<T> mDataList = new ArrayList<>();
    private OnClickRvListener mOnClickRvListener;

    protected View mFooterView;
    protected View mNoneView;

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footerView) {
        if (footerView != null) {
            mFooterView = footerView;
            notifyItemInserted(getItemCount() - 1);
        }
    }

    public View getNoneView() {
        return mNoneView;
    }

    public void setNoneView(View noneView) {
        mNoneView = noneView;
    }

    // 更新数据
    public void updateData(@NonNull List<T> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    // 分页加载，追加数据
    public void appendData(List<T> dataList) {
        if (null != dataList && !dataList.isEmpty()) {
            int startPosition = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeChanged(startPosition - 1, mDataList.size() - startPosition);
        } else if (dataList != null && dataList.isEmpty()) {
//            notifyDataSetChanged();
            //空数据更新
        }
    }

    // 添加单个数据
    public void addItem(T item) {
        if (item != null) {
            mDataList.add(item);
            notifyItemChanged(mDataList.size());
        }
    }

    public T getItem(int position) {
        if (!(position < 0 || position > mDataList.size())) {
            return mDataList.get(position);
        }
        return null;
    }

    // 判断item的类型，从而绑定不同的view
    @Override
    public int getItemViewType(int position) {
        if (mDataList.size() == 0) {
            if (mNoneView != null) {
                // 显示noneView
                return TYPE_NONE;
            }
        } else if (position != getItemCount() - 1) {
            // 普通item
            return TYPE_NORMAL;
        }
        return TYPE_FOOTER;
    }

    @Override
    public int getItemCount() {
        return mDataList.size() + (mFooterView == null ? 0 : 1);
    }

    // RecyclerView不提供点击事件，自定义点击事件
    public void setOnRecyclerViewListener(OnClickRvListener onClickRvListener) {
        mOnClickRvListener = onClickRvListener;
    }

    public abstract class BaseRvHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        public BaseRvHolder(View itemView) {
            super(itemView);
            // 如果是footerView,设置点击事件，不 bind。
            if (itemView == mFooterView) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnClickRvListener != null) {
                            mOnClickRvListener.onFooterViewClick();
                        }
                    }
                });
            } else if (itemView == mNoneView) {

            } else {
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }
        }

        protected abstract void bindView(T t);

        @Override
        public void onClick(View v) {
            if (mOnClickRvListener != null) {
                mOnClickRvListener.onItemClick(getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mOnClickRvListener != null) {
                mOnClickRvListener.onItemLongClick(getLayoutPosition());
                return true;
            }
            return false;
        }
    }
}
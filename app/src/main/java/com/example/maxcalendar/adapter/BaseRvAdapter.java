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
    protected static final int TYPE_NONE = 2; // 没有数据

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
                //显示noneView
                return TYPE_NONE;
            }
        } else if (mFooterView == null) {
            //普通item
            return TYPE_NORMAL;
        } else if (position == getItemCount() - 1 && mFooterView != null) {
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        if (mDataList.size() == 0) {
            // 当 normal item 的数量为0时，若有 noneView，则显示 noneView
            return mNoneView != null ? 1 : 0;     // ？？
//            return mFooterView != null ? 1 : 0;
        }
        return mDataList.size() + (mFooterView == null ? 0 : 1);
//        return mDataList.size();
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

//public abstract class BaseRvAdapter<T> extends RecyclerView.Adapter {
//
//    protected List<T> mDataList;
//    protected Context mContext;
//    protected OnMonthSelectedListener mOnRvItemClickListener;
//    private OnClickListener mOnClickListener;
//
//
//    public BaseRvAdapter(Context context) {
//        this.mContext = context;
//        this.mDataList = new ArrayList<>();
//
//        mOnClickListener = new OnClickListener() {
//            @Override
//            void onClick(int position, long itemId) {
//                mOnRvItemClickListener.onItemClick(position, itemId);
//            }
//        };
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        RecyclerView.ViewHolder holder = createDefViewHolder(parent, viewType);
//        if (holder != null) {
//            holder.itemView.setTag(holder);
//            holder.itemView.setOnClickListener(mOnClickListener);
//        }
//        return null;
//    }
//
//
//    public abstract RecyclerView.ViewHolder createDefViewHolder(ViewGroup parent, int viewType);
//
//
//    public void setOnItemClickListener(OnMonthSelectedListener onRvItemClickListener) {
//        this.mOnRvItemClickListener = onRvItemClickListener;
//    }
//
//    public void addItem(T item) {
//        if (item != null) {
//            mDataList.add(item);
//            notifyItemChanged(mDataList.size());
//        }
//    }
//
//    public void setItemList(List<T> itemList) {
//        if (itemList != null) {
//            mDataList.addAll(itemList);
//            notifyDataSetChanged();
//        }
//    }
//
//    public T getItem(int position) {
//        if (!(position < 0 || position > mDataList.size())) {
//            return mDataList.get(position);
//        }
//        return null;
//    }
//
//    @Override
//    public int getItemCount() {
//        return mDataList.size();
//    }
//
//    static abstract class OnClickListener implements View.OnClickListener {
//        @Override
//        public void onClick(View v) {
//            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
//            onClick(holder.getAdapterPosition(), holder.getItemId());
//
//            Logger.d("TTTESTTT onClick : " + holder.getAdapterPosition() + " , " + holder.getItemId());
//        }
//
//        abstract void onClick(int position, long itemId);
//    }
//}

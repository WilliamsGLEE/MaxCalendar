package com.example.maxcalendar.calendar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.core.view.NestedScrollingParent;
import androidx.viewpager.widget.ViewPager;

import com.example.maxcalendar.listener.OnCalendarSelectedChangedListener;
import com.example.maxcalendar.listener.OnEndAnimatorListener;
import com.example.maxcalendar.listener.OnMWDateChangeListener;
import com.example.maxcalendar.listener.OnMonthCalendarScrolledListener;
import com.example.maxcalendar.painter.IMWPainter;
import com.example.maxcalendar.painter.MWCalendarPainter;
import com.example.maxcalendar.util.Attrs;
import com.example.maxcalendar.util.AttrsUtil;
import com.example.maxcalendar.util.DateUtil;

import org.joda.time.LocalDate;

public class CalendarLayout extends FrameLayout implements ICalendar, NestedScrollingParent, ValueAnimator.AnimatorUpdateListener {

    private MonthCalendarPager mMCalendarPager;
    private WeekCalendarPager mWCalendarPager;
    private IMWPainter mIMWPainter;         // 月和周的绘制器

    protected Attrs mAttrs;
    private ViewGroup mContentView;

    protected Rect mMonthRect;   // 月日历大小的矩形
    protected Rect mWeekRect;    // 周日历大小的矩形 ，用于判断点击事件是否在日历的范围内

    protected ValueAnimator mMonthValueAnimator;     // 月日历动画和子View动画，用于竖直拖动的中途复原
    protected ValueAnimator mChildViewValueAnimator;

    protected int mWeekHeight;   // 周日历高度
    protected int mMonthHeight;  // 月日历高度

    // 布尔值
    private boolean mIsInflateFinish;                // 是否加载完成
    private boolean mIsFirstLayout = true;
    private boolean mIsAdjustY = false;                    // 解决周日历下滑粘连以及不能复原的问题（autoScroll第三种情况）
    private boolean mIsSelectDifHeightMonInWeek = false;
    private boolean mIsSelectDifMonthInDate = false;     // 是否在周模式下切换月份
    private boolean mIsFirstScroll = true;           // 第一次手势滑动

    private final int STATE_MONTH = 1;
    private final int STATE_WEEK = 0;
    private int STATE = STATE_MONTH;

    // 滑动逻辑
    private int mDownY;
    private int mDownX;
    private int mLastY;                  // 上次的y
    private int mVerticalY = 50;         // 竖直方向上滑动的临界值，大于这个值认为是竖直滑动

    public CalendarLayout(Context context) {
        this(context, null);
    }

    public CalendarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mAttrs = AttrsUtil.getAttrs(context, attrs);
        mIMWPainter = new MWCalendarPainter(context, mAttrs);

        mMCalendarPager = new MonthCalendarPager(context, this.mAttrs, mIMWPainter);
        mWCalendarPager = new WeekCalendarPager(context, this.mAttrs, mIMWPainter);
        mMCalendarPager.setOnMWDateChangeListener(onMWDateChangeListener);
        mWCalendarPager.setOnMWDateChangeListener(onMWDateChangeListener);

        addView(mMCalendarPager, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mWCalendarPager, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mWeekHeight = mAttrs.mItemHeight;

        mMCalendarPager.setVisibility(VISIBLE);
        mWCalendarPager.setVisibility(INVISIBLE);

        mMonthValueAnimator = new ValueAnimator();
        mMonthValueAnimator.setDuration(mAttrs.animatorDuration);
        mMonthValueAnimator.addUpdateListener(this);
        mMonthValueAnimator.addListener(onEndAnimatorListener);

        mChildViewValueAnimator = new ValueAnimator();
        mChildViewValueAnimator.setDuration(mAttrs.animatorDuration);
        mChildViewValueAnimator.addUpdateListener(this);
        mChildViewValueAnimator.addListener(onEndAnimatorListener);

        mMCalendarPager.setMonthCalendarScrollListener(new OnMonthCalendarScrolledListener() {
            @Override
            public void onMonthCalendarScroll(int dy) {
                ScrolledMoveContent(dy);                // scroll滑动时刷新monthView高度
                mMonthHeight = dy;
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams childLayoutLayoutParams = mContentView.getLayoutParams();
        childLayoutLayoutParams.height = getMeasuredHeight() - mWeekHeight;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int measuredWidth = getMeasuredWidth();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        if (mIsFirstLayout) {
            mMonthHeight = mMCalendarPager.getLayoutParams().height;
            mIsFirstLayout = false;
        }

        mWCalendarPager.layout(0 + paddingLeft, 0, measuredWidth - paddingRight, mWeekHeight);
        mMCalendarPager.layout(0 + paddingLeft, 0, measuredWidth - paddingRight, mMonthHeight);

        if (!mIsSelectDifHeightMonInWeek) {        // 周模式下选择另外一个高度不同的月，不同的mMonthHeight会导致mContentView的layout高度不同，要通过gestureMove来设置才能流畅滑动
            mContentView.layout(0 + paddingLeft, mMonthHeight, measuredWidth - paddingRight, mContentView.getMeasuredHeight() + mMonthHeight);
            mIsSelectDifHeightMonInWeek = false;
        }
    }

    @Override
    protected void onFinishInflate() {      // 当View中所有的子控件均被映射成xml后触发
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) != mMCalendarPager && getChildAt(i) != mWCalendarPager) {
                mContentView = (ViewGroup) getChildAt(i);
                mContentView.bringToFront();
            }
        }
        super.onFinishInflate();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mIsInflateFinish) {
            return super.onInterceptTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = (int) ev.getY();
                mDownX = (int) ev.getX();
                mLastY = mDownY;
                break;
            case MotionEvent.ACTION_MOVE:
                int y = (int) ev.getY();
                int absY = Math.abs(mDownY - y);
                boolean inCalendar = isInCalendar(mDownX, mDownY);

                mIsAdjustY = (mDownY - y) < 0;

                if (absY > mVerticalY && inCalendar) {           // 处理在Calendar范围内而且方向是Y的事件
                    return true;                            // 拦截，交给onTouchEvent处理
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsSelectDifMonthInDate = false;
                mIsAdjustY = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int y = (int) event.getY();
                int dy = mLastY - y;         // dy < 0 ：向下滑动
                if (mIsFirstScroll) {
                    if (dy > mVerticalY) {      // 防止第一次的偏移量过大
                        dy = dy - mVerticalY;
                    } else if (dy < -mVerticalY) {
                        dy = dy + mVerticalY;
                    }
                    mIsFirstScroll = false;
                }
                gestureMove(dy, null);
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                mIsAdjustY = false;
                mIsSelectDifMonthInDate = false;
            case MotionEvent.ACTION_CANCEL:
                mIsFirstScroll = true;
                autoScroll();
                break;
        }
        return true;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (animation == mMonthValueAnimator) {
            float animatedValue = (float) animation.getAnimatedValue();
            float top = mMCalendarPager.getY();
            float i = animatedValue - top;
            float y = mMCalendarPager.getY();
            mMCalendarPager.setY(i + y);

        } else if (animation == mChildViewValueAnimator) {
            float animatedValue = (float) animation.getAnimatedValue();
            float top = mContentView.getY();
            float i = animatedValue - top;
            float y = mContentView.getY();

            mContentView.setY(i + y);
            setWeekVisible();
        }
    }

    protected void setWeekVisible() {
        if (isChildWeekState()) {
            if (mWCalendarPager.getVisibility() != VISIBLE) {
                mWCalendarPager.setVisibility(VISIBLE);
            }
            if (mMCalendarPager.getVisibility() != INVISIBLE) {
                mMCalendarPager.setVisibility(INVISIBLE);
            }
        } else {
            if (mWCalendarPager.getVisibility() != INVISIBLE) {
                mWCalendarPager.setVisibility(INVISIBLE);
            }
            if (mMCalendarPager.getVisibility() != VISIBLE) {
                mMCalendarPager.setVisibility(VISIBLE);
            }
        }
    }

    private void autoScroll() {
        float childLayoutY = mContentView.getY();
        if (STATE == STATE_MONTH && mMonthHeight - childLayoutY < mWeekHeight) {    // 月日历上滑，往下复原
            autoToMonthState();
        } else if (STATE == STATE_MONTH && mMonthHeight - childLayoutY >= mWeekHeight) {    // 月日历上滑，往上变成周日历
            autoToWeekState();
        } else if (!mIsAdjustY && STATE == STATE_WEEK && childLayoutY < mWeekHeight * 2) {       // 周日历下滑，往上复原（因为最低判定竖直滑动距离为50，所以需要调整距离以免产生bug）
            autoToWeekState();
        } else if (STATE == STATE_WEEK && childLayoutY >= mWeekHeight * 2) {      // 周日历下滑，往下变成月日历
            autoToMonthState();
        }
    }

    // 自动滑动到周
    private void autoToWeekState() {
        float monthCalendarStart = mMCalendarPager.getY();  // 起始位置
        float monthCalendarEnd = getMonthCalendarAutoWeekEndY();

        mMonthValueAnimator.setFloatValues(monthCalendarStart, monthCalendarEnd);
        mMonthValueAnimator.start();

        float childViewStart = mContentView.getY();
        float childViewEnd = mWeekHeight;
        mChildViewValueAnimator.setFloatValues(childViewStart, childViewEnd);
        mChildViewValueAnimator.start();
    }

    // 自动滑动到月
    private void autoToMonthState() {
        float monthCalendarStart = mMCalendarPager.getY();  // 起始位置
        float monthCalendarEnd = 0;
        mMonthValueAnimator.setFloatValues(monthCalendarStart, monthCalendarEnd);

        mMonthValueAnimator.start();

        float childViewStart = mContentView.getY();
        float childViewEnd = mMonthHeight;
        mChildViewValueAnimator.setFloatValues(childViewStart, childViewEnd);
        mChildViewValueAnimator.start();
    }

    protected void gestureMove(int dy, int[] consumed) {

        float monthCalendarY = mMCalendarPager.getY();      // 先获取各自的Y坐标
        float childLayoutY = mContentView.getY();           // 周模式下月份切换，mContentView.getY已经改变

        if (dy > 0 && !isChildWeekState()) {            // 上滑
            mMCalendarPager.setY(-getGestureMonthUpOffset(dy) + monthCalendarY);
            mContentView.setY(-getGestureChildUpOffset(dy) + childLayoutY);         // 900.0 -> 197.0 变正常，说明错误不在此
            if (consumed != null) consumed[1] = dy;

        } else if (dy < 0 && !isChildMonthState() && !mContentView.canScrollVertically(-1)) {        // 下滑
            mMCalendarPager.setY((int) getGestureMonthDownOffset(dy) + (int) monthCalendarY);
            mContentView.setY((int) getGestureChildDownOffset(dy) + (int) childLayoutY);
            if (consumed != null) consumed[1] = dy;     // 如果是下滑且内部View已经无法继续下拉，则消耗掉dy

        }
        setWeekVisible();
    }

    protected void ScrolledMoveContent(int dy) {
        if (!mIsSelectDifMonthInDate) {                  // 解决周视图切换月时产生的闪屏问题：
            mContentView.setY(dy);
            mIsSelectDifMonthInDate = false;
        }
    }

    // childView周状态的条件
    protected boolean isChildWeekState() {
        return mContentView.getY() <= mWeekHeight;
    }

    // childView月状态的条件
    protected boolean isChildMonthState() {
        return mContentView.getY() >= mMonthHeight;
    }


    // 点击事件是否在日历的范围内
    private boolean isInCalendar(int x, int y) {
        if (STATE == STATE_MONTH) {
            return mMonthRect.contains(x, y);
        } else {
            return mWeekRect.contains(x, y);
        }
    }

    private OnEndAnimatorListener onEndAnimatorListener = new OnEndAnimatorListener() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            if (animation == mChildViewValueAnimator) {
                setCalendarState();
            }
        }
    };

    //设置日历的状态、月周的显示及状态回调
    private void setCalendarState() {
        if (isMonthCalendarWeekState() && isChildWeekState() && STATE == STATE_MONTH) {
            STATE = STATE_WEEK;
            mWCalendarPager.setVisibility(VISIBLE);
            mMCalendarPager.setVisibility(INVISIBLE);
        } else if (isMonthCalendarMonthState() && isChildMonthState() && STATE == STATE_WEEK) {
            STATE = STATE_MONTH;
            mWCalendarPager.setVisibility(INVISIBLE);
            mMCalendarPager.setVisibility(VISIBLE);
            LocalDate pivot = mMCalendarPager.getSelectedDate();
            mWCalendarPager.jumpDate(pivot, false);
        }
    }

    // 月日历周状态
    protected boolean isMonthCalendarWeekState() {
        return mMCalendarPager.getY() <= -mMCalendarPager.getPivotDistanceFromTop();
    }

    // 月日历月状态
    protected boolean isMonthCalendarMonthState() {
        return mMCalendarPager.getY() >= 0;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (!mIsInflateFinish) {
            mMonthRect = new Rect(0, 0, mMCalendarPager.getMeasuredWidth(), mMCalendarPager.getMeasuredHeight());
            mWeekRect = new Rect(0, 0, mWCalendarPager.getMeasuredWidth(), mWCalendarPager.getMeasuredHeight());
            mMCalendarPager.setY(STATE == STATE_MONTH ? 0 : getMonthYOnWeekState(mWCalendarPager.getFirstDate()));
            mContentView.setY(STATE == STATE_MONTH ? mMonthHeight : mWeekHeight);
            mIsInflateFinish = true;
        }
    }

    protected float getMonthYOnWeekState(LocalDate localDate) {
        return -mMCalendarPager.getDistanceFromTop(localDate);
    }

    // 根据手指滑动的距离和月日历被选中的日期获取月日历滑动距离
    protected float getGestureMonthUpOffset(int dy) {
        float maxOffset;
        float monthCalendarOffset;
        if (STATE == STATE_MONTH) {
            maxOffset = mMCalendarPager.getPivotDistanceFromTop() - Math.abs(mMCalendarPager.getY());
            monthCalendarOffset = mMCalendarPager.getPivotDistanceFromTop();
        } else {
            maxOffset = mMCalendarPager.getDistanceFromTop(mWCalendarPager.getFirstDate()) - Math.abs(mMCalendarPager.getY());
            monthCalendarOffset = mMCalendarPager.getDistanceFromTop(mWCalendarPager.getFirstDate());
        }
        float childLayoutOffset = mMonthHeight - mWeekHeight;
        float offset = ((monthCalendarOffset * dy) / childLayoutOffset);
        return getOffset(offset, maxOffset);
    }

    // 向下
    protected float getGestureMonthDownOffset(int dy) {
        float maxOffset = Math.abs(mMCalendarPager.getY());
        float monthCalendarOffset;
        if (STATE == STATE_MONTH) {
            monthCalendarOffset = mMCalendarPager.getPivotDistanceFromTop();
        } else {
            monthCalendarOffset = mMCalendarPager.getDistanceFromTop(mWCalendarPager.getFirstDate());
        }
        float childLayoutOffset = mMonthHeight - mWeekHeight;
        float offset = ((monthCalendarOffset * dy) / childLayoutOffset);
        return getOffset(Math.abs(offset), maxOffset);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {     // 决定了当前控件是否能接收到其内部View(非并非是直接子View)滑动时的参数
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {       // 传入内部View移动的dx,dy
        gestureMove(dy, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        // 只有都在都在周状态下，才允许子View Fling滑动
        return !(isChildWeekState() && isMonthCalendarWeekState());
    }

    @Override
    public void onStopNestedScroll(View target) {
        if (!mIsAdjustY && isMonthCalendarMonthState() && isChildMonthState() && STATE == STATE_WEEK) {
            setCalendarState();
        } else if (isMonthCalendarWeekState() && isChildWeekState() && STATE == STATE_MONTH) {
            setCalendarState();
        } else if (!isChildMonthState() && !isChildWeekState()) {
            // 不是周状态也不是月状态时，自动滑动
            autoScroll();
        }
    }

    protected float getGestureChildDownOffset(int dy) {
        float maxOffset = mMonthHeight - mContentView.getY();
        return getOffset(Math.abs(dy), maxOffset);
    }

    protected float getGestureChildUpOffset(int dy) {
        float maxOffset = mContentView.getY() - mWeekHeight;
        return getOffset(dy, maxOffset);
    }

    // 滑动过界处理 ，如果大于最大距离就返回最大距离
    protected float getOffset(float offset, float maxOffset) {
        if (offset > maxOffset) {
            return maxOffset;
        }
        return offset;
    }

    // 获取月日历自动到周状态的y值
    protected float getMonthCalendarAutoWeekEndY() {
        float end;
        if (STATE == STATE_MONTH) {
            end = -mMCalendarPager.getPivotDistanceFromTop();       // 结束位置
        } else {
            end = -mMCalendarPager.getDistanceFromTop(mWCalendarPager.getFirstDate());
        }
        return end;
    }

    private OnMWDateChangeListener onMWDateChangeListener = new OnMWDateChangeListener() {
        @Override
        public void onMwDateChange(com.example.maxcalendar.calendar.BaseCalendarPager baseCalendarPager, final LocalDate localDate) {
            if (baseCalendarPager == mMCalendarPager && STATE == STATE_MONTH) {
                // 月日历变化,改变周的选中
                mWCalendarPager.jumpDate(localDate, false);
            } else if (baseCalendarPager == mWCalendarPager && STATE == STATE_WEEK) {

                if (!DateUtil.isEqualMonth(mMCalendarPager.getSelectedDate(), localDate)) {
                    mIsSelectDifMonthInDate = true;
                    mIsSelectDifHeightMonInWeek = true;
                }
                mMCalendarPager.jumpDate(localDate, false);     // 周日历变化，改变月的选中
                mMCalendarPager.post(new Runnable() {
                    @Override
                    public void run() {
                        mMCalendarPager.setY(getMonthYOnWeekState(localDate));      // 根据月日历的选中日期调整值
                    }
                });
            }
        }
    };

    @Override
    public Attrs getAttrs() {
        return mAttrs;
    }

    @Override
    public void setCalendarSelectedListener(OnCalendarSelectedChangedListener listener) {
        mMCalendarPager.setCalendarSelectedListener(listener);
        mWCalendarPager.setCalendarSelectedListener(listener);
    }

    @Override
    public void setMonthCalendarScrollListener(OnMonthCalendarScrolledListener listener) {
        mMCalendarPager.setMonthCalendarScrollListener(listener);
    }

    public void jumptoADate(LocalDate localDate) {
        if (STATE == STATE_MONTH) {
            mMCalendarPager.jumpDate(localDate, true);
        } else {
            mWCalendarPager.jumpDate(localDate, true);
        }

    }

    public void showYearPager() {
        mMCalendarPager.setVisibility(GONE);
        mWCalendarPager.setVisibility(GONE);
    }

    public void showMWPager() {
//        mMCalendarPager.setVisibility(VISIBLE);
//        mWCalendarPager.setVisibility(INVISIBLE);
        if (STATE == STATE_WEEK) {
            STATE = STATE_MONTH;
            mWCalendarPager.setVisibility(INVISIBLE);
            mMCalendarPager.setVisibility(VISIBLE);
            LocalDate pivot = mMCalendarPager.getSelectedDate();
            mWCalendarPager.jumpDate(pivot, false);
        } else {
            mMCalendarPager.setVisibility(VISIBLE);
            mWCalendarPager.setVisibility(INVISIBLE);
        }
    }

    public LocalDate getSelectDate() {
        return mMCalendarPager.getSelectedDate();
    }

}

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

import com.example.maxcalendar.listener.OnCalendarSelectedChangedListener;
import com.example.maxcalendar.listener.OnEndAnimatorListener;
import com.example.maxcalendar.listener.OnMWDateChangeListener;
import com.example.maxcalendar.listener.OnMonthCalendarScrolledListener;
import com.example.maxcalendar.painter.CalendarPainter;
import com.example.maxcalendar.painter.IPainter;
import com.example.maxcalendar.util.Attrs;
import com.example.maxcalendar.util.AttrsUtil;
import com.example.maxcalendar.util.DateUtil;
import com.orhanobut.logger.Logger;

import org.joda.time.LocalDate;

public class CalendarLayout extends FrameLayout implements ICalendar, NestedScrollingParent, ValueAnimator.AnimatorUpdateListener {

    private MonthCalendarPager mMCalendarPager;
    private WeekCalendarPager mWCalendarPager;
    private IPainter mIPainter;
    protected Attrs mAttrs;
    private ViewGroup mContentView;
    private boolean isInflateFinish;    // 是否加载完成

    protected Rect monthRect;   // 月日历大小的矩形
    protected Rect weekRect;    // 周日历大小的矩形 ，用于判断点击事件是否在日历的范围内

    int STATE_MONTH = 1;
    int STATE_WEEK = 0;
    int STATE = STATE_MONTH;

    protected ValueAnimator monthValueAnimator;     // 月日历动画和子View动画，用于竖直拖动的中途复原
    protected ValueAnimator childViewValueAnimator;

    protected int weekHeight;   // 周日历高度
    protected int monthHeight;  // 月日历高度

    private boolean isFirstLayout = true;
    private boolean adjustY = false;        // 解决周日历下滑粘连以及不能复原的问题（autoScroll第三种情况）

    private boolean isSelectDifMonthInDate = false;     // 是否在周模式下切换月份

    public CalendarLayout(Context context) {
        this(context, null);
    }

    public CalendarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mAttrs = AttrsUtil.getAttrs(context, attrs);
        mIPainter = new CalendarPainter(context, mAttrs);

        mMCalendarPager = new MonthCalendarPager(context, this.mAttrs, mIPainter);
        mWCalendarPager = new WeekCalendarPager(context, this.mAttrs, mIPainter);

        mMCalendarPager.setOnMWDateChangeListener(onMWDateChangeListener);
        mWCalendarPager.setOnMWDateChangeListener(onMWDateChangeListener);

        addView(mMCalendarPager, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mWCalendarPager, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        weekHeight = mAttrs.mItemHeight;

        mMCalendarPager.setVisibility(VISIBLE);
        mWCalendarPager.setVisibility(INVISIBLE);

        monthValueAnimator = new ValueAnimator();
        monthValueAnimator.setDuration(mAttrs.animatorDuration);
        monthValueAnimator.addUpdateListener(this);
        monthValueAnimator.addListener(onEndAnimatorListener);

        childViewValueAnimator = new ValueAnimator();
        childViewValueAnimator.setDuration(mAttrs.animatorDuration);
        childViewValueAnimator.addUpdateListener(this);
        childViewValueAnimator.addListener(onEndAnimatorListener);

        mMCalendarPager.setMonthCalendarScrollListener(new OnMonthCalendarScrolledListener() {
            @Override
            public void onMonthCalendarScroll(int dy) {
                ScrolledMoveContent(dy);        // scroll滑动时刷新monthView高度
                monthHeight = dy;
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams childLayoutLayoutParams = mContentView.getLayoutParams();
        childLayoutLayoutParams.height = getMeasuredHeight() - weekHeight;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int measuredWidth = getMeasuredWidth();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        if (isFirstLayout) {
            monthHeight = mMCalendarPager.getLayoutParams().height;
            isFirstLayout = false;
        }

        mWCalendarPager.layout(0 + paddingLeft, 0, measuredWidth - paddingRight, weekHeight);
        mMCalendarPager.layout(0 + paddingLeft, 0, measuredWidth - paddingRight, monthHeight);
        mContentView.layout(0 + paddingLeft, monthHeight, measuredWidth - paddingRight, mContentView.getMeasuredHeight() + monthHeight);
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

    private int dowmY;
    private int downX;
    private int lastY;      // 上次的y
    private int verticalY = 50;         // 竖直方向上滑动的临界值，大于这个值认为是竖直滑动
    private boolean isFirstScroll = true;           // 第一次手势滑动

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isInflateFinish) {
            return super.onInterceptTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dowmY = (int) ev.getY();
                downX = (int) ev.getX();
                lastY = dowmY;
                break;
            case MotionEvent.ACTION_MOVE:
                int y = (int) ev.getY();
                int absY = Math.abs(dowmY - y);
                boolean inCalendar = isInCalendar(downX, dowmY);

                adjustY = (dowmY - y) < 0;

                if (absY > verticalY && inCalendar) {           // 处理在Calendar范围内而且方向是Y的事件
                    return true;                // 拦截，交给onTouchEvent处理
                }
                break;
            case MotionEvent.ACTION_UP:
                isSelectDifMonthInDate = false;
                adjustY = false;
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
                int dy = lastY - y;         // dy < 0 ：向下滑动
                if (isFirstScroll) {
                    // 防止第一次的偏移量过大
                    if (dy > verticalY) {
                        dy = dy - verticalY;
                    } else if (dy < -verticalY) {
                        dy = dy + verticalY;
                    }
                    isFirstScroll = false;
                }
                gestureMove(dy, null);
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                adjustY = false;
                isSelectDifMonthInDate = false;
            case MotionEvent.ACTION_CANCEL:
                isFirstScroll = true;
                autoScroll();
                break;
        }
        return true;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (animation == monthValueAnimator) {
            float animatedValue = (float) animation.getAnimatedValue();
            float top = mMCalendarPager.getY();
            float i = animatedValue - top;
            float y = mMCalendarPager.getY();
            mMCalendarPager.setY(i + y);

        } else if (animation == childViewValueAnimator) {
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
        if (STATE == STATE_MONTH && monthHeight - childLayoutY < weekHeight) {    // 月日历上滑，往下复原
            autoToMonthState();
        } else if (STATE == STATE_MONTH && monthHeight - childLayoutY >= weekHeight) {    // 月日历上滑，往上变成周日历
            autoToWeekState();
        } else if (!adjustY && STATE == STATE_WEEK && childLayoutY < weekHeight * 2) {       // 周日历下滑，往上复原（因为最低判定竖直滑动距离为50，所以需要调整距离以免产生bug）
            autoToWeekState();
        } else if (STATE == STATE_WEEK && childLayoutY >= weekHeight * 2) {      // 周日历下滑，往下变成月日历
            autoToMonthState();
        }
    }

    //自动滑动到周
    private void autoToWeekState() {
        float monthCalendarStart = mMCalendarPager.getY();  // 起始位置
        float monthCalendarEnd = getMonthCalendarAutoWeekEndY();

        monthValueAnimator.setFloatValues(monthCalendarStart, monthCalendarEnd);
        monthValueAnimator.start();

        float childViewStart = mContentView.getY();
        float childViewEnd = weekHeight;
        childViewValueAnimator.setFloatValues(childViewStart, childViewEnd);
        childViewValueAnimator.start();
    }

    //自动滑动到月
    private void autoToMonthState() {
        float monthCalendarStart = mMCalendarPager.getY();  // 起始位置
        float monthCalendarEnd = 0;
        monthValueAnimator.setFloatValues(monthCalendarStart, monthCalendarEnd);

        monthValueAnimator.start();

        float childViewStart = mContentView.getY();
        float childViewEnd = monthHeight;
        childViewValueAnimator.setFloatValues(childViewStart, childViewEnd);
        childViewValueAnimator.start();
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
        if (!isSelectDifMonthInDate) {                  // 解决周视图切换月时产生的闪屏问题
            mContentView.setY(dy);
            isSelectDifMonthInDate = false;
        }
    }

    // childView周状态的条件
    protected boolean isChildWeekState() {
        return mContentView.getY() <= weekHeight;
    }

    // childView月状态的条件
    protected boolean isChildMonthState() {
        return mContentView.getY() >= monthHeight;
    }


    // 点击事件是否在日历的范围内
    private boolean isInCalendar(int x, int y) {
        if (STATE == STATE_MONTH) {
            return monthRect.contains(x, y);
        } else {
            return weekRect.contains(x, y);
        }
    }

    private OnEndAnimatorListener onEndAnimatorListener = new OnEndAnimatorListener() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            if (animation == childViewValueAnimator) {
                setCalenadrState();
            }
        }
    };

    //设置日历的状态、月周的显示及状态回调
    private void setCalenadrState() {
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

    //月日历周状态
    protected boolean isMonthCalendarWeekState() {
        return mMCalendarPager.getY() <= -mMCalendarPager.getPivotDistanceFromTop();
    }

    //月日历月状态
    protected boolean isMonthCalendarMonthState() {
        return mMCalendarPager.getY() >= 0;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (!isInflateFinish) {
            monthRect = new Rect(0, 0, mMCalendarPager.getMeasuredWidth(), mMCalendarPager.getMeasuredHeight());
            weekRect = new Rect(0, 0, mWCalendarPager.getMeasuredWidth(), mWCalendarPager.getMeasuredHeight());
            mMCalendarPager.setY(STATE == STATE_MONTH ? 0 : getMonthYOnWeekState(mWCalendarPager.getFirstDate()));
            mContentView.setY(STATE == STATE_MONTH ? monthHeight : weekHeight);
            isInflateFinish = true;
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
        float childLayoutOffset = monthHeight - weekHeight;
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
        float childLayoutOffset = monthHeight - weekHeight;
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
        if (!adjustY && isMonthCalendarMonthState() && isChildMonthState() && STATE == STATE_WEEK) {
            setCalenadrState();
        } else if (isMonthCalendarWeekState() && isChildWeekState() && STATE == STATE_MONTH) {
            setCalenadrState();
        } else if (!isChildMonthState() && !isChildWeekState()) {
            // 不是周状态也不是月状态时，自动滑动
            autoScroll();
        }
    }

    protected float getGestureChildDownOffset(int dy) {
        float maxOffset = monthHeight - mContentView.getY();
        return getOffset(Math.abs(dy), maxOffset);
    }

    protected float getGestureChildUpOffset(int dy) {
        float maxOffset = mContentView.getY() - weekHeight;
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
        public void onMwDateChange(CalendarPager calendarPager, final LocalDate localDate) {
            if (calendarPager == mMCalendarPager && STATE == STATE_MONTH) {
                // 月日历变化,改变周的选中
                mWCalendarPager.jumpDate(localDate, false);
            } else if (calendarPager == mWCalendarPager && STATE == STATE_WEEK) {

                if (!DateUtil.isEqualMonth(mMCalendarPager.getSelectedDate(), localDate)) {
                    isSelectDifMonthInDate = true;
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

    public LocalDate getSelectDate() {
        return mMCalendarPager.getSelectedDate();
    }
}

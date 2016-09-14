package com.cfbb.android.features.slidingFinishView;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.cfbb.android.R;
import com.cfbb.android.commom.utils.others.L;
import com.cfbb.android.widget.viewpagerindicator.YCPagerIndicator;

import java.util.LinkedList;
import java.util.List;


/**
 * @author MrChang
 */
public class SwipeBackLayout extends FrameLayout {
    private static final String TAG = SwipeBackLayout.class.getSimpleName();
    private View mContentView;
    private int mTouchSlop;
    private int downX;
    private int downY;
    private int tempX;
    private Scroller mScroller;
    private int viewWidth;
    private boolean isSilding;
    private boolean isFinish;

    //边缘阴影视图
    private Drawable mShadowDrawable;
    private Activity mActivity;

    //包裹的布局里面的 ViewPager,解决这两个控件之间的事件冲突
    private List<ViewPager> mViewPagers = new LinkedList<ViewPager>();

    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;
    //手指向右滑动时的最小速度
    private static final int XSPEED_MIN = 200;

    private boolean isEnable = true;

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
        mShadowDrawable = getResources().getDrawable(R.mipmap.shadow_left);
    }


    public void attachToActivity(Activity activity) {
        mActivity = activity;
        TypedArray a = activity.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.windowBackground});
        int background = a.getResourceId(0, 0);
        a.recycle();

        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setBackgroundResource(background);
        decor.removeView(decorChild);
        addView(decorChild);
        setContentView(decorChild);
        decor.addView(this);
    }

    private void setContentView(View decorChild) {
        mContentView = (View) decorChild.getParent();
    }

    private YCPagerIndicator ycPagerIndicator;
    //private ConvenientBanner convenientBanner;

    /**
     * 事件拦截操作
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        //如果禁止，不阻止事件传递
        if (!isEnable) {
            return false;
        }

        //处理ViewPager冲突问题
        ViewPager mViewPager = getTouchViewPager(mViewPagers, ev);
        //不是第一个就不拦截
        if (mViewPager != null && mViewPager.getCurrentItem() != 0) {
            return super.onInterceptTouchEvent(ev);
        }

        //针对该项目新增判断，如果手在该控件上操作 不阻止事件传递
        if (ycPagerIndicator != null && inRangeOfView(ycPagerIndicator, ev)) {
            return super.onInterceptTouchEvent(ev);
        }
        // if (convenientBanner != null && inRangeOfView(convenientBanner, ev)) {
        //     return super.onInterceptTouchEvent(ev);
        // }


        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = tempX = (int) ev.getRawX();
                downY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getRawX();
                // 满足此条件屏蔽SildingFinishLayout里面子类的touch事件
                if (moveX - downX > mTouchSlop
                        && Math.abs((int) ev.getRawY() - downY) < mTouchSlop) {
                    return true;
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnable) {
            createVelocityTracker(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    int moveX = (int) event.getRawX();
                    int deltaX = tempX - moveX;
                    tempX = moveX;
                    if (moveX - downX > mTouchSlop
                            && Math.abs((int) event.getRawY() - downY) < mTouchSlop) {
                        isSilding = true;
                    }

                    if (moveX - downX >= 0 && isSilding) {
                        mContentView.scrollBy(deltaX, 0);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    isSilding = false;

                    //获取顺时速度
                    int xSpeed = getScrollVelocity();

                    if ((mContentView.getScrollX() <= -viewWidth / 2) || xSpeed > XSPEED_MIN) {
                        isFinish = true;
                        scrollRight();
                    } else {
                        scrollOrigin();
                        isFinish = false;
                    }
                    break;
            }
            return true;
        } else {
            return super.onTouchEvent(event);
        }

    }

    /**
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 获取手指在content界面滑动的速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {

        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        //Math.abs(velocity);
        return velocity;
    }


    /**
     * 获取SwipeBackLayout里面的ViewPager的集合
     *
     * @param mViewPagers
     * @param parent
     */
    private void getAlLViewPager(List<ViewPager> mViewPagers, ViewGroup parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            if (child instanceof ViewPager) {
                mViewPagers.add((ViewPager) child);
            } else if (child instanceof YCPagerIndicator) {

                ycPagerIndicator = (YCPagerIndicator) child;

            } else if (child instanceof ViewGroup) {

                getAlLViewPager(mViewPagers, (ViewGroup) child);

            }
        }
    }

    /**
     * 往SwipeBackLayout里面添加ViewPager
     *
     * @param viewPager
     */
    public void addViewPager(ViewPager viewPager) {

        if (null != viewPager) {
            if (viewPager instanceof ViewPager) {
                mViewPagers.add(viewPager);
            }
        }

    }


    /**
     * 返回我们touch的ViewPager
     *
     * @param mViewPagers
     * @param ev
     * @return
     */
    private ViewPager getTouchViewPager(List<ViewPager> mViewPagers, MotionEvent ev) {
        if (mViewPagers == null || mViewPagers.size() == 0) {
            return null;
        }
        Rect mRect = new Rect();
        for (ViewPager v : mViewPagers) {
            if (inRangeOfView(v, ev)) {
                return v;
            }
        }
        return null;
    }

    /***
     * 判断点击事件是否在view上
     *
     * @param view
     * @param ev
     * @return
     */
    private boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getX() < x || ev.getX() > (x + view.getWidth()) || ev.getY() < y || ev.getY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            viewWidth = this.getWidth();
            getAlLViewPager(mViewPagers, this);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mShadowDrawable != null && mContentView != null) {

            int left = mContentView.getLeft()
                    - mShadowDrawable.getIntrinsicWidth();
            int right = left + mShadowDrawable.getIntrinsicWidth();
            int top = mContentView.getTop();
            int bottom = mContentView.getBottom();

            mShadowDrawable.setBounds(left, top, right, bottom);
            mShadowDrawable.draw(canvas);
        }

    }


    /**
     * 滚动出界面
     */
    private void scrollRight() {
        final int delta = (viewWidth + mContentView.getScrollX());
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item

        // 以前这样写Math.abs(delta)，套慢了，直接定义执行duration为300
        mScroller.startScroll(mContentView.getScrollX(), 0, -delta + 1, 0,
                300);
        postInvalidate();
    }

    /**
     * 滚动到起始位置
     */
    private void scrollOrigin() {
        int delta = mContentView.getScrollX();
        // 以前这样写Math.abs(delta)，套慢了，直接定义执行duration为300
        mScroller.startScroll(mContentView.getScrollX(), 0, -delta, 0,
                300);
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        // 调用startScroll的时候scroller.computeScrollOffset()返回true，
        if (mScroller.computeScrollOffset()) {
            mContentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();

            if (mScroller.isFinished() && isFinish) {
                mActivity.finish();
            }
        }
    }


    public boolean isEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
        L.i("isEnable=" + isEnable);
    }
}

package com.cfbb.android.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;

import com.cfbb.android.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by MrChang on 14-1-16. 下拉View
 */
@SuppressLint("InflateParams")
public class PullDownView extends FrameLayout implements
        GestureDetector.OnGestureListener, Animation.AnimationListener {

    public static int MAX_LENGHT = 0;
    public static final int STATE_CLOSE = 1;
    public static final int STATE_OPEN = 2;
    public static final int STATE_OPEN_MAX = 4;
    public static final int STATE_OPEN_MAX_RELEASE = 5;
    public static final int STATE_OPEN_RELEASE = 3;
    public static final int STATE_UPDATE = 6;
    public static final int STATE_UPDATE_SCROLL = 7;
    private Animation animationDown;
    private Animation animationUp;
    private ImageView arrow;
    private String date;
    private String time;
    private GestureDetector detector;
    private Flinger flinger;
    private boolean isAutoScroller;
    private int pading;
    private ProgressBar progressBar;
    private int state = STATE_CLOSE;
    private TextView title;
    TextView timeView;
    private UpdateHandle updateHandle;

    private Drawable arrow_up, arrow_down;
    private Context context;

    private boolean isEnable = true;

    public PullDownView(Context paramContext) {
        super(paramContext);
        this.context = paramContext;
        flinger = new Flinger();
        init(paramContext);
        addUpdateBar(paramContext);
    }

    public PullDownView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.context = paramContext;
        flinger = new Flinger();
        init(paramContext);
        addUpdateBar(paramContext);
    }

    // 添加正在更新的头
    private void addUpdateBar(Context context) {

        arrow_up = context.getResources().getDrawable(R.mipmap.pull_arrow_up);
        arrow_down = context.getResources().getDrawable(
                R.mipmap.pull_arrow_down);

        animationUp = AnimationUtils.loadAnimation(context, R.anim.rotate_up);
        assert animationUp != null;
        animationUp.setAnimationListener(this);
        animationDown = AnimationUtils.loadAnimation(context,
                R.anim.rotate_down);
        assert animationDown != null;
        animationDown.setAnimationListener(this);

        View updateBarView = LayoutInflater.from(context).inflate(
                R.layout.pull_down_head, null);
        assert updateBarView != null;
        updateBarView.setVisibility(View.INVISIBLE);
        addView(updateBarView);
        arrow = new ImageView(context);

        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        arrow.setScaleType(ImageView.ScaleType.FIT_CENTER);
        arrow.setLayoutParams(layoutParams);
        arrow.setImageDrawable(arrow_down);
        FrameLayout updateContent = ((FrameLayout) updateBarView
                .findViewById(R.id.iv_content));
        updateContent.addView(arrow);

        progressBar = (ProgressBar) updateBarView
                .findViewById(R.id.progressBar);
        title = ((TextView) updateBarView.findViewById(R.id.tv_title));
        timeView = (TextView) updateBarView.findViewById(R.id.tv_time);
    }

    @SuppressWarnings("deprecation")
    private void init(Context context) {
        MAX_LENGHT = context.getResources().getDimensionPixelSize(
                R.dimen.pull_view_max_height);
        setDrawingCacheEnabled(false);
        setBackgroundDrawable(null);
        // setClipChildren(false);
        detector = new GestureDetector(context, this);
        detector.setIsLongpressEnabled(true);

        mTouchSlop = ViewConfiguration
                .get(getContext()).getScaledTouchSlop();
    }

    private boolean move(float distance, boolean direction) {
        if (this.state == STATE_UPDATE) {
            if (distance < 0.0F)
                return true;
            if (direction)
                this.state = STATE_UPDATE_SCROLL;
        }
        if ((this.state != STATE_UPDATE_SCROLL) || (distance >= 0.0F)
                || (-this.pading < MAX_LENGHT)) {
            this.pading = (int) (distance + this.pading);
            if (this.pading > 0)
                this.pading = 0;
            if (direction) {
                switch (this.state) {
                    case STATE_CLOSE:
                        if (this.pading >= 0)
                            break;
                        this.state = STATE_OPEN;
                        this.progressBar.setVisibility(View.INVISIBLE);
                        this.arrow.setVisibility(View.VISIBLE);
                        break;
                    case STATE_OPEN:
                        if (Math.abs(this.pading) < MAX_LENGHT) {
                            if (this.pading != 0)
                                break;
                            this.state = STATE_CLOSE;
                            break;
                        }
                        this.state = STATE_OPEN_MAX;
                        this.progressBar.setVisibility(View.INVISIBLE);
                        this.arrow.setVisibility(View.VISIBLE);
                        this.arrow.startAnimation(this.animationUp);
                        break;
                    case STATE_OPEN_RELEASE:
                    case STATE_OPEN_MAX_RELEASE:
                        if (!direction) {
                            if (this.pading == 0)
                                this.state = STATE_CLOSE;
                        } else if (Math.abs(this.pading) < MAX_LENGHT) {
                            if (Math.abs(this.pading) >= MAX_LENGHT) {
                                if (this.pading == 0)
                                    this.state = STATE_CLOSE;
                            } else {
                                this.state = STATE_OPEN;
                                this.progressBar.setVisibility(View.INVISIBLE);
                                this.arrow.setVisibility(View.VISIBLE);
                                this.arrow.startAnimation(this.animationDown);
                            }
                        } else {
                            this.state = STATE_OPEN_MAX;
                            this.progressBar.setVisibility(View.INVISIBLE);
                            this.arrow.setVisibility(View.VISIBLE);
                            this.arrow.startAnimation(this.animationUp);
                        }
                        invalidate();
                        break;
                    case STATE_OPEN_MAX:
                        if (Math.abs(this.pading) >= MAX_LENGHT)
                            break;
                        this.state = STATE_OPEN;
                        this.progressBar.setVisibility(View.INVISIBLE);
                        this.arrow.setVisibility(View.VISIBLE);
                        this.arrow.startAnimation(this.animationDown);
                        break;
                    case STATE_UPDATE:
                        if (this.pading == 0)
                            this.state = STATE_CLOSE;
                        invalidate();
                        break;
                }
            } else {
                if (this.state != STATE_OPEN_MAX_RELEASE) {
                    if ((this.state != STATE_UPDATE) || (this.pading != 0)) {
                        if ((this.state != STATE_OPEN_RELEASE)
                                || (this.pading != 0)) {
                            if ((this.state == STATE_UPDATE_SCROLL)
                                    && (this.pading == 0))
                                this.state = STATE_CLOSE;
                        } else
                            this.state = STATE_CLOSE;
                    } else
                        this.state = STATE_CLOSE;
                } else {
                    this.state = STATE_UPDATE;
                    if (this.updateHandle != null)
                        this.updateHandle.onUpdate();
                }
                invalidate();
            }
        }
        return true;
    }

    private boolean release() {
        boolean ret = false;
        if (this.pading < 0) {
            switch (this.state) {
                case STATE_OPEN:
                case STATE_OPEN_RELEASE:
                    if (Math.abs(this.pading) < MAX_LENGHT)
                        this.state = STATE_OPEN_RELEASE;
                    scrollToClose();
                    break;
                case STATE_OPEN_MAX:
                case STATE_OPEN_MAX_RELEASE:
                    this.state = STATE_OPEN_MAX_RELEASE;
                    scrollToUpdate();
            }
            ret = true;
        }
        return ret;
    }

    private void scrollToClose() {
        this.flinger.startUsingDistance(-this.pading, 300);
        this.arrow.setImageDrawable(arrow_down);
    }

    private void scrollToUpdate() {
        this.flinger.startUsingDistance(-this.pading - MAX_LENGHT, 300);
    }

    private void updateView() {
        View view1 = getChildAt(0);
        View view2 = getChildAt(1);
        assert view1 != null;
        assert view2 != null;

        if (this.date == null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.CHINA);
            this.date = format.format(new Date());
        }

        if (this.time == null) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm",
                    Locale.CHINA);
            this.time = format.format(new Date());
        }

        int j;
        int i;
        switch (this.state) {
            case STATE_CLOSE:
                if (view1.getVisibility() != View.INVISIBLE)
                    view1.setVisibility(View.INVISIBLE);
                view2.offsetTopAndBottom(-view2.getTop());
                break;
            case STATE_OPEN:
            case STATE_OPEN_RELEASE:
                j = view2.getTop();
                view2.offsetTopAndBottom(-this.pading - j);
                if (view1.getVisibility() != View.VISIBLE)
                    view1.setVisibility(View.VISIBLE);
                i = view1.getTop();
                view1.offsetTopAndBottom(-MAX_LENGHT - this.pading - i);

                title.setText("下拉即可以刷新");

                // time.setText("更新时间：" + this.date);
                timeView.setText("最后更新: 今天 " + this.time);
                break;
            case STATE_OPEN_MAX:
            case STATE_OPEN_MAX_RELEASE:
                j = view2.getTop();
                view2.offsetTopAndBottom(-this.pading - j);
                if (view1.getVisibility() != View.VISIBLE)
                    view1.setVisibility(View.VISIBLE);
                i = view1.getTop();
                view1.offsetTopAndBottom(-MAX_LENGHT - this.pading - i);
                title.setText("释放即可刷新");
                // time.setText("更新时间：" + this.date);
                timeView.setText("最后更新: 今天 " + this.time);
                break;
            case STATE_UPDATE:
            case STATE_UPDATE_SCROLL:
                j = view2.getTop();
                view2.offsetTopAndBottom(-this.pading - j);
                i = view1.getTop();
                if (this.progressBar.getVisibility() != View.VISIBLE)
                    this.progressBar.setVisibility(View.VISIBLE);
                if (this.arrow.getVisibility() != View.INVISIBLE)
                    this.arrow.setVisibility(View.INVISIBLE);
                title.setText("加载中...");
                // time.setText("更新时间：" + this.date);
                timeView.setText("最后更新: 今天 " + this.time);
                view1.offsetTopAndBottom(-MAX_LENGHT - this.pading - i);
                if (view1.getVisibility() == View.VISIBLE)
                    break;
                view1.setVisibility(View.VISIBLE);
        }
        invalidate();
    }


    private float mLastMotionX;
    private int mTouchSlop;

    @SuppressWarnings("unused")
    public boolean dispatchTouchEvent(MotionEvent e) {

//         int i = MotionEvent.EDGE_TOP;
//         boolean ret = false;
//         if (!this.isAutoScroller) {
//         boolean detectorTouchRet = this.detector.onTouchEvent(e);
//         int j = e.getAction();
//         if (j != i) {
//         if (j == MotionEvent.ACTION_CANCEL)
//         detectorTouchRet = release();
//         } else
//         detectorTouchRet = release();
//         if ((this.state != STATE_UPDATE)
//         && (this.state != STATE_UPDATE_SCROLL)) {
//         if (((!detectorTouchRet) && (this.state != STATE_OPEN)
//         && (this.state != STATE_OPEN_MAX)
//         && (this.state != STATE_OPEN_MAX_RELEASE) && (this.state !=
//         STATE_OPEN_RELEASE))
//         || (getChildAt(i).getTop() == 0)) {
//         updateView();
//         ret = super.dispatchTouchEvent(e);
//         } else {
//         e.setAction(MotionEvent.ACTION_CANCEL);
//         super.dispatchTouchEvent(e);
//         updateView();
//         }
//         } else {
//         updateView();
//         ret = super.dispatchTouchEvent(e);
//         }
//         }
//         return ret;

        int i = MotionEvent.EDGE_TOP;
        // boolean ret = false;
        if (!this.isAutoScroller) {
            boolean detectorTouchRet = this.detector.onTouchEvent(e);
            int j = e.getAction();
            if (j != i) {
                if (j == MotionEvent.ACTION_CANCEL)
                    release();
            } else
                release();
            updateView();
            return super.dispatchTouchEvent(e);
        }
        return false;

    }


    public void endUpdate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
                Locale.CHINA);
        this.date = format.format(new Date());

        if (this.pading == 0)
            this.state = STATE_CLOSE;
        else
            scrollToClose();
    }

    public void onAnimationEnd(Animation animation) {
        if ((this.state != STATE_OPEN) && (this.state != STATE_OPEN_RELEASE))
            this.arrow.setImageDrawable(arrow_up);
        else
            this.arrow.setImageDrawable(arrow_down);
    }

    public void onAnimationRepeat(Animation animation) {
    }

    public void onAnimationStart(Animation animation) {
    }

    public boolean onDown(MotionEvent e) {
        return false;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        return false;
    }

    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        View view = getChildAt(0);
        if (view != null) {
            view.layout(0, -MAX_LENGHT - this.pading, getMeasuredWidth(),
                    -this.pading);
        }
        View view1 = getChildAt(1);
        if (view1 != null) {
            view1.layout(0, -this.pading, getMeasuredWidth(),
                    getMeasuredHeight() - this.pading);
        }
    }

    public void onLongPress(MotionEvent e) {
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        if (isEnable) {
            boolean bool = false;
            // float f = (float) (0.5D *distanceY);
            float f = distanceY;
            AdapterView<?> adapterView = null;
            if (keyAdapterView)
                adapterView = (AdapterView<?>) getChildAt(1);
            else {
                FrameLayout frameLayout = (FrameLayout) getChildAt(1);
                if (frameLayout != null) {
                    adapterView = (AdapterView<?>) frameLayout.getChildAt(0);
                }
            }
            int i;
            assert adapterView != null;
            if (adapterView.getFirstVisiblePosition() != 0) {
                i = 0;
            } else {
                i = 1;
            }
            if ((i != 0) && (adapterView.getChildAt(0) != null)) {
                View view = adapterView.getChildAt(0);
                assert view != null;
                if (view.getTop() != 0 && adapterView instanceof ListView) {
                    i = 0;
                } else if (view.getTop() < 0 && adapterView instanceof GridView) {
                    i = 0;
                } else {
                    i = 1;
                }
            }
            if (adapterView instanceof GridView) {
                if (((f < 0.0F) && (i != 0)) || (this.pading < 0)) {
                    //最多只能下拉半屏
                    if (f < 0) {
                        f = (float) (0.5D * distanceY);
                    }
                    bool = move(f, true);
                }
            }
            if (adapterView instanceof ListView) {
                if (((f < 0.0F) && (i != 0) && ( Math.abs(f) > 2) ) || (this.pading < 0)) {
                    //最多只能下拉半屏
                    if (f < 0) {
                        f = (float) (0.5D * distanceY);
                    }
                    bool = move(f, true);
                }
            }
            return bool;
        } else {
            return false;
        }

    }

    private boolean keyAdapterView = true;

    public void setIsAdapterView(boolean keyAdapterView) {
        this.keyAdapterView = keyAdapterView;
    }

    public void onShowPress(MotionEvent e) {
    }

    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    public void setUpdateHandle(UpdateHandle updateHandle) {
        this.updateHandle = updateHandle;
    }

    // 主动调出加载状态
    public void update() {
        this.pading = (-MAX_LENGHT);
        this.state = STATE_UPDATE_SCROLL;
        postDelayed(new Runnable() {
            public void run() {
                PullDownView.this.updateView();
            }
        }, 10L);
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    private class Flinger implements Runnable {

        private int lastFlingX;
        private Scroller scroller = new Scroller(context);

        public Flinger() {
        }

        private void startCommon() {
            PullDownView.this.removeCallbacks(this);
        }

        public void run() {
            Scroller scroller = this.scroller;
            boolean bool = scroller.computeScrollOffset();
            int i = scroller.getCurrX();
            int j = this.lastFlingX - i;
            PullDownView.this.move(j, false);
            PullDownView.this.updateView();
            if (!bool) {
                PullDownView.this.isAutoScroller = false;
                PullDownView.this.removeCallbacks(this);
            } else {
                this.lastFlingX = i;
                PullDownView.this.post(this);
            }
        }

        public void startUsingDistance(int dx, int duration) {
            if (dx == 0)
                dx--;
            startCommon();
            this.lastFlingX = 0;
            this.scroller.startScroll(0, 0, -dx, 0, duration);
            PullDownView.this.isAutoScroller = true;
            PullDownView.this.post(this);
        }
    }

    public static abstract interface UpdateHandle {
        public abstract void onUpdate();
    }
}

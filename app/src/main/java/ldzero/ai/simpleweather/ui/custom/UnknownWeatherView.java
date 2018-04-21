package ldzero.ai.simpleweather.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ldzero.ai.simpleweather.R;

/**
 * unknown weather effect view
 * within the view, many circles will appear at random locations and expand
 * Created on 2018/4/18.
 *
 * @author ldzero
 */
public class UnknownWeatherView extends BaseWeatherView {

    /* paint */
    private Paint mPaint;

    /* circle view info list */
    private List<Circle> mCircleList;

    /* the number of circles in the animation, default value is 10 */
    private int mCircleCnt = 5;

    /* interval of refresh circles info and invalidate, unit is ms, default value is 30ms */
    private int mRefreshInterval = 30;

    /* random object, used to generate the axis of circle center */
    private Random mRandom;

    /* Handler of view, send messages at regular intervals */
    private ViewHandler mHandler;

    public UnknownWeatherView(Context context) {
        super(context);
    }

    public UnknownWeatherView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnknownWeatherView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                startRefreshView();
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                stopRefreshView();
            }
        });

        mHandler = new ViewHandler(this);
        mCircleList = new ArrayList<>();
        mRandom = new Random();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorUnknownCircle));
    }

    /**
     * start to refresh circles info and invalidate
     *
     */
    private void startRefreshView() {
        mHandler.sendEmptyMessage(ViewHandler.MSG_REFRESH_VIEW);
    }

    /**
     * stop to refresh circles info and invalidate
     *
     */
    private void stopRefreshView() {
        mHandler.removeMessages(ViewHandler.MSG_REFRESH_VIEW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        drawCircles(canvas);
    }

    /**
     * draw view background
     *
     * @param canvas canvas
     */
    private void drawBg(Canvas canvas) {
        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.colorUnknownWeatherBg));
    }

    /**
     * draw all circle with the info store in mCircleList
     *
     * @param canvas canvas
     */
    private void drawCircles(Canvas canvas) {
        for (Circle circle : mCircleList) {
            drawCircle(canvas, circle);
        }
    }

    /**
     * draw white circle with specific info
     *
     * @param canvas canvas
     */
    private void drawCircle(Canvas canvas, Circle circle) {
        mPaint.setAlpha(circle.alpha);
        canvas.drawCircle(circle.x, circle.y, circle.radius, mPaint);
    }

    private static class ViewHandler extends Handler {

        private WeakReference<UnknownWeatherView> mViewWeakReference;

        private static final int MSG_REFRESH_VIEW = 0x300;

        private ViewHandler(UnknownWeatherView view) {
            super(Looper.getMainLooper());
            mViewWeakReference = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_REFRESH_VIEW) {
                if (mViewWeakReference != null && mViewWeakReference.get() != null) {
                    mViewWeakReference.get().refreshView();
                }
            } else {
                super.handleMessage(msg);
            }
        }
    }

    /**
     * refresh circle info in list, and invalidate
     *
     */
    private void refreshView() {
        // refresh value of circle in list
        for (int i = 0; i < mCircleList.size(); i++) {
            Circle circle = mCircleList.get(i);
            circle.alpha = circle.alpha - 2;
            // if alpha of circle less than or equal to zero, remove this circle
            if (circle.alpha <= 0) {
                mCircleList.remove(i);
                i--;
                continue;
            }
            circle.radius = circle.radius + 2;
        }
        // if number of circles less then mCircleCnt and random value less than 5, add a circle
        // determining random numbers is to avoid generating circles too quickly
        if (mCircleList.size() < mCircleCnt && mRandom.nextInt(100) < 5) {
            Circle circle = new Circle();
            circle.alpha = 255;
            circle.radius = 0;
            circle.x = mRandom.nextInt(getWidth());
            circle.y = mRandom.nextInt(getHeight());
            mCircleList.add(circle);
        }
        postInvalidate();
        // send a message, continue to refresh view after next interval
        mHandler.sendEmptyMessageDelayed(ViewHandler.MSG_REFRESH_VIEW, mRefreshInterval);
    }

    /**
     * circle entity
     */
    private class Circle {

        /* alpha of circle */
        private int alpha;

        /* radius of circle */
        private int radius;

        /* x-axis of center of circle */
        private int x;

        /* y-axis of center of circle */
        private int y;

    }

    /**
     * number of circles setter
     *
     * @param circleCnt number of circles
     */
    public void setCircleCnt(int circleCnt) {
        mCircleCnt = circleCnt;
    }

    /**
     * interval of refreshing view setter
     *
     * @param refreshInterval interval of refreshing view, unit is ms
     */
    public void setRefreshInterval(int refreshInterval) {
        mRefreshInterval = refreshInterval;
    }
}

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
import ldzero.ai.simpleweather.utils.ViewUtils;

/**
 * sunny weather daytime effect view
 * Created on 2018/4/21.
 *
 * @author ldzero
 */
public class SunnyDayView extends View {

    /* radius of sun, unit is px, method init() will initialize sun radius with a default value of
    corresponding to the pixel value of 230dp  */
    private int mSunRadius;

    /* paint of drawing sun and light */
    private Paint mSunPaint;

    /* recording the info of each light */
    List<Light> mLightList = new ArrayList<>();

    /* maximum length of light, method init() will initialize maximum length of light with
    a default value of corresponding to the pixel value of 30dp */
    private int mMaxLightLen;

    /* width of light, method init() will initialize width of light with a default value of
    corresponding to the pixel value of 3dp */
    private int mLightWidth;

    /* the number of lights, default value is 5 */
    private int mLightCnt = 5;

    /* random object */
    private Random mRandom = new Random();

    /* interval of refresh circles info and invalidate, unit is ms, default value is 60ms */
    private int mRefreshViewInterval = 60;

    /* view handler */
    private ViewHandler mHandler = new ViewHandler(this);

    public SunnyDayView(Context context) {
        super(context);
    }

    public SunnyDayView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SunnyDayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        mSunRadius = ViewUtils.dp2px(getContext(), 230);
        mMaxLightLen = ViewUtils.dp2px(getContext(), 30);
        mLightWidth = ViewUtils.dp2px(getContext(), 3);
        initLightLenList();
        initPaint();
    }

    /**
     * start to refresh view, expose this method to subclass
     *
     */
    protected void startRefreshView() {
        mHandler.sendEmptyMessageDelayed(ViewHandler.MSG_REFRESH_VIEW, mRefreshViewInterval);
    }

    /**
     * stop refreshing view
     *
     */
    protected void stopRefreshView() {
        mHandler.removeMessages(ViewHandler.MSG_REFRESH_VIEW);
    }

    /**
     * init list of lengths of light, expose this method to subclass
     *
     */
    private void initLightLenList() {
        mLightList.clear();
        for (int i = 0; i < mLightCnt; i++) {
            Light light = new Light();
            light.mLightLen = mRandom.nextInt(mMaxLightLen);
            light.mDirection = 0;
            mLightList.add(light);
        }
    }

    /**
     * init paints
     *
     */
    private void initPaint() {
        mSunPaint = new Paint();
        mSunPaint.setAntiAlias(true);
        mSunPaint.setStrokeWidth(mLightWidth);
        mSunPaint.setStrokeCap(Paint.Cap.ROUND);
        mSunPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorSun));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSky(canvas);
        drawSun(canvas);
        drawLight(canvas);
    }

    /**
     * draw blue sky as background for the entire view
     *
     * @param canvas canvas
     */
    private void drawSky(Canvas canvas) {
        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.colorSunnyDaySky));
    }

    /**
     * draw a sun in the upper right corner of the view
     *
     * @param canvas canvas
     */
    private void drawSun(Canvas canvas) {
        canvas.drawCircle(getWidth(), 0, mSunRadius, mSunPaint);
    }

    /**
     * draw the light of sun
     *
     * @param canvas canvas
     */
    private void drawLight(Canvas canvas) {
        int marginSun = ViewUtils.dp2px(getContext(), 10);
        canvas.save();
        canvas.translate(getWidth(), 0);
        for (int i = 0; i < mLightList.size(); i++) {
            canvas.rotate(90 / (mLightCnt + 1));
            canvas.drawLine(0, mSunRadius + marginSun, 0,
                    mSunRadius + marginSun + mLightList.get(i).mLightLen, mSunPaint);
        }
        canvas.restore();
    }

    private static class ViewHandler extends Handler {

        private WeakReference<SunnyDayView> mViewWeakReference;

        private static final int MSG_REFRESH_VIEW = 0x301;

        private ViewHandler(SunnyDayView view) {
            super(Looper.getMainLooper());
            mViewWeakReference = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_REFRESH_VIEW) {
                if (mViewWeakReference != null && mViewWeakReference.get() != null) {
                    mViewWeakReference.get().refreshViewInfoAndInvalidate();
                }
            } else {
                super.handleMessage(msg);
            }
        }
    }

    /**
     * refresh value in the light list and redraw view
     *
     */
    private void refreshViewInfoAndInvalidate() {
        for (int i = 0; i < mLightList.size(); i++) {
            Light light = mLightList.get(i);
            if (light.mDirection == 0) {
                light.mLightLen = light.mLightLen + 3;
                if (light.mLightLen > mMaxLightLen) {
                    light.mLightLen = mMaxLightLen;
                    light.mDirection = 1;
                }
            } else {
                light.mLightLen = light.mLightLen - 3;
                if (light.mLightLen < 0) {
                    light.mLightLen = 0;
                    light.mDirection = 0;
                }
            }
        }
        invalidate();
        mHandler.sendEmptyMessageDelayed(ViewHandler.MSG_REFRESH_VIEW, mRefreshViewInterval);
    }

    private class Light {

        /* the length of light */
        private int mLightLen;

        /* the direction of light changes,
         * 0 is changing from the inside to outside,
         * non-zero is changing from outside to inside */
        private int mDirection;
    }

    /**
     * radius of sun setter
     *
     * @param sunRadiusDp radius of sun, unit is dp, this value is valid only when it is greater than 0
     */
    public void setSunRadius(int sunRadiusDp) {
        mSunRadius = ViewUtils.dp2px(getContext(), sunRadiusDp);
    }

    /**
     * maximum length of light setter
     *
     * @param maxLightLenDp maximum length of light, unit is dp, this value is valid only when it is greater than 0
     */
    public void setMaxLightLen(int maxLightLenDp) {
        if (maxLightLenDp > 0) {
            mMaxLightLen = ViewUtils.dp2px(getContext(), maxLightLenDp);
        }
    }

    /**
     * width of light setter
     *
     * @param lightWidthDp width of light, unit is dp, this value is valid only when it is greater than 0
     */
    public void setLightWidth(int lightWidthDp) {
        if (lightWidthDp > 0) {
            mLightWidth = ViewUtils.dp2px(getContext(), lightWidthDp);
            mSunPaint.setStrokeWidth(mLightWidth);
        }
    }

    /**
     * number of lights
     *
     * @param lightCnt number of lights, this value is valid only when it is greater than 0
     */
    public void setLightCnt(int lightCnt) {
        if (lightCnt > 0) {
            mLightCnt = lightCnt;
            initLightLenList();
        }
    }
}

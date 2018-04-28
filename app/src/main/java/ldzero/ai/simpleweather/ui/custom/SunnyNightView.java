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
 * sunny weather evening effect view
 * Created on 2018/4/23.
 *
 * @author ldzero
 */
public class SunnyNightView extends BaseWeatherView {

    /* paint of drawing moon */
    private Paint mMoonPaint;

    /* paint of drawing stars */
    private Paint mStarPaint;

    /* margin from view to screen, default value is the corresponding pixel value of 70dp */
    private int mMoonMargin;

    /* radius of moon, default value is the corresponding pixel value of 150dp */
    private int mMoonRadius;

    /* recording the info of stars */
    private List<Star> mStarList = new ArrayList<>();

    /* the number of stars in the animation, default value is 10 */
    private int mStarCnt = 10;

    /* maximum length of stars' light, method init() will initialize maximum length of light with
    a default value of corresponding to the pixel value of 5dp */
    private int mMaxLightLen;

    /* random object */
    private Random mRandom = new Random();

    /* handler of view */
    private ViewHandler mHandler = new ViewHandler(this);

    /* interval of refresh stars info and invalidate, unit is ms, default value is 60ms */
    private int mRefreshInterval = 60;

    /* indicates if view need to draw the moon and star */
    private boolean mNeedToDrawMoon = true;

    public SunnyNightView(Context context) {
        super(context);
    }

    public SunnyNightView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SunnyNightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                mHandler.sendEmptyMessageDelayed(ViewHandler.MSG_REFRESH_VIEW, mRefreshInterval);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                mHandler.removeMessages(ViewHandler.MSG_REFRESH_VIEW);
            }
        });

        mMoonMargin = ViewUtils.dp2px(getContext(), 70);
        mMoonRadius = ViewUtils.dp2px(getContext(), 70);
        mMaxLightLen = ViewUtils.dp2px(getContext(), 5);

        initPaints();
    }

    private void initPaints() {
        mMoonPaint = new Paint();
        mMoonPaint.setAntiAlias(true);
        mMoonPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorMoon));

        mStarPaint = new Paint();
        mStarPaint.setAntiAlias(true);
        mStarPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorStar));
        mStarPaint.setStrokeWidth(ViewUtils.dp2px(getContext(), 1));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSky(canvas);
        if (mNeedToDrawMoon) {
            drawMoon(canvas);
            drawStarList(canvas);
        }
    }

    /**
     * draw dark sky as background for the entire view
     *
     * @param canvas canvas
     */
    private void drawSky(Canvas canvas) {
        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.colorSunnyEveningSky));
    }

    /**
     * draw the moon
     *
     * @param canvas canvas
     */
    private void drawMoon(Canvas canvas) {
        canvas.drawCircle(getWidth() - mMoonMargin - mMoonRadius,
                mMoonMargin + mMoonRadius, mMoonRadius, mMoonPaint);
    }

    /**
     * draw stars
     *
     * @param canvas canvas
     */
    private void drawStarList(Canvas canvas) {
        for (int i = 0; i < mStarList.size(); i++) {
            Star star = mStarList.get(i);
            canvas.drawLine(star.mX - star.mStarLightLen, star.mY,
                    star.mX + star.mStarLightLen, star.mY, mStarPaint);
            canvas.drawLine(star.mX, star.mY - star.mStarLightLen,
                    star.mX, star.mY + star.mStarLightLen, mStarPaint);
        }
    }

    private static class ViewHandler extends Handler {

        /* weak reference of this SunnyNightView */
        private WeakReference<SunnyNightView> mViewWeakReference;

        private static final int MSG_REFRESH_VIEW = 0x302;

        private ViewHandler(SunnyNightView view) {
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
        for (int i = 0; i < mStarList.size(); i++) {
            Star star = mStarList.get(i);
            if (star.mDirection == 0) {
                star.mStarLightLen = star.mStarLightLen + 1;
                if (star.mStarLightLen >= mMaxLightLen) {
                    star.mStarLightLen = mMaxLightLen;
                    star.mDirection = 1;
                }
            } else {
                star.mStarLightLen = star.mStarLightLen - 1;
                // if the length of star's light has been less than or equal 0, remove this item
                if (star.mStarLightLen <= 0) {
                    mStarList.remove(i);
                    i--;
                }
            }
        }
        // if number of stars less then mStarCnt and random value less than 10, add a star
        // determining random numbers is to avoid generating stars too quickly
        if (mStarList.size() < mStarCnt && mRandom.nextInt(100) < 10) {
            Star star = new Star();
            star.mX = mRandom.nextInt(getWidth());
            star.mY = mRandom.nextInt(getHeight());
            star.mDirection = 0;
            star.mStarLightLen = 0;
            mStarList.add(star);
        }
        invalidate();
        mHandler.sendEmptyMessageDelayed(ViewHandler.MSG_REFRESH_VIEW, mRefreshInterval);
    }

    private class Star {

        /* x-axis value of star */
        private int mX;

        /* y-axis value of star */
        private int mY;

        /* the length of start light */
        private int mStarLightLen;

        /* the direction of light changes,
         * 0 is changing from the inside to outside,
         * non-zero is changing from outside to inside */
        private int mDirection;
    }

    /**
     * margin from view to screen setter, unit is dp,
     *
     * @param moonMarginDp dp value of margin from view to screen
     */
    public void setMoonMargin(int moonMarginDp) {
        mMoonMargin = ViewUtils.dp2px(getContext(), moonMarginDp);
    }

    /**
     * radius of moon setter, unit is dp
     *
     * @param moonRadiusDp dp value of moon's radius
     */
    public void setMoonRadius(int moonRadiusDp) {
        mMoonRadius = ViewUtils.dp2px(getContext(), moonRadiusDp);
    }

    /**
     * number of stars setter, unit is dp
     *
     * @param starCnt number of stars
     */
    public void setStarCnt(int starCnt) {
        mStarCnt = starCnt;
    }

    /**
     * maximum length of stars' light setter
     *
     * @param maxLightLen maximum length of stars' light
     */
    public void setMaxLightLen(int maxLightLen) {
        mMaxLightLen = maxLightLen;
    }

    /**
     * interval of refresh stars info and invalidate setter
     *
     * @param refreshInterval interval of refresh stars info and invalidate
     */
    public void setRefreshInterval(int refreshInterval) {
        mRefreshInterval = refreshInterval;
    }

    /**
     * whether the view need to draw the sun setting
     *
     * @param needToDrawMoon a flat indicating whether the view need to draw the moon and stars
     */
    public void setNeedToDrawMoon(boolean needToDrawMoon) {
        mNeedToDrawMoon = needToDrawMoon;
        if (mNeedToDrawMoon) {
            mHandler.removeMessages(ViewHandler.MSG_REFRESH_VIEW);
            mHandler.sendEmptyMessageDelayed(ViewHandler.MSG_REFRESH_VIEW, mRefreshInterval);
        } else {
            mHandler.removeMessages(ViewHandler.MSG_REFRESH_VIEW);
        }
        postInvalidate();
    }
}

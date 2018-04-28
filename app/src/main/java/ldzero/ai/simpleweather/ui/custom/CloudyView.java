package ldzero.ai.simpleweather.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.HandlerThread;
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
 * cloudy weather daytime effect view
 * Created on 2018/4/26.
 *
 * @author ldzero
 */
public class CloudyView extends BaseWeatherView {

    /* paint of drawing cloud */
    private Paint mCloudPaint;

    /* recording the info of cloud */
    private List<Cloud> mCloudList = new ArrayList<>();

    /* path of cloud */
    private Path mCloudPath = new Path();

    /* a thread to update cloud information */
    private static HandlerThread mUpdateCloudInfoThread = new HandlerThread("UpdateCloudInfoThread");

    /* view handler */
    private CloudHandler mHandler;

    /* interval of refresh cloud info and invalidate, unit is ms, default value is 60ms */
    private int mRefreshInterval = 60;

    /* number of cloud, default value is 10 */
    private int mCloudCnt = 10;

    /* random object */
    private Random mRandom = new Random();

    /* cloud base path */
    private Path mCloudBasePath = new Path();

    /* cloud base rectangle */
    private RectF mCloudBaseRectF = new RectF();

    /* max width of cloud, default value is the corresponding pixel value of 170dp */
    private int mCloudMaxWidth;

    /* min width of cloud, default value is the corresponding pixel value of 140dp */
    private int mCloudMinWidth;

    /* max height of cloud, default value is the corresponding pixel value of 100dp */
    private int mCloudMaxHeight;

    /* min height of cloud, default value is the corresponding pixel value of 70dp */
    private int mCloudMinHeight;

    /* a path measure, used to calculate positions of path's segments */
    private PathMeasure mPathMeasure = new PathMeasure();

    /* number of cloud curves, default value is 5 */
    private int mCloudCurveCnt = 5;

    /* float arrays using while creating cloud */
    private float[] mPos = new float[2];
    private float[] mTan = new float[2];

    public CloudyView(Context context) {
        super(context);
    }

    public CloudyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CloudyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                mUpdateCloudInfoThread.start();
                mHandler = new CloudHandler(CloudyView.this);
                mHandler.sendEmptyMessageDelayed(CloudHandler.MSG_REFRESH_CLOUD_INFO, mRefreshInterval);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                mHandler.removeMessages(CloudHandler.MSG_REFRESH_CLOUD_INFO);
                mUpdateCloudInfoThread.quit();
            }
        });
        mCloudMaxWidth = ViewUtils.dp2px(getContext(), 170);
        mCloudMinWidth = ViewUtils.dp2px(getContext(), 140);
        mCloudMaxHeight = ViewUtils.dp2px(getContext(), 100);
        mCloudMinHeight = ViewUtils.dp2px(getContext(), 70);
        initPaints();
    }

    private void initPaints() {
        mCloudPaint = new Paint();
        mCloudPaint.setAntiAlias(true);
        mCloudPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorCloud));
        mCloudPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCloud(canvas);
    }

    /**
     * draw cloud
     *
     * @param canvas canvas
     */
    private void drawCloud(Canvas canvas) {
        for (int i = 0; i < mCloudList.size(); i++) {
            Cloud cloud = mCloudList.get(i);
            List<CloudCurveInfo> curveInfoList = cloud.curveInfoList;
            mCloudPath.reset();
            mCloudBaseRectF.set(cloud.baseLeft, cloud.baseTop, cloud.baseRight, cloud.baseBottom);
            mCloudPath.addOval(mCloudBaseRectF, Path.Direction.CW);
            mCloudPath.lineTo(cloud.baseRight, cloud.baseBottom);
            mCloudPath.lineTo(cloud.baseLeft, cloud.baseBottom);
            mCloudPath.lineTo(cloud.baseLeft, (cloud.baseTop + cloud.baseBottom) / 2);
            for (int j = 0; j < curveInfoList.size(); j++) {
                CloudCurveInfo curveInfo = curveInfoList.get(j);
                mCloudPath.addCircle(curveInfo.centerX, curveInfo.centerY, curveInfo.radius, Path.Direction.CW);
            }
            canvas.drawPath(mCloudPath, mCloudPaint);
        }
    }

    private static class CloudHandler extends Handler {

        private WeakReference<CloudyView> mViewWeakReference;

        private static final int MSG_REFRESH_CLOUD_INFO = 0x303;

        private CloudHandler(CloudyView view) {
            super(mUpdateCloudInfoThread.getLooper());
            mViewWeakReference = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_REFRESH_CLOUD_INFO) {
                if (mViewWeakReference != null && mViewWeakReference.get() != null) {
                    mViewWeakReference.get().refreshCloudInto();
                }
            } else {
                super.handleMessage(msg);
            }
        }
    }

    /**
     * refresh information of cloud
     *
     */
    private void refreshCloudInto() {
        int movingInstance = 3;
        for (int i = 0; i < mCloudList.size(); i++) {
            Cloud cloud = mCloudList.get(i);
            List<CloudCurveInfo> curveInfoList = mCloudList.get(i).curveInfoList;
            // move points
            cloud.baseLeft = cloud.baseLeft - movingInstance;
            cloud.baseRight = cloud.baseRight - movingInstance;
            // a flag indicating if all points have left the screen
            boolean isAllPointLeaveScreen = true;
            for (int j = 0; j < curveInfoList.size(); j++) {
                CloudCurveInfo curveInfo = curveInfoList.get(j);
                curveInfo.centerX = curveInfo.centerX - movingInstance;
                if (curveInfo.centerX + curveInfo.radius > 0) {
                    isAllPointLeaveScreen = false;
                }
            }
            // if the points of cloud have left the sceen, remove this cloud
            if (isAllPointLeaveScreen) {
                mCloudList.remove(i);
                i--;
            }
        }
        // if number of cloud less then mCloudCnt and random value less than 2, add a star
        // determining random numbers is to avoid generating stars too quickly
        if (mCloudList.size() < mCloudCnt && mRandom.nextInt(100) < 2) {
            mCloudList.add(createCloud());
        }
        postInvalidate();
        mHandler.sendEmptyMessageDelayed(CloudHandler.MSG_REFRESH_CLOUD_INFO, mRefreshInterval);
    }

    /**
     * create cloud
     *
     * @return cloud
     */
    private Cloud createCloud() {
        Cloud cloud = new Cloud();
        // setup cloud base path as oval
        mCloudBasePath.reset();
        int left = getWidth();
        int top = mRandom.nextInt(getHeight() / 2);
        int right = left + mRandom.nextInt(mCloudMaxWidth - mCloudMinWidth) + mCloudMinWidth;
        int bottom = top + (mRandom.nextInt(mCloudMaxHeight - mCloudMinHeight) + mCloudMinHeight);
        // the top of defined above is the top of the entire cloud,
        // but the top for calculation is the top of the cloud base,
        // so define a constant to fix the new top
        int correctionFactor = (bottom - top) / 2;
        int correctionTop = top + correctionFactor;
        mCloudBaseRectF.set(left, correctionTop, right, bottom);
        mCloudBasePath.addOval(mCloudBaseRectF, Path.Direction.CCW);
        // get positions value of path's segments
        mPathMeasure.setPath(mCloudBasePath, false);
        // setup base info of cloud
        cloud.baseLeft = left;
        cloud.baseTop = correctionTop;
        cloud.baseRight = right;
        cloud.baseBottom = bottom;
        // generate curve info
        float basePathLen = mPathMeasure.getLength();
        List<CloudCurveInfo> curveInfoList = new ArrayList<>();
        for (int i = 0; i < mCloudCurveCnt; i++) {
            CloudCurveInfo curveInfo = new CloudCurveInfo();
            // calculate start point position
            float measureLen = basePathLen / 2 / (mCloudCurveCnt - 1) * i;
            mPathMeasure.getPosTan(measureLen, mPos, mTan);
            curveInfo.centerX = mPos[0];
            curveInfo.centerY = mPos[1];
            curveInfo.radius = bottom - curveInfo.centerY;

            curveInfoList.add(curveInfo);
        }
        cloud.curveInfoList = curveInfoList;
        return cloud;
    }

    /**
     * containing a list of BezierPoints
     *
     */
    private class Cloud {

        private float baseLeft;

        private float baseTop;

        private float baseRight;

        private float baseBottom;

        private List<CloudCurveInfo> curveInfoList = new ArrayList<>();
    }

    /**
     * Bezier quadratic curve points holder
     *
     */
    private class CloudCurveInfo {

        /* x-axis value of center */
        private float centerX;

        /* y-axis value of center */
        private float centerY;

        /* radius value of circle */
        private float radius;
    }

    /**
     * interval of refresh cloud info and invalidate setter
     *
     * @param refreshInterval interval of refresh cloud info and invalidate
     */
    public void setRefreshInterval(int refreshInterval) {
        mRefreshInterval = refreshInterval;
    }

    /**
     * number of cloud setter
     *
     * @param cloudCnt number of cloud
     */
    public void setCloudCnt(int cloudCnt) {
        mCloudCnt = cloudCnt;
    }

    /**
     * maximum width of cloud setter, must be greater than mCloudMinWidth
     *
     * @param cloudMaxWidth maximum width of cloud
     */
    public void setCloudMaxWidth(int cloudMaxWidth) {
        if (cloudMaxWidth > mCloudMinWidth) {
            mCloudMaxWidth = cloudMaxWidth;
        }
    }

    /**
     * minimum width of cloud setter, must be less than mCloudMaxWidth
     *
     * @param cloudMinWidth minimum width of cloud
     */
    public void setCloudMinWidth(int cloudMinWidth) {
        if (cloudMinWidth < mCloudMaxWidth) {
            mCloudMinWidth = cloudMinWidth;
        }
    }

    /**
     * maximum height of cloud setter, must be greater than mCloudMinHeight
     *
     * @param cloudMaxHeight maximum height of cloud
     */
    public void setCloudMaxHeight(int cloudMaxHeight) {
        if (cloudMaxHeight > mCloudMinHeight) {
            mCloudMaxHeight = cloudMaxHeight;
        }
    }

    /**
     * minimum height of cloud setter, must be less than mCloudMaxHeight
     *
     * @param cloudMinHeight minimum height of cloud
     */
    public void setCloudMinHeight(int cloudMinHeight) {
        if (cloudMinHeight < mCloudMaxHeight) {
            mCloudMinHeight = cloudMinHeight;
        }
    }

    /**
     * number of cloud curve setter
     *
     * @param cloudCurveCnt number of cloud curve
     */
    public void setCloudCurveCnt(int cloudCurveCnt) {
        mCloudCurveCnt = cloudCurveCnt;
    }
}

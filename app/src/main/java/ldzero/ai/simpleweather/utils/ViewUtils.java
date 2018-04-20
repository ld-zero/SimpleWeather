package ldzero.ai.simpleweather.utils;

import android.content.Context;
import android.view.View;

/**
 * view utils
 * Created on 2017/11/9.
 *
 * @author ldzero
 */

public class ViewUtils {

    /**
     * px convert to dp
     *
     * @param context context
     * @param pxValue pixel value
     * @return dp value
     */
    public static int px2dp(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * dp convert to px
     *
     * @param context context
     * @param dpValue dp value
     * @return pixel value
     */
    public static int dp2px(Context context, float dpValue) {
        return (int) (dpValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * px convert to sp
     *
     * @param context context
     * @param pxValue pixel value
     * @return sp value
     */
    public static int px2sp(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

    /**
     * sp convert to px
     *
     * @param context context
     * @param spValue sp value
     * @return pixel value
     */
    public static int sp2px(Context context, float spValue) {
        return (int) (spValue * context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

    /**
     * process value of MeasureSpec
     *
     * @param measureSpec measureSpec
     * @param defSize default size
     * @return size after processing
     */
    public static int handleMeasure(int measureSpec, int defSize) {
        int result;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(defSize, specSize);
        } else {
            result = defSize;
        }
        return result;
    }
}

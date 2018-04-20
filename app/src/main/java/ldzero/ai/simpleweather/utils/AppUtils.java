package ldzero.ai.simpleweather.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import ldzero.ai.simpleweather.BuildConfig;

/**
 * app common utils
 * Created on 2018/3/24.
 *
 * @author ldzero
 */

public class AppUtils {

    /**
     * determine whether the app is debug mode
     *
     * @return whether the app is debug mode
     */
    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    /**
     * get width pixel value of screen
     *
     * @param context context
     * @return width pixel value of screen
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = getDisplayMetrics(context);
        return displayMetrics.widthPixels;
    }

    /**
     * get height pixel value of screen
     *
     * @param context context
     * @return height pixel value of screen
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = getDisplayMetrics(context);
        return displayMetrics.heightPixels;
    }

    /**
     * get display metrics
     *
     * @param context context
     * @return display metrics
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        Resources resources = context.getResources();
        return resources.getDisplayMetrics();
    }
}

package ldzero.ai.simpleweather.utils;

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
}

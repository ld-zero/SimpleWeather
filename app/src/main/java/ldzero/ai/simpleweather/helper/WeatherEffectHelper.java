package ldzero.ai.simpleweather.helper;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.Calendar;

import ldzero.ai.simpleweather.R;
import ldzero.ai.simpleweather.model.NowWeather;
import ldzero.ai.simpleweather.ui.weather_effect_fragment.SunnyFragment;
import ldzero.ai.simpleweather.ui.weather_effect_fragment.UnknownWeatherFragment;

/**
 * helper class of weather effect fragment
 * Created on 2018/4/18.
 *
 * @author ldzero
 */
public class WeatherEffectHelper {

    /**
     * instantiate fragment based on weather data
     *
     * @param nowWeather weather data
     * @return corresponding fragment
     */
    public static Fragment getWeatherEffectFragment(NowWeather nowWeather) {
        // determine if the weather time is daytime
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(nowWeather.getLastUpdateTime());
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        boolean isDaytime = hourOfDay >= 6 && hourOfDay <= 18;
        // get the corresponding fragment
        switch (nowWeather.getWeatherCode()) {
            case 0:
            case 1:
            case 2:
            case 3:
                return SunnyFragment.newInstance(isDaytime);
            default:
                return UnknownWeatherFragment.newInstance();
        }
    }

    /**
     * get text color of weather data according to weather data showing on the screen
     *
     * @param nowWeather weather data
     * @return text color id
     */
    @ColorRes
    public static int getTextColorIdAccordingWeather(NowWeather nowWeather) {
        switch (nowWeather.getWeatherCode()) {
            default:
                return R.color.textWhite;
        }
    }

    /**
     * return the current weather effect background is dark or not
     *
     * @param nowWeather weather data
     * @return the current weather effect background is dark or not
     */
    public static boolean isWeatherEffectBgDark(NowWeather nowWeather) {
        switch (nowWeather.getWeatherCode()) {
            default:
                return true;
        }
    }
}

package ldzero.ai.simpleweather.model;

/**
 * weather data model
 * Created on 2018/4/12.
 *
 * @author ldzero
 */

public interface NowWeather {

    String getLocation();

    String getUnit();

    String getWeatherText();

    int getWeatherCode();

    int getTemperature();

    long getLastUpdateTime();

    long getLastUpdateDbTime();
}

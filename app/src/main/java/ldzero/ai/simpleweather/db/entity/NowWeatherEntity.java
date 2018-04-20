package ldzero.ai.simpleweather.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ldzero.ai.simpleweather.model.NowWeather;

/**
 * weather data entity
 * Created on 2018/4/12.
 *
 * @author ldzero
 */

@Entity(tableName = "now_weather")
public class NowWeatherEntity implements NowWeather {

    private static final String UNIT_C = "c";
    private static final String UNIT_F = "f";

    @StringDef({UNIT_C, UNIT_F})
    @Retention(RetentionPolicy.SOURCE)
    private  @interface UNIT {
    }

    @PrimaryKey
    private int id;

    /* city name */
    private String location;

    /* data unit, this value would be "c" or "f"
     * case "c", the temperature unit is Celsius, the wind speed unit is km/h, the visibility unit is km, the pressure unit is mb
     * case "f", the temperature unit is Fahrenheit, the wind speed unit is mph, the visibility unit is mile, the pressure unit is inch*/
    @UNIT
    private String unit = UNIT_C;

    private String weatherText;

    private int weatherCode;

    private int temperature;

    /* last update time of weather data */
    private long lastUpdateTime;

    /* last update time of db data */
    private long lastUpdateDbTime;

    public int getId() {
        return id;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public String getWeatherText() {
        return weatherText;
    }

    @Override
    public int getWeatherCode() {
        return weatherCode;
    }

    @Override
    public int getTemperature() {
        return temperature;
    }

    @Override
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    @Override
    public long getLastUpdateDbTime() {
        return lastUpdateDbTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setWeatherText(String weatherText) {
        this.weatherText = weatherText;
    }

    public void setWeatherCode(int weatherCode) {
        this.weatherCode = weatherCode;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setLastUpdateDbTime(long lastUpdateDbTime) {
        this.lastUpdateDbTime = lastUpdateDbTime;
    }
}

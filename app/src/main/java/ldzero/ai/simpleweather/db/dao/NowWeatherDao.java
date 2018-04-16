package ldzero.ai.simpleweather.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ldzero.ai.simpleweather.db.entity.NowWeatherEntity;
import ldzero.ai.simpleweather.model.NowWeather;

/**
 * weather data dao
 * Created on 2018/4/12.
 *
 * @author ldzero
 */

@Dao
public interface NowWeatherDao {

    @Query("SELECT * FROM now_weather")
    LiveData<List<NowWeatherEntity>> loadNowWeather();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNowWeather(NowWeatherEntity weather);
}

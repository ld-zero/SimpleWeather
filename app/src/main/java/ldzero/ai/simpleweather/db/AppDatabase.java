package ldzero.ai.simpleweather.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import ldzero.ai.simpleweather.utils.AppExecutors;
import ldzero.ai.simpleweather.db.dao.NowWeatherDao;
import ldzero.ai.simpleweather.db.entity.NowWeatherEntity;

/**
 * common database
 * Created on 2018/4/12.
 *
 * @author ldzero
 */

@Database(entities = {NowWeatherEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    private static final String DB_NAME = "weather_db";

    public static AppDatabase getInstance(Context context, AppExecutors executors) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return sInstance;
    }

    public abstract NowWeatherDao nowWeatherDao();
}

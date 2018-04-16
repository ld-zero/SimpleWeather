package ldzero.ai.simpleweather;

import android.app.Application;

import ldzero.ai.simpleweather.constant.ApiConstant;
import ldzero.ai.simpleweather.db.AppDatabase;
import ldzero.ai.simpleweather.http.ApiAgent;
import ldzero.ai.simpleweather.repository.DataRepository;
import ldzero.ai.simpleweather.utils.AppExecutors;

/**
 * application subclass of simple weather app
 * Created on 2018/3/25.
 *
 * @author ldzero
 */

public class WeatherApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        setupSeniverseApiVar();
        initHttpApi();
    }

    /**
     * load seniverse api key and seniverse use id from build config
     *
     */
    private void setupSeniverseApiVar() {
        ApiConstant.SENIVERSE_API_KEY = BuildConfig.SENIVERSE_API_KEY;
        ApiConstant.SENIVERSE_USER_ID = BuildConfig.SENIVERSE_USER_ID;
    }

    /**
     * create api retrofit
     *
     */
    private void initHttpApi() {
        ApiAgent.getInstance().init(-1);
    }

    /**
     * get reference of app database
     *
     * @return reference of app database
     */
    private AppDatabase getDatabase() {
        return AppDatabase.getInstance(this, new AppExecutors());
    }

    /**
     * get reference of data repository
     *
     * @return reference of data repository
     */
    public DataRepository getDataRepository() {
        return DataRepository.getInstance(getDatabase());
    }
}

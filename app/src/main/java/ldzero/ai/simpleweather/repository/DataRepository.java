package ldzero.ai.simpleweather.repository;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ldzero.ai.simpleweather.db.AppDatabase;
import ldzero.ai.simpleweather.db.entity.NowWeatherEntity;
import ldzero.ai.simpleweather.http.ApiAgent;
import ldzero.ai.simpleweather.http.model.response.NowWeatherResponse;
import ldzero.ai.simpleweather.model.NowWeather;
import ldzero.ai.simpleweather.utils.RxUtils;

/**
 * weather data repository
 * Created on 2018/4/16.
 *
 * @author ldzero
 */

public class DataRepository {

    private static DataRepository sInstance;

    private AppDatabase mDatabase;

    private MediatorLiveData<NowWeather> mObservableNowWeather;

    /* if the last update time of data stored in database greater than this value,
     * fetch new data from network and store it in database */
    private long mFetchNewDataInterval = 30 * 60 * 1000;

    public static DataRepository getInstance(AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    private DataRepository(AppDatabase database) {
        mDatabase = database;

        // setup now weather observable
        mObservableNowWeather = new MediatorLiveData<>();
        mObservableNowWeather.addSource(database.nowWeatherDao().loadNowWeather(), nowWeatherEntities -> {
            if (nowWeatherEntities == null || nowWeatherEntities.size() == 0) {
                fetchNowWeatherFromNetwork();
                mObservableNowWeather.postValue(null);
            } else {
                if (nowWeatherEntities.get(0).getLastUpdateDbTime() < 0 ||
                        (System.currentTimeMillis() - nowWeatherEntities.get(0).getLastUpdateDbTime()) > mFetchNewDataInterval) {
                    fetchNowWeatherFromNetwork();
                }
                mObservableNowWeather.postValue(nowWeatherEntities.get(0));
            }
        });
    }

    /**
     * fetch real-time weather data via seniverse api
     *
     */
    @SuppressLint("CheckResult")
    private void fetchNowWeatherFromNetwork() {
        ApiAgent.getInstance().getSeniverseApi()
                .getNowWeather("ip", "zh-Hans", "c")
                .compose(RxUtils.io2main())
                .subscribe(new Observer<NowWeatherResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(NowWeatherResponse nowWeatherResponse) {
                        // get response data holder reference
                        NowWeatherResponse.NowWeatherResponseMember responseMember = nowWeatherResponse.mResults.get(0);
                        // setup real-time weather data entity
                        NowWeatherEntity nowWeatherEntity = new NowWeatherEntity();
                        nowWeatherEntity.setId(0);
                        nowWeatherEntity.setLocation(responseMember.mLocation.mName);
                        nowWeatherEntity.setTemperature(Integer.parseInt(responseMember.mWeather.mTemperature));
                        nowWeatherEntity.setUnit("c");
                        nowWeatherEntity.setWeatherCode(Integer.parseInt(responseMember.mWeather.mWeatherCode));
                        nowWeatherEntity.setWeatherText(responseMember.mWeather.mWeatherText);
                        nowWeatherEntity.setLastUpdateDbTime(System.currentTimeMillis());
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault());
                        try {
                            nowWeatherEntity.setLastUpdateTime(format.parse(responseMember.mLastUpdate).getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                            nowWeatherEntity.setLastUpdateTime(-1);
                        }
                        // update database
                        mDatabase.nowWeatherDao().insertNowWeather(nowWeatherEntity);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * load real-time weather data from database or network
     *
     * @return weather data
     */
    public LiveData<NowWeather> loadNowWeather() {
        return mObservableNowWeather;
    }
}

package ldzero.ai.simpleweather.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ldzero.ai.simpleweather.WeatherApp;
import ldzero.ai.simpleweather.model.NowWeather;

/**
 * Real-time weather data view model
 * Created on 2018/4/17.
 *
 * @author ldzero
 */
public class NowWeatherViewModel extends AndroidViewModel {

    private MediatorLiveData<NowWeather> mObservableNowWeather;

    public NowWeatherViewModel(@NonNull Application application) {
        super(application);

        // setup now weather observable
        mObservableNowWeather = new MediatorLiveData<>();
        mObservableNowWeather.setValue(null);
        LiveData<NowWeather> nowWeather = ((WeatherApp) application).getDataRepository().loadNowWeather();
        mObservableNowWeather.addSource(nowWeather, mObservableNowWeather::setValue);
    }

    /**
     * getter of real-time weather data observable
     *
     * @return real-time weather data observable
     */
    public MediatorLiveData<NowWeather> getObservableNowWeather() {
        return mObservableNowWeather;
    }
}

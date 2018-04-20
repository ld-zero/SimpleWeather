package ldzero.ai.simpleweather.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ldzero.ai.simpleweather.R;
import ldzero.ai.simpleweather.databinding.WeatherBinding;
import ldzero.ai.simpleweather.helper.WeatherEffectHelper;
import ldzero.ai.simpleweather.model.NowWeather;
import ldzero.ai.simpleweather.viewmodel.NowWeatherViewModel;

/**
 * Weather data fragment
 * Created on 2018/4/17.
 *
 * @author ldzero
 */
public class WeatherFragment extends Fragment {

    public static final String TAG = "WeatherFragment";

    private WeatherBinding mBinding;

    public static WeatherFragment newInstance() {

        Bundle args = new Bundle();

        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false);
        mBinding.setIsDarkBg(false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // observe weather data
        NowWeatherViewModel viewModel = ViewModelProviders.of(this).get(NowWeatherViewModel.class);
        subscribeUi(viewModel);
    }

    /**
     * observe weather data
     *
     * @param viewModel weather data view model
     */
    private void subscribeUi(NowWeatherViewModel viewModel) {
        viewModel.getObservableNowWeather().observe(this, nowWeather -> {
            if (nowWeather != null) {
                updateBindingVarFromData(nowWeather);
            }
        });
    }

    /**
     * set data to binding variable
     *
     * @param nowWeather data holder
     */
    private void updateBindingVarFromData(NowWeather nowWeather) {
        // modify text color according to weather
        mBinding.setIsDarkBg(WeatherEffectHelper.isWeatherEffectBgDark(nowWeather));
        mBinding.setTemperature(getString(R.string.temp_data, nowWeather.getTemperature()));
        mBinding.setWeatherText(nowWeather.getWeatherText());
        SimpleDateFormat format = new SimpleDateFormat(getString(R.string.update_time, "yyyy.MM.dd HH:mm"), Locale.getDefault());
        Date date = new Date(nowWeather.getLastUpdateTime());
        mBinding.setUpdateTime(format.format(date));

        FragmentActivity activity = getActivity();
        if (activity != null && activity instanceof MainActivity) {
            activity.setTitle(nowWeather.getLocation());
            // modify toolar text color according to the weather
            activity.setTitleColor(WeatherEffectHelper.getTextColorIdAccordingWeather(nowWeather));
        }

        showWeatherEffectFragment(nowWeather);
    }

    /**
     * show the corresponding weather effect fragment
     *
     * @param nowWeather weather data
     */
    private void showWeatherEffectFragment(NowWeather nowWeather) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.weather_view_container,
                        WeatherEffectHelper.getWeatherEffectFragment(nowWeather))
                .commit();
    }
}

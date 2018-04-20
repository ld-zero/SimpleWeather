package ldzero.ai.simpleweather.ui.weather_effect_fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ldzero.ai.simpleweather.R;
import ldzero.ai.simpleweather.databinding.UnknownWeatherBinding;

/**
 * a fragment containing the unknown weather effect view
 * Created on 2018/4/18.
 *
 * @author ldzero
 */
public class UnknownWeatherFragment extends Fragment {

    public static UnknownWeatherFragment newInstance() {

        Bundle args = new Bundle();

        UnknownWeatherFragment fragment = new UnknownWeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UnknownWeatherBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_unknown_weather, container, false);
        return binding.getRoot();
    }
}

package ldzero.ai.simpleweather.ui.weather_effect_fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ldzero.ai.simpleweather.R;
import ldzero.ai.simpleweather.databinding.SunnyBinding;

/**
 * a fragment containing the sunny weather effect view
 * Created on 2018/4/21.
 *
 * @author ldzero
 */
public class SunnyFragment extends Fragment {

    private static final String EXTRA_IS_DAYTIME = "EXTRA_IS_DAYTIME";

    private boolean mIsDaytime = true;

    private SunnyBinding mBinding;

    /**
     * get sunny fragment instance
     *
     * @param isDay is it daytime
     * @return SunnyFragment Instance
     */
    public static SunnyFragment newInstance(boolean isDay) {

        Bundle args = new Bundle();
        args.putBoolean(EXTRA_IS_DAYTIME, isDay);
        SunnyFragment fragment = new SunnyFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsDaytime = getArguments().getBoolean(EXTRA_IS_DAYTIME);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sunny, container, false);
        mBinding.setIsDaytime(mIsDaytime);
        return mBinding.getRoot();
    }
}

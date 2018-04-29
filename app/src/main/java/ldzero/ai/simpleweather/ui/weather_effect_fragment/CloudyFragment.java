package ldzero.ai.simpleweather.ui.weather_effect_fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ldzero.ai.simpleweather.R;
import ldzero.ai.simpleweather.databinding.CloudyBinding;

/**
 * a fragment containing the cloudy weather effect view
 * Created on 2018/4/28.
 *
 * @author ldzero
 */
public class CloudyFragment extends Fragment {

    public static final int CLOUDY = 0;
    public static final int PARTLY_CLOUDY = 1;
    public static final int MOSTLY_CLOUDY = 2;

    @IntDef({CLOUDY, PARTLY_CLOUDY, MOSTLY_CLOUDY})
    public @interface CLOUDY_LEVEL {

    }

    private static final String EXTRA_IS_DAYTIME = "EXTRA_IS_DAYTIME";

    private static final String EXTRA_CLOUDY_LEVEL = "EXTRA_CLOUDY_LEVEL";

    private boolean mIsDaytime;

    @CLOUDY_LEVEL
    private int mCloudLevel = CLOUDY;

    private CloudyBinding mBinding;

    public static CloudyFragment newInstance(boolean isDaytime, @CLOUDY_LEVEL int cloudyLevel) {
        Bundle args = new Bundle();
        args.putBoolean(EXTRA_IS_DAYTIME, isDaytime);
        args.putInt(EXTRA_CLOUDY_LEVEL, cloudyLevel);
        CloudyFragment fragment = new CloudyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsDaytime = getArguments().getBoolean(EXTRA_IS_DAYTIME);
        mCloudLevel = getArguments().getInt(EXTRA_CLOUDY_LEVEL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_cloudy, container, false);
        setupView();
        return mBinding.getRoot();
    }

    private void setupView() {
        mBinding.setIsDaytime(mIsDaytime);
        switch (mCloudLevel) {
            case CLOUDY:
                mBinding.setDisplaySun(false);
                mBinding.setDisplayMoon(false);
                mBinding.setCloudCnt(15);
                break;
            case PARTLY_CLOUDY:
                mBinding.setDisplaySun(true);
                mBinding.setDisplayMoon(true);
                mBinding.setCloudCnt(5);
                break;
            case MOSTLY_CLOUDY:
                mBinding.setDisplaySun(true);
                mBinding.setDisplayMoon(true);
                mBinding.setCloudCnt(10);
                break;
        }
    }
}

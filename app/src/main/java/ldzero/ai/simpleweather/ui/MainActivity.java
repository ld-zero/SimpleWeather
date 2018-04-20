package ldzero.ai.simpleweather.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

import ldzero.ai.simpleweather.R;
import ldzero.ai.simpleweather.base.BaseActivity;
import ldzero.ai.simpleweather.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {

    /* activity binding */
    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initView();
        if (savedInstanceState == null) {
            showWeather();
        }
    }

    /**
     * setup view binding
     *
     */
    private void initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    /**
     * setup view and binding data
     *
     */
    private void initView() {
        // setup toolbar
        setSupportActionBar(mBinding.toolbarLayout.toolbar);

        // setup variable
        mBinding.setToolbarTitle(getString(R.string.app_name));
        mBinding.setToolbarTitleColor(ContextCompat.getColor(this, R.color.textBlack));
    }

    /**
     * show weather fragment
     *
     */
    private void showWeather() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.weather_container, WeatherFragment.newInstance(), WeatherFragment.TAG)
                .commit();
    }

    /**
     * expose this method to the fragment to modify toolbar title
     *
     * @param title toolbar title
     */
    public void setTitle(String title) {
        mBinding.setToolbarTitle(title);
    }

    /**
     * expose this method to the fragment to modify toolbar title color
     *
     * @param colorResId color resource id
     */
    public void setTitleColor(@ColorRes int colorResId) {
        if (colorResId != 0) {
            mBinding.setToolbarTitleColor(ContextCompat.getColor(this, colorResId));
        }
    }
}

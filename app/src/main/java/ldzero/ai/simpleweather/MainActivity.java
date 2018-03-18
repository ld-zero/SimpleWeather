package ldzero.ai.simpleweather;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ldzero.ai.simpleweather.base.BaseActivity;
import ldzero.ai.simpleweather.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {

    /* toolbar title */
    public String mTitle = "";

    /* activity binding */
    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initView();
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
        mTitle = getString(R.string.app_name);
        mBinding.setToolbarTitle(mTitle);
    }

    @Override
    protected int getMenuId() {
        return R.menu.menu_main;
    }

    @Override
    protected boolean optionsItemSel(int itemId) {
        if (itemId == R.id.action_settings) {
            // TODO: open setting activity
            return true;
        } else {
            return false;
        }
    }
}

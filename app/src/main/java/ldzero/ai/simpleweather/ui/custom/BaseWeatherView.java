package ldzero.ai.simpleweather.ui.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import ldzero.ai.simpleweather.utils.AppUtils;
import ldzero.ai.simpleweather.utils.ViewUtils;

/**
 * base weather effect view
 * Created on 2018/4/18.
 *
 * @author ldzero
 */
public abstract class BaseWeatherView extends View {

    public BaseWeatherView(Context context) {
        super(context);
    }

    public BaseWeatherView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseWeatherView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = ViewUtils.handleMeasure(widthMeasureSpec, AppUtils.getScreenWidth(getContext()));
        int height = ViewUtils.handleMeasure(heightMeasureSpec, AppUtils.getScreenHeight(getContext()));
        setMeasuredDimension(width, height);
    }
}

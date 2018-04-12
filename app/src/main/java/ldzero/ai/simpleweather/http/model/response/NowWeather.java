package ldzero.ai.simpleweather.http.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Current city weather conditions bean.
 * Created on 2018/4/11.
 *
 * @author ldzero
 */

public class NowWeather {

    @SerializedName("text")
    public String mWeatherText;

    @SerializedName("code")
    public String mWeatherCode;

    @SerializedName("temperature")
    public String mTemperature;

}

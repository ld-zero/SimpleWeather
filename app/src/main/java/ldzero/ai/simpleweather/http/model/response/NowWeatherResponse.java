package ldzero.ai.simpleweather.http.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Now weather interface response.
 * Created on 2018/4/11.
 *
 * @author ldzero
 */

public class NowWeatherResponse {

    @SerializedName("results")
    List<NowWeatherResponseMember> mResults;

    public class NowWeatherResponseMember {

        @SerializedName("location")
        public WeatherLocation mLocation;

        @SerializedName("now")
        public NowWeather mWeather;

        @SerializedName("last_update")
        public String mLastUpdate;
    }

}

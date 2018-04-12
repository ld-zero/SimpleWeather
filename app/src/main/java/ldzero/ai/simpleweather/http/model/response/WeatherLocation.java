package ldzero.ai.simpleweather.http.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Weather location bean in weather response
 * Created on 2018/4/11.
 *
 * @author ldzero
 */

public class WeatherLocation {

    @SerializedName("id")
    public String mId;

    @SerializedName("name")
    public String mName;

    @SerializedName("country")
    public String mCountry;

    @SerializedName("timezone")
    public String mTimezone;

    @SerializedName("timezone_offset")
    public String mTimezoneOffset;

}

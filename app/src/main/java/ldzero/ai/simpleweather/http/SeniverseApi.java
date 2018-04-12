package ldzero.ai.simpleweather.http;

import io.reactivex.Observable;
import ldzero.ai.simpleweather.http.interceptor.SignInterceptor;
import ldzero.ai.simpleweather.http.model.response.NowWeatherResponse;
import ldzero.ai.simpleweather.utils.AppUtils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * seniverse api
 * Created on 2018/3/19.
 *
 * @author ldzero
 */

public interface SeniverseApi {

    class Builder {

        /* seniverse api base url */
        static final String BASE_URL = "https://api.seniverse.com";

        /**
         * build retrofit
         *
         * @param signExpirationTime sign expiration time, the unit is second,
         *                           if signExpirationTime is less than 0,
         *                           the sign interceptor would use default sign expiration time.
         * @return SeniverseApi api retrofit
         */
        public static Retrofit build(int signExpirationTime) {
            // setup okhttp client builder
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                    .addInterceptor(new SignInterceptor(-1))
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(AppUtils.isDebug() ?
                            HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE));

            // setup retrofit builder
            Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(clientBuilder.build());

            return retrofitBuilder.build();
        }

    }

    /**
     * get live weather for a given city
     *
     * @param location the location of querying
     *                 the following format parameter is available:
     *                 location id
     *                 location name
     *                 province name city name combination
     *                 pinyin of the city name
     *                 latitude and longitude
     *                 ip address
     *                 "ip" (when passing string "ip", the ip address will be automatically identify)
     * @param language language, please refer to the seniverse api document to check the specific support language range
     * @param unit unit, passing "c" or "f"
     *             when passing "c", the temperature unit is Celsius, the wind speed unit is km/h, the visibility unit is km, the pressure unit is mb
     *             when passing "f", the temperature unit is Fahrenheit, the wind speed unit is mph, the visibility unit is mile, the pressure unit is inch
     * @return response observable
     */
    @GET("v3/weather/now.json")
    Observable<NowWeatherResponse> getNowWeather(@Query("location") String location, @Query("language") String language, @Query("unit") String unit);
}

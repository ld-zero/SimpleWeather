package ldzero.ai.simpleweather.http.interceptor;

import java.io.IOException;

import ldzero.ai.simpleweather.constant.ApiConstant;
import ldzero.ai.simpleweather.utils.SeniverseUtils;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * seniverse api sign interceptor
 * Created on 2018/3/25.
 *
 * @author ldzero
 */

public class SignInterceptor implements Interceptor {

    /* signature expiration time, the unit is second, default is 30 minutes */
    private int mSignExpirationTime = 1800;

    /**
     * if the pass argument is greater than 0, set mSignExpirationTime variable to this value.
     *
     * @param signExpirationTime sign expiration time, the unit is second
     */
    public SignInterceptor(int signExpirationTime) {
        if (signExpirationTime > 0) {
            mSignExpirationTime = signExpirationTime;
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HttpUrl originalUrl = originalRequest.url();

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String sign = SeniverseUtils.generateSignature(ApiConstant.SENIVERSE_API_KEY,
                timestamp, String.valueOf(mSignExpirationTime), ApiConstant.SENIVERSE_USER_ID);

        HttpUrl.Builder newUrlBuilder = originalUrl.newBuilder()
                .addQueryParameter("ts", timestamp)
                .addQueryParameter("ttl", String.valueOf(mSignExpirationTime))
                .addQueryParameter("uid", ApiConstant.SENIVERSE_USER_ID)
                .addEncodedQueryParameter("sig", sign);
        // modify the order of query
        for (String queryName : originalUrl.queryParameterNames()) {
            newUrlBuilder.removeAllQueryParameters(queryName);
            newUrlBuilder.addQueryParameter(queryName, originalUrl.queryParameterValues(queryName).get(0));
        }

        Request newRequest = originalRequest.newBuilder()
                .url(newUrlBuilder.build())
                .build();

        return chain.proceed(newRequest);
    }
}

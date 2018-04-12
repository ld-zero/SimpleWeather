package ldzero.ai.simpleweather.utils;

import android.text.TextUtils;
import android.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * seniverse api utils
 * Created on 2018/3/25.
 *
 * @author ldzero
 */

public class SeniverseUtils {

    /**
     * generate seniverse api signature
     *
     * @param apiKey seniverse api key
     * @param timestamp curretn timestamp, unit is second
     * @param signExpirationTime signature expiration time, unit is second
     * @param uid seniverse use id
     * @return seniverse api signature
     */
    public static String generateSignature(String apiKey, String timestamp, String signExpirationTime, String uid) {
        try {
            // construct sign params
            StringBuilder paramsBuilder = new StringBuilder();
            if (!TextUtils.isEmpty(timestamp)) {
                paramsBuilder.append("ts=");
                paramsBuilder.append(timestamp);
                paramsBuilder.append("&");
            }
            if (!TextUtils.isEmpty(signExpirationTime)) {
                paramsBuilder.append("ttl=");
                paramsBuilder.append(signExpirationTime);
                paramsBuilder.append("&");
            }
            if (!TextUtils.isEmpty(uid)) {
                paramsBuilder.append("uid=");
                paramsBuilder.append(uid);
                paramsBuilder.append("&");
            }
            if (paramsBuilder.length() == 0) {
                // if params builder has no content, return a string of length 0
                return "";
            } else {
                // delete the last '&'
                paramsBuilder.deleteCharAt(paramsBuilder.length() - 1);
            }
            String params = paramsBuilder.toString();

            // use HMAC-SHA1 method to encrypt the parameter string with api key
            SecretKeySpec signKey = new SecretKeySpec(apiKey.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signKey);
            byte[] digest = mac.doFinal(params.getBytes());
            // base64 encoding
            return Base64.encodeToString(digest, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            // error occur, return a string of length 0
            return "";
        }
    }

}

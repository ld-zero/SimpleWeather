package ldzero.ai.simpleweather.http;

/**
 * Http API Agent
 * Created on 2018/4/11.
 *
 * @author ldzero
 */

public class ApiAgent {
    private static final ApiAgent ourInstance = new ApiAgent();

    /* Seniverse api */
    private SeniverseApi mSeniverseApi;

    public static ApiAgent getInstance() {
        return ourInstance;
    }

    private ApiAgent() {
    }

    /**
     * init retrofit interface
     *
     * @param seniverseSignExpirationTime seniverse sign expiration time, unit is second,
     *                                    the passing value will be available only when the value greater than 0
     */
    public void init(int seniverseSignExpirationTime) {
        mSeniverseApi = SeniverseApi.Builder.build(seniverseSignExpirationTime).create(SeniverseApi.class);
    }

    /**
     * get senverse api
     *
     * @return senverse api
     */
    public SeniverseApi getSeniverseApi() {
        return mSeniverseApi;
    }
}

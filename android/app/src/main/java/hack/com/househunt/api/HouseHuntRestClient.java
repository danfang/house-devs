package hack.com.househunt.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.entity.StringEntity;

/**
 * HouseHuntRestClient used to communicate with the API.
 */
public class HouseHuntRestClient {

    private static final String BASE_URL = "http://54.148.214.208:3000/api/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params,
            AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, StringEntity params,
            AsyncHttpResponseHandler responseHandler) {
        client.post(null, getAbsoluteUrl(url), params, "application/json", responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}

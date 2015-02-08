package hack.com.househunt.internal;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import hack.com.househunt.R;
import hack.com.househunt.api.HouseHuntRestClient;
import hack.com.househunt.fragments.RecommendFragment;

/**
 * Static helper methods used to reduce redundancy in the application.
 */
public class Util {

    private Util() {

    }

    public static void switchFragments(FragmentActivity a, Fragment newFrag, String tag, boolean replace) {
        FragmentTransaction ft = a.getSupportFragmentManager().beginTransaction().addToBackStack(tag);
        ft.replace(R.id.container, newFrag, tag).commit();
        Log.d(tag, "Switched to " + tag + " fragment");
    }

    public static void getRecommendation(final FragmentActivity a, boolean switchToRecommend) {
        String userId = UserSession.getInstance().getUserId();
        HouseHuntRestClient.get("/rec/" + userId, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("Util", "Get recommendation succeeded! Status code " + statusCode + "!");
                UserSession.getInstance().setLiveable(response);
                switchFragments(a, new RecommendFragment(),
                        RecommendFragment.TAG, false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("Util", "Get recommendation failed! " + statusCode + " status code thrown. " +
                        errorResponse);
                // for now let's populate a liveable with a hardcoded string
                JSONObject data = new JSONObject();
                try {
                    data.put("latitude", 46.3);
                    data.put("longitude", 48.2);
                    data.put("address", "23315 NE 15TH ST");
                    data.put("zipcode", "98074");
                    data.put("beds", 4);
                    data.put("price", 12000);
                    data.put("type", "house");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                UserSession.getInstance().setLiveable(data);
                switchFragments(a, new RecommendFragment(), RecommendFragment.TAG, false);
            }
        });
    }

}

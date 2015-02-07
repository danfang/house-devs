package hack.com.househunt.internal;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Used to store data about a potential place to live.
 * Includes education rating, estimated price, the lat/long
 * of the property as well.
 */
public class Liveable {

    private static final String TAG = Liveable.class.getSimpleName();

    private double latitude;
    private double longtitude;
    private double estimatedPrice;
    private double educationRating;

    public Liveable(JSONObject data) {
        try {
            if (data != null) {
                latitude = data.getDouble("latitude");
                longtitude = data.getDouble("longitude");
                estimatedPrice = data.getDouble("estimated_price");
                educationRating = data.getDouble("education_rating");
            } else {
                Log.e(TAG, "Data passed into creating the Liveable is null!");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public double getEstimatedPrice() {
        return estimatedPrice;
    }

    public double getEducationRating() {
        return educationRating;
    }
}

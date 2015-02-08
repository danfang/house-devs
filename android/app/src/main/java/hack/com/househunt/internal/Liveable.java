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
    private String address;
    private String type;
    private int beds;
    private int zipCode;
    private int recId;


    public Liveable(JSONObject data) {
        try {
            if (data != null) {
                latitude = data.getDouble("latitude");
                longtitude = data.getDouble("longitude");
                estimatedPrice = data.getDouble("price");
                educationRating = data.getDouble("education_rating");
                address = data.getString("address");
                type = data.getString("type");
                recId = data.getInt("rec_id");
                zipCode = data.getInt("zipcode");
                beds = data.getInt("beds");
                type = data.getString("type");
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

    public int getNumberOfBeds() {
        return beds;
    }

    public int getZipCode() {
        return zipCode;
    }

    public int getRecId() {
        return recId;
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

}

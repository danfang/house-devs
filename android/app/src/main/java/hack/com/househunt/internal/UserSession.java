package hack.com.househunt.internal;

import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Singleton Instance of a UserSession. Keeps track of the User
 * and the fields associated with the User.
 */
public class UserSession {

    private static UserSession instance;
    private JSONObject facebookData;
    private String userId;
    private List<Liveable> savedLiveables; // used to be displayed on the grid view
    private Liveable recommendedLiveable;

    private UserSession() {

    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUserId(String facebookId) {
        userId = facebookId;
    }

    public String getUserId() {
        return userId;
    }

    public void setLocation(Location loc) {
        try {
            facebookData.put("latitude", loc.getLatitude());
            facebookData.put("longtitude", loc.getLongitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the data from the Facebook API call.
     * @param fbData The data received from the /me Facebook API Call.
     */
    public void setFacebookData(JSONObject fbData) {
        facebookData = fbData;
        try {
            userId = fbData.getString("id");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the UserSession from the HouseHunters API call.
     * @param data The data received from the HouseHunters GET Recommendation API Call.
     */
    public void setLiveable(JSONObject data) {
        this.recommendedLiveable = new Liveable(data);
    }

    /**
     * Gets the total data to be sent to the API.
     * @return A JSONObject representing all the data to create a new user.
     */
    public JSONObject getTotalData() {
        return facebookData;
    }
}

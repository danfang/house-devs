package hack.com.househunt.internal;

import android.location.Location;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton Instance of a UserSession. Keeps track of the User
 * and the fields associated with the User.
 */
public class UserSession {

    public static final String TAG = UserSession.class.getSimpleName();

    private static UserSession instance;
    private JSONObject facebookData;
    private JSONObject userData;
    private String userId;
    private List<Liveable> savedLiveables; // used to be displayed on the grid view
    private List<Liveable> recommendedLiveable;

    private UserSession() {
        userData = new JSONObject();
        recommendedLiveable = new ArrayList<>();
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setField(String key, Object value) {
        try {
            Log.d(TAG, "Successfully set " + key + " to the value " + value);
            userData.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        recommendedLiveable.add(new Liveable(data));
    }

    public List<Liveable> getRecommendedLiveables() {
        for (int i = 0; i < 10; i++) {
            recommendedLiveable.add(recommendedLiveable.get(0));
        }
        return recommendedLiveable;
    }

    /**
     * Gets the total data to be sent to the API.
     * @return A JSONObject representing all the data to create a new user.
     */
    public JSONObject getTotalData() {
        // modify the total data
        try {
            String ageRange = (String) userData.remove("age_range");
            switch (ageRange) {
                case "Under 25": {
                    userData.put("age_range", 0);
                    break;
                }
                case "25 - 55": {
                    userData.put("age_range", 1);
                    break;
                }
                case "55+": {
                    userData.put("age_range", 2);
                    break;
                }
            }
            String firstHome = (String) userData.remove("first_home");
            if (firstHome != null) {
                userData.put("first_home", firstHome.equals("Yes"));
            }
            String educationWeight = (String) userData.remove("education_weight");
            if (educationWeight != null) {
                userData.put("education_weight", Integer.parseInt(educationWeight));
            } else {
                userData.put("education_weight", 0);
            }
            String amenitiesWeight = (String) userData.remove("amenities_weight");
            if (amenitiesWeight != null) {
                userData.put("amenities_weight", Integer.parseInt(amenitiesWeight));
            } else {
                userData.put("amenities_weight", 0);
            }
            String voucher = (String) userData.remove("voucher");
            if (voucher != null) {
                userData.put("voucher", voucher.equals("Yes"));
            } else {
                userData.put("voucher", false);
            }
            String subsidies = (String) userData.remove("subsidies");
            if (subsidies != null ) {
                userData.put("subsidy", subsidies.equals("Yes"));
            } else {
                userData.put("subsidy", false);
            }
            String buyOrRent = (String) userData.remove("prop_type");
            userData.put("prop_type", buyOrRent.toLowerCase());
            userData.put("price_weight", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "modified data " + userData.toString());
        return userData;
    }

    public int getIncomeFromIdentifier() {
        int estimatedIncome = -1;
        try {
          String income = userData.getString("category");
          Log.d(TAG, "Success! Income is not null");
          switch (income) {
              case Constant.SINGLE_PROFESSIONAL_STR: {
                  return Constant.SINGLE_PROFESSIONAL;
              }
              case Constant.WORKING_INDIVIDUAL_STR: {
                  return Constant.WORKING_INDIVIDUAL;
              }
              case Constant.SINGLE_PARENT_FAMILY_STR: {
                  return Constant.SINGLE_PARENT_FAMILY;
              }
              case Constant.MODERATE_INCOME_FAMILY_STR: {
                  return Constant.MODERATE_INCOME_FAMILY;
              }
              case Constant.DUAL_PROFESSIONAL_FAMILY_STR: {
                  return Constant.DUAL_PROFESSIONAL_FAMILY;
              }
          }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return estimatedIncome;
    }
}

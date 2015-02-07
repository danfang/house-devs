package hack.com.househunt.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hack.com.househunt.R;
import hack.com.househunt.api.HouseHuntRestClient;
import hack.com.househunt.internal.UserSession;
import hack.com.househunt.internal.Util;

/**
 * Login Fragment used to prompt the user to use their Facebook for ease of access.
 */
public class LoginFragment extends Fragment {

    public static final String TAG = LoginFragment.class.getSimpleName();
    private UiLifecycleHelper mUiHelper;

    @InjectView(R.id.auth_button) LoginButton mLogin;
    private Session.StatusCallback mStatusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState sessionState, Exception e) {
            onSessionStateChange(session, sessionState, e);
        }
    };

//    @OnClick(R.id.auth_button)
//    public void submit() {
//        Session s = Session.getActiveSession();
//        if (!s.isClosed() && !s.isOpened()) {
//            s.openForRead(new Session.OpenRequest(this)
//                    .setPermissions(Arrays.asList("public_profile"))
//                    .setCallback(mStatusCallback));
//        } else { // then we should go back
//            Session.openActiveSession(getActivity(), this, true, mStatusCallback);
//        }
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_frag, container, false);
        ButterKnife.inject(this, v);
        mLogin.setFragment(this);
        mUiHelper = new UiLifecycleHelper(getActivity(), mStatusCallback);
        mUiHelper.onCreate(savedInstanceState);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mUiHelper.onResume();
    }

    /**
     * Changes the UI when an interaction with the Session object occurs with the user.
     * @param session       The current active Session.
     * @param sessionState  The current state of the active Session.
     * @param e             An Exception if there is one.
     */
    private void onSessionStateChange(Session session, SessionState sessionState, Exception e) {
        if (sessionState == SessionState.OPENED) {
            Log.d(TAG, "Successful login!");
            new Request(session, "/me", null, HttpMethod.GET, new Request.Callback() {
                @Override
                public void onCompleted(Response response) {
                    JSONObject obj = response.getGraphObject().getInnerJSONObject();
                    Log.d(TAG, "Got back " + obj + " from Facebook API.");
                    UserSession.getInstance().setFacebookData(obj);
                    getUserData();
                }
            }).executeAsync();
        } else if (e != null) { // handle exception

        }
    }

    private void getUserData() {
        String userId = UserSession.getInstance().getUserId();
        HouseHuntRestClient.get("user/" + userId, null, new JsonHttpResponseHandler(){
            // user exists in the database
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) { // success!
                    UserSession.getInstance().setLiveable(response);
                    Util.switchFragments(getActivity(), new RecommendFragment(),
                            RecommendFragment.TAG, true);
                }
            }

            // status code == 404, user does not exist yet
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                if (statusCode == 404) { // no user exists yet, prompt the questionaire.
                    Util.switchFragments(getActivity(), new QuestionaireFragment(),
                            QuestionaireFragment.TAG, true);
//                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mUiHelper.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroyView();
        mUiHelper.onDestroy();
    }
}

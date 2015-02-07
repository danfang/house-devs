package hack.com.househunt.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.widget.LoginButton;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hack.com.househunt.R;

/**
 * Login Fragment used to prompt the user to use their Facebook.
 */
public class HomeFragment extends Fragment {

    public static final String TAG = HomeFragment.class.getSimpleName();

    @InjectView(R.id.latitude) TextView mLat;
    @InjectView(R.id.longtitude) TextView mLong;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_frag, container, false);
        ButterKnife.inject(this, v);
        return v;
    }
}

package hack.com.househunt.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import butterknife.ButterKnife;
import hack.com.househunt.R;

/**
 * MainFragment used after the user authenticates with Facebook.
 */
public class QuestionaireFragment extends Fragment {

    public static final String TAG = QuestionaireFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.questionaire_frag, container, false);
        ButterKnife.inject(this, v);
        return v;
    }



}

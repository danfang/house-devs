package hack.com.househunt.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.andtinder.view.CardContainer;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hack.com.househunt.R;
import hack.com.househunt.internal.CardDataAdapter;
import hack.com.househunt.internal.Liveable;
import hack.com.househunt.internal.UserSession;

/**
 * Recommendation
 */
public class RecommendFragment extends Fragment {

    public static final String TAG = RecommendFragment.class.getSimpleName();

    @InjectView(R.id.card_stack) CardContainer mLiveableCards;
    private CardDataAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recommend_frag, container, false);
        ButterKnife.inject(this, v);
        List<Liveable> liveables = UserSession.getInstance().getRecommendedLiveables();
        mAdapter = new CardDataAdapter(getActivity(), R.layout.liveable_card, liveables);
        mLiveableCards.setAdapter(mAdapter);

        return v;
    }



}
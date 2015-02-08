package hack.com.househunt.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenchao.cardstack.CardStack;

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

    @InjectView(R.id.card_stack) CardStack mLiveableCards;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recommend_frag, container, false);
        ButterKnife.inject(this, v);
        mLiveableCards.setContentResource(R.layout.liveable_card);
        mLiveableCards.setStackMargin(20);
        List<Liveable> liveables = UserSession.getInstance().getRecommendedLiveables();
        CardDataAdapter adapter = new CardDataAdapter(getActivity(), 0, liveables);
        mLiveableCards.setAdapter(adapter);
        mLiveableCards.setListener(new LiveableListener());
        return v;
    }

    public class LiveableListener implements CardStack.CardEventListener {
        @Override
        public boolean swipeEnd(int section, float distance) {
            return distance > 300;
        }

        @Override
        public boolean swipeStart(int i, float v) {
            return false;
        }

        @Override
        public boolean swipeContinue(int i, float v, float v2) {
            return false;
        }

        @Override
        public void discarded(int mIndex, int direction) {

        }

        @Override
        public void topCardTapped() {
            // open up new screen
        }
    }


}

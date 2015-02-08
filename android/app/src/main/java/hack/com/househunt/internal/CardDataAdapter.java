package hack.com.househunt.internal;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;
import hack.com.househunt.R;

/**
 * Card Data Adapter
 */
public class CardDataAdapter extends ArrayAdapter<LiveableCardModel> {

    private List<LiveableCardModel> data;
    @Optional @InjectView(R.id.liveable_image) ImageView housePic;
    @Optional @InjectView(R.id.price) TextView price;

    // Feedback Card
    @Optional @InjectView(R.id.price_high) Button mPriceHigh;
    @Optional @InjectView(R.id.price_low) Button mPriceLow;
    @Optional @InjectView(R.id.accessible_high) Button mAccessibleHigh;
    @Optional @InjectView(R.id.accessible_low) Button mAccessibleLow;
    @Optional @InjectView(R.id.education_low) Button mEducationLow;
    @Optional @InjectView(R.id.transportation_high) Button mTransportationHigh;

    @Optional @OnClick({R.id.price_high, R.id.price_low, R.id.accessible_high, R.id.accessible_low,
            R.id.education_low, R.id.transportation_high})
    public void sendReason(Button btn) {

    }

    public CardDataAdapter(Context context, int layoutResourceId, List<LiveableCardModel> livables) {
        super(context, layoutResourceId, livables);
        data = livables;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Liveable cur = data.get(position);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        if (cur == null) { // then we need to inflate the feedback card
            v = layoutInflater.inflate(R.layout.feedback_card, null);
        } else if (v == null) {
            v = layoutInflater.inflate(R.layout.liveable_card, null);
        }
        ButterKnife.inject(this, v);
        if (cur != null) {
            price.setText("Estimated price: " + cur.getEstimatedPrice());
            housePic.setBackgroundColor(Color.BLACK);
        }
        return v;
    }

    public void addReasonCard() {
        data.add(null); // signifies that it should be specified
    }

}

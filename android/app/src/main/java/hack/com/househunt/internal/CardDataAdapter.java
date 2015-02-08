package hack.com.househunt.internal;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hack.com.househunt.R;

/**
 * Card Data Adapter
 */
public class CardDataAdapter extends ArrayAdapter<Liveable> {

    private List<Liveable> data;
    @InjectView(R.id.liveable_image) ImageView housePic;
    @InjectView(R.id.price) TextView price;


    public CardDataAdapter(Context context, int layoutResourceId, List<Liveable> livables) {
        super(context, layoutResourceId, livables);
        data = livables;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.liveable_card, null);
        }
        ButterKnife.inject(this, v);
        price.setText("Estimated price: " + data.get(position).getEstimatedPrice());
        housePic.setBackgroundColor(Color.BLACK);
        return v;
    }



}

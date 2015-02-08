package hack.com.househunt.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import hack.com.househunt.R;
import hack.com.househunt.api.HouseHuntRestClient;
import hack.com.househunt.internal.DecisionTree;
import hack.com.househunt.internal.UserSession;
import hack.com.househunt.internal.Util;

/**
 * Questionaire given to users if they don't have an account yet.
 */
public class QuestionaireFragment extends Fragment {

    public static final String TAG = QuestionaireFragment.class.getSimpleName();

    @InjectView(R.id.next_button) Button mNextQuestion;
    @InjectView(R.id.question) TextView mQuestion;
    @InjectView(R.id.skip_button) Button mSkip;

    @OnClick(R.id.next_button)
    public void nextQuestion() {
        Object value = "";
        if (mUserInputView instanceof AutoCompleteTextView) {
            value = ((AutoCompleteTextView) mUserInputView).getText().toString();
        } else if (mUserInputView instanceof Spinner) {
            value = ((Spinner) mUserInputView).getItemAtPosition(((Spinner) mUserInputView)
                    .getSelectedItemPosition());
        } else if (mUserInputView instanceof SeekBar) {
            value = (((SeekBar) mUserInputView).getProgress() + 1);
        } else if (mUserInputView instanceof EditText) {
            value = ((EditText) mUserInputView).getText().toString();
        }
        DecisionTree.DecisionNode cur = mDecision.getCurrentNode();
        mDecision.decide(value);
        if (cur.id != null) {
            UserSession.getInstance().setField(cur.id, value);
        }
        if (mDecision.getCurrentNode() != null) {
            Util.switchFragments(getActivity(), new QuestionaireFragment(), QuestionaireFragment.TAG,
                    true);
        } else { // current node is null, so make an async request with all the data.
            JSONObject data = UserSession.getInstance().getTotalData();
            StringEntity entity = null;
            try {
                entity = new StringEntity(data.toString());
                entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String userId = UserSession.getInstance().getUserId();
            HouseHuntRestClient.post("/user/" + userId, entity, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.d(TAG, statusCode + " status code. Success!");
                    Util.getRecommendation(getActivity(), true);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.d(TAG, "It failed! Status code " + statusCode + ". Error response: " +
                            errorResponse);
                }
            });
        }
    }

    @OnClick(R.id.skip_button)
    public void skipQuestion() {
        mDecision.decide("");
        UserSession u = UserSession.getInstance();
        UserSession.getInstance().setField("income", u.getIncomeFromIdentifier());
        Util.switchFragments(getActivity(), new QuestionaireFragment(), QuestionaireFragment.TAG,
                true);
    }

    private static DecisionTree mDecision;
    private View mUserInputView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.questionaire_frag, container, false);
        ButterKnife.inject(this, v);
        if (mDecision == null) {
            mDecision = new DecisionTree();
        }
        createView(v);
        return v;
    }

    // Dynamically create a view from the data in the decision tree.
    private void createView(View v) {
        DecisionTree.DecisionNode cur = mDecision.getCurrentNode();
        mQuestion.setText(cur.question);
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.questionaire_layout);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout
                .LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        if (!checkOptions(cur.option)) { // then it's not a special case, populate a spinner
            Spinner options = new Spinner(getActivity());
            List<String> allOptions = new ArrayList<>();
            for (int i = 0; i < cur.pointers.length; i++) {
                allOptions.add(cur.pointers[i].option);
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.spinner_item, allOptions);
            options.setAdapter(dataAdapter);
            mUserInputView = options;
            layout.addView(mUserInputView, params);
        } else { // special case
            createSpecialView(layout, params, cur.option);
        }
        if (mDecision.getCurrentNode().id != null && mDecision.getCurrentNode().id.equals
                ("income")) {
            mSkip.setVisibility(View.VISIBLE);
        } else {
            mSkip.setVisibility(View.INVISIBLE);
        }
    }

    // Create a View other than a Spinner
    private void createSpecialView(RelativeLayout layout, RelativeLayout.LayoutParams params,
                                   String option) {
        switch (option) {
            case DecisionTree.INCOME_SCALE: {
                SeekBar sb = new SeekBar(getActivity());
                int resourceId = 1000;
                sb.setId(resourceId);
                final TextView v = new TextView(getActivity());
                RelativeLayout.LayoutParams tvParams = new RelativeLayout.LayoutParams
                        (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams
                                .WRAP_CONTENT);
                tvParams.addRule(RelativeLayout.BELOW, sb.getId());
                tvParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                tvParams.setMargins(0, 10, 0, 0);
                v.setTextColor(Color.WHITE);
                v.setTextSize(30);
                v.setText("$" + 70000);
                sb.setMax(250000);
                sb.setProgress(70000);
                sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        v.setText("$" + progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                mUserInputView = sb;
                layout.addView(sb, params);
                layout.addView(v, tvParams);
                break;
            }
            case DecisionTree.NUMBER_PICKER: {
                SeekBar sb = new SeekBar(getActivity());
                int resourceId = 1000;
                sb.setId(resourceId);
                final TextView v = new TextView(getActivity());
                RelativeLayout.LayoutParams tvParams = new RelativeLayout.LayoutParams
                        (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams
                                .WRAP_CONTENT);
                tvParams.addRule(RelativeLayout.BELOW, sb.getId());
                tvParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                tvParams.setMargins(0, 10, 0, 0);
                v.setTextColor(Color.WHITE);
                v.setTextSize(30);
                v.setText(1 + "");
                sb.setMax(9);
                sb.setProgress(2);
                sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        v.setText((progress + 1) + "");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                layout.addView(sb, params);
                layout.addView(v, tvParams);
                mUserInputView = sb;
                break;
            }
            case DecisionTree.TEXT: {
                String[] cities = new String[]{"Seattle", "Everett", "Capital Hill"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.spinner_item, cities);
                AutoCompleteTextView textView = new AutoCompleteTextView(getActivity());
                textView.setAdapter(adapter);
                textView.setTextColor(Color.parseColor("#FFFFFF"));
                mUserInputView = textView;
                break;
            }
            case DecisionTree.YES_NO: {
                String[] options = new String[]{"Yes", "No", "Don't know"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.spinner_item, options);
                Spinner yesNoIdk = new Spinner(getActivity());
                yesNoIdk.setAdapter(adapter);
                mUserInputView = yesNoIdk;
                break;
            }
            case DecisionTree.RENT_OR_BUY: {
                String[] options = new String[]{"Rent", "Buy"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                        R.layout.spinner_item, options);
                Spinner rentOrBuy = new Spinner(getActivity());
                rentOrBuy.setAdapter(adapter);
                mUserInputView = rentOrBuy;
                break;
            }
            case DecisionTree.FIRST_TIME_HOME: {
                String[] options = new String[]{"Yes", "No"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.spinner_item, options);
                Spinner firstTimeHome = new Spinner(getActivity());
                firstTimeHome.setAdapter(adapter);
                mUserInputView = firstTimeHome;
                break;
            }
        }
        Log.d(TAG, "Attempting to create a view for " + option);
        if (!option.equals(DecisionTree.NUMBER_PICKER) && !option.equals(DecisionTree
                .INCOME_SCALE)) {
            layout.addView(mUserInputView, params);
        }
    }



    private boolean checkOptions(String option) {
        if (option != null) {
            return option.equals(DecisionTree.INCOME_SCALE) || option.equals(DecisionTree.NUMBER_PICKER) ||
                    option.equals(DecisionTree.YES_NO) || option.equals(DecisionTree.NUMBER) ||
                    option.equals(DecisionTree.RENT_OR_BUY) || option.equals(DecisionTree.TEXT)
                    || option.equals(DecisionTree.FIRST_TIME_HOME);
        }
        return false;
    }


}

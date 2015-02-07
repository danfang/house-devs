package hack.com.househunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import hack.com.househunt.fragments.LoginFragment;
import hack.com.househunt.internal.Constant;
import hack.com.househunt.internal.TypefaceUtil;
import hack.com.househunt.internal.Util;


public class MainActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TypefaceUtil.overrideFont(getApplicationContext(), "MONOSPACE", Constant.DEFAULT_FONT);
        Util.switchFragments(this, new LoginFragment(), LoginFragment.TAG, true);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    /**
     * Used to get the Fragment currently sitting on the back stack.
     * @return  The Fragment on the back stack.
     */
    private Fragment getFragmentOnBackStack() {
        FragmentManager.BackStackEntry backEntry = getSupportFragmentManager()
                .getBackStackEntryAt(this.getSupportFragmentManager().getBackStackEntryCount() - 1);
        String str = backEntry.getName();
        return getSupportFragmentManager().findFragmentByTag(str);
    }
}

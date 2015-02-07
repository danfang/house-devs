package hack.com.househunt.internal;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import hack.com.househunt.R;

/**
 * Static helper methods used to reduce redundancy in the application.
 */
public class Util {

    private Util() {

    }

    public static void switchFragments(FragmentActivity a, Fragment newFrag, String tag, boolean replace) {
        FragmentTransaction ft = a.getSupportFragmentManager().beginTransaction().addToBackStack(tag);
        ft.replace(R.id.container, newFrag, tag).commit();
        Log.d(tag, "Switched to " + tag + " fragment");
    }

}

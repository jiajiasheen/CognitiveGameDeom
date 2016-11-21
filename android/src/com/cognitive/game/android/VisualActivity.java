package com.cognitive.game.android;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

/**
 * Created by Jiaxing on 20/11/2016.
 */

public class VisualActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visual_activity);

        if(savedInstanceState == null){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            VisualMemFragment vm = new VisualMemFragment();
            fragmentTransaction.add(R.id.visual_activity, vm);
            fragmentTransaction.commit();
        }
    }
}

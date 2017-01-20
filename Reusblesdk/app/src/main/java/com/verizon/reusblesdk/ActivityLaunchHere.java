package com.verizon.reusblesdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by ronak on 1/19/2017.
 */

public class ActivityLaunchHere extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);


    }
}

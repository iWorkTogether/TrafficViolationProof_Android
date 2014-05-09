package com.ln.TrafficViolationProof;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class StartActivity extends Activity {
    private String TAG = StartActivity.class.getSimpleName();
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "traficViolationProof start...");
        setContentView(R.layout.layout_start_activity);

    }
}

package com.ln.ViolationQueries;

import android.app.Activity;
import android.os.Bundle;

public class StartActivity extends Activity {
    private String TAG = StartActivity.class.getSimpleName();
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_start_activity);

    }
}

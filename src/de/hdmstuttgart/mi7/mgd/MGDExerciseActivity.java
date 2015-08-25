package de.hdmstuttgart.mi7.mgd;

import android.app.Activity;
import android.os.Bundle;

public class MGDExerciseActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
        MGDExerciseView view = new MGDExerciseView(this);
        setContentView(view);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

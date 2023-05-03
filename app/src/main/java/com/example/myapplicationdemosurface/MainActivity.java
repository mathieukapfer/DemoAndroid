package com.example.myapplicationdemosurface;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceView;

public class MainActivity extends AppCompatActivity {

	private BouncingBall bouncingBall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inflate xlm file res/layout/activity_main.xml
        setContentView(R.layout.activity_main);

        // get surface previously inflated
        SurfaceView surf = findViewById(R.id.surfaceView);

        // create ball view and attach layout
        bouncingBall = new BouncingBall(this, surf);

    }

	@Override
    protected void onResume() {
        super.onResume();
        bouncingBall.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bouncingBall.pause();
    }

}

package com.example.root.villagedesigner;

import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.root.villagedesigner.views.Palette;

public class VillageActivity extends AppCompatActivity implements View.OnTouchListener {

    Palette P;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        Bundle extras = getIntent().getExtras();
        int drawable_id = Integer.parseInt(extras.getString("drawable_id"));

        P = new Palette(VillageActivity.this, drawable_id);
        this.setContentView(P);
        P.setOnTouchListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        P.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        P.pause();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onTouch(View v, MotionEvent me) {

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        switch(me.getAction()) {
            case MotionEvent.ACTION_DOWN:
                PointF loc = new PointF(me.getX(), me.getY());
                //P.location = loc;
                P.getIsometricTouchLocation(loc);
                break;
            case MotionEvent.ACTION_UP:
                Log.d("touch", "touched");
                //P.placeInVillage();
                break;
            case MotionEvent.ACTION_MOVE:
                loc = new PointF(me.getX(), me.getY());
                //P.location = loc;
                P.getIsometricTouchLocation(loc);
                break;
        }

        P.updateTreeMap();

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        P.saveToDb();
    }
}

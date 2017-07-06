package com.example.root.villagedesigner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.root.villagedesigner.views.SPalette;

import java.util.ArrayList;

public class VillageActivity extends AppCompatActivity implements View.OnTouchListener {

    SPalette P;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        Bundle extras = getIntent().getExtras();
        int drawable_id = Integer.parseInt(extras.getString("drawable_id"));

        P = new SPalette(VillageActivity.this, drawable_id);
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
                P.x = me.getX() - 100;
                P.y = me.getY() + 100;
                break;
            case MotionEvent.ACTION_UP:
                Log.d("touch", "touched");
                //P.placeInVillage();
                break;
            case MotionEvent.ACTION_MOVE:
                P.x = me.getX() - 100;
                P.y = me.getY() + 100;
                break;
        }

        P.updateTreeMap();

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        P.addToDb();
    }
}

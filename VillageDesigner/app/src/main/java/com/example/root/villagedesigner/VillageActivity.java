package com.example.root.villagedesigner;

import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.root.villagedesigner.sprite.Clickable;
import com.example.root.villagedesigner.views.Palette;

public class VillageActivity extends AppCompatActivity implements View.OnTouchListener {

    Palette P;
    int drawable_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        Bundle extras = getIntent().getExtras();
        int drawable_id = Integer.parseInt(extras.getString("drawable_id"));
        String type = extras.getString("type");

        this.drawable_id = drawable_id;

        P = new Palette(VillageActivity.this, drawable_id, type);
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
                P.getIsometricTouchLocation(loc);

                if (drawable_id == 0) {
                    for (Clickable c : P.clickables_list) {
                        RectF rectF = new RectF(c.position.x, c.position.y, c.position.x + c.bitmap_scaled.getWidth(), c.position.y + c.bitmap_scaled.getHeight());
                        if (rectF.contains(loc.x - (P.window_width / 2), loc.y))
                            c.onClick();
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.d("touch", "touched");
                break;
            case MotionEvent.ACTION_MOVE:
                loc = new PointF(me.getX(), me.getY());
                P.getIsometricTouchLocation(loc);
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        P.saveToDb();
    }
}

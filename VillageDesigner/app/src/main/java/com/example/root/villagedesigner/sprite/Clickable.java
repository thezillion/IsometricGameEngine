package com.example.root.villagedesigner.sprite;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;

public abstract class Clickable extends Sprite {

    public PointF position;
    public Clickable(Context context, int id) {
        super(context, id);

        this.scaleBitmap(107, 94);
    }

    public void setPosition(float x, float y) {
        this.position = new PointF(x, y);
    }

    public abstract void onClick();
}

package com.example.root.villagedesigner.sprite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

public class Sprite {

    public int height, width;
    Context mContext;
    public Bitmap bitmap;
    public Bitmap bitmap_scaled;
    public int zorder;
    public String type;
    public int drawable_id;
    public boolean offset = false;
    public boolean isWalkable;

    Bitmap getBitmap(int id) {
        return BitmapFactory.decodeResource(mContext.getResources(), id);
    }

    public Sprite(Context context, int id) {

        this.mContext = context;
        this.bitmap = this.getBitmap(id);
        this.drawable_id = id;
        this.isWalkable = false;

    }

    public void scaleBitmap(int width, int height) {

        this.width = width;
        this.height = height;
        this.bitmap_scaled = Bitmap.createScaledBitmap(this.bitmap, this.width, this.height, true);

    }

    public void setZorder(int z) {
        this.zorder = z;
    }

}

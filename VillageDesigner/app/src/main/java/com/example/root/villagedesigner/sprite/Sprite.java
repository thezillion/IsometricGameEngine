package com.example.root.villagedesigner.sprite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by root on 22/6/17.
 */

public class Sprite {

    public int height, width;
    Context mContext;
    public Bitmap bitmap;
    public Bitmap bitmap_scaled;
    public int zorder;

    Bitmap getBitmap(int id) {
        return BitmapFactory.decodeResource(mContext.getResources(), id);
    }

    public Sprite(Context context, int id) {

        this.mContext = context;
        this.bitmap = this.getBitmap(id);

    }

    public void scaleBitmap(int width, int height) {

        this.width = width;
        this.height = height;
        this.bitmap_scaled = Bitmap.createScaledBitmap(this.bitmap, this.width, this.height, true);

    }

    public void setZorder(int z) {
        this.zorder = z;
    }

    public void drawSprite(Canvas canvas, int x, int y) {

        canvas.drawBitmap(bitmap_scaled, x, y, null);

    }

}

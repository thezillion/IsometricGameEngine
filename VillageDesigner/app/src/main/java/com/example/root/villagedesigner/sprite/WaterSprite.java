package com.example.root.villagedesigner.sprite;

import android.content.Context;

public class WaterSprite extends Sprite {
    public WaterSprite(Context context, int id) {
        super(context, id);

        this.type = "water";
        this.scaleBitmap(142, 132);
        this.offset = true;
    }
}

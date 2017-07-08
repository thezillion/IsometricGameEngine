package com.example.root.villagedesigner.sprite;

import android.content.Context;

import com.example.root.villagedesigner.sprite.Sprite;

public class HouseSprite extends Sprite {

    public HouseSprite(Context context, int id) {
        super(context, id);

        this.type = "house";
        this.scaleBitmap(142, 132);
        this.offset = true;
    }
}

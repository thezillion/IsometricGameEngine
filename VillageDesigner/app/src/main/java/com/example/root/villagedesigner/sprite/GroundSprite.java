package com.example.root.villagedesigner.sprite;

import android.content.Context;

public class GroundSprite extends Sprite {
    public GroundSprite(Context context, int id) {
        super(context, id);
        this.type = "ground";
        this.scaleBitmap(142, 82);
    }
}

package com.example.root.villagedesigner.sprite;

import android.content.Context;

public class RoadSprite extends Sprite {
    public RoadSprite(Context context, int id) {
        super(context, id);

        this.type = "road";
        this.scaleBitmap(142, 82);
        this.isWalkable = true;
    }
}

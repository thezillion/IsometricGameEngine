package com.example.root.villagedesigner.sprite;

import android.content.Context;

public class TreesSprite extends Sprite {
    public TreesSprite(Context context, int id) {
        super(context, id);

        this.type = "trees";
        this.scaleBitmap(142, 132);
        this.offset = true;
    }
}

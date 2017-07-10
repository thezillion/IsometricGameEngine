package com.example.root.villagedesigner.sprite;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;

public class WalkingMan extends Sprite {
    int ZOM = 10;

    public PointF position_cart, position_iso;
    boolean[][] walkable_matrix = new boolean[ZOM][ZOM];
    public int man_tile_i, man_tile_j;

    int TILE_CART_DIMEN;
    public WalkingMan(Context context, int id, int TILE_CART_DIMEN, boolean[][] walkable_matrix) {
        super(context, id);

        this.scaleBitmap(10, 10);
        this.TILE_CART_DIMEN = TILE_CART_DIMEN;
        this.walkable_matrix = walkable_matrix;
        this.man_tile_i = 0;
        this.man_tile_j = 0;
    }

    public void setPosition(PointF point) {
        this.position_cart = point;
        this.position_iso = TwoDToIso(position_cart);
    }

    boolean isInWalkableTile(PointF touch) {
        for (int i = 0; i < ZOM; i++) {
            for (int j = 0; j < ZOM; j++) {
                int x1 = i * TILE_CART_DIMEN, y1 = j * TILE_CART_DIMEN, x2 = (i + 1) * TILE_CART_DIMEN, y2 = (j + 1) * TILE_CART_DIMEN;
                RectF squareTileRect = new RectF(x1, y1, x2, y2);
                if (squareTileRect.contains(touch.x, touch.y)) {
                    if (walkable_matrix[i][j]) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    int[] getCurrentTilePos() {
        int[] res = new int[2];
        for (int i = 0; i < ZOM; i++) {
            for (int j = 0; j < ZOM; j++) {
                int x1 = i * TILE_CART_DIMEN, y1 = j * TILE_CART_DIMEN, x2 = (i + 1) * TILE_CART_DIMEN, y2 = (j + 1) * TILE_CART_DIMEN;
                RectF squareTileRect = new RectF(x1, y1, x2, y2);

                if (squareTileRect.contains(position_cart.x, position_cart.y)) {
                    res[0] = i;
                    res[1] = j;
                    return res;
                }
            }
        }
        return res;
    }

    public void walk(String dir) {
        int speed = 4;
        PointF newPos = new PointF(this.position_cart.x, this.position_cart.y);
        switch (dir) {
            case "nw":
                if (this.position_cart.x > 0)
                    newPos.x -= speed;
                break;
            case "ne":
                if (this.position_cart.y > 0)
                    newPos.y -= speed;
                break;
            case "sw":
                if (this.position_cart.y < 10*TILE_CART_DIMEN)
                    newPos.y += speed;
                break;
            case "se":
                if (this.position_cart.x < 10*TILE_CART_DIMEN)
                    newPos.x += speed;
                break;
        }
        if (isInWalkableTile(newPos)) {
            this.position_cart = new PointF(newPos.x, newPos.y);
            this.position_iso = TwoDToIso(position_cart);
            int[] currentTilePos = getCurrentTilePos();
            this.man_tile_i = currentTilePos[0];
            this.man_tile_j = currentTilePos[1];
        }
    }

    PointF TwoDToIso(PointF pt) {
        PointF IsoPoint = new PointF(0, 0);
        IsoPoint.x = (pt.x - pt.y);
        IsoPoint.y = ((pt.x + pt.y))*(float)(1/Math.sqrt(3));
        return IsoPoint;
    }

    PointF isoTo2d(PointF pt) {
        PointF TwoDPoint = new PointF(0, 0);
        TwoDPoint.x = (((float)Math.sqrt(3)*pt.y + pt.x) / 2);
        TwoDPoint.y = (((float)Math.sqrt(3)*pt.y - pt.x) / 2);

        return TwoDPoint;
    }
}

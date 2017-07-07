package com.example.root.villagedesigner.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.root.villagedesigner.R;
import com.example.root.villagedesigner.db.DatabaseHelper;
import com.example.root.villagedesigner.extras.RandomString;
import com.example.root.villagedesigner.sprite.Sprite;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;


public class Palette extends SurfaceView implements Runnable {

    Thread t = null;
    SurfaceHolder surfaceHolder;
    boolean running = false;
    public PointF location;

    int[] tile_pos = new int[2];

    Map<Integer, HashMap> houses_on_canvas;
    Context mContext;
    int drawable_id;

    DatabaseHelper myDb;
    Sprite new_house;
    Sprite ground;

    float window_height, window_width;

    String unique_id;

    int ZOM = 10;

    int[][] zorder_matrix = new int[ZOM][ZOM];
    boolean[][] occupancy_matrix = new boolean[ZOM][ZOM];
    PointF[][] position_matrix = new PointF[ZOM][ZOM];
    Sprite[][] sprite_matrix = new Sprite[ZOM][ZOM];

    int TILE_WIDTH, TILE_HEIGHT, TILE_CART_DIMEN, TILE_HOUSE_OFFSET;

    Sprite house_1, house_2, house_3, house_4;

    public Palette(Context context, int drawable_id) {
        super(context);
        this.surfaceHolder = getHolder();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        window_height = displayMetrics.heightPixels;
        window_width = displayMetrics.widthPixels;

        this.mContext = context;

        this.myDb = new DatabaseHelper(context);

        this.houses_on_canvas = new TreeMap<Integer, HashMap>();

        this.TILE_WIDTH = 142;
        this.TILE_HEIGHT = 82;
        this.TILE_HOUSE_OFFSET = 50;
        this.TILE_CART_DIMEN = TILE_WIDTH/2;

        ground = new Sprite(mContext, R.drawable.ground_re);
        ground.scaleBitmap(142, 82);
        ground.type = "ground";

        house_1 = new Sprite(mContext, R.drawable.house_1);
        house_1.scaleBitmap(142, 132);
        house_1.type = "house";

        house_2 = new Sprite(mContext, R.drawable.house_2);
        house_2.scaleBitmap(142, 132);
        house_2.type = "house";

        house_3 = new Sprite(mContext, R.drawable.house_3);
        house_3.scaleBitmap(142, 132);
        house_3.type = "house";

        house_4 = new Sprite(mContext, R.drawable.house_4);
        house_4.scaleBitmap(142, 132);
        house_4.type = "house";

        for (int i = 0; i<ZOM; i++) {
            for (int j = 0; j<ZOM; j++) {
                zorder_matrix[i][j] = (i+1)*100 - j;
                occupancy_matrix[i][j] = false;
                PointF IsoPoint = TwoDToIso(new PointF(i*TILE_CART_DIMEN, j*TILE_CART_DIMEN));
                position_matrix[i][j] = IsoPoint;
                sprite_matrix[i][j] = ground;
            }
        }

        this.loadDbData();

        this.drawable_id = drawable_id;
        this.unique_id = new RandomString(15).nextString();

        this.new_house = new Sprite(context, drawable_id);
        this.new_house.scaleBitmap(142, 132);
        this.tile_pos = this.firstAvailableTile();
        this.location = position_matrix[tile_pos[0]][tile_pos[1]];
        this.new_house.zorder = zorder_matrix[tile_pos[0]][tile_pos[1]];
        this.new_house.type = "house";

        this.sprite_matrix[tile_pos[0]][tile_pos[1]] = this.new_house;

        this.addToTreeMap();

    }

    public void getIsometricTouchLocation(PointF touch) {
        touch.x -= window_width/2;
        PointF TwoDPoint = isoTo2d(touch);
        boolean flag = false;
        for (int i = 0; i<ZOM; i++) {
            for (int j = 0 ;j<ZOM; j++) {
                int x1 = i*TILE_CART_DIMEN, y1 = j*TILE_CART_DIMEN, x2 = (i+1)*TILE_CART_DIMEN, y2 = (j+1)*TILE_CART_DIMEN;
                RectF squareTileRect = new RectF(x1, y1, x2, y2);
                if (squareTileRect.contains(TwoDPoint.x, TwoDPoint.y)) {
                    if (!occupancy_matrix[i][j]) {
                        sprite_matrix[tile_pos[0]][tile_pos[1]] = ground;
                        sprite_matrix[i][j] = this.new_house;
                        tile_pos[0] = i;
                        tile_pos[1] = j;
                    }
                    flag = true;
                    break;
                }
            }
            if (flag) break;
        }
    }

    public int[] firstAvailableTile() {
        int[] tile = new int[2];
        for (int i = 0; i<ZOM; i++) {
            for (int j = 0; j<ZOM; j++) {
                if (!occupancy_matrix[i][j]) {
                    tile[0] = i;
                    tile[1] = j;
                    return tile;
                }
            }
        }
        return tile;
    }

    public PointF isoTo2d(PointF pt) {
        PointF TwoDPoint = new PointF(0, 0);
        TwoDPoint.x = (((float)Math.sqrt(3)*pt.y + pt.x) / 2);
        TwoDPoint.y = (((float)Math.sqrt(3)*pt.y - pt.x) / 2);

        return TwoDPoint;
    }

    public PointF TwoDToIso(PointF pt) {
        PointF IsoPoint = new PointF(0, 0);
        IsoPoint.x = (pt.x - pt.y);
        IsoPoint.y = ((pt.x + pt.y))*(float)(1/Math.sqrt(3));
        return IsoPoint;
    }

    @Override
    public void run() {
        while(running) {
            if (!surfaceHolder.getSurface().isValid())
                continue;

            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawARGB(255, 255, 255, 255);

            canvas.translate(window_width/2, 0);

            for (int i = 0; i<ZOM; i++) {
                for (int j = 0; j<ZOM; j++) {
                    PointF point = position_matrix[i][j];
                    Sprite sprite = sprite_matrix[i][j];
                    float y_offset = 0;
                    if (sprite.type == "house")
                        y_offset = TILE_HOUSE_OFFSET;
                    float left = point.x - TILE_WIDTH/2, top = (point.y - y_offset);
                    canvas.drawBitmap(sprite.bitmap_scaled, left, top, null);
                }
            }

            surfaceHolder.unlockCanvasAndPost(canvas);

        }
    }

    public void pause() {
        running = false;
        while(true) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            break;
        }
        t = null;
    }

    public void resume() {
        running = true;
        t = new Thread(this);
        t.start();
    }

    void loadDbData() {

        Cursor res = myDb.getAllData();
        if (res.getColumnCount() == 0) {
            Log.d("Error", "Nothing found");
        }

        while (res.moveToNext()) {

            int drawable_id = Integer.parseInt(res.getString(1)),
                tile_i = Integer.parseInt(res.getString(2)),
                tile_j = Integer.parseInt(res.getString(3));

            switch(drawable_id) {

                case R.drawable.house_1:
                    sprite_matrix[tile_i][tile_j] = house_1;
                    break;
                case R.drawable.house_2:
                    sprite_matrix[tile_i][tile_j] = house_2;
                    break;
                case R.drawable.house_3:
                    sprite_matrix[tile_i][tile_j] = house_3;
                    break;
                case R.drawable.house_4:
                    sprite_matrix[tile_i][tile_j] = house_4;
                    break;


            }

            occupancy_matrix[tile_i][tile_j] = true;

        }

    }

    public void saveToDb() {
        boolean isInserted = myDb.insertData(drawable_id, tile_pos[0], tile_pos[1]);
        if (isInserted == true) {
            Log.d("DbInsert", "Successful");
        } else {
            Log.d("DbInsert", "Unsuccessful");
        }
    }

}

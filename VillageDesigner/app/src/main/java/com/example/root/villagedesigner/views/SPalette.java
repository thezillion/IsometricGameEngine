package com.example.root.villagedesigner.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
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


public class SPalette extends SurfaceView implements Runnable {

    Thread t = null;
    SurfaceHolder surfaceHolder;
    boolean running = false;
    public float x, y;

    Map<Integer, HashMap> houses_on_canvas;
    Context mContext;
    int drawable_id;

    DatabaseHelper myDb;
    Sprite new_house;

    String unique_id;

    int[][] zorder_matrix = new int[20][20];
    boolean[][] occupancy_matrix = new boolean[20][20];
    PointF[][] position_matrix = new PointF[20][20];

    int TILE_WIDTH, TILE_HEIGHT, TILE_HOUSE_OFFSET, TILE_CART_DIMEN;

    public SPalette(Context context, int drawable_id) {
        super(context);
        this.surfaceHolder = getHolder();

        this.drawable_id = drawable_id;

        this.new_house = new Sprite(context, drawable_id);
        this.new_house.scaleBitmap(142, 139);
        this.new_house.setZorder(0);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        this.y = height/2 - this.new_house.height/2;
        this.x = width/2 - this.new_house.width/2;

        this.mContext = context;

        this.myDb = new DatabaseHelper(context);

        this.houses_on_canvas = new TreeMap<Integer, HashMap>();

        this.loadDbData();

        this.unique_id = new RandomString(15).nextString();

        this.addToTreeMap();

        this.TILE_WIDTH = 142;
        this.TILE_HEIGHT = 82;
        this.TILE_HOUSE_OFFSET = 50;
        this.TILE_CART_DIMEN = TILE_WIDTH/2;

        for (int i = 0; i<20; i++) {
            for (int j = 0; j<20; j++) {
                zorder_matrix[i][j] = (i+1)*100 - j;
                occupancy_matrix[i][j] = false;
                Point TwoDPoint = new Point();
                position_matrix[i][j] = TwoDToIso(new PointF(i*TILE_CART_DIMEN, j*TILE_CART_DIMEN));
                Log.d("Positions", i+" "+j+" "+position_matrix[i][j]);
            }
        }

    }

    public PointF isoTo2d(PointF pt) {
        PointF TwoDPoint = new PointF(0, 0);
        TwoDPoint.x = ((2*pt.y - pt.x) / 2);
        TwoDPoint.y = ((2*pt.y + pt.x) / 2);

        return TwoDPoint;
    }

    public PointF TwoDToIso(PointF pt) {
        PointF IsoPoint = new PointF(0, 0);
        IsoPoint.x = (pt.y - pt.x);
        IsoPoint.y = ((pt.x + pt.y) / 2);
        return IsoPoint;
    }

    @Override
    public void run() {
        while(running) {
            if (!surfaceHolder.getSurface().isValid())
                continue;

            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawARGB(255, 255, 255, 255);

            Sprite ground = new Sprite(mContext, R.drawable.ground);
            ground.scaleBitmap(142, 82);

            for (int i = 0; i<20; i++) {
                for (int j = 0; j<20; j++) {
                    PointF point = position_matrix[i][j];
                    canvas.drawBitmap(ground.bitmap_scaled, point.x - TILE_WIDTH/2, point.y, null);
                }
            }

            /*for (int x: houses_on_canvas.keySet()) {
                HashMap<Integer, HashMap<String, PointF>> houses_at_zorder = houses_on_canvas.get(x);

                for (Map.Entry<Integer, HashMap<String, PointF>> houses: houses_at_zorder.entrySet()) {

                    int drawable_id = houses.getKey();
                    HashMap<String, PointF> points = houses.getValue();

                    for (String uniq: points.keySet()) {
                        PointF point = points.get(uniq);
                        Sprite house = new Sprite(mContext, drawable_id);
                        house.scaleBitmap(142, 139);
                        canvas.drawBitmap(house.bitmap_scaled, point.x - (house.width / 2), point.y - (house.height / 2), null);
                    }
                }

            }*/

            for (int i = 0; i<20; i++) {
                for (int j = 0; j<20; j++) {

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

            int drawable_id = Integer.parseInt(res.getString(1));

            float coord_x = Float.parseFloat(res.getString(2)),
                  coord_y = Float.parseFloat(res.getString(3));

            int zorder = Integer.parseInt(res.getString(4));

            String uniq = res.getString(5);

            if(!houses_on_canvas.containsKey(zorder)){
                houses_on_canvas.put(zorder, new HashMap<Integer, ArrayList<PointF>>());
            }

            HashMap<Integer, HashMap<String, PointF>> houses_at_zorder = houses_on_canvas.get(zorder);

            if (!houses_at_zorder.containsKey(drawable_id)) {
                houses_at_zorder.put(drawable_id, new HashMap<String, PointF>());
            }

            HashMap<String, PointF> this_house = houses_at_zorder.get(drawable_id);

            this_house.put(uniq, new PointF(coord_x, coord_y));


        }

    }

    public void addToDb() {
        boolean isInserted = myDb.insertData(drawable_id, x, y, new_house.zorder, unique_id);
        if (isInserted == true) {
            Log.d("DbInsert", "Successful");
        } else {
            Log.d("DbInsert", "Unsuccessful");
        }
    }

    public void updateTreeMap() {
        HashMap<Integer, HashMap> houses_at_zorder = houses_on_canvas.get(new_house.zorder);
        HashMap<String, PointF> this_house = houses_at_zorder.get(drawable_id);
        this_house.get(unique_id).set(new PointF(x, y));
    }

    public void addToTreeMap() {
        if(!houses_on_canvas.containsKey(this.new_house.zorder)){
            houses_on_canvas.put(this.new_house.zorder, new HashMap<Integer, ArrayList>());
        }
        HashMap<Integer, HashMap> houses_at_zorder = houses_on_canvas.get(new_house.zorder);
        if (!houses_at_zorder.containsKey(drawable_id)) {
            houses_at_zorder.put(drawable_id, new HashMap<String, PointF>());
        }
        HashMap<String, PointF> this_house = houses_at_zorder.get(drawable_id);
        this_house.put(unique_id, new PointF(x, y));
    }

    void changeZorder(int z) {
        ((HashMap<String, PointF>)(houses_on_canvas.get(new_house.zorder).get(drawable_id))).remove(unique_id);
        if (!houses_on_canvas.containsKey(z))
            houses_on_canvas.put(z, new HashMap<Integer, HashMap>());
        if (!houses_on_canvas.get(z).containsKey(drawable_id))
            houses_on_canvas.get(z).put(drawable_id, new HashMap<String, PointF>());
        ((HashMap<String, PointF>)(houses_on_canvas.get(z).get(drawable_id))).put(unique_id, new PointF(x, y));
        // something
    }
}

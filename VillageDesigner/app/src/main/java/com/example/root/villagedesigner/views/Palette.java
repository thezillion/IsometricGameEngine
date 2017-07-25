package com.example.root.villagedesigner.views;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.root.villagedesigner.R;
import com.example.root.villagedesigner.db.DatabaseHelper;
import com.example.root.villagedesigner.sprite.Clickable;
import com.example.root.villagedesigner.sprite.GroundSprite;
import com.example.root.villagedesigner.sprite.HouseSprite;
import com.example.root.villagedesigner.sprite.RoadSprite;
import com.example.root.villagedesigner.sprite.Sprite;
import com.example.root.villagedesigner.sprite.TreesSprite;
import com.example.root.villagedesigner.sprite.WalkingMan;
import com.example.root.villagedesigner.sprite.WaterSprite;


public class Palette extends SurfaceView implements Runnable {

    Thread t = null;
    SurfaceHolder surfaceHolder;
    boolean running = false;
    public PointF location;

    int[] tile_pos = new int[2];

    Context mContext;
    int drawable_id;

    DatabaseHelper myDb;
    Sprite new_sprite;

    public float window_height, window_width;

    int ZOM = 10;

    int[][] zorder_matrix = new int[ZOM][ZOM];
    boolean[][] occupancy_matrix = new boolean[ZOM][ZOM];
    boolean[][] walkable_matrix = new boolean[ZOM][ZOM];
    PointF[][] position_matrix = new PointF[ZOM][ZOM];
    Sprite[][] sprite_matrix = new Sprite[ZOM][ZOM];
    public Clickable[] clickables_list;

    int TILE_WIDTH, TILE_HEIGHT, TILE_CART_DIMEN, TILE_TOP_OFFSET;

    GroundSprite ground;
    HouseSprite house_1, house_2, house_3, house_4;
    RoadSprite road_1, road_2, road_3, road_4, road_5, road_6, road_7, road_8, road_9, road_10;
    TreesSprite trees_1;
    WaterSprite water;

    Clickable north_west, north_east, south_west, south_east;

    WalkingMan walkingMan;

    public Palette(Context context, int drawable_id, String type) {
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

        this.TILE_WIDTH = 142;
        this.TILE_HEIGHT = 82;
        this.TILE_TOP_OFFSET = 50;
        this.TILE_CART_DIMEN = TILE_WIDTH/2;

        ground = new GroundSprite(mContext, R.drawable.ground);

        house_1 = new HouseSprite(mContext, R.drawable.house_1);
        house_2 = new HouseSprite(mContext, R.drawable.house_2);
        house_3 = new HouseSprite(mContext, R.drawable.house_3);
        house_4 = new HouseSprite(mContext, R.drawable.house_4);

        road_1 = new RoadSprite(mContext, R.drawable.road_1);
        road_2 = new RoadSprite(mContext, R.drawable.road_2);
        road_3 = new RoadSprite(mContext, R.drawable.road_3);
        road_4 = new RoadSprite(mContext, R.drawable.road_4);
        road_5 = new RoadSprite(mContext, R.drawable.road_5);
        road_6 = new RoadSprite(mContext, R.drawable.road_6);
        road_7 = new RoadSprite(mContext, R.drawable.road_7);
        road_8 = new RoadSprite(mContext, R.drawable.road_8);
        road_9 = new RoadSprite(mContext, R.drawable.road_9);
        road_10 = new RoadSprite(mContext, R.drawable.road_10);

        trees_1 = new TreesSprite(mContext, R.drawable.trees_1);

        water = new WaterSprite(mContext, R.drawable.water);

        for (int i = 0; i<ZOM; i++) {
            for (int j = 0; j<ZOM; j++) {
                zorder_matrix[i][j] = (i+1)*100 + j;
                occupancy_matrix[i][j] = false;
                PointF IsoPoint = TwoDToIso(new PointF(i*TILE_CART_DIMEN, j*TILE_CART_DIMEN));
                position_matrix[i][j] = IsoPoint;
                sprite_matrix[i][j] = ground;
                walkable_matrix[i][j] = true;
            }
        }

        this.loadDbData();

        this.drawable_id = drawable_id;

        if (drawable_id != 0) {
            if (type.equals("house"))
                this.new_sprite = new HouseSprite(context, drawable_id);
            else if (type.equals("road"))
                this.new_sprite = new RoadSprite(context, drawable_id);
            else if (type.equals("trees"))
                this.new_sprite = new TreesSprite(context, drawable_id);
            else if (type.equals("water"))
                this.new_sprite = new WaterSprite(context, drawable_id);

            this.tile_pos = this.firstAvailableTile();
            this.location = position_matrix[tile_pos[0]][tile_pos[1]];
            this.new_sprite.zorder = zorder_matrix[tile_pos[0]][tile_pos[1]];
            this.sprite_matrix[tile_pos[0]][tile_pos[1]] = this.new_sprite;
        } else {

            north_west = new Clickable(mContext, R.drawable.north_west) {
                @Override
                public void onClick() {
                    walkingMan.walk("nw");
                }
            };
            north_west.setPosition(20 - (window_width/2), 20);

            north_east = new Clickable(mContext, R.drawable.north_east) {
                @Override
                public void onClick() {
                    walkingMan.walk("ne");
                }
            };
            north_east.setPosition(window_width/2 - (north_east.width+20), 20);

            south_west = new Clickable(mContext, R.drawable.south_west) {
                @Override
                public void onClick() {
                    walkingMan.walk("sw");
                }
            };
            south_west.setPosition(20 - window_width/2, window_height - (20+south_west.height));

            south_east = new Clickable(mContext, R.drawable.south_east) {
                @Override
                public void onClick() {
                    walkingMan.walk("se");
                }
            };
            south_east.setPosition(window_width/2 - (south_east.width+20), window_height - (20+south_east.height));


            clickables_list = new Clickable[4];
            clickables_list[0] = north_west;
            clickables_list[1] = north_east;
            clickables_list[2] = south_west;
            clickables_list[3] = south_east;

            walkingMan = new WalkingMan(mContext, R.drawable.man, TILE_CART_DIMEN, walkable_matrix);
            walkingMan.setPosition(TwoDToIso(new PointF(0, 0)));

        }

    }

    public void getIsometricTouchLocation(PointF touch) {
        if (this.drawable_id != 0) {
            touch.x -= window_width / 2;
            PointF TwoDPoint = isoTo2d(touch);
            boolean flag = false;
            for (int i = 0; i < ZOM; i++) {
                for (int j = 0; j < ZOM; j++) {
                    int x1 = i * TILE_CART_DIMEN, y1 = j * TILE_CART_DIMEN, x2 = (i + 1) * TILE_CART_DIMEN, y2 = (j + 1) * TILE_CART_DIMEN;
                    RectF squareTileRect = new RectF(x1, y1, x2, y2);
                    if (squareTileRect.contains(TwoDPoint.x, TwoDPoint.y)) {
                        if (!occupancy_matrix[i][j]) {
                            sprite_matrix[tile_pos[0]][tile_pos[1]] = ground;
                            sprite_matrix[i][j] = this.new_sprite;
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
                    //if (sprite.type.equals("ground")) {
                    float left = point.x - TILE_WIDTH / 2, top = (point.y);
                    canvas.drawBitmap(ground.bitmap_scaled, left, top, null);
                    //}
                }
            }

            for (int i = 0; i<ZOM; i++) {
                for (int j = 0; j<ZOM; j++) {
                    PointF point = position_matrix[i][j];
                    Sprite sprite = sprite_matrix[i][j];
                    if (!sprite.type.equals("ground")) {
                        float y_offset = 0;
                        if (sprite.offset)
                            y_offset = TILE_TOP_OFFSET;
                        float left = point.x - TILE_WIDTH / 2, top = (point.y - y_offset);
                        canvas.drawBitmap(sprite.bitmap_scaled, left, top, null);
                    }
                    if (drawable_id == 0) {
                        if (walkingMan.man_tile_i == i && walkingMan.man_tile_j == j) {
                            //canvas.drawBitmap(walkingMan.bitmap_scaled, walkingMan.position_iso.x, walkingMan.position_iso.y, null);
                            canvas.drawBitmap(walkingMan.bitmap_scaled, walkingMan.srcRect, new RectF(walkingMan.position_iso.x-28 + 7, walkingMan.position_iso.y-61 + 7, walkingMan.position_iso.x + 7, walkingMan.position_iso.y + 7), null);
                        }
                    }
                }
            }

            if (drawable_id == 0) {
                for (Clickable c : clickables_list) {
                    canvas.drawBitmap(c.bitmap_scaled, c.position.x, c.position.y, null);
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
                case R.drawable.road_1:
                    sprite_matrix[tile_i][tile_j] = road_1;
                    break;
                case R.drawable.road_2:
                    sprite_matrix[tile_i][tile_j] = road_2;
                    break;
                case R.drawable.road_3:
                    sprite_matrix[tile_i][tile_j] = road_3;
                    break;
                case R.drawable.road_4:
                    sprite_matrix[tile_i][tile_j] = road_4;
                    break;
                case R.drawable.road_5:
                    sprite_matrix[tile_i][tile_j] = road_5;
                    break;
                case R.drawable.road_6:
                    sprite_matrix[tile_i][tile_j] = road_6;
                    break;
                case R.drawable.road_7:
                    sprite_matrix[tile_i][tile_j] = road_7;
                    break;
                case R.drawable.road_8:
                    sprite_matrix[tile_i][tile_j] = road_8;
                    break;
                case R.drawable.road_9:
                    sprite_matrix[tile_i][tile_j] = road_9;
                    break;
                case R.drawable.road_10:
                    sprite_matrix[tile_i][tile_j] = road_10;
                    break;
                case R.drawable.trees_1:
                    sprite_matrix[tile_i][tile_j] = trees_1;
                    break;
                case R.drawable.water:
                    sprite_matrix[tile_i][tile_j] = water;
                    break;

            }

            walkable_matrix[tile_i][tile_j] = sprite_matrix[tile_i][tile_j].isWalkable;
            occupancy_matrix[tile_i][tile_j] = true;

        }

    }

    public void saveToDb() {
        if (drawable_id != 0) {
            boolean isInserted = myDb.insertData(drawable_id, tile_pos[0], tile_pos[1]);
            if (isInserted == true) {
                Log.d("DbInsert", "Successful");
            } else {
                Log.d("DbInsert", "Unsuccessful");
            }
        }
    }

}

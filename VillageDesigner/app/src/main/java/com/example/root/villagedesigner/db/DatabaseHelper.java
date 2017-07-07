package com.example.root.villagedesigner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Locations.db";
    public static final String TABLE_NAME = "locations_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "DRAWABLE_ID";
    public static final String COL_3 = "TILE_I";
    public static final String COL_4 = "TILE_J";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 + " INTEGER, " + COL_3 + " INTEGER, " + COL_4 + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(int drawable_id, float tile_i, float tile_j) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, drawable_id);
        contentValues.put(COL_3, tile_i);
        contentValues.put(COL_4, tile_j);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }

    public boolean updateData(int id, int drawable_id, float tile_i, float tile_j) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, drawable_id);
        contentValues.put(COL_3, tile_i);
        contentValues.put(COL_4, tile_j);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[] { Integer.toString(id) });
        return true;
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME);
    }
}

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
    public static final String COL_3 = "COORD_X";
    public static final String COL_4 = "COORD_Y";
    public static final String COL_5 = "ZORDER";
    public static final String COL_6 = "UNIQUEID";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 + " INTEGER, " + COL_3 + " FLOAT, " + COL_4 + " FLOAT, " + COL_5 + " INTEGER, " + COL_6 + " VARCHAR(15))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(int drawable_id, float coord_x, float coord_y, int zorder, String unique) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, drawable_id);
        contentValues.put(COL_3, coord_x);
        contentValues.put(COL_4, coord_y);
        contentValues.put(COL_5, zorder);
        contentValues.put(COL_6, unique);
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

    public boolean updateData(int id, int drawable_id, float coord_x, float coord_y, int zorder, String unique) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, drawable_id);
        contentValues.put(COL_3, coord_x);
        contentValues.put(COL_4, coord_y);
        contentValues.put(COL_5, zorder);
        contentValues.put(COL_6, unique);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[] { Integer.toString(id) });
        return true;
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME);
    }
}

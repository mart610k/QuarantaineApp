package com.example.quarantaine.Classes;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String LOCATION_TABLE = "LOCATION_TABLE";
    private static final String COLUMN_LAT = "LAT";
    private static final String COLUMN_LON = "LON";
    private static final String COLUMN_TIME = "TIME";
    private static final String COLUMN_ID = "ID";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "quarantaine.db", null, 1);
    }

    // This is going to be called the first a database is accessed, should be code to generate new table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + LOCATION_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_LAT + " DOUBLE, " + COLUMN_LON + " DOUBLE, " + COLUMN_TIME + " DATE)";

        db.execSQL(createTableStatement);
    }
    // Called everytime the database version updates
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addLocation(LocationModel locationModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_LAT, locationModel.getLat());
        values.put(COLUMN_LON, locationModel.getLon());
        values.put(COLUMN_TIME, locationModel.getTime().toString());

        long insert = db.insert(LOCATION_TABLE, null, values);

        if(insert == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteLocation() {
        SQLiteDatabase db = this.getWritableDatabase();
        int delete = db.delete(LOCATION_TABLE, null, null);
        if(delete > 0) {
            return true;
        }
        else {
            return false;
        }
    }
}

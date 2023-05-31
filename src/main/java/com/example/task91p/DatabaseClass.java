package com.example.task91p;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseClass extends SQLiteOpenHelper {

    // Creating the database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LostFoundDb.db";
    private static final String TABLE_NAME = "lost_found";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_IS_LOST_OR_FOUND = "is_lost_or_found";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_LOCATION = "location";

    public DatabaseClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Create the table in the database
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_IS_LOST_OR_FOUND + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_LOCATION + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    //It will insert data in the databse
    public long insertData(Advert advert) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, advert.getName());
        values.put(COLUMN_IS_LOST_OR_FOUND, advert.getIsLostOrFound());
        values.put(COLUMN_PHONE, advert.getPhone());
        values.put(COLUMN_DESCRIPTION, advert.getDescription());
        values.put(COLUMN_DATE, advert.getDate());
        values.put(COLUMN_LOCATION, advert.getLocation());
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(TABLE_NAME, null, values);
        db.close();

        return id;
    }
    //It will get the data
    public ArrayList<Advert> getData() {
        ArrayList<Advert> data = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                int lostOrFoundIndex = cursor.getColumnIndex(COLUMN_IS_LOST_OR_FOUND);
                int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);
                int descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);
                int dateIndex = cursor.getColumnIndex(COLUMN_DATE);
                int locationIndex = cursor.getColumnIndex(COLUMN_LOCATION);
                if (idIndex != -1 && nameIndex != -1 && lostOrFoundIndex != -1 &&
                        phoneIndex != -1 && descriptionIndex != -1 && dateIndex != -1 &&
                        locationIndex != -1) {
                    Advert model = new Advert(
                            cursor.getString(lostOrFoundIndex),
                            cursor.getString(nameIndex),
                            cursor.getString(phoneIndex),
                            cursor.getString(descriptionIndex),
                            cursor.getString(dateIndex),
                            cursor.getString(locationIndex),
                            cursor.getInt(idIndex)
                    );
                    data.add(model);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return data;
    }
    //It will get the data by its ID
    public Advert getDataById(int id) {
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Advert advert = null;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
            int lostOrFoundIndex = cursor.getColumnIndex(COLUMN_IS_LOST_OR_FOUND);
            int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);
            int descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);
            int dateIndex = cursor.getColumnIndex(COLUMN_DATE);
            int locationIndex = cursor.getColumnIndex(COLUMN_LOCATION);
            if (idIndex != -1 && nameIndex != -1 && lostOrFoundIndex != -1 &&
                    phoneIndex != -1 && descriptionIndex != -1 && dateIndex != -1 &&
                    locationIndex != -1) {
                advert = new Advert(
                        cursor.getString(lostOrFoundIndex),
                        cursor.getString(nameIndex),
                        cursor.getString(phoneIndex),
                        cursor.getString(descriptionIndex),
                        cursor.getString(dateIndex),
                        cursor.getString(locationIndex),
                        cursor.getInt(idIndex)
                );
            }
        }
        cursor.close();
        db.close();
        return advert;
    }
    //It will delete the data by its id
    public int deleteDataById(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        // Delete the data from the table based on the ID
        int deletedRows = db.delete(TABLE_NAME, selection, selectionArgs);
        db.close();
        return deletedRows;
    }
}
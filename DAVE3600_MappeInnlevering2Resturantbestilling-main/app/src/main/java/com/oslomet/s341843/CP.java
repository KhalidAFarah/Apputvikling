package com.oslomet.s341843;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CP extends ContentProvider {
    //Resturant tabell
    public static final String TABLE_RESTURANT = "Resturanter";
    public static final String RESTURANT_KEY_ID = "_ID";
    public static final String RESTURANT_KEY_NAME = "Name";
    public static final String RESTURANT_KEY_ADRESS = "Adresse";
    public static final String RESTURANT_KEY_TLF = "Tlf";
    public static final String RESTURANT_KEY_TYPE = "Type";

    //Database
    private static final String DB_NAVN = "resturant.db";
    private static final int DB_VERSJON = 1;

    //Provider
    public static final String PROVIDER = "com.oslomet.s341843";
    public static final int RESTURANT = 1;
    public static final int MRESTURANT = 2;
    CP.DatabaseHelper DBHelper;
    SQLiteDatabase db;

    //URI Matcher
    public static final  Uri CONTENT_URI = Uri.parse("content://"+PROVIDER+"/resturant");
    public static final  Uri CONTENT_URI2 = Uri.parse("content://"+PROVIDER+"/resturant/#");
    private static final UriMatcher urimatcher;

    static {
        urimatcher = new UriMatcher(UriMatcher.NO_MATCH);
        urimatcher.addURI(PROVIDER, "resturant", MRESTURANT);
        urimatcher.addURI(PROVIDER, "resturant/#", RESTURANT);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DB_NAVN, null, DB_VERSJON);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            final String lagResturantTabell = "CREATE TABLE " + TABLE_RESTURANT + "("
                    + RESTURANT_KEY_ID + " INTEGER PRIMARY KEY," + RESTURANT_KEY_NAME + " TEXT,"
                    + RESTURANT_KEY_ADRESS + " TEXT," + RESTURANT_KEY_TLF + " TEXT,"
                    + RESTURANT_KEY_TYPE + " TEXT)";

            Log.d("DB", "lager Resturant tabellen: " + lagResturantTabell);

            db.execSQL(lagResturantTabell);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTURANT);

            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        DBHelper = new CP.DatabaseHelper(getContext());
        db = DBHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (urimatcher.match(uri)) {
            case MRESTURANT:
                return "vnd.android.cursor.dir/vnd.oslomet.resturant";
            case RESTURANT:
                return "vnd.android.cursor.item/vnd.oslomet.resturant";
            default:
                throw new IllegalArgumentException("Ugyldig URI " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor c = null;
        if(urimatcher.match(uri) == RESTURANT) {
            c = db.query(TABLE_RESTURANT, strings, RESTURANT_KEY_ID + "="+uri.getPathSegments().get(1), strings1, null, null, s1);
            return c;
        }else{
            c = db.query(TABLE_RESTURANT, null, null, null, null, null, null);
            return c;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        db.insert(TABLE_RESTURANT, null, contentValues);
        Cursor c = db.query(TABLE_RESTURANT, null, null, null, null, null, null);
        c.moveToLast();
        long minId = c.getLong(0);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, minId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        if(urimatcher.match(uri) == RESTURANT){
            db.delete(TABLE_RESTURANT, RESTURANT_KEY_ID + " = " + uri.getPathSegments().get(1), strings);
            getContext().getContentResolver().notifyChange(uri, null);
            return 1;
        }if(urimatcher.match(uri) == MRESTURANT){
            db.delete(TABLE_RESTURANT, null, null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 2;
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        if(urimatcher.match(uri) == RESTURANT) {
            db.update(TABLE_RESTURANT, contentValues, RESTURANT_KEY_ID + " = " + uri.getPathSegments().get(1), null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 1;
        }if(urimatcher.match(uri) == MRESTURANT){
            db.update(TABLE_RESTURANT, null, null, null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 2;
        }
        return 0;
    }
}

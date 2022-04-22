package com.oslomet.databaser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static String TABLE_KONTAKTER = "Kontakter";
    private static String KEY_ID = "_ID";
    private static String KEY_NAME = "Navn";
    private static String KEY_TLF = "Telefon";
    private static int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "Telefonkontakter";

    public DBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String LAG_TABELL = "CREATE TABLE " + TABLE_KONTAKTER + "(" + KEY_ID +
                " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_TLF +
                " TEXT" + ")";
        Log.d("SQL", LAG_TABELL);
        sqLiteDatabase.execSQL(LAG_TABELL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_KONTAKTER);
        onCreate(sqLiteDatabase);
    }
    public void leggTilKontakt(Kontakt kontakt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, kontakt.getNavn());
        values.put(KEY_TLF, kontakt.getTlf());
        db.insert(TABLE_KONTAKTER, null, values);
        db.close();
    }

    public List<Kontakt> finnAlleKontakter() {
        List<Kontakt> kontaktListe = new ArrayList<Kontakt>();
        String selectQuery = "SELECT * FROM " + TABLE_KONTAKTER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor= db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            do {
                Kontakt kontakt = new Kontakt();
                kontakt.setID(cursor.getLong(0));
                kontakt.setNavn(cursor.getString(1));
                kontakt.setTlf(cursor.getString(2));
                kontaktListe.add(kontakt);
            } while(cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return kontaktListe;
    }

    public void slettKontakt(Long inn_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_KONTAKTER, KEY_ID + " =? ", new String[]{Long.toString(inn_id)});
        db.close();
    }

    public int oppdaterKontakt(Kontakt kontakt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(KEY_NAME, kontakt.getNavn());
        values.put(KEY_TLF, kontakt.getTlf());
        int endret = db.update(TABLE_KONTAKTER, values, KEY_ID + "= ?", new String[]{String.valueOf(kontakt.getID())});
        db.close();
        return endret;
    }
    public int finnAntallKontakter() {
        String sql = "SELECT * FROM " + TABLE_KONTAKTER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor= db.rawQuery(sql, null);
        int antall = cursor.getCount();
        cursor.close();
        db.close();
        return antall;
    }
    public Kontakt finnKontakt(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.query(TABLE_KONTAKTER, new String[]{KEY_ID, KEY_NAME, KEY_TLF},
                KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);

        if(cursor!= null)
            cursor.moveToFirst();
        Kontakt kontakt = new Kontakt(Long.parseLong(cursor.getString(0))
                , cursor.getString(1), cursor.getString(2));

        cursor.close();
        db.close();
        return kontakt;
    }

}

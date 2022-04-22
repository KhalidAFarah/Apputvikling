package com.oslomet.s341843.database;

import android.content.Context;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;
import android.widget.Toast;

import com.oslomet.s341843.CP;
import com.oslomet.s341843.MainActivity;

import java.security.KeyStore;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    //Resturant tabellen
    /*private static final String TABLE_RESTURANT = "Resturanter";
    private static final String RESTURANT_KEY_ID = "_ID";
    private static final String RESTURANT_KEY_NAME = "Name";
    private static final String RESTURANT_KEY_ADRESS = "Adresse";
    private static final String RESTURANT_KEY_TLF = "Tlf";
    private static final String RESTURANT_KEY_TYPE = "Type";*/

    //Kontakt tabellen
    private static final String TABLE_KONTAKTER = "Kontakter";
    private static final String KONTAKTER_KEY_ID = "_ID";
    private static final String KONTAKTER_KEY_NAME = "Name";
    private static final String KONTAKTER_KEY_TLF = "Tlf";

    //Bestilling tabellen
    private static final String TABLE_BESØK = "Bestilling";
    private static final String BESØK_KEY_ID = "_ID";
    private static final String BESØK_KEY_RESTURANT_ID = "Resturant_ID";
    private static final String BESØK_KEY_DATE = "Date";


    //Bestilling tabellen
    private static final String TABLE_BESTILLING = "Deltakere";
    private static final String BESTILLING_KEY_KONTAKT_ID = "Kontakt_ID";
    private static final String BESTILLING_KEY_BESØK_ID = "Bestillings_ID";





    //Databasen
    private static final String DATABASE_NAME = "Resturantbestillinger";
    private static int DATABASE_VERSION = 1;

    public DBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /*final String lagResturantTabell = "CREATE TABLE " + TABLE_RESTURANT + "("
                + RESTURANT_KEY_ID + " INTEGER PRIMARY KEY," + RESTURANT_KEY_NAME + " TEXT,"
                + RESTURANT_KEY_ADRESS + " TEXT," + RESTURANT_KEY_TLF + " TEXT,"
                + RESTURANT_KEY_TYPE + " TEXT)";*/

        final String lagKontaktTabell = "CREATE TABLE " + TABLE_KONTAKTER + "("
                + KONTAKTER_KEY_ID + " INTEGER PRIMARY KEY," + KONTAKTER_KEY_NAME + " TEXT,"
                + KONTAKTER_KEY_TLF + " TEXT)";

        final   String lagBesøkTabell = "CREATE TABLE " + TABLE_BESØK + "("
                + BESØK_KEY_ID + " INTEGER PRIMARY KEY,"
                + BESØK_KEY_RESTURANT_ID + " INTEGER,"
                + BESØK_KEY_DATE + " TEXT,"

                + "FOREIGN KEY (" + BESØK_KEY_RESTURANT_ID + ") "
                + "REFERENCES " + CP.TABLE_RESTURANT + "(" + CP.RESTURANT_KEY_ID + "));";

        final   String lagBestillingTabell = "CREATE TABLE " + TABLE_BESTILLING + "("
                + BESTILLING_KEY_KONTAKT_ID + " INTEGER,"
                + BESTILLING_KEY_BESØK_ID + " INTEGER,"


                + "FOREIGN KEY (" + BESTILLING_KEY_KONTAKT_ID + ") "
                + "REFERENCES " + TABLE_KONTAKTER + "(" + KONTAKTER_KEY_ID + "),"

                + "FOREIGN KEY (" + BESTILLING_KEY_BESØK_ID + ") "
                + "REFERENCES " + TABLE_BESØK + "(" + BESØK_KEY_ID + "));";



        //Log.d("DB", "lager Resturant tabellen: " + lagResturantTabell);
        Log.d("DB", "lager Kontakt tabellen: " + lagKontaktTabell);
        Log.d("DB", "lager Besøk tabellen: " + lagBesøkTabell);
        Log.d("DB", "lager Bestilling tabellen: " + lagBestillingTabell);

        //sqLiteDatabase.execSQL(lagResturantTabell);
        sqLiteDatabase.execSQL(lagKontaktTabell);
        sqLiteDatabase.execSQL(lagBesøkTabell);
        sqLiteDatabase.execSQL(lagBestillingTabell);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Dropper tabellene
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BESTILLING);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BESØK);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTURANT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_KONTAKTER);

        onCreate(sqLiteDatabase);
    }

    //Registrering, Henting, Oppdater og Sletting for Resturant tabellen
    /*public void registrerResturant(Resturant resturant){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(RESTURANT_KEY_NAME, resturant.getName());
        values.put(RESTURANT_KEY_ADRESS, resturant.getAdress());
        values.put(RESTURANT_KEY_TLF, resturant.getTlf());
        values.put(RESTURANT_KEY_TYPE, resturant.getType());

        db.insert(TABLE_RESTURANT, null, values);
        db.close();
    }
    public List<Resturant> hentAlleResturanter(){
        List<Resturant> resturanter = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_RESTURANT;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                resturanter.add(new Resturant(cursor.getLong(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            }while(cursor.moveToNext());
            cursor.close();
            db.close();
        }

        return resturanter;
    }
    public Resturant hentResturant(long id){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_RESTURANT, new String[]{RESTURANT_KEY_ID, RESTURANT_KEY_NAME,
                RESTURANT_KEY_ADRESS, RESTURANT_KEY_TLF, RESTURANT_KEY_TYPE},
                RESTURANT_KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);

        if(cursor == null) return null;

        if(cursor.moveToFirst()) {
            Resturant resturant = new Resturant(cursor.getLong(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4));

            db.close();
            return resturant;
        }
        return null;
    }
    public int oppdaterResturant(Resturant resturant){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RESTURANT_KEY_NAME, resturant.getName());
        values.put(RESTURANT_KEY_ADRESS, resturant.getAdress());
        values.put(RESTURANT_KEY_TLF, resturant.getTlf());
        values.put(RESTURANT_KEY_TYPE, resturant.getType());

        int oppdatert = db.update(TABLE_RESTURANT, values, RESTURANT_KEY_ID + "= ?",
                new String[]{String.valueOf(resturant.getId())});
        db.close();
        return  oppdatert;
    }
    public void slettResturant(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RESTURANT, RESTURANT_KEY_ID + "=?",
                new String[]{Long.toString(id)});
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_BESØK+" WHERE "+BESØK_KEY_RESTURANT_ID+"="+id, null);
        if(c.moveToFirst()){
            db.delete(TABLE_BESØK, BESØK_KEY_RESTURANT_ID+"=?", new String[]{Long.toString(c.getLong(1))});
            db.delete(TABLE_BESTILLING, BESTILLING_KEY_BESØK_ID+"=?", new String[]{Long.toString(c.getLong(0))});
        }

        db.close();
    }*/

    public void slettBesokerMedFjernetResturant(long resturantID){
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_BESØK+" WHERE "+BESØK_KEY_RESTURANT_ID+"="+resturantID, null);
        if(c.moveToFirst()){
            do{
                db.delete(TABLE_BESTILLING, BESTILLING_KEY_BESØK_ID+"=?", new String[]{Long.toString(c.getLong(0))});
            }while (c.moveToNext());
            db.delete(TABLE_BESØK, BESØK_KEY_RESTURANT_ID+"=?", new String[]{Long.toString(resturantID)});

        }

        db.close();
    }

    //Registrering, Henting, Oppdatering og Sletting for Kontakter tabellen
    public boolean registrerKontakter(Kontakt kontakter){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KONTAKTER_KEY_NAME, kontakter.getName());
        values.put(KONTAKTER_KEY_TLF, kontakter.getTlf());

        db.insert(TABLE_KONTAKTER, null, values);
        db.close();
        return true;
    }
    public List<Kontakt> hentAlleKontakter(){
        List<Kontakt> kontakter = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_KONTAKTER;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                kontakter.add(new Kontakt(cursor.getLong(0),
                        cursor.getString(1), cursor.getString(2)));
            }while(cursor.moveToNext());
            cursor.close();
            db.close();
        }


        return kontakter;
    }
    public Kontakt hentKontakt(long id){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_KONTAKTER, new String[]{KONTAKTER_KEY_ID, KONTAKTER_KEY_NAME,
                        KONTAKTER_KEY_TLF},
                KONTAKTER_KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);

        if(cursor == null) return null;

        cursor.moveToFirst();
        Kontakt kontakt = new Kontakt(cursor.getLong(0),
                cursor.getString(1), cursor.getString(2));
        db.close();
        return kontakt;
    }
    public int oppdaterKontakt(Kontakt kontakt){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KONTAKTER_KEY_NAME, kontakt.getName());
        values.put(KONTAKTER_KEY_TLF, kontakt.getTlf());

        int oppdatert = db.update(TABLE_KONTAKTER, values, KONTAKTER_KEY_ID + "= ?",
                new String[]{String.valueOf(kontakt.getId())});
        db.close();
        return  oppdatert;
    }
    public void slettKontakt(long id){
        SQLiteDatabase db = getWritableDatabase();
        int res = db.delete(TABLE_KONTAKTER, KONTAKTER_KEY_ID + " = ? ",
                new String[]{Long.toString(id)});
        Log.d("Test", "val: " + res);
        Log.d("Test", "id: " + id);
        db.close();
        db = getWritableDatabase();

        String query = "";

        db.delete(TABLE_BESTILLING, BESTILLING_KEY_KONTAKT_ID+" =? ", new String[]{Long.toString(id)});
        db.close();
    }

    //Registrering, Henting for Bestillinger tabellen
    public void registrerBestilling(Bestilling bestilling){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(BESTILLING_KEY_BESØK_ID, bestilling.getBesøkID());
        values.put(BESTILLING_KEY_KONTAKT_ID, bestilling.getKontaktID());

        db.insert(TABLE_BESTILLING, null, values);
        db.close();
    }
    public List<Bestilling> hentAlleBestillinger(){
        List<Bestilling> bestillinger = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_BESTILLING;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                bestillinger.add(new Bestilling(cursor.getLong(0),
                        cursor.getLong(1)));//hvordan henter vi DATE fra sqlite
            }while(cursor.moveToNext());
            cursor.close();
            db.close();
        }

        return bestillinger;
    }
    public Bestilling hentBestilling(long Besøk_id, long Kontakt_id){
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(TABLE_BESTILLING, new String[]{BESTILLING_KEY_BESØK_ID,
                        BESTILLING_KEY_KONTAKT_ID},
                BESTILLING_KEY_BESØK_ID + "=? AND " + BESTILLING_KEY_KONTAKT_ID + "=?", new String[]{String.valueOf(Besøk_id), String.valueOf(Kontakt_id)},
                null, null, null, null);

        if(cursor == null) return null;

        cursor.moveToFirst();
        Bestilling bestilling = new Bestilling(cursor.getLong(0),
                cursor.getLong(1));//hvordan henter vi DATE fra sqlite
        db.close();
        return bestilling;
    }


    //Registrering, Henting for Besøk tabellen
    public void registrerBesøk(Besøk besøk, List<Kontakt> delttakere){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        //values.put(BESØK_KEY_ID, besøk.getId());

        values.put(BESØK_KEY_RESTURANT_ID, besøk.getResturantID());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        //Log.d("Test", simpleDateFormat.format(besøk.getDate().getTime()));
        String date = simpleDateFormat.format(besøk.getDate().getTime());
        values.put(BESØK_KEY_DATE, date);

        db.insert(TABLE_BESØK, null, values);

        String query = "SELECT * FROM " + TABLE_BESØK +";";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        long id = DatabaseUtils.queryNumEntries(getReadableDatabase(), TABLE_BESØK);


        for(Kontakt k : delttakere){
            values = new ContentValues();
            values.put(BESTILLING_KEY_BESØK_ID, id);
            values.put(BESTILLING_KEY_KONTAKT_ID, k.getId());
            db.insert(TABLE_BESTILLING, null, values);
        }
        db.close();
    }
    public List<Besøk> hentAlleBesøk(){
        List<Besøk> besøk = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_BESØK;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {

                String[] datetime = cursor.getString(2).split(" ");

                String[] date = datetime[0].split("-");
                String[] time = datetime[1].split(":");

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, Integer.parseInt(date[2]));
                calendar.set(Calendar.MONTH, Integer.parseInt(date[1])-1);
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[0]));

                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));

                SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String te = s.format(calendar.getTime());

                besøk.add(new Besøk(cursor.getLong(0),
                        cursor.getLong(1), calendar));//hvordan henter vi DATE fra sqlite
            }while(cursor.moveToNext());
            cursor.close();
            db.close();
        }

        return besøk;
    }
    /*public List<String[]> hentAlleBesøk2(){
        List<String[]> besøk = new ArrayList<>();

        String query = "SELECT R."+RESTURANT_KEY_NAME+", B."+BESØK_KEY_DATE+
                " FROM " + TABLE_BESØK + "AS B, " + TABLE_RESTURANT + "AS R "
                + "WHERE B."+BESØK_KEY_RESTURANT_ID+"=R."+RESTURANT_KEY_ID+";";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {

            do {
                String[] values = new String[2];
                values[0] = cursor.getString(0);
                values[1] = cursor.getString(1);
                besøk.add(values);
            }while(cursor.moveToNext());
            cursor.close();
            db.close();
        }

        return besøk;
    }*/
    /*public String[] hentBesøk2(){
        String query = "SELECT R."+RESTURANT_KEY_NAME+", B."+BESØK_KEY_DATE+
                " FROM " + TABLE_BESØK + "AS B, " + TABLE_RESTURANT + "AS R "
                + "WHERE B."+BESØK_KEY_RESTURANT_ID+"=R."+RESTURANT_KEY_ID+";";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String[] values = new String[2];
        if(cursor.moveToFirst()) {
            values[0] = cursor.getString(0);
            values[1] = cursor.getString(1);
            cursor.close();
            db.close();
        }

        return values;
    }*/
    public Besøk hentBesøk(long id){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_BESTILLING, new String[]{BESØK_KEY_ID, BESØK_KEY_RESTURANT_ID,
                        BESØK_KEY_DATE},
                BESØK_KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);

        if(cursor == null) return null;

        cursor.moveToFirst();

        String[] datetime = cursor.getString(2).split(" ");

        String[] date = datetime[0].split("-");
        String[] time = datetime[1].split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(date[2]));
        calendar.set(Calendar.MONTH, Integer.parseInt(date[1]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[0]));

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));

        Besøk besøk = new Besøk(cursor.getLong(0),
                cursor.getLong(1), calendar);//hvordan henter vi DATE fra sqlite
        db.close();
        return besøk;
    }

    public void slettBesøk(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BESØK, BESØK_KEY_ID + "=?",
                new String[]{Long.toString(id)});

        db.delete(TABLE_BESTILLING, BESTILLING_KEY_BESØK_ID + "=?", new String[]{Long.toString(id)});

        db.close();
    }

    public List<Kontakt> getDeltakere(long besøkID){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT K."+KONTAKTER_KEY_ID+", K."+KONTAKTER_KEY_NAME+", K."+KONTAKTER_KEY_TLF+
                " FROM "+TABLE_KONTAKTER+" AS K, "+TABLE_BESTILLING+" AS D WHERE D."+BESTILLING_KEY_BESØK_ID+"="+besøkID+" AND K."+KONTAKTER_KEY_ID+"=D."+BESTILLING_KEY_KONTAKT_ID+";";
        Cursor cursor = db.rawQuery(query, null);

        List<Kontakt> deltakere = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                deltakere.add(new Kontakt(cursor.getLong(0),cursor.getString(1),cursor.getString(2)));
            }while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return deltakere;
    }

    public void setDeltakere(long besøkID, List<Kontakt> deltakere){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_BESTILLING, BESTILLING_KEY_BESØK_ID + "=?", new String[]{String.valueOf(besøkID)});

        for(Kontakt d : deltakere){
            ContentValues values = new ContentValues();

            values.put(BESTILLING_KEY_BESØK_ID, besøkID);
            values.put(BESTILLING_KEY_KONTAKT_ID, d.getId());

            db.insert(TABLE_BESTILLING, null, values);

        }
        db.close();

    }

}

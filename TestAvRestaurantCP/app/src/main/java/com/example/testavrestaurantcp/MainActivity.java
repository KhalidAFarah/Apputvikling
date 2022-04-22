package com.example.testavrestaurantcp;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public final static String PROVIDER = "com.oslomet.s341843";
    public final static Uri CONTENT_URI = Uri.parse("content://" + PROVIDER + "/resturant");

    //Resturant tabell
    public static final String TABLE_RESTURANT = "Resturanter";
    public static final String RESTURANT_KEY_ID = "_ID";
    public static final String RESTURANT_KEY_NAME = "Name";
    public static final String RESTURANT_KEY_ADRESS = "Adresse";
    public static final String RESTURANT_KEY_TLF = "Tlf";
    public static final String RESTURANT_KEY_TYPE = "Type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> s = new ArrayList<>();
        for(String str : getResources().getStringArray(R.array.planets_array)){
            s.add(str);
        }
        Spinner spinner = (Spinner) findViewById(R.id.type);

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_activated_1,s);
        spinner.setAdapter(adapter);









    }

    public void vis(View v){
        MyDialogFragment f = new MyDialogFragment();
        f.show(getSupportFragmentManager().beginTransaction(), "my fragment");

        //getSupportFragmentManager().beginTransaction().add(R.id.cLayout, f).commit();

    }

    public void leggTil(View v){

            //db.registrerResturant(resturant);
            ContentValues values = new ContentValues();
            values.put(RESTURANT_KEY_NAME, ((EditText) findViewById(R.id.navn)).getText().toString());
            values.put(RESTURANT_KEY_ADRESS, ((EditText) findViewById(R.id.adresse)).getText().toString());
            values.put(RESTURANT_KEY_TLF, ((EditText) findViewById(R.id.tlf)).getText().toString());
            values.put(RESTURANT_KEY_TYPE, ((Spinner) findViewById(R.id.type)).getSelectedItem().toString());

            getApplicationContext().getContentResolver().insert(CONTENT_URI, values);


            Toast.makeText(getApplicationContext(),"Resturanten er registrert",Toast.LENGTH_SHORT).show();

    }
    public void slett(View v){
        getApplicationContext().getContentResolver()
                .delete(Uri.parse(CONTENT_URI + "/" + (((EditText) findViewById(R.id.id)).getText().toString())), null, null);
    }

    public void oppdater(View v){
        ContentValues values = new ContentValues();
        values.put(RESTURANT_KEY_NAME, ((EditText) findViewById(R.id.navn)).getText().toString());
        values.put(RESTURANT_KEY_ADRESS, ((EditText) findViewById(R.id.adresse)).getText().toString());
        values.put(RESTURANT_KEY_TLF, ((EditText) findViewById(R.id.tlf)).getText().toString());
        values.put(RESTURANT_KEY_TYPE, ((Spinner) findViewById(R.id.type)).getSelectedItem().toString());

        getApplicationContext().getContentResolver().update(Uri.parse(CONTENT_URI+ "/" + (((EditText) findViewById(R.id.id)).getText().toString())), values, null, null);


        Toast.makeText(getApplicationContext(),"Resturanten er oppdatert",Toast.LENGTH_SHORT).show();
    }
}
package com.oslomet.s341843;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.Manifest;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.oslomet.s341843.database.Bestilling;
import com.oslomet.s341843.database.Besøk;
import com.oslomet.s341843.database.DBHandler;
import com.oslomet.s341843.database.Kontakt;
import com.oslomet.s341843.database.Resturant;
import com.oslomet.s341843.database.ResturantType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AlertDialog.DialogClickListener{
    private DBHandler db;
    private ListView list;
    private Toolbar customToolbar;

    private String state;
    private long id;
    private static final String CHANNEL_ID = "1";


    private ImageButton btn_registrer;
    private Resources resource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        resource = getResources();

        db = new DBHandler(getApplicationContext());
        list = (ListView) findViewById(R.id.list);
        customToolbar = (Toolbar) findViewById(R.id.customToolbar);
        btn_registrer = (ImageButton) findViewById(R.id.btn_registrer);
        setSupportActionBar(customToolbar);
        //customToolbar.inflateMenu(R.menu.customtoolbar);



        //standard skal appen vise bestillinger
        getBesøk(null);
        BottomNavigationView navView = findViewById(R.id.bottom_nav);
        navView.setOnNavigationItemSelectedListener(navListener);
        navView.getMenu().getItem(2).setChecked(true);

        if(!(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
        }


        //trenger legg inn siden for å virkelig registere ting ordentlige.
        //registerResturant();
        //registerKontakter();
        //registerBesøk();

    }



    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                    switch (item.getItemId()){
                        case R.id.navigation_contact:
                            getKontakter(null);
                            break;
                        case R.id.navigation_restuant:
                            getResturanter(null);
                            break;
                        case R.id.navigation_orders:
                            getBesøk(null);
                            break;
                    }

                    return true;
                }
            };

    public void getResturanter(View v){
        List<Resturant> resturanter = new ArrayList<>();
        Cursor c = getApplicationContext().getContentResolver().query(CP.CONTENT_URI, null, null, null, null);
        if(c.moveToFirst()){
            do{
                resturanter.add(new Resturant(c.getLong(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)));
            }while (c.moveToNext());
            c.close();
        }
        state = "Resturant";

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_activated_1, resturanter);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ShowDataActivity.class);
                intent.putExtra("Data_class", "Resturant");
                intent.putExtra("Data", resturanter.get(i).encode());
                startActivityForResult(intent, 501);


            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                id = resturanter.get(i).getId();
                AlertDialog a = new AlertDialog(resource.getString(R.string.slett_resturant), resource.getString(R.string.slett_resturant_description), resource.getString(R.string.slett), resource.getString(R.string.avbrytt));
                a.show(getSupportFragmentManager(), resource.getString(R.string.slett_resturant));



                return true;
            }
        });

        btn_registrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.putExtra("Data_class", "Resturant");
                startActivityForResult(intent, 504);
            }
        });



    }

    public void getKontakter(View v){
        List<Kontakt> kontakter = db.hentAlleKontakter();
        state = "Kontakt";

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_activated_1, kontakter);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ShowDataActivity.class);
                intent.putExtra("Data_class", "Kontakt");
                intent.putExtra("Data", kontakter.get(i).encode());
                startActivityForResult(intent, 500);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                id = kontakter.get(i).getId();
                AlertDialog a = new AlertDialog(resource.getString(R.string.slett_kontakt), resource.getString(R.string.slett_kontakt_description), resource.getString(R.string.slett), resource.getString(R.string.avbrytt));
                a.show(getSupportFragmentManager(), resource.getString(R.string.slett_kontakt));
                return true;
            }
        });

        btn_registrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.putExtra("Data_class", "Kontakt");
                startActivityForResult(intent, 503);
            }
        });

    }


    public void getBesøk(View v){
        List<Besøk> besøk = db.hentAlleBesøk();
        List<String> lesbart = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for(Besøk b : besøk){
            if(b.getDate().get(Calendar.YEAR) < calendar.get(Calendar.YEAR)){
                db.slettBesøk(b.getId());
            }else if(b.getDate().get(Calendar.YEAR) == calendar.get(Calendar.YEAR) && b.getDate().get(Calendar.MONTH) < calendar.get(Calendar.MONTH)){
                db.slettBesøk(b.getId());
            }else if(b.getDate().get(Calendar.YEAR) == calendar.get(Calendar.YEAR) && b.getDate().get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                    b.getDate().get(Calendar.DAY_OF_MONTH) < calendar.get(Calendar.DAY_OF_MONTH)){
                db.slettBesøk(b.getId());
            }

            SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String se = s.format(b.getDate().getTime());
            try {
                Cursor c = getApplicationContext().getContentResolver().query(Uri.parse(CP.CONTENT_URI + "/" + b.getResturantID()), null, null, null, null);
                c.moveToFirst();
                lesbart.add(c.getString(1) + ", " + s.format(b.getDate().getTime()));
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), resource.getString(R.string.restaurant_mangler), Toast.LENGTH_SHORT).show();
                db.slettBesøk(b.getId());
            }
        }
        state = "Besøk";

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_activated_1, lesbart);
        //bør connecte to tabeller for resturant navnet
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ShowDataActivity.class);
                intent.putExtra("Data_class", "Besøk");
                intent.putExtra("Data", besøk.get(i).encode());
                startActivityForResult(intent, 502);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                id = besøk.get(i).getId();
                AlertDialog a = new AlertDialog(resource.getString(R.string.slett_bestilling), resource.getString(R.string.slett), resource.getString(R.string.avbrytt));
                a.show(getSupportFragmentManager(), resource.getString(R.string.slett_bestilling));
                return true;
            }
        });

        btn_registrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.putExtra("Data_class", "Besøk");
                startActivityForResult(intent, 505);
            }
        });
    }

    public void visPreferences(View v){
        Intent i = new Intent(this ,PreferenceActivity.class);
        startActivity(i);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 500){
            if(resultCode == RESULT_OK) {
                getKontakter(null);
            }
        }
        if(requestCode == 501){
            if(resultCode == RESULT_OK) {
                getResturanter(null);
            }
        }
        if(requestCode == 502){
            if(resultCode == RESULT_OK) {
                getBesøk(null);
            }
        }

        if(requestCode == 503){
            if(resultCode == RESULT_OK) {
                getKontakter(null);
            }
        }
        if(requestCode == 504){
            if(resultCode == RESULT_OK) {
                getResturanter(null);
            }
        }
        if(requestCode == 505){
            if(resultCode == RESULT_OK) {
                getBesøk(null);
            }
        }
    }

    @Override
    public void onYesClick() {
        if(state.equals("Kontakt")){
            db.slettKontakt(id);
            getKontakter(null);
        }else if(state.equals("Resturant")){
            Cursor c  = getApplicationContext().getContentResolver().query(Uri.parse(CP.CONTENT_URI+"/"+id), null, null, null, null);
            c.moveToFirst();
            db.slettBesokerMedFjernetResturant(id);
            Resturant r = new Resturant(c.getLong(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));

            getApplicationContext().getContentResolver().delete(Uri.parse(CP.CONTENT_URI+"/"+id), null, null);
            getResturanter(null);
        }else if(state.equals("Besøk")){
            db.slettBesøk(id);
            getBesøk(null);
        }
    }

    @Override
    public void onNoClick() {
        return;
    }

    public void createNotificationChannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "Bestilling";
            String desc = "Planlagt bestilling";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(desc);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(!(requestCode == 0 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("sendMeldinger", false).apply();
        }
    }
}
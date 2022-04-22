package com.example.s341843;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.s341843.settings.Preferanser;
import com.example.s341843.statistics.Stat;
import com.example.s341843.statistics.Statistikk;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnstartspill;
    private ImageButton btnstatistikk;
    private ImageButton btnpreferanser;


    private SharedPreferences shared;

    //private Statistikk statistikk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shared = PreferenceManager.getDefaultSharedPreferences(this);
        Preferanser.byttSpråk(this, shared.getString("Landskode", "NO"));
        setContentView(R.layout.activity_main);
        btnstartspill = (ImageButton) findViewById(R.id.btnstartspill);
        btnpreferanser = (ImageButton) findViewById(R.id.btnpreferanse);
        btnstatistikk = (ImageButton) findViewById(R.id.btnstatistikk);


        //statistikk = new Statistikk();

        btnstartspill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("Limit", Integer.parseInt(shared.getString("Limit", "5")));
                startActivityForResult(intent, 500);
            }
        });

        btnpreferanser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivityForResult(intent, 502);
            }
        });

        btnstatistikk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), StatisticsActivity.class);

                intent2.putExtra("Statistikk", getSharedPreferences("Statistikk", MODE_PRIVATE).getString("Statistikk", ""));
                startActivityForResult(intent2, 501);
            }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 500){
            if(resultCode == RESULT_OK){
                Statistikk statistikk = new Statistikk();
                statistikk.decode(getSharedPreferences("Statistikk", MODE_PRIVATE).getString("Statistikk", ""));
                statistikk.add(new Stat(data.getIntExtra("Riktige", 0), data.getIntExtra("Feile", 0)));
                getSharedPreferences("Statistikk", MODE_PRIVATE).edit().putString("Statistikk" , statistikk.toString()).apply();
            }
        }else if(requestCode == 501){
            if(resultCode == RESULT_OK){
                getSharedPreferences("Statistikk", MODE_PRIVATE).edit().putString("Statistikk" , data.getStringExtra("Statistikk")).apply();
            }
        }else if(requestCode == 502){
            if(resultCode == RESULT_OK){
                TextView title = (TextView) findViewById(R.id.app_title);
                title.setText(getResources().getString(R.string.app_name));

            }
        }
    }
}
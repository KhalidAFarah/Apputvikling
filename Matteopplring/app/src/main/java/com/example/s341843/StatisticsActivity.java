package com.example.s341843;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.s341843.settings.Preferanser;
import com.example.s341843.statistics.Dialog;
import com.example.s341843.statistics.Statistikk;

public class StatisticsActivity extends AppCompatActivity implements Dialog.DialogClickListener {

    TextView counter_riktig;
    TextView counter_feil;
    TextView percentage_info;

    ImageButton søppelkasse;

    ProgressBar percentage;
    Statistikk statistikk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Preferanser.byttSpråk(this, sharedPreferences.getString("Landskode", "NO"));
        setContentView(R.layout.activity_statistics);



        statistikk = new Statistikk();
        statistikk.decode(getSharedPreferences("Statistikk", MODE_PRIVATE).getString("Statistikk", ""));

        counter_riktig = (TextView) findViewById(R.id.counter_riktig);
        counter_feil = (TextView) findViewById(R.id.counter_feil);
        percentage_info = (TextView) findViewById(R.id.percentage_info);
        percentage = (ProgressBar) findViewById(R.id.percentage);
        søppelkasse = (ImageButton) findViewById(R.id.btn_can);

        int prosent = statistikk.getTotalProsent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            percentage.setProgress(prosent, true);
        }else{
            percentage.setProgress(prosent);
        }

        if (statistikk.isEmpty()){
            percentage_info.setText(getResources().getString(R.string.data_mangel));
        }else{
            percentage_info.setText(prosent + "% " + getResources().getString(R.string.statistikk_riktig).toLowerCase());
        }


        counter_riktig.setText(String.valueOf(statistikk.getTotalRiktig()));
        counter_feil.setText(String.valueOf(statistikk.getTotalFeil()));

        søppelkasse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment df = new Dialog();
                df.show(getSupportFragmentManager(), getResources().getString(R.string.tøm_tittel));
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("Statistikk", statistikk.toString());
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onYesClick() {
        statistikk.empty();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            percentage.setProgress(0, true);
        }else{
            percentage.setProgress(0);
        }
        counter_riktig.setText("0");
        counter_feil.setText("0");

        Log.d("sd", "----"+statistikk.toString());


        getSharedPreferences("Statistikk", MODE_PRIVATE).edit().putString("Statistikk", "").apply();
        percentage_info.setText(getResources().getString(R.string.data_mangel));
    }

    @Override
    public void onNoClick() {
        return;
    }
}
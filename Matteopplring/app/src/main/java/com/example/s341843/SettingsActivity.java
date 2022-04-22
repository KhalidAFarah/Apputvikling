package com.example.s341843;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.s341843.settings.PreferanseFragment;
import com.example.s341843.settings.Preferanser;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences shared;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        shared = PreferenceManager.getDefaultSharedPreferences(this);
        Preferanser.byttSpråk(this , shared.getString("Landskode", "NO"));

        getSupportFragmentManager().beginTransaction().replace(R.id.preferanse_container, new PreferanseFragment()).commit();


    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}
package com.example.dialogprefs;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PreferencesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        getSupportFragmentManager().beginTransaction().replace(R.id.settings_container, new Preferansefragment()).commit();
    }
}

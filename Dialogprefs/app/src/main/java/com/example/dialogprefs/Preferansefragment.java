package com.example.dialogprefs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.*;
import android.preference.PreferenceFragment;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

public class Preferansefragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener,Preference.OnPreferenceChangeListener{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String  rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        load();
        Log.d("SETTINGS", "-----");
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d("SETTINGS", "onSharedPreferenceChanged");
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        Log.d("SETTINGS", "onSharedPreferenceChanged");

        return false;
    }

    @Override
    public void onResume() {
        load();
        super.onResume();
    }

    public void load(){

    }


}

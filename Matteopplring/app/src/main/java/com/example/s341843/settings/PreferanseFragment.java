package com.example.s341843.settings;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.s341843.R;

import java.util.Locale;

public class PreferanseFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener{

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }
    @Override
    public boolean onPreferenceChange(Preference preference, Object o){
        return false;
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);


        CheckBoxPreference cNO = (CheckBoxPreference) findPreference("Landskode_NO");
        CheckBoxPreference cDE = (CheckBoxPreference) findPreference("Landskode_DE");




        CheckBoxPreference cLimit_5 = (CheckBoxPreference) findPreference("Limit_5");
        CheckBoxPreference cLimit_10 = (CheckBoxPreference) findPreference("Limit_10");
        CheckBoxPreference cLimit_15 = (CheckBoxPreference) findPreference("Limit_15");

        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getContext() );

        String val = shared.getString("Landskode", "NO");
        if(val.equals("NO")){
            cNO.setChecked(true);
            cDE.setChecked(false);
        }else if(val.equals("DE")){
            cNO.setChecked(false);
            cDE.setChecked(true);
        }else {
            shared.edit().putString("Landskode","NO").apply();
        }


        cNO.setOnPreferenceChangeListener(new android.support.v7.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.support.v7.preference.Preference preference, Object o) {
                cDE.setChecked(false);
                shared.edit().putString("Landskode","NO").apply();
                Preferanser.byttSpråk(getContext() , "NO");
                getActivity().recreate();
                return true;
            }
        });


        cDE.setOnPreferenceChangeListener(new android.support.v7.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.support.v7.preference.Preference preference, Object o) {
                cNO.setChecked(false);
                shared.edit().putString("Landskode","DE").apply();
                Preferanser.byttSpråk(getContext() , "DE");
                getActivity().recreate();
                return true;
            }
        });


        val = shared.getString("Limit", "5");
        if(val.equals("5")){
            cLimit_5.setChecked(true);
            cLimit_10.setChecked(false);
            cLimit_15.setChecked(false);
        }else if(val.equals("10")){
            cLimit_5.setChecked(false);
            cLimit_10.setChecked(true);
            cLimit_15.setChecked(false);
        }else if(val.equals("15")){
            cLimit_5.setChecked(false);
            cLimit_10.setChecked(false);
            cLimit_15.setChecked(true);
        }else{
            shared.edit().putString("Limit","5").apply();
        }

        cLimit_5.setOnPreferenceChangeListener(new android.support.v7.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.support.v7.preference.Preference preference, Object o) {
                cLimit_10.setChecked(false);
                cLimit_15.setChecked(false);

                shared.edit().putString("Limit","5").apply();

                return true;
            }
        });
        cLimit_10.setOnPreferenceChangeListener(new android.support.v7.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.support.v7.preference.Preference preference, Object o) {
                cLimit_5.setChecked(false);
                cLimit_15.setChecked(false);

                shared.edit().putString("Limit","10").apply();

                return true;
            }
        });
        cLimit_15.setOnPreferenceChangeListener(new android.support.v7.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.support.v7.preference.Preference preference, Object o) {
                cLimit_5.setChecked(false);
                cLimit_10.setChecked(false);

                shared.edit().putString("Limit","15").apply();

                return true;
            }
        });
    }



}

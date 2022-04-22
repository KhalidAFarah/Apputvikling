package com.oslomet.s341843;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;


public class Preferansefragment extends PreferenceFragmentCompat{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String  rootKey) {
        addPreferencesFromResource(R.xml.preferanse);

        SwitchPreference msg = (SwitchPreference) findPreference("sendMeldinger");
        //EditTextPreference tid = (EditTextPreference) findPreference("klokkeslett");
        EditTextPreference mld = (EditTextPreference) findPreference("meldingen");
        SharedPreferences data = PreferenceManager.getDefaultSharedPreferences(getContext());


        msg.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if(!msg.isChecked()){
                    if(!(ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)){
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 0);
                    }
                }
                return true;
            }
        });


        //tid.setSummary(data.getString("klokkeslett", "21:00"));
        //mld.setSummary(data.getString("meldingen", getContext().getResources().getString(R.string.default_msg)));
        mld.setSummary(getContext().getResources().getString(R.string.teksten_med));

        //tid.setFragment(null);

    }

    public void change(){
        ((SwitchPreference) findPreference("sendMeldinger")).setChecked(false);
    }
}

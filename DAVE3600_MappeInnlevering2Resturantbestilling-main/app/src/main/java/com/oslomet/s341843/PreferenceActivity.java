package com.oslomet.s341843;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class PreferenceActivity extends AppCompatActivity {

    private Resources resources;
    private Preferansefragment preferansefragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        resources = getResources();
        preferansefragment = new Preferansefragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.pref_container, preferansefragment).commit();
        SharedPreferences data = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        EditText txttid = (EditText) findViewById(R.id.velg_klokkeslett);
        txttid.setHint(resources.getString(R.string.preferanse_tid) + " " + data.getString("Tid", "21:00"));
        Calendar calendar = Calendar.getInstance();
        txttid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i1, int i2) {
                        calendar.set(Calendar.HOUR_OF_DAY, i1);
                        calendar.set(Calendar.MINUTE, i2);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        data.edit().putString("Tid", simpleDateFormat.format(calendar.getTime())).apply();
                        txttid.setText(resources.getString(R.string.preferanse_tid) + " " + simpleDateFormat.format(calendar.getTime()));

                        Intent i = new Intent(getApplicationContext(), MinService.class);
                        PendingIntent pi = PendingIntent.getService(getApplicationContext(), 0, i, 0);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(getApplicationContext().ALARM_SERVICE);
                        if(alarmManager != null) alarmManager.cancel(pi);


                        Intent utenBoot = new Intent();
                        utenBoot.setAction("com.oslomet.s341843.StartUtenBoot");
                        getApplicationContext().sendBroadcast(utenBoot);
                    }
                };
                new TimePickerDialog(PreferenceActivity.this, listener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(!(requestCode == 0 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("sendMeldinger", false).apply();
            preferansefragment.change();
        }
    }
}
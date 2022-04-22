package com.oslomet.s341843;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.oslomet.s341843.database.Besøk;
import com.oslomet.s341843.database.DBHandler;

import java.util.Calendar;

public class PeriodiskService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        //Toast.makeText(getApplicationContext(), "er i PeriodiskService", Toast.LENGTH_SHORT).show();

        String[] tid = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Tid", "21:00").split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tid[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(tid[1]));
        Intent i = new Intent(getApplicationContext(), MinService.class);



        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000, pi);//for  hvert minutt 60*1000 og 24*60*60*1000 for hver dag



        return super.onStartCommand(intent, flags, startId);
    }
}

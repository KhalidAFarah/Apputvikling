package com.oslomet.s341843;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.oslomet.s341843.database.Besøk;
import com.oslomet.s341843.database.DBHandler;

import java.util.Calendar;

public class MinBroadcastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            //Toast.makeText(context, "er i BroadcastReciever", Toast.LENGTH_SHORT).show();
            Intent ii = new Intent(context, PeriodiskService.class);
            context.startService(ii);
        //}
    }

}

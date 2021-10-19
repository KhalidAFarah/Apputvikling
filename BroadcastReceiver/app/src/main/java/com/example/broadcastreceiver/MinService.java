package com.example.broadcastreceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class MinService extends Service {
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*Toast.makeText(getApplicationContext(), "I MinService", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);*/

        Toast.makeText(getApplicationContext(), "  I MinService", Toast.LENGTH_SHORT).show();
        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent i = new Intent(this, Resultat.class);
        PendingIntent pIntent= PendingIntent.getActivity(this, 0, i, 0);
        Notification notifikasjon = new NotificationCompat.Builder(this,"22")
                .setContentTitle("Not if y").setContentText("Lorem Ipsum")
                .setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pIntent).build();
        notifikasjon.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notifikasjon);
        return super.onStartCommand(intent, flags, startId);
    }
}
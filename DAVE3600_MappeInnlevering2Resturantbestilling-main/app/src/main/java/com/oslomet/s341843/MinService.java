package com.oslomet.s341843;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.oslomet.s341843.database.Besøk;
import com.oslomet.s341843.database.DBHandler;
import com.oslomet.s341843.database.Kontakt;
import com.oslomet.s341843.database.Resturant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MinService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences data = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean val = data.getBoolean("sendMeldinger", true);
        //Toast.makeText(getApplicationContext(), "Er i MinService", Toast.LENGTH_SHORT).show();

            DBHandler db = new DBHandler(getApplicationContext());
            Calendar calendar = Calendar.getInstance();
            //Toast.makeText(getApplicationContext(), "Notifikasjoner er slått på", Toast.LENGTH_SHORT).show();



            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String day = simpleDateFormat.format(calendar.getTime()).split(" ")[0];


            List<Besøk> besøkIDag = new ArrayList<>();
            //Toast.makeText(getApplicationContext(), "-"+day, Toast.LENGTH_SHORT).show();
            for (Besøk b : db.hentAlleBesøk()) {
                String date = simpleDateFormat.format(b.getDate().getTime()).split(" ")[0];
                //Toast.makeText(getApplicationContext(), date, Toast.LENGTH_SHORT).show();
                if (date.equals(day)) {
                    besøkIDag.add(b);
                }
            }

            //Notifikasjons Manager
            if (besøkIDag.size() > 0) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Intent i = new Intent(this, MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
                Notification notification = new NotificationCompat.Builder(this, "1")
                        .setContentTitle(getResources().getString(R.string.notifikasjon_tittel))
                        .setContentText(getResources().getString(R.string.notifikasjon_tekst))
                        .setSmallIcon(R.drawable.notification_icon)
                        .setColor(getResources().getColor(R.color.blue))
                        //.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.icon_launcher_round))
                        .setContentIntent(pi).build();
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(0, notification);
            }

            //SMS Manager
        if(val) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

                SmsManager smsManager = SmsManager.getDefault();
                //Toast.makeText(getApplicationContext(), "MESSAGE SENT"+, Toast.LENGTH_SHORT).show();


                for (Besøk b : besøkIDag) {
                    List<Kontakt> deltakere = db.getDeltakere(b.getId());
                    //Resturant resturant = db.hentResturant(b.getResturantID());
                    //Toast.makeText(getApplicationContext(), "MESSAGE SENT", Toast.LENGTH_SHORT).show();
                    Cursor c  = getApplicationContext().getContentResolver().query(Uri.parse(CP.CONTENT_URI+"/"+b.getResturantID()), null, null, null, null);
                    c.moveToFirst();
                    Resturant r = new Resturant(c.getLong(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));


                    for (Kontakt d : deltakere) {
                        //sender meldinger
                        String mld = data.getString("meldingen", getResources().getString(R.string.default_msg))
                                 +"\n" + getResources().getString(R.string.melding_navn) +"\n"+ c.getString(1)
                                + "\n\n" + getResources().getString(R.string.melding_adresse) +"\n"+ c.getString(2)
                                + "\n\n" + getResources().getString(R.string.melding_dato_tid) +"\n"+ simpleDateFormat.format(b.getDate().getTime()).split(" ")[1];

                        smsManager.sendTextMessage(d.getTlf(), null, mld, null, null);
                        //Toast.makeText(getApplicationContext(), "MESSAGE SENT", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

}

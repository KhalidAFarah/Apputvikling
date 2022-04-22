package com.example.grad_eksempel;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

class Savedata{
    private String setting;
    private float value;

    public void setSetting(String setting) {
        this.setting = setting;
    }
    public void setValue(float value) {
        this.value = value;
    }

    public String getSetting() {
        return setting;
    }
    public float getValue() {
        return value;
    }
}

public class MainActivity extends AppCompatActivity {
    EditText text;
    NotificationCompat.Builder not;
    View bar;
    String filename;

    final String CHANNEL_ID = "Wat";
    final String CHANNEL_NAME = "A channel";
    final int IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT;

    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text =  (EditText) findViewById(R.id.temp);
        final TextView info = (TextView) findViewById(R.id.tempinfo);
        final Button btngrader = (Button) findViewById(R.id.Tilgrader);
        final Button btnfahrenheit = (Button) findViewById(R.id.Tilfahrenheit);
        bar = (View) findViewById(R.id.underbar);

        notificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        createNotificationChannel();

        info.setVisibility(View.INVISIBLE);
        text.setVisibility(View.INVISIBLE);

        filename = "Grad_eksempel_state";

        Savedata data = loadfile();
        Log.d("Grad", "(savedata) setting: " + data.getSetting() + " value: " + data.getValue());

        if(data != null){
            if(data.getSetting().equals("grader")){
                text.setVisibility(View.VISIBLE);
                info.setVisibility(View.VISIBLE);

                btngrader.setText(R.string.grader);
                btnfahrenheit.setText(R.string.fahrenheit);

                if(data.getValue() != 0.0)
                    text.setText(String.valueOf(data.getValue()));

                btngrader.setClickable(false);
                bar.setVisibility(View.VISIBLE);
            }else if(data.getSetting().equals("fahrenheit")){
                text.setVisibility(View.VISIBLE);
                info.setVisibility(View.VISIBLE);

                btngrader.setText(R.string.grader);
                btnfahrenheit.setText(R.string.fahrenheit);

                if(data.getValue() != 0.0)
                    text.setText(String.valueOf(data.getValue()));

                btnfahrenheit.setClickable(false);
                bar.setVisibility(View.VISIBLE);
                bar.setX(500);
            }
        }
        btngrader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text.getVisibility() == View.INVISIBLE){
                    text.setVisibility(View.VISIBLE);
                    info.setVisibility(View.VISIBLE);

                    btngrader.setText(R.string.grader);
                    btnfahrenheit.setText(R.string.fahrenheit);

                    btngrader.setClickable(false);

                    bar.setVisibility(View.VISIBLE);

                    savefile("grader");
                    createNotification("Du valgte grader");
                }else {
                    float temp;
                    try{
                        temp = Float.parseFloat(text.getText().toString());
                        temp = ((temp - 32) * 5 / 9);

                        text.setText(String.valueOf(temp));
                        Log.d("Grad eksempel", "converterte til grader");

                        btnfahrenheit.setClickable(true);
                        btngrader.setClickable(false);

                        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(bar, "x", 0);
                        objectAnimator.setDuration(500);
                        objectAnimator.start();

                        savefile("grader;"+text.getText());
                        createNotification("Du valgte grader");
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), R.string.Error, Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });



        btnfahrenheit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text.getVisibility() == View.INVISIBLE){
                    text.setVisibility(View.VISIBLE);
                    info.setVisibility(View.VISIBLE);

                    btngrader.setText(R.string.grader);
                    btnfahrenheit.setText(R.string.fahrenheit);

                    btnfahrenheit.setClickable(false);
                    bar.setVisibility(View.VISIBLE);
                    bar.setX(view.getWidth());

                    savefile("fahrenheit");
                    createNotification("Du valgte fahrenheit");
                }else {
                    try{
                        float temp = Float.parseFloat(text.getText().toString());
                        temp = ((temp * 9) / 5) + 32;

                        text.setText(String.valueOf(temp));
                        Log.d("Grad eksempel", "converterte til fahrenheit");

                        btnfahrenheit.setClickable(false);
                        btngrader.setClickable(true);

                        //animation
                        AnimatorSet animation = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.slide);
                        View bar = (View) findViewById(R.id.underbar);
                        animation.setTarget(bar);
                        animation.start();

                        //ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(bar, "x", view.getWidth());
                        //objectAnimator.setDuration(500);
                        //objectAnimator.start();

                        savefile("fahrenheit;"+text.getText());
                        createNotification("Du valgte fahrenheit");

                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), R.string.Error, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void savefile(String content){
        try(FileOutputStream fos = getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE)){
            fos.write(content.getBytes());
        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Unable to find save file!", Toast.LENGTH_SHORT);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Unable to save!", Toast.LENGTH_SHORT);
        }
    }

    private Savedata loadfile(){
        //String content = "";
        Savedata savedata = new Savedata();
        try (FileInputStream fis = getApplicationContext().openFileInput(filename)) {
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);

            try (BufferedReader reader = new BufferedReader(inputStreamReader)){
                String line = reader.readLine();
                if(line != null){
                    String[] split = line.split(";");
                    if(split.length > 1) {
                        savedata.setSetting(split[0]);
                        try {
                            savedata.setValue(Float.parseFloat(split[1]));
                        }catch (Exception e){
                            savedata.setValue(0);
                        }
                    }else{
                        savedata.setSetting(split[0]);
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }finally {
            return savedata;
        }


    }

    public void createNotification(String content){
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        notification.setSmallIcon(R.mipmap.te);
        notification.setContentTitle("Fra grad eksempel");
        notification.setContentText(content);
        notification.setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    public void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
            notificationChannel.setDescription("I don't know how this works");
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }
}
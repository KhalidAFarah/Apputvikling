package com.example.dialogprefs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements MyDialog.DialogClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onYesClick() {
        finish();
    }

    @Override
    public void onNoClick() {
        return;
    }

    public void visDialog(View v){
        DialogFragment dialogFragment = new MyDialog();
        dialogFragment.show(getSupportFragmentManager(),"Avslutt");
    }

    public void visPreferences(View v){
        Intent i = new Intent(this, PreferencesActivity.class);
        startActivity(i);
    }
}
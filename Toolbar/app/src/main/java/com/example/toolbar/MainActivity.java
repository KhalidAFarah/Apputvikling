package com.example.toolbar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("TAG", "Er i onCreate");

        toolbar = (Toolbar) findViewById(R.id.min_meny);
        toolbar.inflateMenu(R.menu.menu_hoved);
        setActionBar(toolbar);


    }

    @Override
    protected void onStart() {
        Log.d("TAG", "Er i OnStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d("TAG", "Er i onResume");
        super.onResume();

        EditText et = (EditText) findViewById(R.id.hoved_editText);
        et.setText(getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("Hovedtekst",""));
    }

    @Override
    protected void onPause() {
        Log.d("TAG", "Er i onPause");
        super.onPause();

        EditText et = (EditText)findViewById(R.id.hoved_editText);
        String tekst = et.getText().toString();
        getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("Hovedtekst",tekst).apply();
    }

    @Override
    protected void onStop() {
        Log.d("TAG", "Er i onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("TAG", "Er i onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Log.d("TAG", "Er i onRestart");
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_hoved, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aktivitet2:
                Intent i = new Intent(this, Aktivitet2.class);
                EditText et = (EditText) findViewById(R.id.hoved_editText);
                i.putExtra("FRAHOVED", et.getText().toString());
                startActivity(i);
                break;
            case R.id.aktivitet3:
                Intent i2 = new Intent(this, Aktivitet3.class);
                startActivityForResult(i2, 555);
                break;
            default:// If we got here, the user's action was not recognized.
                // Invoke the super class to handle it.
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 555){
            if(resultCode == RESULT_OK){
                EditText et = (EditText) findViewById(R.id.hoved_editText);
                et.setText(data.getStringExtra("FRAAKTIVITET3"));
            }if(resultCode == RESULT_CANCELED){

            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("TAG", "er i save");
        EditText et = (EditText) findViewById(R.id.hoved_editText);
        outState.putString("SAVE_HOVEDTEKST", et.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("TAG", "er i restore");
        EditText et = (EditText) findViewById(R.id.hoved_editText);
        et.setText(savedInstanceState.getString("SAVE_HOVEDTEKST"));
    }
}
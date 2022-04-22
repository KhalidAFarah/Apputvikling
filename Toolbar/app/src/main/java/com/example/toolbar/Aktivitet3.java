package com.example.toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toolbar;

public class Aktivitet3 extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktivitet3);

        toolbar = (Toolbar) findViewById(R.id.min_meny);
        toolbar.inflateMenu(R.menu.menu_akt_3);
        setActionBar(toolbar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_akt_3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.hovedaktivitet:
                EditText et = (EditText) findViewById(R.id.akt3_editText);
                Bundle bundle = new Bundle();
                bundle.putString("FRAAKTIVITET3", et.getText().toString());
                Intent i = new Intent(this, MainActivity.class);
                i.putExtras(bundle);
                setResult(RESULT_OK, i);
                finish();

                break;
            case R.id.aktivitet2:
                Intent i2 = new Intent(this, Aktivitet2.class);
                startActivity(i2);
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("TAG", "er i save");
        EditText et = (EditText) findViewById(R.id.akt3_editText);
        outState.putString("SAVE_AKTIVITET3", et.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("TAG", "er i restore");
        EditText et = (EditText) findViewById(R.id.akt3_editText);
        Log.d("SAVE", savedInstanceState.getString("SAVE_AKTIVITET3"));
        et.setText(savedInstanceState.getString("SAVE_AKTIVITET3"));
    }
}

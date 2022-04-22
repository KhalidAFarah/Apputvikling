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
import android.widget.TextView;
import android.widget.Toolbar;

public class Aktivitet2 extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktivitet2);

        toolbar = (Toolbar) findViewById(R.id.min_meny);
        toolbar.inflateMenu(R.menu.menu_akt_2);
        setActionBar(toolbar);

        TextView text = (TextView) findViewById(R.id.akt2_textView);
        text.setText(getIntent().getStringExtra("FRAHOVED"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_akt_2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.hovedaktivitet:
                finish();
                break;
            case R.id.aktivitet3:
                Intent i = new Intent(this, Aktivitet3.class);
                startActivity(i);
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
        EditText et = (EditText) findViewById(R.id.akt2_editText);
        outState.putString("SAVE_AKTIVITET2", et.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("TAG", "er i restore");
        EditText et = (EditText) findViewById(R.id.akt2_editText);
        et.setText(savedInstanceState.getString("SAVE_AKTIVITET2"));
    }
}

package com.example.testavbokcp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public final static String PROVIDER="com.example.contentproviderbok";
    public static final Uri CONTENT_URI= Uri.parse("content://"+ PROVIDER + "/bok");
    public static final String TITTEL="Tittel";
    public static final String ID="_id";
    private EditText tittel;
    private TextView visbok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tittel=(EditText)findViewById(R.id.tittel);
        Button leggtil=(Button)findViewById(R.id.leggtil);
        Button visalle=(Button)findViewById(R.id.visalle);
        visbok=(TextView)findViewById(R.id.vis);

    }

    public void leggtil(View v){
        ContentValues values= new ContentValues();
        String inn=tittel.getText().toString();
        values.put(TITTEL, inn);
        Uri uri= getContentResolver().insert( CONTENT_URI, values);
        tittel.setText("");
    }

    public void visalle(View v) {
        String tekst = "";
        Cursor cur = getContentResolver().query(CONTENT_URI, null, null, null, null); //Uri.parse(CONTENT_URI+"/3") for bok med id 3
        if (cur.moveToFirst()) {
            do {
                tekst = tekst + (cur.getString(1)) + "\r\n";
            } while (cur.moveToNext());
            cur.close();
            visbok.setText(tekst);
        }
    }
}
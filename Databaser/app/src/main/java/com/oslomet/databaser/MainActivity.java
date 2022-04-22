package com.oslomet.databaser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText navninn;
    EditText telefoninn;
    EditText idinn;
    TextView utskrift;
    DBHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navninn= (EditText) findViewById(R.id.navn);
        telefoninn= (EditText) findViewById(R.id.telefon);
        idinn= (EditText) findViewById(R.id.min_id);
        utskrift = (TextView) findViewById(R.id.utskrift);
        db = new DBHandler(this);
    }
    public void leggtil(View v) {
        if(!navninn.getText().toString().equals("") || !telefoninn.getText().toString().equals("")) {
            Kontakt kontakt = new Kontakt(navninn.getText().toString(), telefoninn.getText().toString());
            db.leggTilKontakt(kontakt);
            Log.d("Legg inn: ", "legger til kontakter");
        }else{
            Toast.makeText(this, "vennligst fyll inn nødvdendig informasjon", Toast.LENGTH_SHORT);
        }
    }
    public void visalle(View v) {
        String tekst = "";
        List<Kontakt> kontakter = db.finnAlleKontakter();
        for (Kontakt kontakt: kontakter) {
            tekst = tekst + "Id: " + kontakt.getID() +
                    ", Navn: " +kontakt.getNavn() +
                    ", Telefon: " +kontakt.getTlf();

            Log.d("Navn: ", tekst);
        }
        utskrift.setText(tekst);
    }
    public void slett(View v) {
        if(!idinn.getText().toString().equals("")) {
            Long kontaktid = (Long.parseLong(idinn.getText().toString()));
            db.slettKontakt(kontaktid);
            Log.d("Slett", " slettet Id: " + idinn.getText().toString());
        }else{
            Toast.makeText(this, "vennligst fyll inn nødvdendig informasjon", Toast.LENGTH_SHORT);
        }
    }
    public void oppdater(View v) {
        if(!navninn.getText().toString().equals("") || !telefoninn.getText().toString().equals("")
                || !idinn.getText().toString().equals("")) {
            Kontakt kontakt = new Kontakt();
            kontakt.setNavn(navninn.getText().toString());
            kontakt.setTlf(telefoninn.getText().toString());
            kontakt.setID(Long.parseLong(idinn.getText().toString()));
            db.oppdaterKontakt(kontakt);
        }else{
            Toast.makeText(this, "vennligst fyll inn nødvdendig informasjon", Toast.LENGTH_SHORT);
        }
    }
}
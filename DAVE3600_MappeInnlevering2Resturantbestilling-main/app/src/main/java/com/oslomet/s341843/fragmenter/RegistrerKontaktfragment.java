package com.oslomet.s341843.fragmenter;

import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.oslomet.s341843.CP;
import com.oslomet.s341843.R;
import com.oslomet.s341843.database.DBHandler;
import com.oslomet.s341843.database.Kontakt;
import com.oslomet.s341843.database.Resturant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegistrerKontaktfragment extends Fragment {

    DBHandler db;
    boolean isAllFieldsChecked = false;
    EditText registrer_fornavn, registrer_etternavn, registrer_tlf;
    Button btn_registrer_kontakt;
    private Resources resources;
    public RegistrerKontaktfragment(DBHandler db){
        this.db = db;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.registrerkontakt, container, false);
        resources = getResources();

        registrer_fornavn = (EditText) v.findViewById(R.id.registrer_fornavn);
        registrer_etternavn = (EditText) v.findViewById(R.id.registrer_etternavn);
        registrer_tlf = (EditText) v.findViewById(R.id.registrer_tlf);

        //btn_registrer_kontakt = (Button) v.findViewById(R.id.btn_registrer_kontakt);
        /*btn_registrer_kontakt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Kontakt nyKontakt = new Kontakt();
                //Fornavn og ettnavn til en navn
                nyKontakt.setName(registrer_fornavn.getText().toString()+" "+registrer_etternavn.getText().toString());
                //Tlf
                nyKontakt.setTlf(registrer_tlf.getText().toString());


                isAllFieldsChecked = CheckAllFields();
                if(isAllFieldsChecked && CheckPhone(nyKontakt)){
                    Log.d("Checked","alt er riktig fungerer");
                    db.registrerKontakter(nyKontakt);
                    Toast.makeText(getContext(),"Kontakt er registrert",Toast.LENGTH_SHORT).show();
                    //Refresh edittextview
                    registrer_fornavn.setText("");
                    registrer_etternavn.setText("");
                    registrer_tlf.setText("");
                }
            }*/



        return v;
    }
    private boolean CheckAllFields() {
        boolean valid = true;
        if (registrer_fornavn.length() == 0) {
            registrer_fornavn.setError(resources.getString(R.string.skriv_fornavn));
            valid = false;
        }

        if (registrer_etternavn.length() == 0) {
            registrer_etternavn.setError(resources.getString(R.string.skriv_etternavn));
            valid = false;
        }

        if (registrer_tlf.length() == 0) {
            registrer_tlf.setError(resources.getString(R.string.skriv_tlf));
            valid = false;
        }

        // after all validation return true.
        return valid;
    }

    private boolean CheckPhone(Kontakt nyKontakt){
        for(Kontakt k : db.hentAlleKontakter()){
            if (k.getTlf().equals(nyKontakt.getTlf())) {
                registrer_tlf.setError(resources.getString(R.string.err_kontakt_tlf_opptatt));
                return false;
                //Log.d("Finnes allerede en med samme tlfnr i databasen",k.getTlf());
            }
        }
        Cursor c = getContext().getContentResolver().query(CP.CONTENT_URI, null, null, null, null);
        if(c.moveToFirst()){
            do{
                if(nyKontakt.getTlf().equals(c.getString(3))) {
                    registrer_tlf.setError(resources.getString(R.string.err_kontakt_tlf_opptatt));
                    return false;
                    //Log.d("Finnes allerede en med samme tlfnr i databasen",k.getTlf());
                }

            }while (c.moveToNext());
            c.close();
        }
        return true;
    }


    public void registrer(){
        Kontakt nyKontakt = new Kontakt();
        //Fornavn og ettnavn til en navn
        nyKontakt.setName(registrer_fornavn.getText().toString()+" "+registrer_etternavn.getText().toString());
        //Tlf
        nyKontakt.setTlf(registrer_tlf.getText().toString());


        isAllFieldsChecked = CheckAllFields();
        if(isAllFieldsChecked && CheckPhone(nyKontakt)){
            db.registrerKontakter(nyKontakt);
            Toast.makeText(getContext(),resources.getString(R.string.kontakten_registrert),Toast.LENGTH_SHORT).show();
            //Refresh edittextview
            registrer_fornavn.setText("");
            registrer_etternavn.setText("");
            registrer_tlf.setText("");
        }
    }
}
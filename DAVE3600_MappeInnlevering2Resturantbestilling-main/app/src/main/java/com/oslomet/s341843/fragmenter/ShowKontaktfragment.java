package com.oslomet.s341843.fragmenter;

import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
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
import com.oslomet.s341843.database.Kontakt;
import com.oslomet.s341843.database.Resturant;

import java.util.ArrayList;
import java.util.List;

public class ShowKontaktfragment extends Fragment {

    private Kontakt kontakt;
    private boolean edit;
    private Resources resources;
    private DBHandler db;

    EditText txt_fornavn, txt_etternavn, txt_tlf;

    public ShowKontaktfragment(Kontakt kontakt, DBHandler db){
        this.kontakt = kontakt;
        this.edit = false;
        this.db = db;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.viskontakt, container, false);
        resources = getResources();

        txt_fornavn = (EditText) v.findViewById(R.id.txt_edit_kontakt_fornavn);
        txt_fornavn.setText(((Kontakt) kontakt).getName().split(" ")[0]);

        txt_etternavn = (EditText) v.findViewById(R.id.txt_edit_kontakt_etternavn);
        try {
            txt_etternavn.setText(((Kontakt) kontakt).getName().split(" ")[1]);
        }catch (Exception e){}
        txt_tlf = (EditText) v.findViewById(R.id.txt_edit_kontakt_tlf);
        txt_tlf.setText(((Kontakt) kontakt).getTlf());






        return v;
    }

    public void rediger(View v){
        /*edit = true;
        btn.setText("Lagre");

        ((EditText) v.findViewById(R.id.txt_edit_kontakt_fornavn)).setFocusableInTouchMode(true);
        ((EditText) v.findViewById(R.id.txt_edit_kontakt_etternavn)).setFocusableInTouchMode(true);
        ((EditText) v.findViewById(R.id.txt_edit_kontakt_tlf)).setFocusableInTouchMode(true);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lagre(v);
            }
        });*/
    }
    public void lagre(View v){
        /*btn.setText("Rediger");
        edit = false;

        EditText e = ((EditText) v.findViewById(R.id.txt_edit_kontakt_fornavn));
        e.setFocusable(false);
        String name = e.getText().toString();
        e  = ((EditText) v.findViewById(R.id.txt_edit_kontakt_etternavn));
        e.setFocusable(false);
        kontakt.setName(name + " " + e.getText().toString());
        e = ((EditText) v.findViewById(R.id.txt_edit_kontakt_tlf));
        e.setFocusable(false);
        kontakt.setTlf(e.getText().toString());


        db.oppdaterKontakt(kontakt);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rediger(v);
            }
        });*/
    }

    public boolean getEndrer(){return edit;}

    public boolean checkAllFields(){
        boolean valid = true;
        if(txt_fornavn.length() == 0){
            valid = false;
            txt_fornavn.setError(resources.getString(R.string.tom_felt));
        }
        if(txt_etternavn.length() == 0){
            valid = false;
            txt_etternavn.setError(resources.getString(R.string.tom_felt));
        }
        if(txt_tlf.length() == 0){
            valid = false;
            txt_tlf.setError(resources.getString(R.string.tom_felt_tlf));
        }
        return valid;
    }

    public boolean checkPhone(){
        for(Kontakt k : db.hentAlleKontakter()){
            if (k.getId() != kontakt.getId() && k.getTlf().equals(kontakt.getTlf())) {
                txt_tlf.setError(resources.getString(R.string.err_kontakt_tlf_opptatt));
                return false;
                //Log.d("Finnes allerede en med samme tlfnr i databasen",k.getTlf());
            }
        }
        Cursor c = getContext().getContentResolver().query(CP.CONTENT_URI, null, null, null, null);
        if(c.moveToFirst()){
            do{
                if(kontakt.getTlf().equals(c.getString(3))) {
                    txt_tlf.setError(resources.getString(R.string.err_kontakt_tlf_opptatt));
                    return false;
                    //Log.d("Finnes allerede en med samme tlfnr i databasen",k.getTlf());
                }

            }while (c.moveToNext());
            c.close();
        }
        return true;
    }

    public void save(){
        kontakt.setName(txt_fornavn.getText().toString() + " " + txt_etternavn.getText().toString());
        kontakt.setTlf(txt_tlf.getText().toString());

        if(checkAllFields() && checkPhone()){
            db.oppdaterKontakt(kontakt);
            Toast.makeText(getContext(),resources.getString(R.string.toast_lagret_endringer),Toast.LENGTH_SHORT).show();
        }



    }
}

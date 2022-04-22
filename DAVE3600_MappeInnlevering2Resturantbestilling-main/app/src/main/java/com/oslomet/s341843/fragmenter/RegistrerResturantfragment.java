package com.oslomet.s341843.fragmenter;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class RegistrerResturantfragment extends Fragment {

    DBHandler db;
    EditText edt_resturant_navn, edt_resturant_adresse, edt_resturant_tlf;
    Spinner spinner;
    Button btn_registrer_resturant;
    private Resources resources;
    public RegistrerResturantfragment(DBHandler db){
        this.db = db;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.registrerresturant, container, false);
        spinner = (Spinner) v.findViewById(R.id.planets_spinner);
        edt_resturant_navn = (EditText) v.findViewById(R.id.edt_resturant_navn);
        edt_resturant_adresse = (EditText) v.findViewById(R.id.edt_resturant_adresse);
        edt_resturant_tlf = (EditText) v.findViewById(R.id.edt_resturant_tlf);
        resources = getResources();
        //edt_resturant_type = (EditText) v.findViewById(R.id.edt_resturant_type);

        List<String> s  = new ArrayList<>();
        for (String str : getResources().getStringArray(R.array.planets_array)){
            s.add(str);

        }

        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.drop_items,s);
        spinner.setAdapter(adapter);

        spinner.setSelection(0);



        /*btn_registrer_resturant = (Button) v.findViewById(R.id.btn_registrer_resturant);
        //skal jobbe videre
        btn_registrer_resturant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Spinner selected:",spinner.getSelectedItem().toString());
                Resturant resturant = new Resturant();
                /*resturant.setName(edt_resturant_navn.getText().toString());
                resturant.setAdress(edt_resturant_adresse.getText().toString());
                resturant.setTlf(edt_resturant_tlf.getText().toString());
                resturant.setType(edt_resturant_type.getText().toString());*//*
                resturant.setName(edt_resturant_navn.getText().toString());
                resturant.setAdress(edt_resturant_adresse.getText().toString());
                resturant.setTlf(edt_resturant_tlf.getText().toString());
                resturant.setType(spinner.getSelectedItem().toString());
                //Log.d("INFOO OM ALLE EDITBOXER:",edt_resturant_navn.getText().toString()+" "+edt_resturant_adresse.getText().toString()+" "+edt_resturant_tlf.getText().toString()+" "+edt_resturant_type.getText().toString());
                if(CheckAllFields() && CheckPhone(resturant)){

                    //db.registrerResturant(resturant);
                    ContentValues values = new ContentValues();
                    values.put(CP.RESTURANT_KEY_NAME, resturant.getName());
                    values.put(CP.RESTURANT_KEY_ADRESS, resturant.getAdress());
                    values.put(CP.RESTURANT_KEY_TLF, resturant.getTlf());
                    values.put(CP.RESTURANT_KEY_TYPE, resturant.getType());

                    getContext().getContentResolver().insert(CP.CONTENT_URI, values);

                    //Refresh edittextview
                    edt_resturant_navn.setText("");
                    edt_resturant_adresse.setText("");
                    edt_resturant_tlf.setText("");
                    //edt_resturant_type.setText("");
                    spinner.setSelection(0);
                    Toast.makeText(getContext(),"Resturanten er registrert",Toast.LENGTH_SHORT).show();
                }
            }





        });*/


        return v;
    }

    private boolean CheckAllFields() {
        boolean valid = true;
        if (edt_resturant_navn.length() == 0) {
            edt_resturant_navn.setError(resources.getString(R.string.skriv_navn));
            valid = false;
        }

        if (edt_resturant_adresse.length() == 0) {
            edt_resturant_adresse.setError(resources.getString(R.string.skriv_adresse));
            valid = false;
        }

        if (edt_resturant_tlf.length() == 0) {
            edt_resturant_tlf.setError(resources.getString(R.string.skriv_tlf));
            valid = false;
        }
        if (spinner.getSelectedItem().toString().equals("Velg type")) {
            ((TextView)spinner.getSelectedView()).setError("Error message");
            if(valid){
                valid = false;
                Toast.makeText(getContext(),resources.getString(R.string.err_velg_type),Toast.LENGTH_SHORT).show();
            }

            //spinner.getSelectedView()setError("Skriv inn resturant type");
        }

        // after all validation return true.
        return valid;
    }

    private boolean CheckPhone(Resturant nyResturant){
        for(Kontakt k : db.hentAlleKontakter()){
            if (k.getTlf().equals(nyResturant.getTlf())) {
                edt_resturant_tlf.setError(resources.getString(R.string.err_restaurant_tlf_opptatt));
                return false;
                //Log.d("Finnes allerede en med samme tlfnr i databasen",k.getTlf());
            }
        }
        Cursor c = getContext().getContentResolver().query(CP.CONTENT_URI, null, null, null, null);
        if(c.moveToFirst()){
            do{
                if(nyResturant.getTlf().equals(c.getString(3))) {
                    edt_resturant_tlf.setError(resources.getString(R.string.err_restaurant_tlf_opptatt));
                    return false;
                    //Log.d("Finnes allerede en med samme tlfnr i databasen",k.getTlf());
                }

            }while (c.moveToNext());
            c.close();
        }
        return true;
    }

    public void registrer(){
        Log.d("Spinner selected:",spinner.getSelectedItem().toString());
        Resturant resturant = new Resturant();
        resturant.setName(edt_resturant_navn.getText().toString());
        resturant.setAdress(edt_resturant_adresse.getText().toString());
        resturant.setTlf(edt_resturant_tlf.getText().toString());
        resturant.setType(spinner.getSelectedItem().toString());
        //Log.d("INFOO OM ALLE EDITBOXER:",edt_resturant_navn.getText().toString()+" "+edt_resturant_adresse.getText().toString()+" "+edt_resturant_tlf.getText().toString()+" "+edt_resturant_type.getText().toString());
        if(CheckAllFields() && CheckPhone(resturant)){
            //db.registrerResturant(resturant);
            ContentValues values = new ContentValues();
            values.put(CP.RESTURANT_KEY_NAME, resturant.getName());
            values.put(CP.RESTURANT_KEY_ADRESS, resturant.getAdress());
            values.put(CP.RESTURANT_KEY_TLF, resturant.getTlf());
            values.put(CP.RESTURANT_KEY_TYPE, resturant.getType());

            getContext().getContentResolver().insert(CP.CONTENT_URI, values);

            //Refresh edittextview
            edt_resturant_navn.setText("");
            edt_resturant_adresse.setText("");
            edt_resturant_tlf.setText("");
            //edt_resturant_type.setText("");
            spinner.setSelection(0);
            Toast.makeText(getContext(),resources.getString(R.string.restaurant_registrert),Toast.LENGTH_SHORT).show();
        }

    }

}

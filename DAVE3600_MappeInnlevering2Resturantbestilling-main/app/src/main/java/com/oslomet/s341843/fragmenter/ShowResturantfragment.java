package com.oslomet.s341843.fragmenter;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
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
import java.util.List;

public class ShowResturantfragment extends Fragment {

    private Resturant resturant;
    private boolean edit;
    EditText txt_edit_resturant_navn, txt_edit_resturant_adresse, txt_edit_resturant_tlf;
    boolean isAllFieldsChecked = false;

    private DBHandler db;
    private Spinner spinner;

    private Resources resources;

    public ShowResturantfragment(Resturant resturant, DBHandler db){
        this.resturant = resturant;
        this.edit = false;
        this.db = db;


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.visresturant, container, false);

        resources = getResources();

        txt_edit_resturant_navn = (EditText) v.findViewById(R.id.txt_edit_resturant_navn);
        txt_edit_resturant_adresse = (EditText) v.findViewById(R.id.txt_edit_resturant_adresse);
        txt_edit_resturant_tlf = (EditText) v.findViewById(R.id.txt_edit_resturant_tlf);

        ((EditText) v.findViewById(R.id.txt_edit_resturant_navn)).setText(((Resturant) resturant).getName());
        ((EditText) v.findViewById(R.id.txt_edit_resturant_adresse)).setText(((Resturant) resturant).getAdress());
        ((EditText) v.findViewById(R.id.txt_edit_resturant_tlf)).setText(((Resturant) resturant).getTlf());
        //((EditText) v.findViewById(R.id.txt_edit_resturant_type)).setText(((Resturant) resturant).getType());
        int i = 0;
        spinner = (Spinner) v.findViewById(R.id.txt_edit_resturant_type);
        List<String> s  = new ArrayList<>();
        for (String str : getResources().getStringArray(R.array.planets_array)){
            s.add(str);
            if(str.equals(resturant.getType())){
                i = s.size()-1;
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.drop_items,s);
        spinner.setAdapter(adapter);

        spinner.setSelection(i);
        //spinner.setSelection(0);






        return v;
    }

    public void rediger(View v){
        edit = true;
        /*btn.setText("Lagre");

        ((EditText) v.findViewById(R.id.txt_edit_resturant_navn)).setFocusableInTouchMode(true);
        ((EditText) v.findViewById(R.id.txt_edit_resturant_adresse)).setFocusableInTouchMode(true);
        ((EditText) v.findViewById(R.id.txt_edit_resturant_tlf)).setFocusableInTouchMode(true);
        //spinner = (Spinner) v.findViewById(R.id.txt_edit_resturant_type);
        spinner.setEnabled(true);
        //((EditText) v.findViewById(R.id.txt_edit_resturant_type)).setFocusableInTouchMode(true);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lagre(v);
            }
        });*/
    }
    public void lagre(View v){
        /*isAllFieldsChecked = CheckAllFields();
        EditText edtNavn = ((EditText) v.findViewById(R.id.txt_edit_resturant_navn));
        resturant.setName(edtNavn.getText().toString());

        EditText edtAdresse  = ((EditText) v.findViewById(R.id.txt_edit_resturant_adresse));
        resturant.setAdress(edtAdresse.getText().toString());

        EditText edtTlf = ((EditText) v.findViewById(R.id.txt_edit_resturant_tlf));
        resturant.setTlf(edtTlf.getText().toString());

        Log.d("Spinner",spinner.getSelectedItem().toString());

        resturant.setType(spinner.getSelectedItem().toString());

        if(CheckAllFields() && CheckPhone(resturant)){
            btn.setText("Rediger");
            edit = false;
            Log.d("Navn", txt_edit_resturant_navn.getText().toString()+" adresse"+ txt_edit_resturant_adresse.getText().toString()+"tlf"+ txt_edit_resturant_tlf.getText().toString());
            edtNavn.setFocusable(false);
            edtAdresse.setFocusable(false);
            edtTlf.setFocusable(false);
            spinner.setEnabled(false);


            ContentValues values = new ContentValues();
            values.put(CP.RESTURANT_KEY_NAME, resturant.getName());
            values.put(CP.RESTURANT_KEY_ADRESS, resturant.getAdress());
            values.put(CP.RESTURANT_KEY_TLF, resturant.getTlf());
            values.put(CP.RESTURANT_KEY_TYPE, resturant.getType());

            getContext().getContentResolver().update(Uri.parse(CP.CONTENT_URI+"/"+resturant.getId()),values, null, null);


            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rediger(v);
                }
            });
        }*/

    }

    public boolean getEndrer(){
        return edit;
    }

    private boolean CheckAllFields() {
        boolean valid = true;
        if (txt_edit_resturant_navn.length() == 0) {
            txt_edit_resturant_navn.setError(resources.getString(R.string.tom_felt));
            valid = false;
        }

        if (txt_edit_resturant_adresse.length() == 0) {
            txt_edit_resturant_adresse.setError(resources.getString(R.string.tom_felt));
            valid = false;
        }

        if (txt_edit_resturant_tlf.length() == 0) {
            txt_edit_resturant_tlf.setError(resources.getString(R.string.tom_felt_tlf));
            valid = false;
        }

        if(spinner.getSelectedItem().toString().equals("Velg type")){
            ((TextView)spinner.getSelectedView()).setError("Error message");
            if(valid) {
                Toast.makeText(getContext(), resources.getString(R.string.err_velg_type), Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }

        return valid;
    }

    private boolean CheckPhone(){
        for(Kontakt k : db.hentAlleKontakter()){
            if (k.getTlf().equals(resturant.getTlf())) {
                txt_edit_resturant_tlf.setError(resources.getString(R.string.err_restaurant_tlf_opptatt));
                return false;
                //Log.d("Finnes allerede en med samme tlfnr i databasen",k.getTlf());
            }
        }
        Cursor c = getContext().getContentResolver().query(CP.CONTENT_URI, null, null, null, null);
        if(c.moveToFirst()){
            do{
                if(resturant.getId() != c.getLong(0) && resturant.getTlf().equals(c.getString(3))) {
                    txt_edit_resturant_tlf.setError(resources.getString(R.string.err_restaurant_tlf_opptatt));
                    return false;
                    //Log.d("Finnes allerede en med samme tlfnr i databasen",k.getTlf());
                }

            }while (c.moveToNext());
            c.close();
        }
        return true;
    }

    public void save(){

        resturant.setName(txt_edit_resturant_navn.getText().toString());
        resturant.setAdress(txt_edit_resturant_adresse.getText().toString());
        resturant.setTlf(txt_edit_resturant_tlf.getText().toString());
        resturant.setType(spinner.getSelectedItem().toString());

        if(CheckAllFields() && CheckPhone()) {
            ContentValues values = new ContentValues();
            values.put(CP.RESTURANT_KEY_NAME, resturant.getName());
            values.put(CP.RESTURANT_KEY_ADRESS, resturant.getAdress());
            values.put(CP.RESTURANT_KEY_TLF, resturant.getTlf());
            values.put(CP.RESTURANT_KEY_TYPE, resturant.getType());

            //slett besøker med resturanten som blir slettet og fjern også gamle besøk

            getContext().getContentResolver().update(Uri.parse(CP.CONTENT_URI + "/" + resturant.getId()), values, null, null);
            Toast.makeText(getContext(),resources.getString(R.string.toast_lagret_endringer),Toast.LENGTH_SHORT).show();
        }
    }

}

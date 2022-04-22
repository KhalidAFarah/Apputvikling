package com.oslomet.s341843.fragmenter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.oslomet.s341843.Hus;
import com.oslomet.s341843.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class RedigerDialogfragment extends DialogFragment {
    private TextView rediger_adresse;
    private EditText rediger_etasjer, rediger_beskrivelse;
    private Hus hus;
    private Button btn_rediger;

    public RedigerDialogfragment(Hus hus){
        this.hus = hus;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.rediger, container, false);

        rediger_adresse = (TextView) v.findViewById(R.id.rediger_adresse);
        rediger_etasjer = (EditText) v.findViewById(R.id.rediger_etasjer);
        rediger_beskrivelse = (EditText) v.findViewById(R.id.rediger_beskrivelse);

        if(hus == null){
            dismiss();
        }
        rediger_adresse.setText(hus.getAdresse());
        rediger_etasjer.setText(String.valueOf(hus.getEtasjer()));
        rediger_beskrivelse.setText(hus.getBeskrivelse());


        btn_rediger = (Button) v.findViewById(R.id.btn_rediger);
        btn_rediger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_rediger.setEnabled(false);
                lagre();
            }
        });

        return v;
    }

    public boolean valid(){
        boolean valid = true;
        if(rediger_adresse.length() == 0){
            valid = false;
            rediger_adresse.setError(getResources().getString(R.string.fyll_felt));
        }
        if(rediger_etasjer.length() == 0){
            valid = false;
            rediger_etasjer.setError(getResources().getString(R.string.fyll_felt));
        }else if(Integer.parseInt(rediger_etasjer.getText().toString()) <= 0){
            valid = false;
            rediger_etasjer.setError(getResources().getString(R.string.negativt_tall));
        }
        if(rediger_beskrivelse.length() == 0){
            valid = false;
            rediger_beskrivelse.setError(getResources().getString(R.string.fyll_felt));
        }

        if(!valid) btn_rediger.setEnabled(true);
        return valid;
    }

    public void lagre(){
        if(valid()){
            //lagre endringer
            UpdateHusJSON task = new UpdateHusJSON();

            try {
                task.execute(new String[]{"http://studdata.cs.oslomet.no/~dbuser8/oppdaterhus.php/"
                        +"?Id="+String.valueOf(hus.getId())
                        + "&Beskrivelse="+URLEncoder.encode(rediger_beskrivelse.getText().toString(), "UTF-8")//rediger_beskrivelse.getText().toString().replaceAll(" ", "%20").replaceAll("\n", "%0D%0A")
                        + "&Etasjer="+rediger_etasjer.getText()});
            } catch (UnsupportedEncodingException e) {
                rediger_beskrivelse.setError(getResources().getString(R.string.ugyldig_karakterer));
            }


        }
    }

    class UpdateHusJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            try{
                URL url = new URL(strings[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept", "application/json");

                if(connection.getResponseCode() != 200){
                    return getResources().getString(R.string.error_oppdater_registrerte_hus);

                }else{
                    hus.setAdresse(rediger_adresse.getText().toString());
                    hus.setEtasjer(Integer.parseInt(rediger_etasjer.getText().toString()));
                    hus.setBeskrivelse(rediger_beskrivelse.getText().toString());

                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    String out = "";
                    while((line = br.readLine()) != null){
                        out = line;
                    }
                    if(out.equals("null")){
                        return getResources().getString(R.string.error_registrering_server);
                    }


                }
                connection.disconnect();
            }catch (IOException e){
                return getResources().getString(R.string.error_oppdater_registrerte_hus);
            }
            return getResources().getString(R.string.registrert_hus_oppdatert);
        }

        @Override
        protected void onPostExecute(String message) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            if(message.equals(getResources().getString(R.string.registrert_hus_oppdatert))) {
                dismiss();
            }
            btn_rediger.setEnabled(true);
        }
    }
}

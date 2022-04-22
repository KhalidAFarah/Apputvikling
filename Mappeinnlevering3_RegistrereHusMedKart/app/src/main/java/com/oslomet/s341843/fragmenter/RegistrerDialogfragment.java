package com.oslomet.s341843.fragmenter;

import android.content.DialogInterface;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.oslomet.s341843.Hus;
import com.oslomet.s341843.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class RegistrerDialogfragment extends DialogFragment {

    private Button btn_registrer;
    private TextView registrer_adresse;
    private EditText registrer_etasjer, registrer_beskrivelse;
    private Hus hus;

    private LatLng latLng;
    private String adresse;

    private Marker marker;
    private List<Hus> huser;

    public RegistrerDialogfragment(LatLng latLng, String adresse, Marker marker, List<Hus> huser){
        this.latLng = latLng;
        this.adresse = adresse;
        this.marker = marker;
        this.huser = huser;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.registrer, container, false);

        registrer_adresse = (TextView) v.findViewById(R.id.registrer_adresse);
        registrer_etasjer = (EditText) v.findViewById(R.id.registrer_etasjer);
        registrer_beskrivelse = (EditText) v.findViewById(R.id.registrer_beskrivelse);
        btn_registrer = (Button) v.findViewById(R.id.btn_registrer);

        btn_registrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_registrer.setEnabled(false);
                registrer();
            }
        });


        registrer_adresse.setText(adresse);


        return v;
    }

    public boolean valid(){
        boolean valid = true;
        if(registrer_adresse.length() == 0){
            valid = false;
            registrer_adresse.setError(getResources().getString(R.string.fyll_felt));
        }
        if(registrer_etasjer.length() == 0){
            valid = false;
            registrer_etasjer.setError(getResources().getString(R.string.fyll_felt));
        }else if(Integer.parseInt(registrer_etasjer.getText().toString()) <= 0){
            valid = false;
            registrer_etasjer.setError(getResources().getString(R.string.negativt_tall));
        }
        if(registrer_beskrivelse.length() == 0){
            valid = false;
            registrer_beskrivelse.setError(getResources().getString(R.string.fyll_felt));
        }
        if(!valid) btn_registrer.setEnabled(true);
        return valid;
    }

    public void registrer(){
        if(valid()){
            //registrer hus

            PostJSON task = new PostJSON();
            String gateadresse;
            String beskrivelse;
            try {
                gateadresse = URLEncoder.encode(registrer_adresse.getText().toString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                gateadresse = "";
                Toast.makeText(getContext(),getResources().getString(R.string.ugyldig_karakterer_adresse), Toast.LENGTH_SHORT).show();
            }
            try {
                beskrivelse = URLEncoder.encode(registrer_beskrivelse.getText().toString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                beskrivelse = "";
                registrer_beskrivelse.setError(getResources().getString(R.string.ugyldig_karakterer));
            }

            if(!gateadresse.equals("") && !beskrivelse.equals("")){

                task.execute(new String[]{"http://studdata.cs.oslomet.no/~dbuser8/registrerhus.php/"
                        +"?Adresse="+gateadresse
                        + "&Latitude="+latLng.latitude
                        + "&Longitude="+latLng.longitude
                        + "&Beskrivelse="+beskrivelse
                        + "&Etasjer="+registrer_etasjer.getText()});
            }


        }
    }

    class PostJSON extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... link) {
            try {
                URL url = new URL(link[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept", "application/json");

                if(connection.getResponseCode() != 200){
                    return getResources().getString(R.string.error_registrering_server);
                }else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    String out = "";
                    while((line = br.readLine()) != null){
                        out = line;
                    }
                    if(out.equals("null")){
                        return getResources().getString(R.string.error_registrering_server);
                    }
                    JSONObject json = new JSONObject(out);


                    hus = new Hus();

                    //nytt hus objekt lages
                    hus.setId(json.getLong("id"));
                    hus.setAdresse(registrer_adresse.getText().toString());
                    hus.setLatLng(latLng);
                    hus.setEtasjer(Integer.parseInt(registrer_etasjer.getText().toString()));
                    hus.setBeskrivelse(registrer_beskrivelse.getText().toString());
                }
                connection.disconnect();
            }catch (IOException e){
                return getResources().getString(R.string.error_registrering_server);
            } catch (JSONException e) {
                return getResources().getString(R.string.error_fra_server);
            }


            return getResources().getString(R.string.hus_regisrert);
        }

        @Override
        protected void onPostExecute(String msg) {
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            if(hus != null){
                huser.add(hus);
                marker.setSnippet(String.valueOf(hus.getId()));
                dismiss();
            }
            btn_registrer.setEnabled(true);
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        if(hus == null) marker.remove();
    }
}
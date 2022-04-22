package com.oslomet.s341843;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oslomet.s341843.databinding.ActivityMapsBinding;
import com.oslomet.s341843.fragmenter.AlertDialog;
import com.oslomet.s341843.fragmenter.RedigerDialogfragment;
import com.oslomet.s341843.fragmenter.RegistrerDialogfragment;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        AlertDialog.DialogClickListener{

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    //private GoogleApiClient apiManager;
    private List<Marker> positions;
    private Marker deletable_marker;
    private List<Hus> huser;
    private LatLng p;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        positions = new ArrayList<>();
        huser = new ArrayList<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        /*apiManager = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();*/

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.googlemapsstyle));



        //legger til markere ved onClick
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try{
                    List <Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if(addresses != null && addresses.size() > 0){
                        Address address = addresses.get(0);
                        if(address != null && address.getThoroughfare() != null && address.getSubThoroughfare() != null){
                            MarkerOptions m = new MarkerOptions();
                            m.position(latLng);
                            m.draggable(true);
                            //gateadressen
                            RegistrerDialogfragment df = new RegistrerDialogfragment(latLng,
                                    address.getThoroughfare()+" "+address.getSubThoroughfare(),
                                    mMap.addMarker(m), huser);
                            df.show(getSupportFragmentManager().beginTransaction(), "registrering");
                        }else{
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.ugyldig_omrade), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.ugyldig_omrade), Toast.LENGTH_SHORT).show();
                    }
                }catch (IOException e){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.ugyldig_omrade), Toast.LENGTH_SHORT).show();
                }

            }
        });

        //for marker longclick siden det ikke er innebygd
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(@NonNull Marker marker) {
                marker.setPosition(p);
            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                marker.setPosition(p);
            }

            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {
                for(int i = 0; i < huser.size(); i++){
                    if(String.valueOf(huser.get(i).getId()).equals(marker.getSnippet())) {
                        p = huser.get(i).getLatLng();
                    }
                }

                AlertDialog al = new AlertDialog(getResources().getString(R.string.slett),
                        getResources().getString(R.string.slett_hus_beskrivelse),
                        getResources().getString(R.string.slett),
                        getResources().getString(R.string.avbryt));
                al.show(getSupportFragmentManager(), getResources().getString(R.string.slett));
                deletable_marker = marker;

                marker.setPosition(p);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                showBottomDialog(marker);

                return true;
            }
        });


        //lager en marker og zoomer inn mot pilestredet
        LatLng pilestredet = new LatLng(59.92381142152463, 10.731466554866053);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pilestredet, 17));

        GetHusJSON task = new GetHusJSON();
        task.execute(new String[]{"http://studdata.cs.oslomet.no/~dbuser8/listuthus.php"});
    }

    private void placeMarker(long id, LatLng marker){

        MarkerOptions m = new MarkerOptions();
        m.position(marker);

        m.snippet(Long.toString(id));
        m.draggable(true);
        positions.add(mMap.addMarker(m));

    }
    /*private boolean isGPSOn(){
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        //apiManager.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //if(apiManager.isConnected())
        //    apiManager.disconnect();
    }





    //AlertDialog
    @Override
    public void onYesClick() {
        if(dialog != null)
            dialog.cancel();

        DeleteHusJSON task = new DeleteHusJSON(deletable_marker);
        task.execute(new String[]{"http://studdata.cs.oslomet.no/~dbuser8/deletehus.php/"
                + "?Id="+deletable_marker.getSnippet()});

    }

    @Override
    public void onNoClick() {
        return;
    }

    //BottomDialog
    public void showBottomDialog(Marker marker){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomdialog);
        Hus hus = null;
        for(int i = 0; i < huser.size(); i++) {
            if (marker.getSnippet() != null && huser.get(i).getId() == Integer.parseInt(marker.getSnippet())) {
                hus = huser.get(i);
                break;
            }
        }
        if(hus == null) {
            dialog.cancel();
            return;
        }

        //asksesserer Views inne i bottomdialog fragmentet
        ((TextView) dialog.findViewById(R.id.markerAdresse)).setText(hus.getAdresse());
        ((TextView) dialog.findViewById(R.id.markerEtasjer)).setText(
                getResources().getString(R.string.marker_etasjer)
                        + " " +String.valueOf(hus.getEtasjer()));
        ((TextView) dialog.findViewById(R.id.markerBeskrivelse)).setText(hus.getBeskrivelse());

        Hus finalHus = hus;
        ((ImageButton) dialog.findViewById(R.id.markerEdit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

                RedigerDialogfragment df = new RedigerDialogfragment(finalHus);
                df.show(getSupportFragmentManager().beginTransaction(), "Rediger");
            }
        });
        ((ImageButton) dialog.findViewById(R.id.markerDelete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletable_marker = marker;
                AlertDialog al = new AlertDialog(getResources().getString(R.string.slett),
                        getResources().getString(R.string.slett_hus_beskrivelse),
                        getResources().getString(R.string.slett),
                        getResources().getString(R.string.avbryt));
                al.show(getSupportFragmentManager(), getResources().getString(R.string.slett));


            }
        });

        //viser bottomdialog
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.BottomDialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    //henter huser
    class GetHusJSON extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String s = "";
            String out = "";
            if(strings.length > 0){
                try{
                    URL url = new URL(strings[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept", "application/json");

                    if(connection.getResponseCode() != 200){
                        return getResources().getString(R.string.error_hente_registrerte_hus);
                    }else {
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        while ((s = br.readLine()) != null) {
                            out += s;
                        }
                        connection.disconnect();
                        try {
                            if(!out.equals("null")) {
                                JSONArray array = new JSONArray(out);
                                for (int i = 0; i < array.length(); i++) {
                                    double latitude, longitude;
                                    int etasjer;
                                    long id;

                                    try{
                                        id = Long.parseLong(array.getJSONObject(i).getString("id"));
                                    }catch (Exception e){
                                        continue;
                                    }

                                    try{
                                        latitude = Float.parseFloat(array.getJSONObject(i).getString("latitude"));
                                    }catch (Exception e){
                                        continue;
                                    }

                                    try{
                                        longitude = Float.parseFloat(array.getJSONObject(i).getString("longitude"));
                                    }catch (Exception e){
                                        continue;
                                    }

                                    try{
                                        etasjer = Integer.parseInt(array.getJSONObject(i).getString("etasjer"));
                                    }catch (Exception e){
                                        continue;
                                    }

                                    huser.add(new Hus(
                                            id,
                                            array.getJSONObject(i).getString("adresse"),
                                            latitude,
                                            longitude,
                                            etasjer,
                                            array.getJSONObject(i).getString("beskrivelse")
                                    ));

                                }
                            }


                        } catch (Exception e) {
                            return getResources().getString(R.string.error_hente_registrerte_hus);
                        }
                    }
                }catch (IOException e){
                    return getResources().getString(R.string.error_hente_registrerte_hus);
                }

            }


            return "";
        }

        @Override
        protected void onPostExecute(String msg) {

            if(msg.equals("")){
                for(Hus hus : huser){
                    placeMarker(hus.getId(), hus.getLatLng());

                }
            }else{
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

        }
    }

    //sletter et hus
    class DeleteHusJSON extends AsyncTask<String, Void, String> {
        private Marker marker;

        public DeleteHusJSON(Marker marker){
            this.marker = marker;
        }
        @Override
        protected String doInBackground(String... strings) {
            if(strings.length > 0){
                try{
                    URL url = new URL(strings[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Accept", "application/json");

                    if(connection.getResponseCode() != 200){
                        return getResources().getString(R.string.error_slette_registrerte_hus);
                    }else {

                    }connection.disconnect();
                }catch (IOException e){
                    return getResources().getString(R.string.error_slette_registrerte_hus);
                }

            }


            return  getResources().getString(R.string.registrert_hus_slettet);
        }

        @Override
        protected void onPostExecute(String msg) {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            if(msg.equals(getResources().getString(R.string.registrert_hus_slettet))){
                for(int i = 0; i < huser.size(); i++){
                    if(huser.get(i).getId() == Long.parseLong(marker.getSnippet())){
                        huser.remove(i);
                        break;
                    }
                }

                marker.remove();
            }


        }
    }

}
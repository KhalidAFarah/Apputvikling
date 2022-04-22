package com.oslomet.s341843;

import com.google.android.gms.maps.model.LatLng;

public class Hus {
    private long id;
    private String adresse;
    private LatLng latLng;
    private int etasjer;
    private String beskrivelse;
    private static final String DELIMETER = ";";

    public Hus(){}
    public Hus(long id, String adresse, LatLng latLng, int etasjer, String beskrivelse){
        this.id = id;
        this.adresse = adresse;
        this.latLng = latLng;
        this.etasjer = etasjer;
        this.beskrivelse = beskrivelse;
    }

    public Hus(long id, String adresse, double latitude, double longitude, int etasjer, String beskrivelse){
        this.id = id;
        this.adresse = adresse;
        this.latLng = new LatLng(latitude, longitude);
        this.etasjer = etasjer;
        this.beskrivelse = beskrivelse;
    }

    public Hus(String adresse, LatLng latLng, int etasjer, String beskrivelse){
        this.id = id;
        this.adresse = adresse;
        this.latLng = latLng;
        this.etasjer = etasjer;
        this.beskrivelse = beskrivelse;
    }

    public Hus(String adresse, double latitude, double longitude, int etasjer, String beskrivelse){
        this.id = id;
        this.adresse = adresse;
        this.latLng = new LatLng(latitude, longitude);
        this.etasjer = etasjer;
        this.beskrivelse = beskrivelse;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public LatLng getLatLng() {
        return latLng;
    }
    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public int getEtasjer() {
        return etasjer;
    }
    public void setEtasjer(int etasjer) {
        this.etasjer = etasjer;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }
    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public String toString(){
        return adresse;
    }
    public String encode(){
        return id+DELIMETER+adresse+DELIMETER+latLng.latitude+DELIMETER+latLng.longitude+DELIMETER
                +etasjer+DELIMETER+beskrivelse+DELIMETER;
    }
    public static Hus decode(String encoded){
        String[] array = encoded.split(DELIMETER);
        Hus hus = null;
        if(array.length == 6){
            hus = new Hus();
            LatLng latLng = new LatLng(Double.parseDouble(array[2]), Double.parseDouble(array[3]));

            hus.setId(Long.parseLong(array[0]));
            hus.setAdresse(array[1]);
            hus.setLatLng(latLng);
            hus.setEtasjer(Integer.parseInt(array[4]));
            hus.setBeskrivelse(array[5]);
        }
        return hus;
    }
}

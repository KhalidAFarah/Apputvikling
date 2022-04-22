package com.oslomet.s341843.database;


public class Bestilling {
    private long besøkID;
    private long kontaktID;

    public Bestilling(){}
    public Bestilling(long besøkID, long kontaktID){
        this.besøkID = besøkID;
        this.kontaktID = kontaktID;
    }

    public long getBesøkID() {return besøkID;}
    public void setBesøkID(long besøkID) {this.besøkID = besøkID;}

    public long getKontaktID() {return kontaktID;}
    public void setKontaktID(long kontaktID) {this.kontaktID = kontaktID;}


    public String toString(){
        return "KontaktID: " + kontaktID + ", BesøkID: " + besøkID;
    }
}

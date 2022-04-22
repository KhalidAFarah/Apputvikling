package com.oslomet.databaser;

public class Kontakt {
    private Long ID;
    private String navn;
    private String tlf;

    public Kontakt(){

    }
    public Kontakt(String navn, String tlf){
        this.navn = navn;
        this.tlf = tlf;
    }
    public Kontakt(Long ID,String navn, String tlf){
        this.ID = ID;
        this.navn = navn;
        this.tlf = tlf;
    }

    public Long getID() {return ID;}
    public String getNavn() {return navn;}
    public String getTlf() {return tlf;}

    public void setID(Long ID) {this.ID = ID;}
    public void setNavn(String navn) {this.navn = navn;}
    public void setTlf(String tlf) {this.tlf = tlf;}
}

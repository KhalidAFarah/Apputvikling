package com.oslomet.s341843.database;

public class Kontakt {
    private long id;
    private String name;
    private String tlf;

    public Kontakt(){}
    public Kontakt(String name, String tlf){
        this.name = name;
        this.tlf = tlf;
    }
    public Kontakt(long id, String name, String tlf){
        this.id = id;
        this.name = name;
        this.tlf = tlf;
    }

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getTlf() {return tlf;}
    public void setTlf(String tlf) {this.tlf = tlf;}

    public String toString(){
        return name;

    }

    public static Kontakt decode(String str){
        String[] variables = str.split(";");
        return new Kontakt(Integer.parseInt(variables[0]), variables[1], variables[2]);
    }
    public String encode(){
        return id+";"+name+";"+tlf;
    }
}

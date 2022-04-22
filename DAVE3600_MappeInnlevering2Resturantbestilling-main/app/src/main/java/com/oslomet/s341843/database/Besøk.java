package com.oslomet.s341843.database;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Besøk {
    private long id;
    private long resturantID;
    private Calendar date;

    public Besøk(){}
    public Besøk(long resturantID, Calendar date){
        this.resturantID = resturantID;
        this.date = date;
    }
    public Besøk(long id,long resturantID, Calendar date){
        this.id = id;
        this.resturantID = resturantID;
        this.date = date;
    }

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public long getResturantID() {return resturantID;}
    public void setResturantID(long resturantID) {this.resturantID = resturantID;}

    public Calendar getDate() {return date;}
    public void setDate(Calendar date) {this.date = date;}

    public String toString(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        return simpleDateFormat.format(date.getTime());
    }

    public static Besøk decode(String str){
        String[] variables = str.split(";");
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");

        String[] datetime = variables[2].split(" ");


        String[] date = datetime[0].split("-");
        String[] time = datetime[1].split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(date[2]));
        calendar.set(Calendar.MONTH, Integer.parseInt(date[1]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[0]));

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
        return new Besøk(Integer.parseInt(variables[0]), Long.parseLong(variables[1]), calendar);
    }
    public String encode(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        return id+";"+resturantID+";"+simpleDateFormat.format(date.getTime());
    }


}


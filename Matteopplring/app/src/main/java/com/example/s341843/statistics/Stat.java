package com.example.s341843.statistics;

public class Stat {
    private int rikitige;
    private int feile;

    public Stat(int rikitige, int feile){
        this.rikitige = rikitige;
        this.feile = feile;
    }

    public int getRikitige() {
        return rikitige;
    }
    public int getFeile() {
        return feile;
    }

    @Override
    public String toString(){
        return String.valueOf(rikitige) + "," + String.valueOf(feile) + ";";
    }
    public static Stat decode(String str){
        String[] values = str.split(",");
        int r;
        int f;
        try{
            r = Integer.parseInt(values[0]);
        }catch (Exception e){
            return null;
        }

        try{
            f = Integer.parseInt(values[1]);
        }catch (Exception e){
            return null;
        }

        return new Stat(r, f);

    }
}

package com.example.s341843.statistics;


import java.util.ArrayList;

public class Statistikk {
    private ArrayList<Stat> list;

    public Statistikk(){
        list = new ArrayList<>();

    }

    public void add(Stat stat){
        list.add(stat);
    }

    public int getTotalRiktig(){
        int sum = 0;
        for(int i = 0; i < list.size(); i++){
            sum += list.get(i).getRikitige();
        }
        return sum;
    }
    public int getTotalFeil(){
        int sum = 0;
        for(int i = 0; i < list.size(); i++){
            sum += list.get(i).getFeile();
        }
        return sum;
    }

    public int getTotalOldRiktig(){
        int sum = 0;
        for(int i = 0; i < list.size()-1; i++){
            sum += list.get(i).getRikitige();
        }
        return sum;
    }
    public int getTotalOldFeil(){
        int sum = 0;
        for(int i = 0; i < list.size()-1; i++){
            sum += list.get(i).getFeile();
        }
        return sum;
    }

    public int getNewRiktig(){
        return list.size() > 0 ? list.get(list.size()-1).getRikitige() : 0;
    }
    public int getNewFeil(){
        return list.size() > 0 ? list.get(list.size()-1).getFeile() : 0;
    }



    public int getTotalProsent(){
        double sumR = 0;
        double sumF = 0;

        for(int i = 0; i < list.size(); i++){
            sumR += list.get(i).getRikitige();
            sumF += list.get(i).getFeile();
        }

        double prosent = ((sumR/(sumR + sumF)) * 100);
        return (int)prosent;
    }

    @Override
    public String toString(){
        String utskrift = "";
        for(int i = 0; i < list.size(); i++){
            utskrift += list.get(i).toString();
        }
        return utskrift;
    }

    public void decode(String str){
        if(!str.equals("")){

            if(list.size() != 0) list = new ArrayList<>();

            String[] values = str.split(";");

            for(int i = 0; i < values.length; i++){
                if(!values[i].equals("")){
                    list.add(Stat.decode(values[i]));
                }
            }
        }
    }
    public void empty(){
        list.clear();
    }
    public boolean isEmpty(){
        return list.size()==0;
    }

}

package com.example.s341843.game;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s341843.GameActivity;
import com.example.s341843.MainActivity;
import com.example.s341843.R;

import java.util.Arrays;
import java.util.Random;

public class Game {

    private int riktige;
    private int feile;

    private int tot_riktige;
    private int tot_feile;
    private int limit;

    private String[] spørsmål;
    private int[] svar;
    private int[] ids;
    private int[] brukt;
    private int[] user_svar;
    private int teller;
    private int antall;
    private int start;

    private String placholder;
    private String placholder_feedback;

    private boolean finished;


    public Game(String[] spørsmål, int[] svar, int limit, int start){

        riktige = 0;
        feile = 0;

        tot_riktige = 0;
        tot_riktige = 0;

        teller = -1;
        this.limit = limit;
        this.antall = limit;
        this.start = start;

        placholder = "";
        placholder_feedback = "";

        this.spørsmål = spørsmål;
        this.svar = svar;

        this.user_svar = new int[antall];
        this.brukt = new int[15];

        this.finished = false;


    }

    public Game(int rikitige, int feile, int tot_riktige, int tot_feile, int start, String[] spørsmål, int[] svar, int[] user_svar, int[] brukt
            ,  int limit, int antall,  int teller, String placholder, String placholder_feedback, boolean finished){
        this.riktige = rikitige;
        this.feile = feile;

        this.tot_riktige = tot_riktige;
        this.tot_feile = tot_feile;
        this.start = start;

        this.placholder = placholder;
        this.placholder_feedback = placholder_feedback;

        this.limit = limit;
        this.antall = antall;
        this.teller = teller;

        this.spørsmål = spørsmål;
        this.svar = svar;

        this.user_svar = user_svar;
        this.brukt = brukt;

        this.finished = finished;
    }

    public int getRiktige() {
        return riktige;
    }
    public int getFeile() {
        return feile;
    }
    public int getTot_riktige() {
        return tot_riktige;
    }
    public int getTot_feile() {
        return tot_feile;
    }

    public int getLimit() {
        return limit;
    }
    public String getPlacholder() {
        return placholder;
    }
    public String getPlacholder_feedback() {
        return placholder_feedback;
    }
    public int getAntall() {
        return antall;
    }
    public boolean getFinished(){
        return finished;
    }

    public String getSpørsmål(){
        return spørsmål[brukt[teller]];
    }
    public int getSvar(){
        return svar[brukt[teller]];
    }
    public int getPrevSvar(){
        return svar[brukt[teller-1]];
    }

    public int getTeller() {
        return teller;
    }

    public void setRiktige(int riktige) {
        this.riktige = riktige;
    }
    public void setFeile(int feile) {
        this.feile = feile;
    }
    public void setPlacholder(String placholder) {
        this.placholder = placholder;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void incRiktige(){
        this.riktige++;
    }
    public void incFeile(){
        this.feile++;
    }

    public boolean sjekk(int svar){

        user_svar[teller%antall] = svar;
        return this.svar[brukt[teller]] == svar;
    }
    public String nesteSpørsmål(Context context){
        if(limit != 0) {
            teller++;
            limit--;

            if(teller == brukt.length){
                int t = 0;
                for(int i = start; i < teller; i++){
                    brukt[t] = brukt[i];
                    t++;
                }

                teller = t;
                start = 0;

                for(; t < brukt.length; t++){//trengs kanskje ikke
                    brukt[t] = 0;
                }
                Toast.makeText(context, context.getResources().getString(R.string.brukt_full), Toast.LENGTH_SHORT).show();
            }




            Random r = new Random();

            if(teller == start && finished == false){
                brukt[start] = r.nextInt(15);
            }else {
                if(finished) finished = false;
                while (true) {
                    int rand_int = r.nextInt(15);
                    boolean b = true;
                    for (int i = 0; i < teller; i++) {
                        if (brukt[i] == rand_int){
                            //if(placeholder_values != 0)
                            b = false;
                            break;
                        }

                    }
                    if(b){
                        brukt[teller] = rand_int;
                        break;
                    }
                }
            }

            return spørsmål[brukt[teller]];
        }
        tot_riktige += riktige;
        tot_feile += feile;
        return "";
    }

    public void saveGameData(Bundle outState, String placholder, String placholder_feedback) {
        outState.putInt("Riktige", riktige);
        outState.putInt("Feile", feile);
        outState.putInt("Tot_riktige", tot_riktige);
        outState.putInt("Tot_feile", tot_feile);
        outState.putInt("Start", start);
        outState.putStringArray("Spørsmål", spørsmål);
        outState.putIntArray("Svar", svar);
        outState.putIntArray("User_svar", user_svar);
        outState.putIntArray("Brukt", brukt);
        outState.putIntArray("Ids", ids);
        outState.putInt("limit", limit); // unngår å krasje med ekte limiten
        outState.putInt("Antall", antall);
        outState.putInt("Teller", teller);
        outState.putString("Placeholder", placholder);
        outState.putString("Placeholder_feedback", placholder_feedback);
        outState.putBoolean("Finished", finished);
    }
    public static Game loadGameData(Bundle savedInstanceState){
        Game g = new Game(savedInstanceState.getInt("Riktige"),
                savedInstanceState.getInt("Feile"),
                savedInstanceState.getInt("Tot_riktige"),
                savedInstanceState.getInt("Tot_feile"),
                savedInstanceState.getInt("Start"),
                savedInstanceState.getStringArray("Spørsmål"),
                savedInstanceState.getIntArray("Svar"),
                savedInstanceState.getIntArray("User_svar"),
                savedInstanceState.getIntArray("Brukt"),
                savedInstanceState.getInt("limit"), //unngår å krasje med ekte limiten
                savedInstanceState.getInt("Antall"),
                savedInstanceState.getInt("Teller"),
                savedInstanceState.getString("Placeholder"),
                savedInstanceState.getString("Placeholder_feedback"),
                savedInstanceState.getBoolean("Finished"));

        return g;
    }

    public void showResults(Context context, ScrollView scrollView, TextView score){



        scrollView.removeAllViews();
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setHorizontalGravity(View.TEXT_ALIGNMENT_CENTER);

        Resources r = context.getResources();




        String s1 = r.getString(R.string.score).replace(",", String.valueOf(riktige));
        String s2 = s1.replace(".", String.valueOf(antall));

        score.setText(s2);
        score.setTextColor(r.getColor(R.color.black));


        score.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        score.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);



        for(int i = 0; i < antall; i++){
            String s;
            int index = (i+start)%brukt.length;

            if(svar[brukt[index]] == user_svar[i])
                s = spørsmål[brukt[index]] + " = " + user_svar[i] + " ✔";
            else
                s = spørsmål[brukt[index]] + " = " + user_svar[i] + " ❌";
            TextView text = new TextView(context);
            text.setText(s);
            text.setTextColor(r.getColor(R.color.black));


            text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            text.setTextSize(20);

            linearLayout.addView(text);


        }


        scrollView.addView(linearLayout);
    }
    public void reset(Context context){

        riktige = 0;
        feile = 0;
        limit = antall;
        start = teller+1;

        user_svar = new int[antall];

        if(teller == brukt.length-1){
            brukt = new int[15];
            teller = -1;
            Toast.makeText(context, context.getResources().getString(R.string.brukt_full), Toast.LENGTH_SHORT).show();
        }

    }




}

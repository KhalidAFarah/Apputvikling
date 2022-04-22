package com.example.s341843;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.s341843.game.Dialog;
import com.example.s341843.game.Game;
import com.example.s341843.settings.Preferanser;

import java.util.Arrays;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements Dialog.DialogClickListener {
    private TextView svar;
    private TextView text;
    private TextView resultat;

    private TextView teller_riktig;
    private TextView teller_feil;

    private Game game;
    private Resources resource;

    private ProgressBar game_progress;
    private TextView game_progress_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Preferanser.byttSpråk(this, sharedPreferences.getString("Landskode", "NO"));
        setContentView(R.layout.activity_game);



        svar = (TextView) findViewById(R.id.svar);
        text = (TextView) findViewById(R.id.regnestykke);
        resultat = (TextView) findViewById(R.id.resultat);
        teller_riktig = (TextView) findViewById(R.id.teller_riktig);
        teller_feil = (TextView) findViewById(R.id.teller_feil);

        game_progress_text = (TextView) findViewById(R.id.game_progress_text);
        game_progress = (ProgressBar) findViewById(R.id.game_progress);

        resource = getResources();
        String[] spørsmål = resource.getStringArray(R.array.regnestykker);
        int[] spørsmål_svar = resource.getIntArray(R.array.svar);
        int limit = getIntent().getIntExtra("Limit", 5);

        game = new Game(spørsmål, spørsmål_svar, limit, 0);




        setupbtn();
        //nesteSpørsmål();

        start();


    }




    private void setupbtn(){
        //knapper fra 0 - 9
        int[] btns = {R.id.btn_num_0, R.id.btn_num_1, R.id.btn_num_2, R.id.btn_num_3, R.id.btn_num_4
        , R.id.btn_num_5, R.id.btn_num_6, R.id.btn_num_7, R.id.btn_num_8, R.id.btn_num_9};
        for(int i = 0; i < btns.length; i++){
            Button btn = (Button) findViewById(btns[i]);
            final int num = i;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    svar.setText(svar.getText().toString()+btn.getText().toString());
                }
            });
        }

        //slett knappen
        ImageButton btnSlett = (ImageButton) findViewById(R.id.btn_slett);
        btnSlett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!svar.getText().toString().equals("")) {
                    String text = svar.getText().toString();
                    svar.setText(text.substring(0, text.length() - 1));
                }
            }
        });
        //svar knappen
        Button btnSvar = (Button) findViewById(R.id.btn_svar);
        btnSvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!svar.getText().toString().equals("")) {
                    Animation fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                    if (game.sjekk(Integer.parseInt(svar.getText().toString()))) {
                        game.incRiktige();
                        teller_riktig.setText(String.valueOf(game.getRiktige()));

                        if(game.getTeller() >= 0) {
                            resultat.setBackgroundColor(resource.getColor(R.color.green));
                            resultat.startAnimation(fade_in);
                            resultat.setText(resource.getString(R.string.svar_riktig));
                        }

                    } else {
                        game.incFeile();
                        teller_feil.setText(String.valueOf(game.getFeile()));
                        if(game.getTeller() >= 0) {
                            resultat.setBackgroundColor(resource.getColor(R.color.red));
                            resultat.startAnimation(fade_in);
                            resultat.setText(resource.getString(R.string.svar_feil) + " " + game.getSvar());
                        }
                    }

                    nesteSpørsmål();
                }
            }
        });

        //lever inn resultat knapp
        ImageButton btn_end = (ImageButton) findViewById(R.id.btn_end);
        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endgame();

            }
        });
        //start spillet på nytt
        ImageButton btn_replay = (ImageButton) findViewById(R.id.btn_replay);
        btn_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.reset(getApplicationContext());
                teller_feil.setText("0");
                teller_riktig.setText("0");
                svar.setText("");
                resultat.setText("");

                text.setVisibility(View.VISIBLE);
                svar.setVisibility(View.VISIBLE);
                resultat.setVisibility(View.VISIBLE);


                teller_riktig.setVisibility(View.VISIBLE);
                teller_feil.setVisibility(View.VISIBLE);

                game_progress_text.setVisibility(View.VISIBLE);
                game_progress.setVisibility(View.VISIBLE);

                game_progress_text.setText("0/"+game.getAntall());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    game_progress.setProgress(0, true);
                }else{
                    game_progress.setProgress(0);
                }

                ((TextView) findViewById(R.id.teller_riktig_icon)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.teller_feil_icon)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.svar_ekstra)).setVisibility(View.VISIBLE);

                ((GridLayout) findViewById(R.id.gridLayout)).setVisibility(View.VISIBLE);

                ((LinearLayout) findViewById(R.id.result_list)).setVisibility(View.INVISIBLE);
                nesteSpørsmål();
            }
        });

    }

    public void start(){
        String spørsmål = game.nesteSpørsmål(getApplicationContext());

        text.setText(spørsmål);
        svar.setText("");
        game_progress_text.setText("0/"+game.getAntall());
    }

    public void nesteSpørsmål(){
        String spørsmål = game.nesteSpørsmål(getApplicationContext());

        double prosent = ((double)(game.getTeller()%game.getAntall())/(double)game.getAntall())*100;





        game_progress_text.setText((game.getTeller()%game.getAntall())+"/"+game.getAntall());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            game_progress.setProgress((int)prosent, true);
        }else{
            game_progress.setProgress((int)prosent);
        }

        if(!spørsmål.equals("")){
            text.setText(spørsmål);
            svar.setText("");


        }else{
            game.setFinished(true);
            showresults();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        game.saveGameData(outState, svar.getText().toString(), resultat.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        game = Game.loadGameData(savedInstanceState);

        if(!game.getFinished()) {
            teller_riktig.setText(String.valueOf(game.getRiktige()));
            teller_feil.setText(String.valueOf(game.getFeile()));
            text.setText(game.getSpørsmål());
            svar.setText(game.getPlacholder());
            if((game.getTeller()%game.getAntall()) > 0) {
                Animation fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);


                if (game.getPlacholder_feedback().equals(resource.getString(R.string.svar_riktig))) {
                    resultat.setText(game.getPlacholder_feedback());
                    resultat.setBackgroundColor(resource.getColor(R.color.green));
                    resultat.startAnimation(fade_in);
                } else {
                    resultat.setBackgroundColor(resource.getColor(R.color.red));
                    resultat.startAnimation(fade_in);
                    resultat.setText(resource.getString(R.string.svar_feil) + " " + game.getSvar());
                }
            }

            game_progress_text.setText((game.getTeller()%game.getAntall()) + "/" + game.getAntall());
            double prosent = (((double) game.getTeller()%(double) game.getAntall()) / (double) game.getAntall()) * 100;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                game_progress.setProgress((int) prosent, true);
            }else{
                game_progress.setProgress((int) prosent);
            }
        }else{
            showresults();
        }




        if(game.getTeller() > 0) resultat.setText(game.getPlacholder_feedback());




    }

    @Override
    public void onYesClick() {
        if(game != null){
            Log.d("GAME", "--.--"+game.getTot_riktige());
            Log.d("GAME", "--.--"+game.getTot_feile());
            if(game.getTot_riktige() > 0){
                endgame();
            }else{
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        }else{
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }
    @Override
    public void onNoClick() {
        return;
    }

    public void visDialog(){
        DialogFragment df = new Dialog();
        df.show(getSupportFragmentManager(), resource.getString(R.string.avslutt_title));
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(!game.getFinished())
            visDialog();
        else{
            endgame();
        }

    }
    public void endgame(){
        Log.d("GAME", ""+game.getTot_riktige());
        Log.d("GAME", ""+game.getTot_feile());

        Bundle b = new Bundle();
        b.putInt("Riktige", (game.getTot_riktige()));
        b.putInt("Feile", (game.getTot_feile()));

        Intent intent = new Intent();
        intent.putExtras(b);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void showresults(){

        text.setVisibility(View.INVISIBLE);
        svar.setVisibility(View.INVISIBLE); 
        resultat.setVisibility(View.INVISIBLE);
        resultat.setBackgroundColor(resource.getColor(R.color.white));

        teller_riktig.setVisibility(View.INVISIBLE);
        teller_feil.setVisibility(View.INVISIBLE);

        game_progress_text.setVisibility(View.INVISIBLE);
        game_progress.setVisibility(View.INVISIBLE);

        TextView teller_riktig_icon = (TextView) findViewById(R.id.teller_riktig_icon);
        TextView teller_feil_icon = (TextView) findViewById(R.id.teller_feil_icon);
        TextView svar_ekstra = (TextView) findViewById(R.id.svar_ekstra);

        teller_riktig_icon.setVisibility(View.INVISIBLE);
        teller_feil_icon.setVisibility(View.INVISIBLE);
        svar_ekstra.setVisibility(View.INVISIBLE);

        GridLayout grid = (GridLayout) findViewById(R.id.gridLayout);
        grid.setVisibility(View.INVISIBLE);

        ScrollView scrollView = (ScrollView) findViewById(R.id.questions_list);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.result_list);
        linearLayout.setVisibility(View.VISIBLE);

        TextView score_txt = (TextView) findViewById(R.id.score_txt);

        game.showResults(getApplicationContext(), scrollView, score_txt);







    }
}
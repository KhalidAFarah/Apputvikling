package com.oslomet.s341843;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.oslomet.s341843.database.DBHandler;
import com.oslomet.s341843.fragmenter.RegistrerBesøkfragment;
import com.oslomet.s341843.fragmenter.RegistrerKontaktfragment;
import com.oslomet.s341843.fragmenter.RegistrerResturantfragment;
import com.oslomet.s341843.fragmenter.ShowResturantfragment;

public class RegisterActivity extends AppCompatActivity {

    Object side;
    ImageButton velg_gjester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        String classname = getIntent().getStringExtra("Data_class");
        velg_gjester = (ImageButton) findViewById(R.id.velg_gjester);

        if(classname.equals("Kontakt")){

            side = new RegistrerKontaktfragment(new DBHandler(getApplicationContext()));
            getSupportFragmentManager().beginTransaction().replace(R.id.register_frame,
                    (RegistrerKontaktfragment)side).commit();
        }else if(classname.equals("Resturant")){
            side = new RegistrerResturantfragment(new DBHandler(getApplicationContext()));
            getSupportFragmentManager().beginTransaction().replace(R.id.register_frame,
                    (RegistrerResturantfragment)side).commit();
        }else if(classname.equals("Besøk")){
            velg_gjester.setVisibility(View.VISIBLE);
            side = new RegistrerBesøkfragment(new DBHandler(getApplicationContext()), velg_gjester);
            getSupportFragmentManager().beginTransaction().replace(R.id.register_frame,
                    (RegistrerBesøkfragment)side).commit();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        Intent back = new Intent();
        back.putExtra("Data_class", getIntent().getStringExtra("Data_class").toString());
        setResult(RESULT_OK, back);
        finish();
    }

    public void registrer(View v){

        if(side instanceof RegistrerKontaktfragment){
            ((RegistrerKontaktfragment) side).registrer();
        }else if(side instanceof RegistrerResturantfragment){
            ((RegistrerResturantfragment) side).registrer();
        }else if(side instanceof RegistrerBesøkfragment){
            ((RegistrerBesøkfragment) side).registrer();
        }

    }

    public void inviter(View v){
        if(side instanceof RegistrerBesøkfragment){
            ((RegistrerBesøkfragment) side).inviter();
        }
    }
}
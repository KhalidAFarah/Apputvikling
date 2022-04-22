package com.oslomet.s341843;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.oslomet.s341843.database.Besøk;
import com.oslomet.s341843.database.DBHandler;
import com.oslomet.s341843.database.Kontakt;
import com.oslomet.s341843.database.Resturant;
import com.oslomet.s341843.fragmenter.ShowBesøkfragment;
import com.oslomet.s341843.fragmenter.ShowKontaktfragment;
import com.oslomet.s341843.fragmenter.ShowResturantfragment;

public class ShowDataActivity extends AppCompatActivity implements AlertDialog.DialogClickListener{

    private Object obj;
    private LinearLayout displayablelayout;
    private Object side;
    private DBHandler db;
    private boolean iDelete = false;

    private Resources resource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        Toolbar customToolbar = (Toolbar) findViewById(R.id.customToolbar);
        resource = getResources();
        setSupportActionBar(customToolbar);

        String classname = getIntent().getStringExtra("Data_class");
        db = new DBHandler(getApplicationContext());

        if(classname.equals("Kontakt")){
            obj = Kontakt.decode(getIntent().getStringExtra("Data"));
            side = new ShowKontaktfragment((Kontakt) obj, db);
            getSupportFragmentManager().beginTransaction().replace(R.id.dynamicPresentLayout, (ShowKontaktfragment)side).commit();
        }else if(classname.equals("Resturant")){
            obj = Resturant.decode(getIntent().getStringExtra("Data"));
            side = new ShowResturantfragment((Resturant) obj, db);
            getSupportFragmentManager().beginTransaction().replace(R.id.dynamicPresentLayout, (ShowResturantfragment)side).commit();
        }else if(classname.equals("Besøk")){
            obj = Besøk.decode(getIntent().getStringExtra("Data"));
            side = new ShowBesøkfragment((Besøk) obj, db);

            ((ImageButton) findViewById(R.id.edit_save)).setImageResource(R.drawable.invite);
            //((ImageButton) findViewById(R.id.edit_invite)).setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.dynamicPresentLayout, (ShowBesøkfragment)side).commit();
        }


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(side != null){
            if(side instanceof ShowKontaktfragment){
                if(((ShowKontaktfragment)side).getEndrer()){
                    visAlert();
                }else{
                    end();
                }
            }else if(side instanceof ShowResturantfragment){
                if(((ShowResturantfragment) side).getEndrer()){
                    visAlert();
                }else{
                    end();
                }
            }else if(side instanceof ShowBesøkfragment){
                if(((ShowBesøkfragment) side).getEndrer()){
                    visAlert();
                }else{
                    end();
                }
            }
        }else {
            end();
        }
    }

    public void end(){
        Intent back = new Intent();
        back.putExtra("Data_class", getIntent().getStringExtra("Data_class").toString());
        setResult(RESULT_OK, back);
        finish();
    }
    public void visAlert(){
        AlertDialog al = new AlertDialog(resource.getString(R.string.endrer), resource.getString(R.string.forlat), resource.getString(R.string.avbrytt));
        al.show(getSupportFragmentManager(), resource.getString(R.string.endrer));
    }

    @Override
    public void onYesClick() {
        if(iDelete){
            if(obj instanceof Kontakt){
                db.slettKontakt(((Kontakt) obj).getId());
            }else if(obj instanceof Resturant){
                db.slettBesokerMedFjernetResturant(((Resturant) obj).getId());
                getApplicationContext().getContentResolver().delete(Uri.parse(CP.CONTENT_URI+"/"+((Resturant) obj).getId()), null, null);
            }else if(obj instanceof Besøk){
                db.slettBesøk(((Besøk) obj).getId());
            }
        }
        end();
    }

    @Override
    public void onNoClick() {
        if(iDelete) iDelete = false;
        return;
    }

    public void save(View v){
        if(obj instanceof Kontakt){
            ((ShowKontaktfragment) side).save();
        }else if(obj instanceof Resturant){
            ((ShowResturantfragment) side).save();
        }else if(obj instanceof Besøk){
            if(!((ShowBesøkfragment) side).getEndrer()){
                ((ImageButton) findViewById(R.id.edit_save)).setImageResource(R.drawable.save);
                ((ShowBesøkfragment) side).invite();
            }else{
                ((ImageButton) findViewById(R.id.edit_save)).setImageResource(R.drawable.invite);
                ((ShowBesøkfragment) side).save();
            }


        }
    }
    public void delete(View v){
        iDelete = true;
        if(obj instanceof Kontakt){
            AlertDialog a = new AlertDialog(resource.getString(R.string.slett_kontakt), resource.getString(R.string.slett_kontakt_description), resource.getString(R.string.slett), resource.getString(R.string.avbrytt));
            a.show(getSupportFragmentManager(), resource.getString(R.string.slett_kontakt));


        }else if(obj instanceof Resturant){
            AlertDialog a = new AlertDialog(resource.getString(R.string.slett_resturant), resource.getString(R.string.slett_resturant_description), resource.getString(R.string.slett), resource.getString(R.string.avbrytt));
            a.show(getSupportFragmentManager(), resource.getString(R.string.slett_resturant));


        }else if(obj instanceof Besøk){
            AlertDialog a = new AlertDialog(resource.getString(R.string.slett_bestilling), resource.getString(R.string.slett), resource.getString(R.string.avbrytt));
            a.show(getSupportFragmentManager(), resource.getString(R.string.slett_bestilling));

        }

    }
}
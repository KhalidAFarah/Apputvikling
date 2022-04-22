package com.oslomet.s341843.fragmenter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oslomet.s341843.CP;
import com.oslomet.s341843.R;
import com.oslomet.s341843.database.Bestilling;
import com.oslomet.s341843.database.Besøk;
import com.oslomet.s341843.database.DBHandler;
import com.oslomet.s341843.database.Besøk;
import com.oslomet.s341843.database.Kontakt;
import com.oslomet.s341843.database.Resturant;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;



public class ShowBesøkfragment extends Fragment {

    private Besøk besøk;
    private boolean inviterer;
    //private Button btn;
    private DBHandler db;
    private List<Kontakt> kontakter;
    private List<Kontakt> deltakere;
    private MultiAdapter multiadapter;
    View v;

    LinearLayout kontakt_container;

    public ShowBesøkfragment(Besøk besøk, DBHandler db){
        this.besøk = besøk;
        this.inviterer = false;
        this.db = db;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.visbesok, container, false);

        TextView txt = (TextView) v.findViewById(R.id.txt_edit_besøk_resturant);
        Cursor c  = getContext().getContentResolver().query(Uri.parse(CP.CONTENT_URI+"/"+besøk.getResturantID()), null, null, null, null);
        c.moveToFirst();
        Resturant r = new Resturant(c.getLong(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
        txt.setText(r.getName());

        txt = (TextView) v.findViewById(R.id.txt_edit_besøk_adresse);
        txt.setText(r.getAdress());

        TextView datetime = (TextView) v.findViewById(R.id.txt_edit_besøk_date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        datetime.setText(simpleDateFormat.format(besøk.getDate().getTime()));
        kontakt_container = (LinearLayout) v.findViewById(R.id.show_kontakter_container);

        kontakter = db.hentAlleKontakter();
        deltakere = db.getDeltakere(besøk.getId());





        //btn = ((Button) v.findViewById(R.id.btn_legg_til_kontakter));
        RecyclerView l = (RecyclerView) v.findViewById(R.id.visBesøklayout_kontakter);

        multiadapter = new MultiAdapter(getContext(), kontakter, deltakere);
        deltakere();

        l.setLayoutManager(new LinearLayoutManager(getContext()));
        l.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        l.setAdapter(multiadapter);





        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviterer = true;
                ((LinearLayout) v.findViewById(R.id.visBesøklayout)).setVisibility(View.INVISIBLE);
                ((LinearLayout) v.findViewById(R.id.visBesøklayout_inviter)).setVisibility(View.VISIBLE);
            }
        });

        btn = (Button) v.findViewById(R.id.btn_visBesøklayout_avbryt);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviterer = false;

                ((LinearLayout) v.findViewById(R.id.visBesøklayout)).setVisibility(View.VISIBLE);
                ((LinearLayout) v.findViewById(R.id.visBesøklayout_inviter)).setVisibility(View.INVISIBLE);




                db.setDeltakere(besøk.getId() , multiadapter.getSelected());
                deltakere();

            }
        });*/


        return v;
    }

    public void deltakere(){



        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        kontakt_container.removeAllViews();


        if(multiadapter.getSelected().size() != 0) {
            for (int i = 0; i < multiadapter.getSelected().size(); i++) {
                TextView txt = new TextView(getContext());
                txt.setLayoutParams(params);
                txt.setText(multiadapter.getSelected().get(i).getName());
                txt.setTextColor(getResources().getColor(R.color.black));
                txt.setTextSize(20);

                kontakt_container.addView(txt);
            }
        }else{
            TextView txt = new TextView(getContext());
            txt.setLayoutParams(params);
            txt.setText(getResources().getString(R.string.defualt_gjester));
            txt.setTextColor(getResources().getColor(R.color.black));
            txt.setTextSize(20);

            kontakt_container.addView(txt);
        }
    }



    public boolean getEndrer(){return inviterer;}

    public void invite(){
        inviterer = true;
        ((LinearLayout) v.findViewById(R.id.visBesøklayout)).setVisibility(View.INVISIBLE);
        ((LinearLayout) v.findViewById(R.id.visBesøklayout_inviter)).setVisibility(View.VISIBLE);


    }

    public void save(){
        inviterer = false;

        ((LinearLayout) v.findViewById(R.id.visBesøklayout)).setVisibility(View.VISIBLE);
        ((LinearLayout) v.findViewById(R.id.visBesøklayout_inviter)).setVisibility(View.INVISIBLE);

        db.setDeltakere(besøk.getId() , multiadapter.getSelected());
        deltakere();


    }


}

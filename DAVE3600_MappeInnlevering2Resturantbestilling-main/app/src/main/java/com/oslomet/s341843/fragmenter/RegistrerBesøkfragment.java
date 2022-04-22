package com.oslomet.s341843.fragmenter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oslomet.s341843.CP;
import com.oslomet.s341843.R;
import com.oslomet.s341843.database.Bestilling;
import com.oslomet.s341843.database.Besøk;
import com.oslomet.s341843.database.DBHandler;
import com.oslomet.s341843.database.Kontakt;
import com.oslomet.s341843.database.Resturant;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegistrerBesøkfragment extends Fragment {
    private DBHandler db;
    private List<Kontakt> deltakere;
    private LinearLayout layout;
    private View v;

    private Spinner spinner;
    private  ArrayAdapter adapter;

    private Calendar calendar;
    private EditText et;

    private RecyclerView r;
    private MultiAdapter a;

    private Resources resources;

    private ImageButton imageButton;
    private boolean inviterer;

    public RegistrerBesøkfragment(DBHandler db, ImageButton imageButton){
        this.db = db;
        this.deltakere = new ArrayList<>();
        inviterer = false;
        this.imageButton = imageButton;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        v = inflater.inflate(R.layout.registrerbesok, container, false);
        resources = getResources();

        spinner = (Spinner) v.findViewById(R.id.resturant_selector);
        layout = (LinearLayout) v.findViewById(R.id.registrerte_deltakere);

        //List<Resturant> resturant = db.hentAlleResturanter();
        List<Resturant> resturant = new ArrayList<>();
        resturant.add(0, new Resturant(resources.getString(R.string.velg_restaurant), "0", "0", "0"));

        Cursor c = getContext().getContentResolver().query(CP.CONTENT_URI, null, null, null, null);
        if(c.moveToFirst()){
            do{
                resturant.add(new Resturant(c.getLong(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)));
            }while (c.moveToNext());
        }

        adapter = new ArrayAdapter(getContext(), R.layout.drop_items, resturant);




        spinner.setAdapter(adapter);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");


        et = (EditText) v.findViewById(R.id.date_time_selector);
        calendar = Calendar.getInstance();

        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                DatePickerDialog d = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i1, int i2, int i3) {
                        datePicker.setMinDate(System.currentTimeMillis());
                        calendar.set(Calendar.YEAR, i1);
                        calendar.set(Calendar.MONTH, i2);
                        calendar.set(Calendar.DAY_OF_MONTH, i3);

                        TimePickerDialog t = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i1, int i2) {

                                calendar.set(Calendar.HOUR_OF_DAY, i1);
                                calendar.set(Calendar.MINUTE, i2);




                                et.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        },calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                        t.show();

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                d.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                d.show();

            }
        });
        List<Kontakt> listK = db.hentAlleKontakter();
        a = new MultiAdapter(getContext(), listK);
        if(listK.size() == 0){
            ((TextView) v.findViewById(R.id.ingen_kontakter)).setVisibility(View.VISIBLE);
        }



        r = (RecyclerView) v.findViewById(R.id.list_deltakere);
        r.setLayoutManager(new LinearLayoutManager(getContext()));
        r.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        r.setAdapter(a);

        /*((Button) v.findViewById(R.id.registrer_deltakere)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ConstraintLayout) v.findViewById(R.id.layout_register)).setVisibility(View.INVISIBLE);
                ((ConstraintLayout) v.findViewById(R.id.layout_invite)).setVisibility(View.VISIBLE);
            }
        });*/
        /*((Button) v.findViewById(R.id.confirm_invite)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ConstraintLayout) v.findViewById(R.id.layout_register)).setVisibility(View.VISIBLE);
                ((ConstraintLayout) v.findViewById(R.id.layout_invite)).setVisibility(View.INVISIBLE);


                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                layout.removeAllViews();

                TextView txt = new TextView(getContext());
                txt.setLayoutParams(params);
                txt.setText("Inviterte kontakte:");
                txt.setTextSize(20);
                txt.setTextColor(getResources().getColor(R.color.black));
                layout.addView(txt);


                for(Kontakt k : a.getSelected()){
                    txt = new TextView(getContext());
                    txt.setLayoutParams(params);
                    txt.setText(k.getName());
                    txt.setTextSize(15);
                    txt.setTextColor(getResources().getColor(R.color.black));
                    layout.addView(txt);
                }

            }
        });*/

        /*((Button) v.findViewById(R.id.register_besok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid = true;

                if(((Resturant)spinner.getSelectedItem()).getName().equals("Velg en resturant")){
                    valid = false;
                    Toast.makeText(getContext(), "Vennligst velg en resturant", Toast.LENGTH_SHORT).show();
                }

                Calendar c = Calendar.getInstance();
                c.set(Calendar.MINUTE,c.get(Calendar.MINUTE) + 5);
                if(!calendar.after(c)){
                    if(valid){
                        valid = false;
                        Toast.makeText(getContext(), "Vennligst velg et gyldidg tidspunkt", Toast.LENGTH_SHORT).show();
                    }
                }

                if(valid){
                    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
                    Besøk besøk = new Besøk(((Resturant)spinner.getSelectedItem()).getId(), calendar);
                    db.registrerBesøk(besøk, a.getSelected());
                    Toast.makeText(getContext(), "Bestillingen er registrert", Toast.LENGTH_SHORT).show();
                    layout.removeAllViews();
                    a.empty();
                    //a = new MultiAdapter(getContext(), android.R.layout.simple_list_item_activated_1, resturant);

                    r.setAdapter(a);

                    et.setText("");
                    spinner.setAdapter(adapter);
                }

            }
        });*/



        return v;
    }

    public void registrer(){
        if(inviterer) {
            ((ConstraintLayout) v.findViewById(R.id.layout_register)).setVisibility(View.VISIBLE);
            ((ConstraintLayout) v.findViewById(R.id.layout_invite)).setVisibility(View.INVISIBLE);
            inviterer = false;
            imageButton.setImageResource(R.drawable.invite);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            layout.removeAllViews();

            for(Kontakt k : a.getSelected()){
                TextView txt = new TextView(getContext());
                txt.setLayoutParams(params);
                txt.setText(k.getName());
                txt.setTextSize(20);
                txt.setTextColor(getResources().getColor(R.color.black));
                layout.addView(txt);
            }

        }


        boolean valid = true;

        if(((Resturant)spinner.getSelectedItem()).getName().equals(resources.getString(R.string.velg_restaurant))){
            valid = false;
            ((TextView)spinner.getSelectedView()).setError("");
            Toast.makeText(getContext(), resources.getString(R.string.err_velg_restaurant), Toast.LENGTH_SHORT).show();
        }

        Calendar c = Calendar.getInstance();
        c.set(Calendar.MINUTE,c.get(Calendar.MINUTE) + 5);
        if(!calendar.after(c)){
            et.setError("");
            if(valid){
                valid = false;
                Toast.makeText(getContext(), resources.getString(R.string.err_velg_tidpunkt), Toast.LENGTH_SHORT).show();
            }
        }

        if(valid){
            SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Log.d("sjekk", s.format(calendar.getTime()));
            //calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
            //Log.d("sjekk", s.format(calendar.getTime()));
            Besøk besøk = new Besøk(((Resturant)spinner.getSelectedItem()).getId(), calendar);
            db.registrerBesøk(besøk, a.getSelected());
            Toast.makeText(getContext(), resources.getString(R.string.bestilling_registrert), Toast.LENGTH_SHORT).show();
            layout.removeAllViews();
            a.empty();
            //a = new MultiAdapter(getContext(), android.R.layout.simple_list_item_activated_1, resturant);

            r.setAdapter(a);

            et.setText("");
            spinner.setAdapter(adapter);
        }

    }
    public void inviter(){
        if(!inviterer){
            inviterer = true;
            imageButton.setImageResource(R.drawable.save);
            ((ConstraintLayout) v.findViewById(R.id.layout_register)).setVisibility(View.INVISIBLE);
            ((ConstraintLayout) v.findViewById(R.id.layout_invite)).setVisibility(View.VISIBLE);
        }else{
            inviterer = false;
            imageButton.setImageResource(R.drawable.invite);

            ((ConstraintLayout) v.findViewById(R.id.layout_register)).setVisibility(View.VISIBLE);
            ((ConstraintLayout) v.findViewById(R.id.layout_invite)).setVisibility(View.INVISIBLE);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            layout.removeAllViews();

            for(Kontakt k : a.getSelected()){
                TextView txt = new TextView(getContext());
                txt.setLayoutParams(params);
                txt.setText(k.getName());
                txt.setTextSize(20);
                txt.setTextColor(getResources().getColor(R.color.black));
                layout.addView(txt);
            }
        }

    }

}

package com.example.testavrestaurantcp;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Visfragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.vis, container, false);

        LinearLayout layout = (LinearLayout) v.findViewById(R.id.layout);


        Cursor c = getContext().getContentResolver().query(MainActivity.CONTENT_URI, null, null, null, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(c.moveToFirst()) {
            do {
                TextView txt = new TextView(getContext());
                txt.setLayoutParams(params);
                txt.setTextSize(20);
                txt.setTextColor(getResources().getColor(R.color.black));
                txt.setText("Id: " + c.getLong(0) + " Navn: " + c.getString(1));
                layout.addView(txt);

            }while (c.moveToNext());
        }

        return v;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}

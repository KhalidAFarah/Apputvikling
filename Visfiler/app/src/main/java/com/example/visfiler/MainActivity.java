package com.example.visfiler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements ListeFragment.UrlEndret {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void linkEndret(String link) {
        if(findViewById(R.id.scriptfragment) != null) {
            VisScriptFragment visscript = (VisScriptFragment) getSupportFragmentManager().findFragmentById(R.id.scriptfragment);
            if(visscript== null) {
                visscript = new VisScriptFragment();
                visscript.init(link);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction trans = fm.beginTransaction();
                trans.replace(R.id.listefragment, visscript);trans.commit();
            }else{
                visscript.updateUrl(link);
            }
        } else{Intent i = new Intent(this, VisScriptActivity.class);
        i.putExtra("scriptnavn", link);
        startActivity(i);
        }
    }
}
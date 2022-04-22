package com.example.visfiler;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class VisScriptActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = this.getIntent();
        String bnavn= i.getExtras().getString("scriptnavn");
        VisScriptFragment visscript = new VisScriptFragment();
        visscript.init(bnavn);
        FragmentManager fragmentManager;
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(android.R.id.content,visscript).commit();
    }
}

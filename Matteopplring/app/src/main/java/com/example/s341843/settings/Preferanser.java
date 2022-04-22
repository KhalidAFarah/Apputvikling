package com.example.s341843.settings;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.preference.ListPreference;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Locale;

public class Preferanser {
    public static boolean byttSpråk(Context context, String landskode){
        Resources resource = context.getResources();
        DisplayMetrics dm = resource.getDisplayMetrics();
        Configuration cf = resource.getConfiguration();
        cf.setLocale(new Locale(landskode));
        resource.updateConfiguration(cf, dm);

        return true;
    }


}

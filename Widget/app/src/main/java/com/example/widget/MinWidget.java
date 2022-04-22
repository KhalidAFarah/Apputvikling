package com.example.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MinWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //super.onUpdate(context, appWidgetManager, appWidgetIds);

        RemoteViews updateViews = new RemoteViews(context.getApplicationContext().getPackageName(), R.layout.widget_layout);
        Date date = new Date();
        java.text.DateFormat format = SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM, Locale.getDefault());
        updateViews.setTextViewText(R.id.widgettekst, "Klokka er: " + format.format(date));
        Intent clickIntent = new Intent(context, MinWidget.class);
        clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        updateViews.setOnClickPendingIntent(R.id.widgettekst, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, updateViews);
    }
}

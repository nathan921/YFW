package com.ddtpt.yfw;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;

/**
 * Created by E228596 on 6/27/2016.
 */
public class SimpleWidgetProvider extends AppWidgetProvider {
    public static final String LOG_ID = "SimpleWidgetProvider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.d(LOG_ID, "onUpdate Has Been Called");
        final int count = appWidgetIds.length;
        SharedPreferences prefs = context.getSharedPreferences("private preferences", Context.MODE_PRIVATE);

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            //TODO:Do I need to clear this before I start putting in new ones?  Risk of updating non-existant widgets?
            prefs.edit().putInt("WidgetId" + i, widgetId).commit();

            Intent intent = new Intent(context, SimpleWidgetProvider.class);
            context.startService(intent);

        }
    }

}

package com.ddtpt.yfw;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;

/**
 * Created by E228596 on 6/27/2016.
 */
public class SimpleWidgetProvider extends AppWidgetProvider {
    public static final String ACTION_REFRESH = "com.ddtpt.yfw.ACTION_REFRESH";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.short_layout_alt);
            //remoteViews.setTextViewText(R.id.text_player1, number);

            Intent intent = new Intent(context, SimpleWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.text_player2, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);

        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        ArrayList<Matchup> matchups = new ArrayList<>();
        if (intent.getAction().equals(ACTION_REFRESH)) {
            matchups = (ArrayList<Matchup>) intent.getSerializableExtra("matchups");
        }

        if (!matchups.isEmpty()) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.short_layout_alt);
            remoteViews.setTextViewText(R.id.text_player1, matchups.get(0).getHomeTeam().getNickname());
        }
    }
}

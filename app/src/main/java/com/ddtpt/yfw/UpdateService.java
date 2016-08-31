package com.ddtpt.yfw;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by e228596 on 8/26/2016.
 */
public class UpdateService extends IntentService {
    public static final String LOG_TAG = "UpdateService";
    public static final int POLL_INTERVAL = 1000 * 60;

    Observable<ArrayList<Matchup>> matchupUpdateObservable;
    ArrayList<Matchup> matches;
    int awID = 0;

    Observable<ArrayList<Matchup>> testJSONParsing;

    public UpdateService() {
        super("UpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences prefs = getSharedPreferences("private preferences", Context.MODE_PRIVATE);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        boolean isNetworkAvailable = cm.getBackgroundDataSetting() &&
                cm.getActiveNetworkInfo() != null;
        if (!isNetworkAvailable) return;

        matches = new ArrayList<>();

        // Check to see if a token exists.  If it doesn't, there's no point in going on here
        //if (!OAuthFactory.TokenExists(getApplicationContext())) return;

        awID = prefs.getInt("WidgetId0", AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.d(LOG_TAG, "App Widget ID : " + awID + "    /Invalid = " + AppWidgetManager.INVALID_APPWIDGET_ID);

        matchupUpdateObservable = Observable.create(new Observable.OnSubscribe<ArrayList<Matchup>>() {
            @Override
            public void call(Subscriber<? super ArrayList<Matchup>> subscriber) {
                try {
                    JsonElement result = getMatchupJSON();
                    final ArrayList<Matchup> m = parseJsonResult(result);
                    subscriber.onNext(m);
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
        });

        UpdateMatchups();

       /* RemoteViews rv = new RemoteViews(getPackageName(), R.layout.short_layout_alt);
        rv.setTextViewText(R.id.player1_name, "Ass to Mouth");
        rv.setTextViewText(R.id.player1_score, "69.69");

        AppWidgetManager appMgr = AppWidgetManager.getInstance(getApplicationContext());
        appMgr.updateAppWidget(awID, rv);
        */

                        //TODO: need to call the download of Yahoo Data here


    }

    private void UpdateMatchups() {
        matchupUpdateObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Matchup>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArrayList<Matchup> matchups) {
                        Log.d(LOG_TAG, "Processing Remote Views");
                        RemoteViews rv = new RemoteViews(getPackageName(), R.layout.short_layout_alt);

                        rv.setTextViewText(R.id.player1_name, matchups.get(0).getHomeTeam().getTeam_name());
                        rv.setTextViewText(R.id.player1_score, matchups.get(0).getHomeTeam().getTotal_points());

                        rv.setTextViewText(R.id.player2_name, matchups.get(0).getAwayTeam().getTeam_name());
                        rv.setTextViewText(R.id.player2_score, matchups.get(0).getAwayTeam().getTotal_points());

                        Picasso.with(getApplicationContext())
                                .load("http://i.imgur.com/t0oA4Iq.jpg")
                                .into(rv, R.id.player1_avatar, new int[] {awID});

                        Picasso.with(getApplicationContext())
                                .load("http://i.imgur.com/abyzk2Y.jpg")
                                .into(rv, R.id.player2_avatar, new int[] {awID});

                        Log.d(LOG_TAG, "Updating Remote Views");
                        AppWidgetManager appMgr = AppWidgetManager.getInstance(getApplicationContext());
                        appMgr.updateAppWidget(awID, rv);
                    }
                });
    }

    public static void setServiceAlarm(Context context, Boolean isOn) {
        Intent i = new Intent(context, UpdateService.class);
        PendingIntent pi = PendingIntent.getService(context, 0,i,0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), POLL_INTERVAL, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }

    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = new Intent(context, UpdateService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);

        return pi != null;
    }

    //This function will be used to make the call to download the JSON from Yahoo
    private JsonElement getMatchupJSON() {
        Log.d(LOG_TAG, "getting JSON");
        JsonElement result = new JsonParser().parse(Strings.test_JSON);
        return result;
    }

    //This function will be called to parse the downloaded Yahoo Results.
    private ArrayList<Matchup> parseJsonResult(JsonElement jsonResult) {
        Log.d(LOG_TAG, "parsing JSON");
        ArrayList<Matchup> matchups = YahooJsonParser.parseMatchups(jsonResult);
        return matchups;
    }

}

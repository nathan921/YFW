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
import com.squareup.picasso.Picasso;
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

    YahooAPI service;
    ArrayList<Matchup> matches;
    int awID = 0;

    Observable<Integer> refreshTokenObservable;
    Observable<JsonElement> getMatchupsObservable;

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

        awID = prefs.getInt("WidgetId0", AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.d(LOG_TAG, "App Widget ID : " + awID + "    /Invalid = " + AppWidgetManager.INVALID_APPWIDGET_ID);

        // Check to see if a token exists.  If it doesn't, there's no point in going on here
        if (!OAuthFactory.TokenExists(getApplicationContext())) {
            RemoteViews rv = new RemoteViews(getPackageName(), R.layout.short_layout_alt);
            AppWidgetManager mgr = AppWidgetManager.getInstance(getApplicationContext());
            mgr.updateAppWidget(awID, rv);
            service = null;
        } else {
            refreshService(OAuthFactory.generateConsumer(Strings.YAHOO,getApplicationContext()));
        }

        refreshTokenObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(0);
                subscriber.onCompleted();
            }
        });

        getMatchupsObservable = Observable.create(new Observable.OnSubscribe<JsonElement>() {
            @Override
            public void call(Subscriber<? super JsonElement> subscriber) {
                service.getMatchups()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<JsonElement>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(LOG_TAG, e.getMessage());
                                if (e.getMessage().contains("401")) {
                                    refreshToken();
                                }

                            }

                            @Override
                            public void onNext(JsonElement jsonElement) {
                                Log.d(LOG_TAG, "onNext Call from getJson");
                                parseJsonResult(jsonElement);
                            }
                        });
            }
        });

        getMatchupJSON();

       /* RemoteViews rv = new RemoteViews(getPackageName(), R.layout.short_layout_alt);
        rv.setTextViewText(R.id.player1_name, "Ass to Mouth");
        rv.setTextViewText(R.id.player1_score, "69.69");

        AppWidgetManager appMgr = AppWidgetManager.getInstance(getApplicationContext());
        appMgr.updateAppWidget(awID, rv);
        */

                        //TODO: need to call the download of Yahoo Data here


    }

    public static void setServiceAlarm(Context context, Boolean isOn) {
        Intent i = new Intent(context, UpdateService.class);
        PendingIntent pi = PendingIntent.getService(context, 0,i,0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Log.d(LOG_TAG, "ServiceAlarm " + isOn);

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

    private void refreshService(RetrofitHttpOAuthConsumer consumer) {
        service = ServiceFactory.createRetrofitService(YahooAPI.class,
                Strings.BASE_URL,
                consumer);
    }

    private void refreshToken() {
        refreshTokenObservable
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        RetrofitHttpOAuthConsumer updatedConsumer = OAuthFactory.RefreshToken(
                                OAuthFactory.generateProvider(Strings.YAHOO),
                                OAuthFactory.generateConsumer(Strings.YAHOO, getApplicationContext()));
                        OAuthFactory.StoreTokenInPrefs(updatedConsumer, getApplicationContext());
                        refreshService(updatedConsumer);
                    }
                });


    }

    //This function will be used to make the call to download the JSON from Yahoo
    private void getMatchupJSON() {
        Log.d(LOG_TAG, "getting JSON");
        getMatchupsObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    //This function will be called to parse the downloaded Yahoo Results.
    private void parseJsonResult(JsonElement jsonResult) {
        Log.d(LOG_TAG, "parsing JSON");
        final ArrayList<Matchup> matchups = YahooJsonParser.parseMatchups(jsonResult);
        updateRemoteViews(matchups);
    }

    private void updateRemoteViews(ArrayList<Matchup> matchups) {
        Log.d(LOG_TAG, "Processing Remote Views");
        RemoteViews rv = new RemoteViews(getPackageName(), R.layout.short_layout_alt);

        rv.setTextViewText(R.id.player1_name, matchups.get(0).getHomeTeam().getTeam_name());
        rv.setTextViewText(R.id.player1_score, matchups.get(0).getHomeTeam().getTotal_points());
        rv.setTextViewText(R.id.player1_projected_score, matchups.get(0).getHomeTeam().getProjected_points());

        rv.setTextViewText(R.id.player2_name, matchups.get(0).getAwayTeam().getTeam_name());
        rv.setTextViewText(R.id.player2_score, matchups.get(0).getAwayTeam().getTotal_points());
        rv.setTextViewText(R.id.player2_projected_score, matchups.get(0).getAwayTeam().getProjected_points());

        Picasso.with(getApplicationContext())
                .load(matchups.get(0).getHomeTeam().getImage_url())
                .into(rv, R.id.player1_avatar, new int[] {awID});

        Picasso.with(getApplicationContext())
                .load(matchups.get(0).getAwayTeam().getImage_url())
                .into(rv, R.id.player2_avatar, new int[] {awID});

        Log.d(LOG_TAG, "Updating Remote Views");
        AppWidgetManager appMgr = AppWidgetManager.getInstance(getApplicationContext());
        appMgr.updateAppWidget(awID, rv);
    }

}

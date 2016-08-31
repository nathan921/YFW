package com.ddtpt.yfw;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.net.Uri;

import com.google.gson.JsonElement;

import java.util.ArrayList;

import butterknife.BindView;

import butterknife.ButterKnife;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button_login) Button button_logon;
    @BindView(R.id.text_current_user) TextView textview_user_name;
    @BindView(R.id.button_test_json) Button button_test_json;


    Uri tokenUri;
    CommonsHttpOAuthProvider provider;
    RetrofitHttpOAuthConsumer consumer;
    YahooAPI service;

    Observable<ArrayList<Matchup>> testJSONParsing;
    Observable<String> yahooLogon;
    Observable<String> getOAuthAccessToken;
    Observable<Boolean> DetermineIfTokenExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Intent intent = new Intent(this, UpdateService.class);
        //this.startService(intent);
        UpdateService.setServiceAlarm(this, true);


        //Clear the User Name text
        textview_user_name.setText("");

        //OnClickListener for the button that allows the user to logon to Yahoo
        button_logon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yahooLogonClick();
            }
        });

        button_test_json.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testJSONDecode();
            }
        });

        //Create observable for logon process
        yahooLogon = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    String result = performYahooLogon();
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                } catch(Exception e) {
                    subscriber.onError(e);
                }
            }
        });

        getOAuthAccessToken = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    provider.retrieveAccessToken(consumer, tokenUri.getQueryParameter("oauth_verifier"));
                } catch (Exception e) {
                    Log.e("getOauthAccessToken", e.toString());
                }
                StoreTokenInPrefs();
            }
        });


        //TODO: Remove this
        testJSONParsing = Observable.create(new Observable.OnSubscribe<ArrayList<Matchup>>() {
            @Override
            public void call(Subscriber<? super ArrayList<Matchup>> subscriber) {
                ArrayList<Matchup> temp;
                YahooJsonParser yparser = new YahooJsonParser(Strings.test_JSON);
                temp = yparser.parseMatchups(yparser.getFullJsonElement());
                subscriber.onNext(temp);
            }
        });

    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        tokenUri = intent.getData();
        if (tokenUri != null && tokenUri.getScheme().equals("yffa")) {
            getOAuthAccessToken
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            Log.d("TOKEN", consumer.getToken());
                            Log.d("SECRET", consumer.getTokenSecret());
                        }
                    });
        }
    }

    private void yahooLogonClick() {
        yahooLogon
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        textview_user_name.setText(s);
                    }
                });

    }

    //TODO: This will need to be removed from the MainActivity
    private void testJSONDecode() {
        Log.d("Service Check", String.valueOf(UpdateService.isServiceAlarmOn(this)));

        testJSONParsing
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Matchup>>() {
                    @Override
                    public void onNext(ArrayList<Matchup> matchups) {
                        textview_user_name.setText(matchups.get(0).getHomeTeam().getNickname());
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("YahooAPI Demo", e.getMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }
                });

    }

    //TODO: This will need to be removed from the MainActivity
    private void testJSONGet() {
        service.getUser()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonElement>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("YahooAPI Demo", e.getMessage());
                    }

                    @Override
                    public void onNext(JsonElement response) {
                        Log.i("YahooAPI", response.toString());
                    }
                });
    }

    private void StoreTokenInPrefs() {
        OAuthFactory.StoreTokenInPrefs(consumer, this);

        BuildRestAdapter();

    }

    private void BuildRestAdapter() {
        //consumer.setTokenWithSecret(consumer.getToken(), consumer.getTokenSecret());
        service = ServiceFactory.createRetrofitService(YahooAPI.class, YahooAPI.SERVICE_ENDPOINT, consumer);
    }


    private String performYahooLogon() {

        String TAG = "OAUTH";

        String results = "";
        consumer = OAuthFactory.generateConsumer(Strings.YAHOO);
        provider = OAuthFactory.generateProvider(Strings.YAHOO);

        Log.i(TAG, "Retrieving request token from Yahoo");
        try {
            final String authUrl = provider.retrieveRequestToken(consumer, "yffa://www.ddtpt.com");
            Log.i(TAG, "AUTHORIZATION URL: " + authUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)).setFlags(
                    Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);
            this.startActivity(intent);

            return authUrl;
        } catch (Exception e) {
            Log.e(TAG, "ERROR: " + e.toString());
        }

        return null;
    }
}

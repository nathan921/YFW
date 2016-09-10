package com.ddtpt.yfw;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.net.Uri;

import com.google.gson.JsonElement;


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
    @BindView(R.id.button_toggle_service) Button button_toggle_service;

    private static final String LOG_TAG = "MainActivity";

    Uri tokenUri;
    CommonsHttpOAuthProvider provider;
    RetrofitHttpOAuthConsumer consumer;
    YahooAPI service;

    Observable<JsonElement> getJson;
    Observable<String> yahooLogon;
    Observable<String> getOAuthAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        UpdateService.setServiceAlarm(this, false);

        //Intent intent = new Intent(this, UpdateService.class);
        //this.startService(intent);

        //Clear the User Name text
        textview_user_name.setText("");

        if (OAuthFactory.TokenExists(this)) {
            //service = ServiceFactory.createRetrofitService(YahooAPI.class,
            //       Strings.BASE_URL,
            //       OAuthFactory.generateConsumer(Strings.YAHOO, this));
            //UpdateService.setServiceAlarm(getApplicationContext(), true);
        }

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
                jsonTest();
            }
        });

        button_toggle_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToggleServiceAlarm();
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

        getJson = Observable.create(new Observable.OnSubscribe<JsonElement>() {
            @Override
            public void call(Subscriber<? super JsonElement> subscriber) {
                service.getMatchups()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<JsonElement>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(LOG_TAG, e.getMessage());
                            }

                            @Override
                            public void onNext(JsonElement jsonElement) {
                                Log.d(LOG_TAG, "onNext Call from getJson");
                            }
                        });
            }
        });

    }

    private void ToggleServiceAlarm() {
        UpdateService.setServiceAlarm(this, !UpdateService.isServiceAlarmOn(this));
    }

    private void jsonTest() {
        if (service == null) {
            return;
        }
        getJson.subscribe(new Action1<JsonElement>() {
            @Override
            public void call(JsonElement jsonElement) {
                Log.d(LOG_TAG, "Got JSON");
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
                .subscribe();
    }

    public void ServiceRunning() {
        Log.d(LOG_TAG, "Service Running: " + UpdateService.isServiceAlarmOn(this));
    }

    private void StoreTokenInPrefs() {
        OAuthFactory.StoreTokenInPrefs(consumer, this);
        service = ServiceFactory.createRetrofitService(YahooAPI.class, Strings.BASE_URL, consumer);
        //UpdateService.setServiceAlarm(getApplicationContext(), true);
    }

    private String performYahooLogon() {

        String TAG = "OAUTH";

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

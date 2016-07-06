package com.ddtpt.yfw;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.net.Uri;

import butterknife.BindView;

import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button_login) Button button_logon;
    @BindView(R.id.text_current_user) TextView textview_user_name;

    Uri tokenUri;
    CommonsHttpOAuthProvider provider;
    RetrofitHttpOAuthConsumer consumer;

    Observable<String> yahooLogon;
    Observable<String> getOAuthAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Clear the User Name text
        textview_user_name.setText("");

        //OnClickListener for the button that allows the user to logon to Yahoo
        button_logon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yahooLogonClick();
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

    private String performYahooLogon() {
        String CONSUMER_KEY = "dj0yJmk9RjVUYUZNc1piMzVRJmQ9WVdrOVNGUnJkMFZFTldVbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD02YQ--";
        String CONSUMER_SECRET = "7c5d77c23d2c2a4c2c117bc846e392c5fa7a4697";
        String TAG = "OAUTH";

        String results = "";
        consumer = new RetrofitHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        provider = new CommonsHttpOAuthProvider(
                "https://api.login.yahoo.com/oauth/v2/get_request_token",
                "https://api.login.yahoo.com/oauth/v2/get_token",
                "https://api.login.yahoo.com/oauth/v2/request_auth"
        );
        provider.setOAuth10a(true);

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

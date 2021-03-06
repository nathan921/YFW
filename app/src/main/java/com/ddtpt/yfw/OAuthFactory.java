package com.ddtpt.yfw;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

/**
 * Created by e228596 on 8/31/2016.
 */
public class OAuthFactory {
    public static final String LOG_TAG = "OAUTH FACTORY";

    public static CommonsHttpOAuthProvider generateProvider(String service) {
        if (service.equals(Strings.YAHOO)) {
            CommonsHttpOAuthProvider provider = new CommonsHttpOAuthProvider(
                    Strings.YAHOO_GET_REQUEST_TOKEN_URL,
                    Strings.YAHOO_GET_TOKEN_URL,
                    Strings.YAHOO_REQUEST_AUTH_URL
            );
            provider.setOAuth10a(true);
            return provider;
        } else {
            return null;
        }
    }

    public static RetrofitHttpOAuthConsumer generateConsumer(String service) {
        if (service.equals(Strings.YAHOO)) {
            return new RetrofitHttpOAuthConsumer(Strings.CONSUMER_KEY, Strings.CONSUMER_SECRET);
        } else {
            return null;
        }
    }

    public static RetrofitHttpOAuthConsumer generateConsumer(String service, Context context) {
        if (service.equals(Strings.YAHOO)) {
            if (TokenExists(context)) {
                RetrofitHttpOAuthConsumer consumer = new RetrofitHttpOAuthConsumer(Strings.CONSUMER_KEY, Strings.CONSUMER_SECRET);
                SharedPreferences prefs = context.getSharedPreferences("private preferences", Context.MODE_PRIVATE);
                consumer.setTokenWithSecret(prefs.getString(Strings.TOKEN, null), prefs.getString(Strings.SECRET, null));
                if (consumer.getToken() != null && consumer.getTokenSecret() != null) {
                    return consumer;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    public static void StoreTokenInPrefs(RetrofitHttpOAuthConsumer consumer, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("private preferences", Context.MODE_PRIVATE).edit();
        editor.putString(Strings.TOKEN, consumer.getToken());
        editor.putString(Strings.SECRET, consumer.getTokenSecret());
        //editor.putString(Strings.OAUTH_SIGNATURE, consumer.getRequestParameters().getFirst(Strings.OAUTH_SIGNATURE));
        editor.putString(Strings.OAUTH_NONCE, consumer.getRequestParameters().getFirst(Strings.OAUTH_NONCE));
        editor.putString(Strings.OAUTH_SIGNATURE_METHOD, consumer.getRequestParameters().getFirst(Strings.OAUTH_SIGNATURE_METHOD));
        editor.putString(Strings.OAUTH_TIMESTAMP, consumer.getRequestParameters().getFirst(Strings.OAUTH_TIMESTAMP));

        Log.d(LOG_TAG, "TOKEN: " + consumer.getToken());
        Log.d(LOG_TAG, "SECRET: " + consumer.getTokenSecret());

        editor.commit();
    }

    public static boolean TokenExists(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("private preferences", Context.MODE_PRIVATE);

        Log.d(LOG_TAG, "TOKEN: " + prefs.getString(Strings.TOKEN, null));
        Log.d(LOG_TAG, "SECRET: " + prefs.getString(Strings.SECRET, null));

        if (prefs.getString(Strings.SECRET, null) != null && prefs.getString(Strings.TOKEN, null) != null){
            return true;
        } return false;
    }

    public static RetrofitHttpOAuthConsumer RefreshToken(CommonsHttpOAuthProvider provider, RetrofitHttpOAuthConsumer consumer) {
        try {
            provider.retrieveAccessToken(consumer, consumer.getTokenSecret(), "");
            return consumer;
        } catch(Exception e) {
            Log.e ("Exception" , e.getMessage());
            return null;
        }
    }

}

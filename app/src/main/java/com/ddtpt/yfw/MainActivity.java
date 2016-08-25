package com.ddtpt.yfw;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
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

    private static final String SECRET = "oauth_secret";
    private static final String TOKEN = "oauth_token";
    private static final String OAUTH_TIMESTAMP = "oauth_timestamp";
    private static final String OAUTH_NONCE = "oauth_nonce";
    private static final String OAUTH_SIGNATURE = "oauth_signature";
    private static final String OAUTH_VERSION = "oauth_version";
    private static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
    private static final String test_JSON = "{\"fantasy_content\":{\"xml:lang\":\"en-US\",\"yahoo:uri\":\"/fantasy/v2/league/331.l.106320/scoreboard;week=15\",\"league\":[{\"league_key\":\"331.l.106320\",\"league_id\":\"106320\",\"name\":\"Bloodlust Bowl\",\"url\":\"http://football.fantasysports.yahoo.com/archive/nfl/2014/106320\",\"password\":\"\",\"league_chat_id\":\"eugordtoqj6j5ydrziqs3bnfl90e7t8\",\"draft_status\":\"postdraft\",\"num_teams\":14,\"edit_key\":\"17\",\"weekly_deadline\":\"\",\"league_update_timestamp\":\"1420101309\",\"scoring_type\":\"head\",\"league_type\":\"private\",\"renew\":\"314_663445\",\"renewed\":\"348_155276\",\"short_invitation_url\":\"https://yho.com/nfl?l=106320&k=d6e1f2f26d736ebe\",\"is_pro_league\":\"0\",\"current_week\":\"16\",\"start_week\":\"1\",\"start_date\":\"2014-09-04\",\"end_week\":\"16\",\"end_date\":\"2014-12-22\",\"is_finished\":1,\"game_code\":\"nfl\",\"season\":\"2014\"},{\"scoreboard\":{\"week\":\"15\",\"0\":{\"matchups\":{\"0\":{\"matchup\":{\"week\":\"15\",\"week_start\":\"2014-12-09\",\"week_end\":\"2014-12-15\",\"status\":\"postevent\",\"is_playoffs\":\"1\",\"is_consolation\":\"0\",\"is_tied\":0,\"winner_team_key\":\"331.l.106320.t.1\",\"0\":{\"teams\":{\"0\":{\"team\":[[{\"team_key\":\"331.l.106320.t.1\"},{\"team_id\":\"1\"},{\"name\":\"Crusher of Dreams\"},{\"is_owned_by_current_login\":1},{\"url\":\"http://football.fantasysports.yahoo.com/archive/nfl/2014/106320/1\"},{\"team_logos\":[{\"team_logo\":{\"size\":\"large\",\"url\":\"https://i.imgur-ysports.com/BjKojTjy.jpg\"}}]},[],{\"waiver_priority\":9},[],{\"number_of_moves\":\"28\"},{\"number_of_trades\":0},{\"roster_adds\":{\"coverage_type\":\"week\",\"coverage_value\":\"17\",\"value\":\"0\"}},{\"clinched_playoffs\":1},{\"league_scoring_type\":\"head\"},{\"managers\":[{\"manager\":{\"manager_id\":\"1\",\"nickname\":\"Nathan\",\"guid\":\"TDJZVZ2P7ATEP64L37ZTW2IO7I\",\"is_commissioner\":\"1\",\"is_current_login\":\"1\",\"email\":\"detlefmagoo@yahoo.com\",\"image_url\":\"https://s.yimg.com/dh/ap/social/profile/profile_b64.png\"}}]}],{\"team_points\":{\"coverage_type\":\"week\",\"week\":\"15\",\"total\":\"82.32\"},\"team_projected_points\":{\"coverage_type\":\"week\",\"week\":\"15\",\"total\":\"92.19\"}}]},\"1\":{\"team\":[[{\"team_key\":\"331.l.106320.t.11\"},{\"team_id\":\"11\"},{\"name\":\"Chum Guzzler\"},[],{\"url\":\"http://football.fantasysports.yahoo.com/archive/nfl/2014/106320/11\"},{\"team_logos\":[{\"team_logo\":{\"size\":\"large\",\"url\":\"https://i.imgur-ysports.com/jOsOjO5y.jpg\"}}]},[],{\"waiver_priority\":12},[],{\"number_of_moves\":\"27\"},{\"number_of_trades\":0},{\"roster_adds\":{\"coverage_type\":\"week\",\"coverage_value\":\"17\",\"value\":\"0\"}},{\"clinched_playoffs\":1},{\"league_scoring_type\":\"head\"},{\"managers\":[{\"manager\":{\"manager_id\":\"11\",\"nickname\":\"lindsay\",\"guid\":\"7ZWG4QUOR7AGTWW6YUSC6GDEIE\",\"email\":\"lindsay.r.speck@gmail.com\",\"image_url\":\"https://s.yimg.com/dh/ap/social/profile/profile_b64.png\"}}]}],{\"team_points\":{\"coverage_type\":\"week\",\"week\":\"15\",\"total\":\"77.98\"},\"team_projected_points\":{\"coverage_type\":\"week\",\"week\":\"15\",\"total\":\"107.25\"}}]},\"count\":2}}}},\"1\":{\"matchup\":{\"week\":\"15\",\"week_start\":\"2014-12-09\",\"week_end\":\"2014-12-15\",\"status\":\"postevent\",\"is_playoffs\":\"1\",\"is_consolation\":\"0\",\"is_tied\":0,\"winner_team_key\":\"331.l.106320.t.4\",\"0\":{\"teams\":{\"0\":{\"team\":[[{\"team_key\":\"331.l.106320.t.4\"},{\"team_id\":\"4\"},{\"name\":\"The Beast Rider\"},[],{\"url\":\"http://football.fantasysports.yahoo.com/archive/nfl/2014/106320/4\"},{\"team_logos\":[{\"team_logo\":{\"size\":\"large\",\"url\":\"https://i.imgur-ysports.com/RQxco7Zy.jpg\"}}]},[],{\"waiver_priority\":14},[],{\"number_of_moves\":\"37\"},{\"number_of_trades\":0},{\"roster_adds\":{\"coverage_type\":\"week\",\"coverage_value\":\"17\",\"value\":\"1\"}},{\"clinched_playoffs\":1},{\"league_scoring_type\":\"head\"},{\"managers\":[{\"manager\":{\"manager_id\":\"4\",\"nickname\":\"Joe\",\"guid\":\"IQDTDHQAXDJRRRUKWBT2YMG464\",\"email\":\"jonick@gmail.com\",\"image_url\":\"https://s.yimg.com/dh/ap/social/profile/profile_b64.png\"}}]}],{\"team_points\":{\"coverage_type\":\"week\",\"week\":\"15\",\"total\":\"112.88\"},\"team_projected_points\":{\"coverage_type\":\"week\",\"week\":\"15\",\"total\":\"105.64\"}}]},\"1\":{\"team\":[[{\"team_key\":\"331.l.106320.t.12\"},{\"team_id\":\"12\"},{\"name\":\"Boss Ballerz\"},[],{\"url\":\"http://football.fantasysports.yahoo.com/archive/nfl/2014/106320/12\"},{\"team_logos\":[{\"team_logo\":{\"size\":\"large\",\"url\":\"http://l.yimg.com/dh/ap/fantasy/nfl/img/icon_01_100.png\"}}]},[],{\"waiver_priority\":3},[],{\"number_of_moves\":\"8\"},{\"number_of_trades\":0},{\"roster_adds\":{\"coverage_type\":\"week\",\"coverage_value\":\"17\",\"value\":\"0\"}},{\"clinched_playoffs\":1},{\"league_scoring_type\":\"head\"},{\"managers\":[{\"manager\":{\"manager_id\":\"12\",\"nickname\":\"Whitney\",\"guid\":\"H7N6QVPYBA6DBRLYP7ELICRWVQ\",\"email\":\"mongress@gmail.com\",\"image_url\":\"https://s.yimg.com/dh/ap/social/profile/profile_b64.png\"}}]}],{\"team_points\":{\"coverage_type\":\"week\",\"week\":\"15\",\"total\":\"97.00\"},\"team_projected_points\":{\"coverage_type\":\"week\",\"week\":\"15\",\"total\":\"89.59\"}}]},\"count\":2}}}},\"2\":{\"matchup\":{\"week\":\"15\",\"week_start\":\"2014-12-09\",\"week_end\":\"2014-12-15\",\"status\":\"postevent\",\"is_playoffs\":\"1\",\"is_consolation\":\"0\",\"is_tied\":0,\"winner_team_key\":\"331.l.106320.t.9\",\"0\":{\"teams\":{\"0\":{\"team\":[[{\"team_key\":\"331.l.106320.t.5\"},{\"team_id\":\"5\"},{\"name\":\"Scorgasmic\"},[],{\"url\":\"http://football.fantasysports.yahoo.com/archive/nfl/2014/106320/5\"},{\"team_logos\":[{\"team_logo\":{\"size\":\"large\",\"url\":\"https://i.imgur-ysports.com/kjZxcMOy.jpg\"}}]},[],{\"waiver_priority\":10},[],{\"number_of_moves\":\"26\"},{\"number_of_trades\":0},{\"roster_adds\":{\"coverage_type\":\"week\",\"coverage_value\":\"17\",\"value\":\"0\"}},{\"clinched_playoffs\":1},{\"league_scoring_type\":\"head\"},{\"managers\":[{\"manager\":{\"manager_id\":\"5\",\"nickname\":\"Whitney\",\"guid\":\"BWKLL5WAKAO7GBOHHZ2JPO2AE4\",\"email\":\"whitney.herbick@yahoo.com\",\"image_url\":\"https://s.yimg.com/dh/ap/social/profile/profile_b64.png\"}}]}],{\"team_points\":{\"coverage_type\":\"week\",\"week\":\"15\",\"total\":\"73.62\"},\"team_projected_points\":{\"coverage_type\":\"week\",\"week\":\"15\",\"total\":\"111.84\"}}]},\"1\":{\"team\":[[{\"team_key\":\"331.l.106320.t.9\"},{\"team_id\":\"9\"},{\"name\":\"Royál with Cheese\"},[],{\"url\":\"http://football.fantasysports.yahoo.com/archive/nfl/2014/106320/9\"},{\"team_logos\":[{\"team_logo\":{\"size\":\"large\",\"url\":\"https://i.imgur-ysports.com/7gfM7x2y.jpg\"}}]},[],{\"waiver_priority\":11},[],{\"number_of_moves\":\"17\"},{\"number_of_trades\":0},{\"roster_adds\":{\"coverage_type\":\"week\",\"coverage_value\":\"17\",\"value\":\"0\"}},{\"clinched_playoffs\":1},{\"league_scoring_type\":\"head\"},{\"managers\":[{\"manager\":{\"manager_id\":\"9\",\"nickname\":\"Aaron\",\"guid\":\"BGM4LFFEF6I33722EOFOWFYW4Q\",\"email\":\"amfische@yahoo.com\",\"image_url\":\"https://s.yimg.com/dh/ap/social/profile/profile_b64.png\"}}]}],{\"team_points\":{\"coverage_type\":\"week\",\"week\":\"15\",\"total\":\"105.90\"},\"team_projected_points\":{\"coverage_type\":\"week\",\"week\":\"15\",\"total\":\"98.77\"}}]},\"count\":2}}}},\"3\":{\"matchup\":{\"week\":\"15\",\"week_start\":\"2014-12-09\",\"week_end\":\"2014-12-15\",\"status\":\"postevent\",\"is_playoffs\":\"1\",\"is_consolation\":\"0\",\"is_tied\":0,\"winner_team_key\":\"331.l.106320.t.7\",\"0\":{\"teams\":{\"0\":{\"team\":[[{\"team_key\":\"331.l.106320.t.6\"},{\"team_id\":\"6\"},{\"name\":\"Nate top Joe Bottom\"},[],{\"url\":\"http://football.fantasysports.yahoo.com/archive/nfl/2014/106320/6\"},{\"team_logos\":[{\"team_logo\":{\"size\":\"large\",\"url\":\"http://l.yimg.com/dh/ap/fantasy/nfl/img/icon_11_100.png\"}}]},[],{\"waiver_priority\":13},[],{\"number_of_moves\":\"23\"},{\"number_of_trades\":0},{\"roster_adds\":{\"coverage_type\":\"week\",\"coverage_value\":\"17\",\"value\":\"0\"}},{\"clinched_playoffs\":1},{\"league_scoring_type\":\"head\"},{\"managers\":[{\"manager\":{\"manager_id\":\"6\",\"nickname\":\"Michael\",\"guid\":\"67ATOXK3E3KOO4R2Q2GYRTRZDE\",\"email\":\"sundevilmike@gmail.com\",\"image_url\":\"https://s.yimg.com/dh/ap/social/profile/profile_b64.png\"}}]}],{\"team_points\":{\"coverage_type\":\"week\",\"week\":\"15\",\"total\":\"83.00\"},\"team_projected_points\":{\"coverage_type\":\"week\",\"week\":\"15\",\"total\":\"100.49\"}}]},\"1\":{\"team\":[[{\"team_key\":\"331.l.106320.t.7\"},{\"team_id\":\"7\"},{\"name\":\"Wrecked 'em\"},[],{\"url\":\"http://football.fantasysports.yahoo.com/archive/nfl/2014/106320/7\"},{\"team_logos\":[{\"team_logo\":{\"size\":\"large\",\"url\":\"https://i.imgur-ysports.com/hrtDyxmy.jpg\"}}]},[],{\"waiver_priority\":5},[],{\"number_of_moves\":\"14\"},{\"number_of_trades\":\"1\"},{\"roster_adds\":{\"coverage_type\":\"week\",\"coverage_value\":\"17\",\"value\":\"0\"}},{\"clinched_playoffs\":1},{\"league_scoring_type\":\"head\"},{\"managers\":[{\"manager\":{\"manager_id\":\"7\",\"nickname\":\"Ian\",\"guid\":\"P5M4CH2WYAJ4MYYQPLQBIMAWYY\",\"email\":\"noitues@yahoo.com\",\"image_url\":\"https://s.yimg.com/dh/ap/social/profile/profile_b64.png\"}}]}],{\"team_points\":{\"coverage_type\":\"week\",\"week\":\"15\",\"total\":\"85.30\"},\"team_projected_points\":{\"coverage_type\":\"week\",\"week\":\"15\",\"total\":\"86.81\"}}]},\"count\":2}}}},\"count\":4}}}}],\"time\":\"140.49601554871ms\",\"copyright\":\"Data provided by Yahoo! and STATS, LLC\",\"refresh_rate\":\"31\"}}\n";

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

        testJSONParsing = Observable.create(new Observable.OnSubscribe<ArrayList<Matchup>>() {
            @Override
            public void call(Subscriber<? super ArrayList<Matchup>> subscriber) {
                ArrayList<Matchup> temp;
                YahooJsonParser yparser = new YahooJsonParser(test_JSON);
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

    private void testJSONDecode() {
        testJSONParsing
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Matchup>>() {
                    @Override
                    public void onNext(ArrayList<Matchup> matchups) {
                        textview_user_name.setText(matchups.get(0).getHomeTeam().getNickname());
                        Intent intent = new Intent(SimpleWidgetProvider.ACTION_REFRESH);
                        intent.putExtra("matchups", matchups);
                        getApplicationContext().sendBroadcast(intent);
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
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString(TOKEN, consumer.getToken());
        editor.putString(SECRET, consumer.getTokenSecret());
        editor.putString(OAUTH_SIGNATURE, consumer.getRequestParameters().getFirst(OAUTH_SIGNATURE));
        editor.putString(OAUTH_NONCE, consumer.getRequestParameters().getFirst(OAUTH_NONCE));
        editor.putString(OAUTH_SIGNATURE_METHOD, consumer.getRequestParameters().getFirst(OAUTH_SIGNATURE_METHOD));
        editor.putString(OAUTH_TIMESTAMP, consumer.getRequestParameters().getFirst(OAUTH_TIMESTAMP));

        editor.commit();

        BuildRestAdapter();

    }

    private void BuildRestAdapter() {
        //consumer.setTokenWithSecret(consumer.getToken(), consumer.getTokenSecret());
        service = ServiceFactory.createRetrofitService(YahooAPI.class, YahooAPI.SERVICE_ENDPOINT, consumer);
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

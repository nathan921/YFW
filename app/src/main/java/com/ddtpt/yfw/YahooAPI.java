package com.ddtpt.yfw;

import com.google.gson.JsonElement;

import retrofit.http.GET;
import rx.Observable;

/**
 * Created by e228596 on 7/11/2016.
 */
public interface YahooAPI {

    @GET("/fantasy/v2/users;use_login=1?format=json") ///games;is_available=1/teams?format=json")
    Observable<JsonElement> getUser();

    @GET("/fantasy/v2/league/359.l.755193/scoreboard?format=json")
    Observable<JsonElement> getMatchups();

}

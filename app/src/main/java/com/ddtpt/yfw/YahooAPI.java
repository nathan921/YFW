package com.ddtpt.yfw;

import com.google.gson.JsonElement;

import retrofit.http.GET;
import rx.Observable;

/**
 * Created by e228596 on 7/11/2016.
 */
public interface YahooAPI {
    String SERVICE_ENDPOINT = "http://fantasysports.yahooapis.com/fantasy/v2";

    @GET("/users;use_login=1/games;is_available=1/teams?format=json")
    Observable<JsonElement> getUser();

}

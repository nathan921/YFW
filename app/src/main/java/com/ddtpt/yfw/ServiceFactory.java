package com.ddtpt.yfw;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by e228596 on 7/11/2016.
 */
public class ServiceFactory {

    static <T> T createRetrofitService(final Class<T> clazz, final String endPoint, final RetrofitHttpOAuthConsumer consumer) {
        OkClient client = new SigningOkClient(consumer);

        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(endPoint)
                .setClient(client)
                .build();
        T service = restAdapter.create(clazz);
        return service;
    }
}

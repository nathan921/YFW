package com.ddtpt.yfw;

/**
 * Created by E228596 on 6/28/2016.
 */
import oauth.signpost.AbstractOAuthConsumer;
import oauth.signpost.http.HttpRequest;
import retrofit.client.Request;

public class RetrofitHttpOAuthConsumer extends AbstractOAuthConsumer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public RetrofitHttpOAuthConsumer(String consumerKey, String consumerSecret) {
        super(consumerKey, consumerSecret);
    }

    @Override
    protected HttpRequest wrap(Object request) {
        if (!(request instanceof retrofit.client.Request)) {
            throw new IllegalArgumentException("This consumer expects requests of type " + retrofit.client.Request.class.getCanonicalName());
        }
        return new HttpRequestAdapter((Request) request);
    }

}

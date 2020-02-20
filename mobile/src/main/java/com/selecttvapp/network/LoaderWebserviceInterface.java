package com.selecttvapp.network;

/**
 * Created by babin on 8/19/2017.
 */

public interface LoaderWebserviceInterface {
    public void onresponseLoaded(String result);
    public void ondataLoadingFailed();
    public void onNetworkError();
}

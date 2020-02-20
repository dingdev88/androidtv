package com.selecttvapp.webservice;


public interface WebserviceListener {


    public void onError(String message);

    public void onSuccess(final String message);
}

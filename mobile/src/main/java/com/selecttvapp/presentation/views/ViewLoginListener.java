package com.selecttvapp.presentation.views;

/**
 * Created by Appsolute dev on 06-Dec-17.
 */

public interface ViewLoginListener {
    void onLoginSuccess();

    void onLoginFailed(String failedMessage);
    void showNeedHelp(String url);
}

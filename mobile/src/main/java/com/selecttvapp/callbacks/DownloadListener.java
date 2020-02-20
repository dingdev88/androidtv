package com.selecttvapp.callbacks;

/**
 * Created by Appsolute dev on 11-Dec-17.
 */

public interface DownloadListener {
    void onStart(String message);
    void onLoading(String message);
    void onSuccess(String message);
    void onError(String error);
}

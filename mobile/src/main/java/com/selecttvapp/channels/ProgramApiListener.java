package com.selecttvapp.channels;


/**
 * Created by babin on 7/5/2017.
 */

public interface ProgramApiListener {
    void onProgramsLoaded(String categorySlug, String slug, Programs mPrograms);
    void onLoadingFailed();

    void onNetworkError();
}

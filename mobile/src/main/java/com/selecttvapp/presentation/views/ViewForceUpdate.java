package com.selecttvapp.presentation.views;

import com.selecttvapp.model.forceupdate.ForceUpdate;
import com.selecttvapp.ui.base.BaseView;

/**
 * Created by ocspl-72 on 10/1/18.
 */

public interface ViewForceUpdate extends BaseView {
    void onAppUpdateAvailable(boolean isAvailable, ForceUpdate update);

    void onAppUpdateSkipped();
}

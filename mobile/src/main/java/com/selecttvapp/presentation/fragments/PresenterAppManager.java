package com.selecttvapp.presentation.fragments;

import android.os.Handler;

import com.selecttvapp.common.Constants;
import com.selecttvapp.model.CategoryBean;
import com.selecttvapp.model.HorizontalListAppManager;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.presentation.views.ViewAppManagerListener;
import com.selecttvapp.ui.base.BasePresenter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 08-Dec-17.
 */

public class PresenterAppManager extends BasePresenter<ViewAppManagerListener> {
    private Handler handler = new Handler();

    public PresenterAppManager() {
    }

    public void loadAppCategories() {
        getViewState().showProgressDialog("Please Wait...");
        Thread thread = new Thread(() -> {
            try {
                JSONArray response = JSONRPCAPI.getAppCategories(Constants.CATEGORY_APPMANAGER);
                if (response == null) return;

                final ArrayList<CategoryBean> appCategoriesList = new ArrayList<>();
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject app_object = response.getJSONObject(i);
                        //String slug=app_object.getString("slug");
                        int id = app_object.getInt("id");
                        String name = app_object.getString("name");
                        if (name.equalsIgnoreCase("broadcast shows")) {
                            name = "Recommended";
                        }
                        if (name.equalsIgnoreCase("cable shows")) {
                            name = "Cable Networks";
                        }
                        appCategoriesList.add(new CategoryBean("", name, id));
                    }
                }
                handler.post(() -> getViewState().loadAppCategories(appCategoriesList));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                getViewState().stopProgressDialog();
            }
        });
        thread.start();
    }

    public void loadCategoryItems(final int categoryId) {
        getViewState().showProgressDialog("Please Wait...");
        Thread thread = new Thread(() -> {
            final ArrayList<HorizontalListAppManager> categoryItems = new ArrayList<>();
            try {
                JSONArray response = JSONRPCAPI.getAppsByCategories(categoryId);
                if (response == null) return;

                for (int i = 0; i < response.length(); i++) {
                    JSONObject object = response.getJSONObject(i);
                    HorizontalListAppManager item = HorizontalListAppManager.create(object);
                    if (item.getCarousel().getData_list().size() > 0)
                        categoryItems.add(item);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                getViewState().stopProgressDialog();
                handler.post(() -> getViewState().loadCategoryItems(categoryItems));
            }
        });
        thread.start();
    }

}

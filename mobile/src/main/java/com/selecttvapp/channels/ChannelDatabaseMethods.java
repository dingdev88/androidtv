package com.selecttvapp.channels;

import com.selecttvapp.ui.helper.MyApplication;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by babin on 8/18/2017.
 */

public class ChannelDatabaseMethods {

    public ChannelDatabaseMethods() {
    }

    public void saveChannelcategoriesList(final ArrayList<ChannelCategoryList> categorylist) {
        MyApplication.setAllCategorylist(categorylist);
    }

    public ArrayList<ChannelCategoryList> getChannelMaincategoriesList() {
        ArrayList<ChannelCategoryList> mChannelCategoryList = MyApplication.getAllCategorylist();
        if (mChannelCategoryList != null && mChannelCategoryList.size() > 0) {
            MenuFilter mMainCategoryFilter = new MenuFilter(mChannelCategoryList);
            ArrayList<ChannelCategoryList> channelMainCategoryList = mMainCategoryFilter.getFilterList();
            for (ChannelCategoryList menuItem : channelMainCategoryList) {
                MenuFilter mSubCategoryFilter = new MenuFilter(mChannelCategoryList, menuItem.getSlug());
                menuItem.setSubCategories(mSubCategoryFilter.getFilterList());
            }
            return channelMainCategoryList;
        } else {
            return null;
        }

    }

    public ArrayList<ChannelCategoryList> getChannelSubcategoriesList(String parentSlug) {
        ArrayList<ChannelCategoryList> mChannelCategoryList = new ArrayList<>();
        return mChannelCategoryList;
    }

    public void saveChannelList(final ArrayList<ChannelScheduler> channellist, final String categorySlug) {
        HashMap<String, ArrayList<ChannelScheduler>> mChannelList = MyApplication.getmChannelsList();
        if (mChannelList != null) {
            mChannelList.put(categorySlug, channellist);
        }
    }

    public ArrayList<ChannelScheduler> getChannelListBycategorySlug(final String categorySlug) {
        ArrayList<ChannelScheduler> mChannelScheduler = new ArrayList<>();
        HashMap<String, ArrayList<ChannelScheduler>> mChannelList = MyApplication.getmChannelsList();
        if (mChannelList != null && mChannelList.size() > 0 && mChannelList.containsKey(categorySlug)) {
            mChannelScheduler = mChannelList.get(categorySlug);
        }
        return mChannelScheduler;
    }

    public void updateProgramList(String categorySlug, final String mSlug, final Programs mPrograms) {
        HashMap<String, ArrayList<ChannelScheduler>> mChannelList = MyApplication.getmChannelsList();
        if (mChannelList != null && mChannelList.size() > 0 && mChannelList.containsKey(categorySlug)) {
            ChannelScheduler mChannelScheduler = VideoFilter.getSelectedChannel(mChannelList.get(categorySlug), mSlug);
            if (mChannelScheduler != null)
                mChannelScheduler.setPrograms(mPrograms);
        }
    }


    public void updateStream(String categorySlug, String sSlug, ArrayList<Streams> mStream) {
        HashMap<String, ArrayList<ChannelScheduler>> mChannelList = MyApplication.getmChannelsList();
        if (mChannelList != null && mChannelList.size() > 0 && mChannelList.containsKey(categorySlug)) {
            ChannelScheduler mChannelScheduler = VideoFilter.getSelectedChannel(mChannelList.get(categorySlug), sSlug);
            if (mChannelScheduler != null)
                mChannelScheduler.setStreams(mStream);
        }
    }

    public Programs getPrograms(String parentcategorySlug, String slug) {
        Programs mPrograms = null;
        HashMap<String, ArrayList<ChannelScheduler>> mChannelList = MyApplication.getmChannelsList();
        if (mChannelList != null && mChannelList.size() > 0 && mChannelList.containsKey(parentcategorySlug)) {
            ChannelScheduler mChannelScheduler = VideoFilter.getSelectedChannel(mChannelList.get(parentcategorySlug), slug);
            if (mChannelScheduler != null)
                mPrograms = mChannelScheduler.getPrograms();
        }
        return mPrograms;
    }

    public ArrayList<Streams> getStreams(String parentcategorySlug, String slug) {
        ArrayList<Streams> mStreams = new ArrayList<>();
        HashMap<String, ArrayList<ChannelScheduler>> mChannelList = MyApplication.getmChannelsList();
        if (mChannelList != null && mChannelList.size() > 0 && mChannelList.containsKey(parentcategorySlug)) {
            ChannelScheduler mChannelScheduler = VideoFilter.getSelectedChannel(mChannelList.get(parentcategorySlug), slug);
            if (mChannelScheduler != null)
                mStreams = mChannelScheduler.getStreams();
        }
        return mStreams;
    }
}

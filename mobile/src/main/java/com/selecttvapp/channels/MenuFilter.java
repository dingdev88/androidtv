package com.selecttvapp.channels;

import android.support.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Created by babin on 7/4/2017.
 */

public class MenuFilter {
    public ArrayList<ChannelCategoryList> getFilterList() {
        return resultList;
    }

    private ArrayList<ChannelCategoryList> filterList=new ArrayList<>();
    private ArrayList<ChannelCategoryList> resultList=new ArrayList<>();
    private String filterString;

    public MenuFilter(ArrayList<ChannelCategoryList> filterList) {
        this.filterList = filterList;
        Collection<ChannelCategoryList> mainMenuList = Collections2.filter(filterList,new MenuPredicate());
        resultList.addAll(mainMenuList);
    }

    public MenuFilter(ArrayList<ChannelCategoryList> filterList, String filterString) {
        this.filterList = filterList;
        this.filterString = filterString;
        Collection<ChannelCategoryList> subMenuList = Collections2.filter(filterList,new SubMenuPredicate());
        resultList.addAll(subMenuList );
    }


    class MenuPredicate implements Predicate<ChannelCategoryList> {

        @Override
        public boolean apply(@Nullable ChannelCategoryList list) {
            return !list.isLeaf()||list.getParent()==null;
        }
    }

    class SubMenuPredicate implements Predicate<ChannelCategoryList> {

        @Override
        public boolean apply(@Nullable ChannelCategoryList list) {
            return (list.getParent()!=null&&list.getParent().equalsIgnoreCase(filterString));
        }
    }
}

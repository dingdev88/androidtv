package com.selecttvapp.ui.bean;

/**
 * Created by ${Madhan} on 9/21/2016.
 */
public class HomeScreenArray {
    int mHomescreenGridImages;
    String mHomeScreenGridname;
    public HomeScreenArray(int mHomescreenGridImages,String mHomeScreenGridname)
    {
        this.mHomescreenGridImages=mHomescreenGridImages;
        this.mHomeScreenGridname=mHomeScreenGridname;

    }
    public String getmHomeScreenGridname() {
        return mHomeScreenGridname;
    }

    public void setmHomeScreenGridname(String mHomeScreenGridname) {
        this.mHomeScreenGridname = mHomeScreenGridname;
    }

    public int getmHomescreenGridImages() {
        return mHomescreenGridImages;
    }

    public void setmHomescreenGridImages(int mHomescreenGridImages) {
        this.mHomescreenGridImages = mHomescreenGridImages;
    }


}

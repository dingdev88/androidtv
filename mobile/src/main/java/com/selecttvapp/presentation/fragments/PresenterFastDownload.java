package com.selecttvapp.presentation.fragments;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.selecttvapp.R;
import com.selecttvapp.callbacks.DownloadListener;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.FontHelper;
import com.selecttvapp.common.Permission;
import com.selecttvapp.model.CategoryBean;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.presentation.views.ViewFastDownload;
import com.selecttvapp.ui.base.BasePresenter;
import com.selecttvapp.ui.fragments.FastDownloadFragment;
import com.selecttvapp.ui.helper.MyApplication;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Appsolute dev on 20-Dec-17.
 */

public class PresenterFastDownload extends BasePresenter<ViewFastDownload> {
    private FontHelper fontHelper = new FontHelper();
    private Handler handler = new Handler();

    private String lastVisibleApkName = "";
    private String lastApkUrl = "";
    private DownloadListener downloadListener = null;

    public PresenterFastDownload() {
    }

    /*accending order by apps by weight*/
    public static ArrayList<CategoryBean> sortAppsWeight(ArrayList<CategoryBean> appsList) {
        Collections.sort(appsList, (lhs, rhs) -> {
            if (lhs.getWeight() == null || rhs.getWeight() == null || lhs.getWeight() == -1 || rhs.getWeight() == -1)
                return 1;

            if (lhs.getWeight() < rhs.getWeight())
                return -1;
            if (lhs.getWeight().equals(rhs.getWeight()))
                return 0;
            if (lhs.getWeight() > rhs.getWeight())
                return 1;
            return 0;
        });
        return appsList;
    }


//    public ArrayList<CategoryBean> getCategoryList() {
//        ArrayList<CategoryBean> categoriesList = new ArrayList<>();
//        categoriesList.add(new CategoryBean("", "ESSENTIALS", 7));
//        categoriesList.add(new CategoryBean("", "BROADCAST", 9));
//        categoriesList.add(new CategoryBean("", "CABLE", 11));
//        categoriesList.add(new CategoryBean("", "SUBSCRIPTIONS", 12));
//        categoriesList.add(new CategoryBean("", "MOVIES", 15));
//        categoriesList.add(new CategoryBean("", "OTHER", 10));
//        return categoriesList;
//    }

    public ArrayList<CategoryBean> getLists(int position) {
        ArrayList<CategoryBean> list = new ArrayList<>();
        switch (position) {
            case 0:
                return getEssentialsList();
            case 1:
                return getBroadcastsList();
            case 2:
                return getCablesList();
            case 3:
                return getSubscriptionsList();
            case 4:
                return getMoviesList();
            case 5:
                return getOthersList();
        }
        return list;
    }

    public ArrayList<CategoryBean> getEssentialsList() {
        ArrayList<CategoryBean> essentialsList = new ArrayList<>();
        essentialsList.add(new CategoryBean("apks/abc.apk", "ABC", R.drawable.abc, "com.disney.datg.videoplatforms.android.abc"));
        essentialsList.add(new CategoryBean("apks/nbc.apk", "NBC", R.drawable.nbc, "com.zumobi.msnbc"));
        essentialsList.add(new CategoryBean("apks/cbs.apk", "CBS", R.drawable.cbs, "com.cbs.app"));
        essentialsList.add(new CategoryBean("apks/crackle.apk", "Crackle", R.drawable.crackle, "com.crackle.androidtv"));
        essentialsList.add(new CategoryBean("apks/fox.apk", "FOX", R.drawable.fox, "com.fox.now"));
//        mEssentialarray.add(new CategoryBean("apks/popcornflix.apk", "Popcornflix", R.drawable.popcorn_flix));
        essentialsList.add(new CategoryBean("apks/vudu.apk", "Vudu", R.drawable.vudu_icon, "air.com.vudu.air.DownloaderTablet"));
        essentialsList.add(new CategoryBean("apks/netflix.apk", "Netflix_v4", R.drawable.netflix_active, "com.netflix.mediaclient"));
        essentialsList.add(new CategoryBean("apks/pbs.apk", "PBS", R.drawable.pbs_network, "com.pbs.video"));
        return essentialsList;
    }

    public ArrayList<CategoryBean> getBroadcastsList() {
        ArrayList<CategoryBean> broadcastsList = new ArrayList<>();
        broadcastsList.add(new CategoryBean("apks/abc.apk", "ABC", R.drawable.abc, "com.disney.datg.videoplatforms.android.abc"));
        broadcastsList.add(new CategoryBean("apks/cbs.apk", "CBS", R.drawable.cbs, "com.cbs.app"));
        broadcastsList.add(new CategoryBean("apks/cw.apk", "The CW", R.drawable.cw, "com.cw.fullepisodes.android"));
        broadcastsList.add(new CategoryBean("apks/fox.apk", "FOX", R.drawable.fox, "com.fox.now"));
        broadcastsList.add(new CategoryBean("apks/nbc.apk", "NBC", R.drawable.nbc, "com.zumobi.msnbc"));
        broadcastsList.add(new CategoryBean("apks/pbs.apk", "PBS", R.drawable.pbs_network, "com.pbs.video"));
        return broadcastsList;
    }

    public ArrayList<CategoryBean> getCablesList() {
        ArrayList<CategoryBean> cablesList = new ArrayList<>();
        cablesList.add(new CategoryBean("apks/ae.apk", "A&E", R.drawable.a_e, "com.aetn.aetv.watch"));
        cablesList.add(new CategoryBean("apks/amc.apk", "AMC", R.drawable.amc, "com.amctve.amcfullepisodes"));
        cablesList.add(new CategoryBean("apks/cartoonnetwork.apk", "Cartoon Network", R.drawable.cartoon_network, "com.turner.cnvideoapp"));
        cablesList.add(new CategoryBean("apks/comedycentral.apk", "Comedy Central", R.drawable.comedy_central, "com.vmn.android.comedycentral"));
        cablesList.add(new CategoryBean("apks/disneychannel.apk", "Disney Channel", R.drawable.disney_channel, "com.disney.dedisneychannel_goo"));
        cablesList.add(new CategoryBean("apks/disneyjunior.apk", "Disney Junior", R.drawable.disney_junior, "com.disney.datg.videoplatforms.android.watchdjr"));
        cablesList.add(new CategoryBean("apks/epix.apk", "EPIX", R.drawable.epix, "com.epix.epix"));
        cablesList.add(new CategoryBean("apks/fxnow.apk", "FXNOW", R.drawable.fx_networks, "com.fxnetworks.fxnow"));
        cablesList.add(new CategoryBean("apks/hgtv.apk", "HGTV", R.drawable.hgtv, "com.hgtv.watcher"));
        cablesList.add(new CategoryBean("apks/history.apk", "HISTORY", R.drawable.history, "com.aetn.history.watch"));
        cablesList.add(new CategoryBean("apks/ifc.apk", "Watch IFC", R.drawable.ifc, "com.ifc.ifcapp"));
        cablesList.add(new CategoryBean("apks/lifetime.apk", "Lifetime", R.drawable.lifetime, "com.aetn.lifetime.watch"));
        cablesList.add(new CategoryBean("apks/maxgo.apk", "MAX GO", R.drawable.max_go, "com.MAXGo"));
        cablesList.add(new CategoryBean("apks/mtv.apk", "MTV", R.drawable.mtv, "com.mtvn.mtvPrimeAndroid"));
        cablesList.add(new CategoryBean("apks/nickstudio.apk", "Nick Studio", R.drawable.nick, "com.nick.studioapp"));
//        cablesList.add(new CategoryBean("apks/nickstudio.apk", "Nick Studio", R.drawable.nick_jr, ""));
        cablesList.add(new CategoryBean("apks/redbulltv.apk", "Redbull", R.drawable.redbull_tv, "com.nousguide.android.rbtv"));
        cablesList.add(new CategoryBean("apks/spike.apk", "Spike", R.drawable.spike, "com.vmn.android.spike"));
        cablesList.add(new CategoryBean("apks/starz.apk", "Starz", R.drawable.starz_icon, "com.bydeluxe.d3.android.program.starz"));
        cablesList.add(new CategoryBean("apks/tbs.apk", "TBS", R.drawable.tbs_icon, "com.turner.tbs.android.networkapp"));
        cablesList.add(new CategoryBean("apks/tnt.apk", "TNT", R.drawable.tnt_icon, "com.turner.tnt.android.networkapp"));
        cablesList.add(new CategoryBean("apks/trutv.apk", "TruTV", R.drawable.trutv_icon, "com.turner.trutv"));
        cablesList.add(new CategoryBean("apks/usanow.apk", "USA", R.drawable.usa_icon, "com.usanetwork.watcher"));
        cablesList.add(new CategoryBean("apks/vh1.apk", "VH1", R.drawable.vh1_icon, "com.mtvn.vh1android"));
        cablesList.add(new CategoryBean("apks/viceland.apk", "Viceland", R.drawable.viceland_icon, "com.vice.viceland"));
        cablesList.add(new CategoryBean("apks/xfinity.apk", "Xfinity", R.drawable.xfinity_icon, "com.xfinity.cloudtvr"));
        return cablesList;
    }

    public ArrayList<CategoryBean> getSubscriptionsList() {
        ArrayList<CategoryBean> subscriptionsList = new ArrayList<>();
        subscriptionsList.add(new CategoryBean("apks/amazonprimenow.apk", "amazon_prime", R.drawable.amazon_prime, "com.amazon.avod.thirdpartyclient"));
        subscriptionsList.add(new CategoryBean("apks/cbs.apk", "CBS_ALL_ACCESS", R.drawable.cbs_all_access, "com.cbs.ott"));
        subscriptionsList.add(new CategoryBean("apks/feeln.apk", "Feeln", R.drawable.feeln, "com.feeln.android"));
        subscriptionsList.add(new CategoryBean("apks/hbo.apk", "HBO_Now", R.drawable.hbo_now, "com.hbo.hbonow"));
        subscriptionsList.add(new CategoryBean("apks/netflix.apk", "Netflix", R.drawable.netflix_active, "com.netflix.mediaclient"));
        subscriptionsList.add(new CategoryBean("apks/seeso.apk", "seeso", R.drawable.seeso, "seeso.com"));
        subscriptionsList.add(new CategoryBean("apks/sundance.apk", "Sundance_Now", R.drawable.sundance_now, "com.dramafever.docclub"));
        return subscriptionsList;
    }

    public ArrayList<CategoryBean> getMoviesList() {
        ArrayList<CategoryBean> moviesList = new ArrayList<>();
        moviesList.add(new CategoryBean("apks/crackle.apk", "CRANCKLE", R.drawable.crackle, "com.crackle.androidtv"));
        moviesList.add(new CategoryBean("apks/epix.apk", "epix", R.drawable.epix, "com.epix.epix"));
        moviesList.add(new CategoryBean("apks/hulu.apk", "hulu", R.drawable.hulu_active, "com.hulu.plus"));
        moviesList.add(new CategoryBean("apks/popcornflix.apk", "Popcorn-Flix", R.drawable.popcorn_flix, "com.curiousbrain.popcornflix"));
        moviesList.add(new CategoryBean("apks/vudu.apk", "Vudu", R.drawable.vudu_icon, "air.com.vudu.air.DownloaderTablet"));
        return moviesList;
    }

    public ArrayList<CategoryBean> getOthersList() {
        ArrayList<CategoryBean> othersList = new ArrayList<>();
        othersList.add(new CategoryBean("https://play.google.com/store/apps/details?id=com.imdb.mobile&hl=en", "IMDb Movies & TV", R.drawable.imdb_app_image, "com.imdb.mobile"));
//        othersList.add(new CategoryBean("https://play.google.com/store/apps/details?id=org.xbmc.kodi&hl=en", "Kodi", R.drawable.kodi, "org.xbmc.kodi"));
        othersList.add(new CategoryBean("https://play.google.com/store/apps/details?id=com.samsung.android.video360&hl=en", "Samsung VR", R.drawable.vr_app_image, "com.samsung.android.video360"));
        othersList.add(new CategoryBean("https://play.google.com/store/apps/details?id=com.foxsports.videogo&hl=en", "FOX Sports GO", R.drawable.fox_sports_go_app, "com.foxsports.videogo"));
        othersList.add(new CategoryBean("https://play.google.com/store/apps/details?id=com.tekoia.sure.activities&hl=en", "SURE Universal Remote for TV", R.drawable.sure_universalremotetv, "com.tekoia.sure.activities"));
        return othersList;
    }

    public SectionPagerAdapter getSectionPagerAdapter(FragmentManager fm, ArrayList<CategoryBean> categoryList) {
        return new SectionPagerAdapter(fm, categoryList);
    }

    public void setOnPageChangeListener(SmartTabLayout smartTabLayout) {
        final LinearLayout lyTabs = (LinearLayout) smartTabLayout.getChildAt(0);
        changeTabsTitleTypeFace(lyTabs, 0);
        smartTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                changeTabsTitleTypeFace(lyTabs, position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    private void changeTabsTitleTypeFace(LinearLayout ly, int position) {
        for (int j = 0; j < ly.getChildCount(); j++) {
            TextView tvTabTitle = (TextView) ly.getChildAt(j);
            fontHelper.applyFont(fontHelper.tfMyriadProRegular, tvTabTitle);
            if (j == position) fontHelper.applyFont(fontHelper.tfMyriadProRegular, tvTabTitle);
        }
    }

    public boolean isApkAlreadyExist(String apkName) {
        try {
            File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + apkName);
            if (file.exists())
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void downloadLastSelectedApk() {
        if (!lastApkUrl.isEmpty() && downloadListener != null)
            mDownloadApk(lastApkUrl, getActivity(), downloadListener);
    }

    public void mDownloadApk(String url, Context context, final DownloadListener downloadListener) {
        lastApkUrl = url;
        this.downloadListener = downloadListener;
        if (Build.VERSION.SDK_INT < 9) {
            throw new RuntimeException("Method requires API level 9 or above");
        }


        if (!Permission.getInstance().checkPermission(getActivity(), Permission.getInstance().WRITE_EXTERNAL_STORAGE, Permission.CODE_WRITE_EXTERNAL_STORAGE))
            return;

        final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        if (Build.VERSION.SDK_INT >= 11) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setTitle(URLUtil.guessFileName(url, "", Intent.ACTION_VIEW));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, "", Intent.ACTION_VIEW));

        final DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        try {
            try {
                dm.enqueue(request);
                getViewState().showMessage("Downloading File");
            } catch (SecurityException e) {
                if (Build.VERSION.SDK_INT >= 11) {
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                }
                dm.enqueue(request);
                getViewState().showMessage("Downloading File");
            }

            final DownloadManager.Query query = new DownloadManager.Query();
            if (query != null) {
                query.setFilterByStatus(DownloadManager.STATUS_FAILED | DownloadManager.STATUS_PAUSED | DownloadManager.STATUS_SUCCESSFUL | DownloadManager.STATUS_RUNNING | DownloadManager.STATUS_PENDING);
            }

            final Handler handler = new Handler();
            handler.post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void run() {
                    boolean downloading = true;
                    try {
                        Cursor c = dm.query(query);
                        if (c.moveToFirst()) {
                            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                            if (status == DownloadManager.STATUS_RUNNING) {
                                downloadListener.onLoading("Downloading..");
                                c.close();
                            } else if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                downloadListener.onSuccess("Download successful");
                                downloading = false;
                                String apkName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
                                String title = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                                if (!apkName.isEmpty()) {
                                    installApk(apkName, true);
                                }
                                c.close();
                            } else if (status == DownloadManager.STATUS_FAILED) {
                                downloadListener.onError("");
                                downloading = false;
                            } else {
                                c.close();
                            }
                        } else c.close();
                    } catch (Exception e) {
                        downloadListener.onError(e.getMessage());
                        e.printStackTrace();
                    } finally {
                        if (downloading && getActivity() != null && !getActivity().isDestroyed())
                            handler.postDelayed(this, 1000);
                        else handler.removeCallbacks(this);
                    }
                }
            });
        }
        // if the download manager app has been disabled on the device
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void installLastSelectedApk() {
        if (!lastVisibleApkName.isEmpty())
            installApk(lastVisibleApkName);
    }

    public void installApk(String apkName) {
        installApk(apkName, false);
    }

    public void installApk(String apkName, boolean isAlertRequired) {
        installApk(apkName, isAlertRequired, false);
    }

    public void installApk(String apkName, boolean isAlertRequired, boolean allowOpenSecurity) {
        lastVisibleApkName = apkName;
        if (!Permission.checkUnknownSourceInstallation(getActivity(), isAlertRequired, allowOpenSecurity))
            return;

        File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + apkName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    public void getCategoryList() {
        getViewState().showProgressDialog("Please wait...");
        Thread thread = new Thread(() -> {
            try {
                JSONArray jsonArray = JSONRPCAPI.getAppCategories(Constants.CATEGORY_FAST_DOWNLOAD);
//                JSONArray jsonArray = JSONRPCAPI.getAppCategories();
                if (jsonArray != null) {
                    ArrayList<CategoryBean> categoryList = MyApplication.getGson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<CategoryBean>>() {
                    }.getType());
                    handler.post(() -> getViewState().loadAppsList(sortAppsWeight(categoryList)));
                } else handler.post(() -> getViewState().onError(""));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                getViewState().stopProgressDialog();
            }
        });
        thread.start();
    }

    public void loadAppsList(Object categoryId) {
        Thread thread = new Thread(() -> {
            JSONArray jsonArray = JSONRPCAPI.getAllApps(categoryId);
            if (jsonArray != null) {
                ArrayList<CategoryBean> listItems = MyApplication.getGson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<CategoryBean>>() {
                }.getType());
                handler.post(() -> getViewState().loadAppsList(listItems));
            } else handler.post(() -> getViewState().onError(""));
        });
        thread.start();
    }

    public class SectionPagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<CategoryBean> categoryList = new ArrayList<>();

        public SectionPagerAdapter(FragmentManager fm, ArrayList<CategoryBean> categoryList) {
            super(fm);
            this.categoryList = categoryList;
        }

        @Override
        public Fragment getItem(int position) {
            return FastDownloadFragment.newInstance(position, categoryList.get(position).getId());
        }

        @Override
        public int getCount() {
            return categoryList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return categoryList.get(position).getName();
        }
    }
}

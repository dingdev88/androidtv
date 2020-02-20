package com.selecttvapp.parser;


import com.selecttvapp.model.SideMenu;
import com.selecttvapp.model.SideMenuChild;
import com.selecttvapp.webservice.WebserviceListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class DataParser {
    public static ArrayList<SideMenu> sideMenus = new ArrayList<>();

    public static void loadSideMenu(JSONArray jsonObjectArray, WebserviceListener webserviceListener) {
        if (jsonObjectArray != null) {
            try {
                sideMenus = new ArrayList<>();

                for (int i = 0; i < jsonObjectArray.length(); i++) {

                    JSONObject jsonObject = jsonObjectArray.getJSONObject(i);
                    SideMenu sideMenu = new SideMenu();
                    if (jsonObject.has("name")) {
                        sideMenu.setName(jsonObject.getString("name").replace("&amp;", "&").replace("amp:", ""));
                    } else {
                        sideMenu.setName("");
                    }

                    if (jsonObject.has("url")) {
                        sideMenu.setUrl(jsonObject.getString("url"));
                    } else {
                        sideMenu.setUrl("");
                    }

                    if (jsonObject.has("slug")) {
                        sideMenu.setSlug(jsonObject.getString("slug"));
                    } else {
                        sideMenu.setSlug("");
                    }
                    if (jsonObject.has("position_android"))
                        sideMenu.setMenuPosition(jsonObject.getInt("position_android"));
                    sideMenu.setClick(false);
                    ArrayList<SideMenuChild> sideMenuChildren = new ArrayList<>();
                    if (jsonObject.has("childs")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("childs");

                        if (jsonArray.length() > 0) {

                            for (int k = 0; k < jsonArray.length(); k++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(k);
                                SideMenuChild sideMenuChild = new SideMenuChild();
                                if (jsonObject1.has("name"))
                                    sideMenuChild.setName(jsonObject1.getString("name").replace("&amp;", "&").replace("amp:", ""));
                                if (jsonObject1.has("url"))
                                    sideMenuChild.setUrl(jsonObject1.getString("url"));
                                if (jsonObject1.has("type")) {
                                    sideMenuChild.setType(jsonObject1.getString("type"));
                                } else {
                                    sideMenuChild.setType("");
                                }
                                if (jsonObject1.has("id")) {
                                    sideMenuChild.setId(jsonObject1.getString("id"));
                                } else {
                                    sideMenuChild.setId("");
                                }
                                if (jsonObject1.has("slug")) {
                                    sideMenuChild.setSlug(jsonObject1.getString("slug"));
                                } else {
                                    sideMenuChild.setSlug("");
                                }
                                sideMenuChild.setClick(false);
                                ArrayList<SideMenuChild> sideMenuChildren1 = new ArrayList<>();
                                if (jsonObject1.has("childs")) {
                                    JSONArray jsonArray1 = jsonObject1.getJSONArray("childs");

                                    if (jsonArray1.length() > 0) {
                                        for (int j = 0; j < jsonArray1.length(); j++) {
                                            JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                                            SideMenuChild sideMenuChild1 = new SideMenuChild();
                                            if (jsonObject2.has("name"))
                                                sideMenuChild1.setName(jsonObject2.getString("name").replace("&amp;", "&").replace("amp:", ""));
                                            if (jsonObject2.has("url"))
                                                sideMenuChild1.setUrl(jsonObject2.getString("url"));
                                            if (jsonObject2.has("type")) {
                                                sideMenuChild1.setType(jsonObject2.getString("type"));
                                            } else {
                                                sideMenuChild1.setType("");
                                            }
                                            if (jsonObject2.has("id")) {
                                                sideMenuChild1.setId(jsonObject2.getString("id"));
                                            } else {
                                                sideMenuChild1.setId("");
                                            }
                                            if (jsonObject2.has("slug")) {
                                                sideMenuChild1.setSlug(jsonObject2.getString("slug"));
                                            } else {
                                                sideMenuChild1.setSlug("");
                                            }
                                            sideMenuChild1.setClick(false);
                                            if (!sideMenuChild1.getType().equals("") || !sideMenuChild1.getSlug().equals(""))
                                                sideMenuChildren1.add(sideMenuChild1);
                                        }

                                    }

                                }
                                sideMenuChild.setSideMenuChild(sideMenuChildren1);
                                if (!sideMenuChild.getName().equalsIgnoreCase("live events") && !sideMenuChild.getName().equalsIgnoreCase("documentaries") /*&& !sideMenuChild.getName().equalsIgnoreCase("$ subscriptions")*/)
                                    if (!sideMenuChild.getType().equals("") || !sideMenuChild.getSlug().equals("") || sideMenuChild.getSideMenuChild().size() > 0)
                                        sideMenuChildren.add(sideMenuChild);
                            }

                        }

                    }
                    sideMenu.setSideMenuChild(sideMenuChildren);
                    if (!sideMenu.getName().equalsIgnoreCase("music") /*&& !sideMenu.getName().equalsIgnoreCase("movies")*/) {
//                        if (sideMenu.getName().equalsIgnoreCase("tv shows")) {
//                            sideMenu.setName("On Demand");
//                            sideMenu.setUrl("custom/demand");
//                            sideMenu.setSlug("custom/demand");
//                            sideMenu.setClick(false);
//
//                            ArrayList<SideMenuChild> onDemandSideMenuChildrens = new ArrayList<>();
//                            sideMenu.setSideMenuChild(onDemandSideMenuChildrens);
//                            /// Suggesstions ///
//                            onDemandSideMenuChildrens.add(getChildSideMenu("", "Suggestions", "ondemand_suggestions", "/custom/Suggestion", "home", false));
//                            /// TV shows ///
//                            onDemandSideMenuChildrens.add(getChildSideMenu("", "TV Shows", "shows", "/custom/TVShows", "/custom/TVShows", false));
//                            /// Movies ///
//                            onDemandSideMenuChildrens.add(getChildSideMenu("", "Movies", "movies", "/custom/Movies", "/custom/Movies", false));
//                            /// $ pay per view ///
//                            onDemandSideMenuChildrens.add(getChildSideMenu("", "$ Pay Per View", "payperview", "/custom/payperview", "/custom/payperview", false));
//                            /// subscriptions ///
//                            onDemandSideMenuChildrens.add(getChildSideMenu("", "$ Subscriptions", "ondemand_subscriptions", "/custom/subscriptions", "/custom/subscriptions", false));
//                        }
                        if (!sideMenu.getType().equals("") || !sideMenu.getSlug().equals("") || sideMenu.getSideMenuChild().size() > 0)
                            if (!sideMenu.getName().equalsIgnoreCase("home") && !sideMenu.getSlug().equalsIgnoreCase("home")) // hide home button
                                sideMenus.add(sideMenu);

//                        if (sideMenus.size() > 0) {
//                            sideMenus = sortMenus(sideMenus);
//                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static SideMenuChild getChildSideMenu(String id, String name, String type, String url, String slug, boolean setClick) {
        SideMenuChild subscriptionSideMenuChild = new SideMenuChild();
        subscriptionSideMenuChild.setId(id);
        subscriptionSideMenuChild.setName(name);
        subscriptionSideMenuChild.setType(type);
        subscriptionSideMenuChild.setUrl(url);
        subscriptionSideMenuChild.setSlug(slug);
        subscriptionSideMenuChild.setClick(setClick);
        return subscriptionSideMenuChild;
    }

    /*accending order by menu position*/
    public static ArrayList<SideMenu> sortMenus(ArrayList<SideMenu> sideMenus) {
        Collections.sort(sideMenus, new Comparator<SideMenu>() {
            @Override
            public int compare(SideMenu lhs, SideMenu rhs) {
                if (lhs.getMenuPosition() == -1 || rhs.getMenuPosition() == -1)
                    return 1;

                if (lhs.getMenuPosition() < rhs.getMenuPosition())
                    return -1;
                if (lhs.getMenuPosition() == rhs.getMenuPosition())
                    return 0;
                if (lhs.getMenuPosition() > rhs.getMenuPosition())
                    return 1;
                return 0;
            }
        });
        return sideMenus;
    }
}



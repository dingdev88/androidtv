package com.selecttvapp.model;

import com.google.gson.annotations.SerializedName;
import com.selecttvapp.ui.helper.MyApplication;

import org.json.JSONObject;

/**
 * Created by Ocs pl-79(17.2.2016) on 7/22/2016.
 */
public class HorizontalListAppManager {

    @SerializedName("network")
    private Network network;
    @SerializedName("link")
    private String link = "";
    @SerializedName("name")
    private String name = "";
    @SerializedName("carousel")
    private Carousel carousel;
    @SerializedName("package")
    private String _package = "";
    @SerializedName("image")
    private String image = "";
    @SerializedName("size")
    private String size = "";
    @SerializedName("id")
    private Integer id;
    @SerializedName("slug")
    private String slug = "";

    public HorizontalListAppManager() {
    }

//    public HorizontalListAppManager(int id, int network_id, String name, String source, String network_image, String size, String network_package, String network_link, String network_name, ArrayList<HorizontalListitemBean> data_list) {
//        this.id = id;
//        this.network_id = network_id;
//        this.name = name;
//        this.source = source;
//        this.network_image = network_image;
//        this.size = size;
//        this.data_list = data_list;
//        this.network_package = network_package;
//        this.network_link = network_link;
//        this.network_name = network_name;
//    }

//    public HorizontalListAppManager(JSONObject response) {
//        try {
//            if (response.has("network")) {
//                try {
//                    Object net_obj = response.get("network");
//                    if (net_obj instanceof JSONObject) {
//                        JSONObject network_object = response.getJSONObject("network");
//                        network_id = network_object.getInt("id");
//                        network_name = network_object.getString("name");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (response.has("network_image"))
//                network_image = response.getString("network_image");
//            else if (response.has("image"))
//                network_image = response.getString("image");
//
//            if (response.has("size"))
//                network_image = response.getString("size");
//
//            if (response.has("link"))
//                network_link = response.getString("link");
//
//            if (response.has("package"))
//                network_package = response.getString("package");
//
//
//            if (response.has("carousel")) {
//                JSONObject carousel_object = response.getJSONObject("carousel");
//                Carousel carousel = new Carousel(carousel_object);
//                data_list = carousel.getData_list();
//
//            } else if (response.has("items")) {
//                JSONArray itemsarray = response.getJSONArray("items");
//                for (int j = 0; j < itemsarray.length(); j++)
//                    data_list.add(new HorizontalListitemBean(itemsarray.getJSONObject(j)));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static HorizontalListAppManager create(JSONObject jsonObject) {
        try {
            HorizontalListAppManager item = MyApplication.getGson().fromJson(jsonObject.toString(), HorizontalListAppManager.class);
            String filterResponse = MyApplication.getGson().toJson(item);
            return MyApplication.getGson().fromJson(filterResponse, HorizontalListAppManager.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HorizontalListAppManager();
    }

    public void setId(int id) {
        this.id = id;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Carousel getCarousel() {
        return carousel;
    }

    public void setCarousel(Carousel carousel) {
        this.carousel = carousel;
    }

    public String getPackage() {
        return _package;
    }

    public void setPackage(String _package) {
        this._package = _package;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}

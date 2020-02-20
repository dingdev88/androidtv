package com.selecttvapp.presentation.views;

import com.selecttvapp.model.Carousel;
import com.selecttvapp.model.Slider;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 23-Nov-17.
 */

public interface ViewSubscriptionsOffersListener {
    void loadContent(ArrayList<Slider> sliders, ArrayList<Carousel> carousels);
}

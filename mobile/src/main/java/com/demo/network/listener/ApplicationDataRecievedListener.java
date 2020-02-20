package com.demo.network.listener;


import com.demo.network.model.Data;

import java.util.List;

public interface ApplicationDataRecievedListener {

    public void onResultRecived(List<Data> data);
}

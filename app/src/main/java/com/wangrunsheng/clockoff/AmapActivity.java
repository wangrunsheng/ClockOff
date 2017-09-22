package com.wangrunsheng.clockoff;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.orhanobut.logger.Logger;

/**
 * Created by Russell on 2017/9/14.
 */

public class AmapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("AmapActivity is created.");
        setContentView(R.layout.activity_amap);
        MapView mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        AMap aMap = mapView.getMap();
    }
}

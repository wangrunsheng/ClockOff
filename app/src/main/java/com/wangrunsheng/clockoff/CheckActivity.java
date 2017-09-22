package com.wangrunsheng.clockoff;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.orhanobut.logger.Logger;
import com.wangrunsheng.clockoff.tools.MyLocationListener;

/**
 * Check reminder activity.
 * Created by Russell on 2017/9/19.
 */

public class CheckActivity extends AppCompatActivity {
    LatLng mPosition;// my seat.
    LatLng mHuahui, mCurrent;
    public LocationClient mLocationClient = null;
    TextView mLatLngTv, mDistanceTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_check);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLatLngTv = (TextView) findViewById(R.id.latlng_tv);
        mDistanceTv = (TextView) findViewById(R.id.distance_tv);
        LatLng latLng = new LatLng(0f,0f);
        LatLng pCenter = new LatLng(1f, 1f);
        mHuahui = new LatLng(22.578944, 113.930367);// Huahui tech.
//        DistanceUtil.getDistance(latLng, latLng);

//        SpatialRelationUtil.isCircleContainsPoint(pCenter, 300, latLng);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        initLocation();
        mLocationClient.start();
    }
    public BDLocationListener myListener = new MyLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            super.onReceiveLocation(location);
            Logger.d(location.getLatitude() + ", " + location.getLongitude());
            mCurrent = new LatLng(location.getLatitude(), location.getLongitude());
            if (mPosition == null) {
                mPosition = new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                mDistanceTv.setText(DistanceUtil.getDistance(mPosition, mCurrent) + "");
            }
            mLatLngTv.setText(location.getLatitude() + ", " + location.getLongitude());
        }

        @Override
        public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {
            super.onLocDiagnosticMessage(locType, diagnosticType, diagnosticMessage);
        }
    };
    private void initLocation(){

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span=1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        //    option.setIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        //    option.setWifiValidTime(5*60*1000);
        //可选，7.2版本新增能力，如果您设置了这个接口，首次启动定位时，会先判断当前WiFi是否超出有效期，超出有效期的话，会先重新扫描WiFi，然后再定位

        mLocationClient.setLocOption(option);
    }
}

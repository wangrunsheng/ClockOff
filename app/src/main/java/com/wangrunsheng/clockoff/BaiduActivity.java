package com.wangrunsheng.clockoff;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.orhanobut.logger.Logger;
import com.wangrunsheng.clockoff.tools.MyLocationListener;

/**
 * Check in and check off Activity.
 * Created by Russell on 2017/9/15.
 */

public class BaiduActivity extends AppCompatActivity {

    TextView mResultTv, mLatTv, mLocTv;
    TextClock mCheckInTc, mCheckOffTc;
    Button mCheckInBtn, mCheckInRefreshBtn, mCheckOffBtn, mCheckOffRefreshBtn;
    TextView mCheckInLocTv, mCheckOffLocTv;
    boolean isCheckedIn;
    final static int CHECKED_IN = 0;// before start to check, the check in btn is on.
    final static int CHECKED_OFF = 1;// already check in, the check off btn is on.
    final static int CHECKED_ALL = 2;// already check off, no btn.
    int checkStatus;
    String currentLocation;
    LatLng mHuahui, mPosition;
    LatLng mWanxiang;//上海市松江区书海路999号 万象汽车制造公司
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            super.onReceiveLocation(location);
        //    Logger.d(location.getAddrStr());
            mResultTv.setText(location.getLocationDescribe());
        //    Logger.d(location.getLatitude()+", "+location.getLongitude());
            mLatTv.setText(location.getLatitude()+", "+location.getLongitude());
            mPosition = new LatLng(location.getLatitude(), location.getLongitude());
            currentLocation = location.getAddrStr();
            if (SpatialRelationUtil.isCircleContainsPoint(mWanxiang, 300, mPosition)) {
                mLocTv.setText("Yes");
                if (checkStatus == CHECKED_IN) {
                    mCheckInLocTv.setText(getString(R.string.check_location_in));
                    mCheckInBtn.setEnabled(true);
                } else if (checkStatus == CHECKED_OFF) {
                    mCheckOffLocTv.setText(getString(R.string.check_location_in));
                    mCheckOffBtn.setEnabled(true);
                }
            } else {
                mLocTv.setText("No");
                if (checkStatus == CHECKED_IN) {
                    mCheckInLocTv.setText(getString(R.string.check_location_out));
                    mCheckInBtn.setEnabled(false);
                } else if (checkStatus == CHECKED_OFF) {
                    mCheckOffLocTv.setText(getString(R.string.check_location_out));
                    mCheckOffBtn.setEnabled(false);
                }
            }
        }

        @Override
        public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {
            super.onLocDiagnosticMessage(locType, diagnosticType, diagnosticMessage);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("BaiduActivity is created.");
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_baidu);
        mResultTv = (TextView) findViewById(R.id.baidu_loc_tv);
        mLatTv = (TextView) findViewById(R.id.baidu_loc_lat_tv);
        mLocTv = (TextView) findViewById(R.id.baidu_loc_loc_tv);
        initCheckCards();
    //    isCheckedIn = getSharedPreferences("CheckIn", Context.MODE_PRIVATE).getBoolean("isCheckedIn", false);
        checkStatus = getSharedPreferences("CheckIn", Context.MODE_PRIVATE).getInt("CheckStatus", 0);
        mHuahui = new LatLng(22.578944, 113.930367);// Huahui tech.
        mWanxiang = new LatLng(31.046566, 121.329131);// Wanxiang car.
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        initLocation();
        mLocationClient.start();
    }

    private void initCheckCards() {
        mCheckInTc = (TextClock) findViewById(R.id.check_in_time_tc);
        mCheckOffTc = (TextClock) findViewById(R.id.check_off_time_tc);
        mCheckInBtn = (Button) findViewById(R.id.check_in_btn);
        mCheckInBtn.setOnClickListener(onClickListener);
        mCheckInRefreshBtn = (Button) findViewById(R.id.check_in_refresh_btn);
        mCheckOffBtn = (Button) findViewById(R.id.check_off_btn);
        mCheckOffBtn.setOnClickListener(onClickListener);
        mCheckOffRefreshBtn = (Button) findViewById(R.id.check_off_refresh_btn);
        mCheckInLocTv = (TextView) findViewById(R.id.check_in_location_tv);
        mCheckOffLocTv = (TextView) findViewById(R.id.check_off_location_tv);
    }
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

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.check_in_btn:
                    mCheckInLocTv.setText(currentLocation);
                    mCheckInBtn.setVisibility(View.GONE);
                    mCheckInTc.setFormat24Hour(mCheckInTc.getText());
                    mCheckInRefreshBtn.setVisibility(View.VISIBLE);
                    checkStatus = CHECKED_OFF;
                    mCheckOffBtn.setVisibility(View.VISIBLE);
                    break;
                case R.id.check_off_btn:
                    mCheckOffLocTv.setText(currentLocation);
                    mCheckOffBtn.setVisibility(View.GONE);
                    mCheckOffTc.setFormat24Hour(mCheckOffBtn.getText());
                    mCheckOffRefreshBtn.setVisibility(View.VISIBLE);
                    checkStatus = CHECKED_ALL;
                    break;
                default:
                    break;
            }
        }
    };
}

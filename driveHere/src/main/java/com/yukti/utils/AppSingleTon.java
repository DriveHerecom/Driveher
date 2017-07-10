package com.yukti.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yukti.driveherenew.LoginActivity;
import com.yukti.driveherenew.ProfileActivity;
import com.yukti.driveherenew.R;
import com.yukti.jsonparser.AppJsonParser;
import com.yukti.location.PlayManager;

import java.util.ArrayList;
import java.util.Collections;

public class AppSingleTon extends Application {

    public static Context CONTEXT;
    public static Resources RESOURCES;
    public static LayoutInflater LAYOUT_INFLATER;
    public static AppPreference SHARED_PREFERENCE;
    public static MethodBox METHOD_BOX;
    public static AppUrl APP_URL;
    public static AppJsonParser APP_JSON_PARSER;
    public static PlayManager PLAY_MANAGER;
    public static RequestQueue VOLLEY_REQUEST_QUEUE;
    public static VersionOS VERSION_OS;


    private static AppSingleTon mInstance;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initStaticObject();

    }

    public void initStaticObject() {
        CONTEXT = getApplicationContext();
        RESOURCES = CONTEXT.getResources();
        VOLLEY_REQUEST_QUEUE = Volley.newRequestQueue(this);
        APP_URL = new AppUrl();
        VERSION_OS = new VersionOS();
        APP_JSON_PARSER = new AppJsonParser();
        METHOD_BOX = new MethodBox();
        SHARED_PREFERENCE = new AppPreference();
        LAYOUT_INFLATER = (LayoutInflater) CONTEXT
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public static synchronized AppSingleTon getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AppSingleTon(context);
        }
        return mInstance;
    }
    public AppSingleTon() {

    }

    public AppSingleTon(Context context) {
        CONTEXT = context;
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    static public String CalculationByDistance(LatLng StartP) {
        double lat1 = StartP.latitude;
        double lon1 = StartP.longitude;
        String LotCode = null;

        Location locationA = new Location("point A");
        locationA.setLatitude(lat1);
        locationA.setLongitude(lon1);

        /////    40.0995671,-75.3037165           DHC-04
        double lat2 = 40.0995671;
        double lon2 = -75.3037165;
        Location locationB = new Location("point B");
        locationB.setLatitude(lat2);
        locationB.setLongitude(lon2);
        Float distance1 = locationA.distanceTo(locationB);

        ////     39.90962, -75.22429              DHP-05
        double lat3 = 39.90962;
        double lon3 = -75.22429;
        Location locationC = new Location("point C");
        locationC.setLatitude(lat3);
        locationC.setLongitude(lon3);
        Float distance2 = locationA.distanceTo(locationC);

        ///      40.1427045,-75.3921425          CV-51
        double lat4 = 40.1427045;
        double lon4 = -75.3921425;
        Location locationD = new Location("point D");
        locationD.setLatitude(lat4);
        locationD.setLongitude(lon4);
        Float distance3 = locationA.distanceTo(locationD);

        ///      40.1226602,-75.3444571          BS-52
        double lat5 = 40.1226602;
        double lon5 = -75.3444571;
        Location locationE = new Location("point E");
        locationE.setLatitude(lat5);
        locationE.setLongitude(lon5);
        Float distance4 = locationA.distanceTo(locationE);

        ///      40.1229971,-75.3456364          405-53
        double lat6 = 40.1229971;
        double lon6 = -75.3456364;
        Location locationF = new Location("point F");
        locationF.setLatitude(lat6);
        locationF.setLongitude(lon6);
        Float distance5 = locationA.distanceTo(locationF);
        Log.e("distance5", "test 1 = " + distance5);

        ////     39.91622318649415,-75.22170383087598   KIA-LOT
        double lat7 = 39.91622318649415;
        double lon7 = -75.22170383087598;
        Location locationG = new Location("point G");
        locationG.setLatitude(lat7);
        locationG.setLongitude(lon7);
        Float distance6 = locationA.distanceTo(locationG);

        ArrayList<Float> list = new ArrayList<>();
        list.add(distance1);
        list.add(distance2);
        list.add(distance3);
        list.add(distance4);
        list.add(distance5);
        list.add(distance6);
        int minIndex = list.indexOf(Collections.min(list));

        switch (minIndex) {
            case 0:
                if (distance1 < 1500) {
                    LotCode = "DHC-04";
                } else {
                    LotCode = "Unknown";
                }
                break;
            case 1:
                if (distance2 < 1500) {
                    LotCode = "DHP-05";
                } else {
                    LotCode = "Unknown";
                }
                break;
            case 2:
                if (distance3 < 1500) {
                    LotCode = "CV-51";
                } else {
                    LotCode = "Unknown";
                }
                break;
            case 3:
                if (distance4 < 1500) {
                    LotCode = "BS-52";
                } else {
                    LotCode = "Unknown";
                }
                break;
            case 4:
                if (distance5 < 1500) {
                    LotCode = "405-53";
                } else {
                    LotCode = "Unknown";
                }
                break;
            case 5:
                if (distance6 < 1500) {
                    LotCode = "Int";
                } else {
                    LotCode = "Unknown";
                }
                break;
        }
        return LotCode;
    }

    public static void logOut(Context context)
    {
        AppSingleTon.SHARED_PREFERENCE.logout();
        Intent intent = new Intent(context, com.yukti.driveherenew.LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static boolean isNetConnection()
    {
        ConnectionDetector cd = new ConnectionDetector(CONTEXT);
        return cd.isConnectingToInternet();
    }

    public static DisplayImageOptions options;

    public static void setPicUniversalImageLoader(ImageView Selectedimgview, String path ,Context context) {
        setDisplayOption();
        ImageLoader il = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        il.init(ImageLoaderConfiguration.createDefault(context));
        il.displayImage(path, Selectedimgview, options);

    }

    public static void setDisplayOption() {
        if (options == null)
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.alto_lxi)
                    .showImageForEmptyUri(R.drawable.alto_lxi)
                    .showImageOnFail(R.drawable.alto_lxi)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
    }
}

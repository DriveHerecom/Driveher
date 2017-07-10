package com.yukti.driveherenew;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.LruCache;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.io.File;

public class MyApplication {

    public static final String TAG = MyApplication.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private int MY_SOCKET_TIMEOUT_MS = 20000;
    private static MyApplication mInstance;
    public static Activity smsActivity = null;
    private static Context mCtx;
    private ImageLoader mImageLoader;
    private File mProfile;
/*
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        ps = new PreferenceSettings(getApplicationContext());
    }*/

    private MyApplication(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public File getProfile() {

        File Dire = new File(mCtx.getExternalCacheDir(), "cashtag");
        if (!Dire.exists())
            Dire.mkdir();

        if (mProfile == null) {
            mProfile= new File(mCtx.getExternalCacheDir() + "/cashtag", "Profile.jpg");
        }
        return mProfile;
    }

    public static synchronized MyApplication getInstance(Context context)
    {
        if(mInstance == null)
        {
            mInstance = new MyApplication(context);
        }
        return mInstance;
    }
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

}

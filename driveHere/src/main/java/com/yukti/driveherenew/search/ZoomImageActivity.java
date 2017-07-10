package com.yukti.driveherenew.search;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.imagezoom.ImageAttacher;
import com.yukti.driveherenew.MyApplication;
import com.yukti.driveherenew.R;

import java.io.UnsupportedEncodingException;

public class ZoomImageActivity extends AppCompatActivity {
    String url;
    public static String KEY_URL="path";
    private int screenHeight = 0;
    private int screenWidth = 0;
    public int imageViewWidth = 0;
    public int imageViewHeight = 0;
    ImageView img1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_image);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        img1 = (ImageView) findViewById(R.id.imgDisplay);

        Bundle extras = getIntent().getExtras();
        url = extras.getString(KEY_URL);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = (int) (displaymetrics.heightPixels );
        screenWidth = (int) (displaymetrics.widthPixels );

        ViewTreeObserver vto = img1.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            public boolean onPreDraw() {
                img1.getViewTreeObserver().removeOnPreDrawListener(this);
                imageViewHeight = img1.getMeasuredHeight();
                imageViewWidth = img1.getMeasuredWidth();
                return true;
            }
        });

        setImage();
    }

    private void setImage() {

        Bitmap bitmap = TitleHistoryActivity.imgBitmap;

        if (bitmap != null) {
            img1.setImageBitmap(bitmap);
        }
        if (url != null && url.length() > 0)
        {
            makeImageRequest(url);
            usingSimpleImage(img1);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Image Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void makeImageRequest(String url)
    {
        ImageLoader imageLoader = MyApplication.getInstance(getApplicationContext()).getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("FRAG DOCUMENT VOLLEY", "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    img1.setImageBitmap(response.getBitmap());
                }
            }
        });

        imageLoader.get(url, ImageLoader.getImageListener(img1, R.drawable.alto_lxi, R.drawable.alto_lxi));

        Cache cache = MyApplication.getInstance(getApplicationContext()).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if(entry != null){
            try {
                String data = new String(entry.data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.zoom_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        TitleHistoryActivity.imgBitmap = null;

        // showToast("zoom img destroy");
    }

    public void usingSimpleImage(ImageView imageView) {
        ImageAttacher mAttacher = new ImageAttacher(imageView);
        ImageAttacher.MAX_ZOOM = 2.0f; // Double the current Size
        ImageAttacher.MIN_ZOOM = 0.1f; // Half the current Size
        MatrixChangeListener mMaListener = new MatrixChangeListener();
        mAttacher.setOnMatrixChangeListener(mMaListener);
        PhotoTapListener mPhotoTap = new PhotoTapListener();
        mAttacher.setOnPhotoTapListener(mPhotoTap);
    }

    private class PhotoTapListener implements ImageAttacher.OnPhotoTapListener {

        @Override
        public void onPhotoTap(View view, float x, float y) {
        }
    }

    private class MatrixChangeListener implements ImageAttacher.OnMatrixChangedListener {

        @Override
        public void onMatrixChanged(RectF rect) {

        }
    }

}

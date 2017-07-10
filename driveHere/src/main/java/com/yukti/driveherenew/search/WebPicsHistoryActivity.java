package com.yukti.driveherenew.search;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yukti.dataone.model.WebPicsModel;
import com.yukti.driveherenew.MyApplication;
import com.yukti.driveherenew.R;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.Constants;
import com.yukti.utils.ParamsKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WebPicsHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog pDialog;
    public ArrayList<WebPicsModel> webPicsHistory;
    private String carid;
    TextView mTextViewData;
    Button mButtonAdd;
    public static Bitmap imgBitmap;
    LinearLayout ll_webPics;
    public static boolean IS_FROM_WEB_PIC = false;
    public static final int WEB_HISTORY_REQUEST_CAMERA = 120;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_pics_history);
        initToolbar();

        ll_webPics = (LinearLayout) findViewById(R.id.ll_webPics);
        webPicsHistory = new ArrayList<WebPicsModel>();

        //mRecyclerViewTitle = (RecyclerView)findViewById(R.carId.recyclerViewTitle);
        mTextViewData = (TextView) findViewById(R.id.tv_no_pics);
        mButtonAdd = (Button) findViewById(R.id.btn_addPics);
        mButtonAdd.setOnClickListener(this);
        carid = getIntent().getExtras().getString("carid");
        getTitleHistory();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_web_pics_history_app_bar);
        toolbar.setTitle("Web Pics");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (IS_FROM_WEB_PIC) {
            IS_FROM_WEB_PIC = false;
            ll_webPics.removeAllViews();
            webPicsHistory.clear();
            getTitleHistory();
        }
    }

    void getTitleHistory() {
        pDialog = new ProgressDialog(WebPicsHistoryActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        String url = AppSingleTon.APP_URL.URL_GET_WEbPITCURE;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("WebPics history", response.toString());
                try {
                    JSONObject responseObj = new JSONObject(response);

                    int status_code = responseObj.getInt(Constant.STATUS_CODE);
                    String msg = responseObj.getString(Constant.STATUS_CODE);
                    WebPicsModel model = new WebPicsModel();

                    if (status_code == 1) {
                        mTextViewData.setVisibility(View.GONE);
                        JSONArray picsArray = responseObj.getJSONArray(ParamsKey.KEY_webPicture);
                        for (int i = 0; i < picsArray.length(); i++) {

                            JSONObject carpics = picsArray.getJSONObject(i);

                            model.setCarid(carpics.getString(ParamsKey.KEY_carId));
                            model.setCreation_date(carpics.getString(ParamsKey.KEY_creationDate));
                            try {
                                JSONArray dealArray = carpics.getJSONArray(ParamsKey.KEY_imagePath);
                                ArrayList<String> images = new ArrayList<>();
                                if (dealArray != null && dealArray.length() > 0) {
                                    for (int j = 0; j < dealArray.length(); j++) {
                                        images.add(dealArray.getJSONObject(j).getString("image"));
                                    }
                                }
                                model.setImagepath(images);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            webPicsHistory.add(model);
                        }
                        //setAdapter(titleHistory);
                        setList(model);
                        pDialog.hide();

                    } else if (status_code == 2) {
                        Log.e("WebPics msg:", "" + msg);
                        pDialog.hide();
                        mTextViewData.setVisibility(View.VISIBLE);
                    } else if (status_code == 4) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        AppSingleTon.logOut(getApplicationContext());
                        pDialog.hide();
                    } else {
                        Log.e("WebPics msg:", "" + msg);
                        pDialog.hide();

                    }

                }  catch (JSONException e) {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(WebPicsHistoryActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getApplicationContext()))
                                getTitleHistory();
                            else
                                Toast.makeText(getApplicationContext(),Constant.ERR_INTERNET , Toast.LENGTH_LONG).show();
                        }
                    });
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TitleHistory", "Error Response: " + error.getMessage());
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_carId, carid);
                params.put(ParamsKey.KEY_type, Constant.APP_TYPE);
                params.put(ParamsKey.KEY_userId, AppSingleTon.SHARED_PREFERENCE.getUserId());
                return params;
            }

        };

        strReq.setTag(Constants.REQUEST_TAG);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                Constants.VOLLEY_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance(getApplicationContext()).addToRequestQueue(strReq);

    }

    void setList(WebPicsModel webPicsHistory) {
        if (webPicsHistory != null && webPicsHistory.getImagepath().size() > 0) {

            for (int i = 0; i < webPicsHistory.getImagepath().size(); i++) {
                View picsView = null;
                picsView = WebPicsHistoryActivity.this.getLayoutInflater().inflate(R.layout.showwebpics, ll_webPics, false);

                final String path = webPicsHistory.getImagepath().get(i);
                File imgFile = new File(path);

                final ImageView iv = ((ImageView) picsView.findViewById(R.id.img_webPic));

                loadImageLoader(path, iv);
                ll_webPics.addView(picsView);

                picsView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //imgBitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
                        if (path != null) {
                            Intent i = new Intent(getApplicationContext(), ZoomImageActivity.class);
                            i.putExtra(ZoomImageActivity.KEY_URL, path);
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), "Image not available", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    public void loadImageLoader(String path, ImageView imageView) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_default_car)
                .showImageForEmptyUri(R.drawable.ic_default_car)
                .showImageOnFail(R.drawable.ic_default_car)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
        imageLoader.getInstance().displayImage(path, imageView, options);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IS_FROM_WEB_PIC = false;
    }

    void takeWebpic() {
        IS_FROM_WEB_PIC = false;
        Intent intent = new Intent(WebPicsHistoryActivity.this, WebPicsActivity.class);
        intent.putExtra("carid", carid);
        startActivity(intent);
        // finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addPics:

                if (AppSingleTon.VERSION_OS.checkVersion()) {
                    // Marshmallow+
                    if ((checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                            (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                        takeWebpic();

                    } else {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(WebPicsHistoryActivity.this);

                            builder.setTitle("");
                            builder.setMessage("Both Camera & Storage Permissions Are Needed To Take Pictures,Allow It?");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    final Intent i = new Intent();
                                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    i.addCategory(Intent.CATEGORY_DEFAULT);
                                    i.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    startActivity(i);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            WEB_HISTORY_REQUEST_CAMERA);
                                }
                            });
                            builder.show();
                        }


                    }

                } else {
                    // Pre-Marshmallow
                    takeWebpic();
                }

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == WEB_HISTORY_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takeWebpic();
            } else {
                Toast.makeText(getApplicationContext(), "Both Camera & Storage Permissions Are Needed", Toast.LENGTH_SHORT).show();
            }
        } else {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

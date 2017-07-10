package com.yukti.driveherenew.search;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;
import com.yukti.dataone.model.TitleHistoryModel;
import com.yukti.dataone.model.TitleHistoryModelCustom;
import com.yukti.driveherenew.BaseActivity;
import com.yukti.driveherenew.MainActivity;
import com.yukti.driveherenew.MyApplication;
import com.yukti.driveherenew.R;

import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.ParamsKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by prashant on 23/1/16.
 */
public class TitleHistoryActivity extends BaseActivity implements View.OnClickListener {
    public ArrayList<TitleHistoryModel> titleHistory;
    private String carid;
    public static Bitmap imgBitmap;
    LinearLayout ll_title;
    public static boolean IS_FROM_TITLE_PIC = false;
    public static final int TITLE_HISTORY_REQUEST_CAMERA = 118;
    View titleView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titlehistory);
        initToolbar();

        ll_title = (LinearLayout) findViewById(R.id.ll_titles);

        titleHistory = new ArrayList<TitleHistoryModel>();
        Button mButtonAdd = (Button) findViewById(R.id.btn_addTitle);
        mButtonAdd.setOnClickListener(this);

        carid = getIntent().getExtras().getString("carid");
        getTitleHistory();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_titlehistory_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (IS_FROM_TITLE_PIC) {
            IS_FROM_TITLE_PIC = false;
            if (titleHistory.size() > 0)
                titleHistory.clear();
            ll_title.removeAllViews();

            getTitleHistory();
        }
    }

    void getTitleHistory() {
        final ProgressDialog pDialog;
        pDialog = new ProgressDialog(TitleHistoryActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        String url = AppSingleTon.APP_URL.URL_GETTITLEHISTORY;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", "" + response);
                try {
                    JSONObject responseObj = new JSONObject(response);

                    int status_code = responseObj.getInt("status_code");
                    String msg = responseObj.getString("message");
                   TextView mTextViewData = (TextView) findViewById(R.id.tv_no_data);
                    if (status_code == 1) {
                        mTextViewData.setVisibility(View.GONE);
                        JSONArray titleArray = responseObj.getJSONArray(ParamsKey.KEY_titlePhoto);
                        for (int i = 0; i < titleArray.length(); i++) {
                            TitleHistoryModel model = new TitleHistoryModel();

                            JSONObject titles = titleArray.getJSONObject(i);
                            model.setCarid(titles.optString(ParamsKey.KEY_carId));
                            model.setLotcode(titles.optString(ParamsKey.KEY_lotCode));
                            model.setCreation_date(titles.optString(ParamsKey.KEY_creationDate));

                            try {
                                JSONArray dealArray = titles.getJSONArray(ParamsKey.KEY_imagePath);
                                ArrayList<String> images = new ArrayList<>();
                                if (dealArray != null && dealArray.length() > 0) {
                                    for (int j = 0; j < dealArray.length(); j++) {
                                        images.add(dealArray.getJSONObject(j).getString("image"));
                                    }
                                }
                                model.setImages(images);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            titleHistory.add(model);
                        }
                        setData(titleHistory);
                        pDialog.dismiss();

                    } else if (status_code == 2) {
                        mTextViewData.setVisibility(View.VISIBLE);
                        pDialog.dismiss();
                    } else if (status_code == 4) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        AppSingleTon.logOut(getApplicationContext());
                    } else {
                        Log.e("TitleHistory msg:", "" + msg);
                        pDialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(TitleHistoryActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getApplicationContext()))
                                getTitleHistory();
                            else
                                Toast.makeText(getApplicationContext(), Constant.ERR_INTERNET, Toast.LENGTH_LONG).show();
                        }
                    });
                    pDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TitleHistory", "Error Response: " + error.getMessage());
                pDialog.dismiss();
                //Toast.makeText(getAp, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            /**
             * Passing user parameters to our server
             *
             * @return
             */
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_carId, carid);
                params.put(ParamsKey.KEY_type, Constant.APP_TYPE);
                params.put(ParamsKey.KEY_userId, AppSingleTon.SHARED_PREFERENCE.getUserId());
                Log.e("parameter", "" + params);
                return params;
            }

        };
        MyApplication.getInstance(getApplicationContext()).addToRequestQueue(strReq);

    }

    void setData(ArrayList<TitleHistoryModel> newList) {
        for (int i = 0; i < newList.size(); i++) {
            String finaldate = "";

            titleView = TitleHistoryActivity.this.getLayoutInflater().inflate(R.layout.addtitle_image, ll_title, false);

            ((TextView) titleView.findViewById(R.id.tv_lotcode)).setText(newList.get(i).getLotcode());

            String strdate = newList.get(i).getCreation_date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            try {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date tempDate = simpleDateFormat.parse(strdate);

                SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMM yyyy");

                String newDate = outputDateFormat.format(tempDate);
                Log.e("Output date is = ", "" + outputDateFormat.format(tempDate));
                ((TextView) titleView.findViewById(R.id.tv_date)).setText(newDate);


            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (newList.get(i).getImages() != null && newList.get(i).getImages().size() > 0) {
                for (int j = 0; j < newList.get(i).getImages().size(); j++) {
                    setPhotoList(newList.get(i).getImages().get(j));
                }
            }

            ll_title.addView(titleView);
        }
    }


    void setPhotoList(String imagepath) {
        if (imagepath != null && imagepath.length() > 0) {

           LinearLayout containerTitlePhotos = (LinearLayout) titleView.findViewById(R.id.container_titlePhotos);

            final String path = imagepath;
            File imgFile = new File(path);
            View view = null;
            view = TitleHistoryActivity.this.getLayoutInflater().inflate(R.layout.item_add_title_history, containerTitlePhotos, false);
            final ImageView iv = ((ImageView) view.findViewById(R.id.img));

            loadImageLoader(path, iv);

            containerTitlePhotos.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //imgBitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
                    if (path != null) {
//                        IS_FROM_TITLE_PIC = true;
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
        IS_FROM_TITLE_PIC = false;
    }

    void takeTitle() {
        IS_FROM_TITLE_PIC = false;
        Intent intent = new Intent(TitleHistoryActivity.this, TitleActivity.class);
        intent.putExtra("carid", carid);
        startActivity(intent);
        // finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addTitle:

                if (AppSingleTon.VERSION_OS.checkVersion()) {
                    // Marshmallow+
                    if ((checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                            (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                        takeTitle();

                    } else {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(TitleHistoryActivity.this);

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
                                            TITLE_HISTORY_REQUEST_CAMERA);
                                }
                            });
                            builder.show();
                        }


                    }

                } else {
                    // Pre-Marshmallow
                    takeTitle();

                }


                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == TITLE_HISTORY_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takeTitle();
            } else {
                Toast.makeText(getApplicationContext(), "Both Camera & Storage Permissions Are Needed", Toast.LENGTH_SHORT).show();
            }
        } else {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

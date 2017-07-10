package com.yukti.driveherenew;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.creadigol.admin.library.CirclePageIndicator;
import com.yukti.driveherenew.search.CarDetailsActivity;
import com.yukti.driveherenew.search.CarInventory;
import com.yukti.driveherenew.search.GalleryActivity;
import com.yukti.driveherenew.search.ImageHolderFragment;
import com.yukti.jsonparser.FindMatch;
import com.yukti.jsonparser.UpdateResponse;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.Constants;
import com.yukti.utils.ParamsKey;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntityBuilder;
import me.drakeet.materialdialog.MaterialDialog;

public class AuctionCarDetailsActivity extends AppCompatActivity implements MessageDialogFragment.MessageDialogListener,AppBarLayout.OnOffsetChangedListener {

    CarInventory car;
    ViewPager viewPager;
    public static final int REQUEST_ACTION_AUCTION = 7894;
    String Vin = null;
    Toolbar toolbar;
    int itemPosition;
    String TAG_PUSH_RESULT = "TAG_PUSH_RESULT";
    public static String EXTRA_KEY_IS_FROM_AUCTION = "isFromAuction";
    boolean isUpdate = false;
    boolean isCarMatch = false;
    Button mButton;
    LinearLayout ll_progress;
    CoordinatorLayout rl;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_car_details);
        toolbar = (Toolbar) findViewById(R.id.activity_car_details_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView tb_text = (TextView) toolbar.findViewById(R.id.tb_text);
        tb_text.setText("Car Auction");

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_profile);
        collapsingToolbar.setExpandedTitleGravity(View.TEXT_ALIGNMENT_CENTER);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_place_profile);
        appBarLayout.addOnOffsetChangedListener(this);

        ll_progress = (LinearLayout) findViewById(R.id.ll_progress_dialog);
        rl = (CoordinatorLayout) findViewById(R.id.rl_auction_car_details);
        viewPager = (ViewPager) findViewById(R.id.gallery_viewpager);

        if (getIntent().getStringExtra(CarDetailsActivity.EXTRAKEY_VIN)!=null)
        {
            getCarDetail(getIntent().getStringExtra(CarDetailsActivity.EXTRAKEY_VIN));
        }
    }

    void initData() {
        init();
        initPhotoPager();
        initUpdateAuction();
        isCarMatch = true;
    }

    void initUpdateAuction() {
        if(car != null) {
            mButton = (Button) findViewById(R.id.btn_sold_update);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isCarMatch) {
                        if (car.vacancy!=null && car.vacancy.equalsIgnoreCase("Sold")) {
                            Toast.makeText(getApplicationContext(), "Car already sold", Toast.LENGTH_SHORT).show();
                        } else {
                            updateAuction();
                        }
                    } else {
                        Log.e("Vacancy Not Available", "Vacancy Not Available");
                        updateAuction();
                    }
                }
            });
        }
    }

    void updateAuction() {
        ll_progress.setVisibility(View.VISIBLE);
        rl.setClickable(false);

        MultipartRequest multipartRequest = new MultipartRequest(AppSingleTon.APP_URL.URL_UPDATE_AUCTION_VACANCY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " ");
                rl.setClickable(true);
                ll_progress.setVisibility(View.GONE);
                try {
                    UpdateResponse orm = AppSingleTon.APP_JSON_PARSER.updateResponse(response);
                    if (orm.status_code.equals("1")) {
                        isUpdate = true;
                        final MaterialDialog materialDialog = new MaterialDialog(AuctionCarDetailsActivity.this);
                        materialDialog.setMessage(orm.message).setPositiveButton("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();
                                getCarDetail(car.vin);
                            }
                        });
                        materialDialog.show();
                    }
                    else if (orm.status_code.equals("4")) {
                        Toast.makeText(getApplicationContext(), orm.message, Toast.LENGTH_SHORT).show();
                        AppSingleTon.logOut(AuctionCarDetailsActivity.this);
                    }
                    else {
                        MessageDialogFragment fragment = MessageDialogFragment.newInstance(
                                "Failed", "Failed to update.", true, "Ok", false,
                                "", false, "", AuctionCarDetailsActivity.this);

                        fragment.show(getSupportFragmentManager(), TAG_PUSH_RESULT);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(AuctionCarDetailsActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getApplicationContext()))
                                updateAuction();
                            else
                                Toast.makeText(getApplicationContext(),Constant.ERR_INTERNET , Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ll_progress.setVisibility(View.GONE);
                rl.setClickable(true);
                Toast.makeText(getApplicationContext()," "+error,Toast.LENGTH_SHORT).show();
            }
        });
        multipartRequest.setTag(Constants.REQUEST_TAG);
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constants.VOLLEY_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance(this.getApplicationContext()).addToRequestQueue(multipartRequest);
    }

    void getCarDetail(final String vinNumber)
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppSingleTon.APP_URL.URL_AUCTION_DETAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Resposnes", response + " test");
                progressDialog.dismiss();
                try {
                    FindMatch findmatch = AppSingleTon.APP_JSON_PARSER.findMatch(response);
                    if (findmatch.status_code.equals("1")) {
                        car = findmatch.cars;
                        initData();
                    }
                    else {
                        Toast.makeText(AuctionCarDetailsActivity.this, ""+ findmatch.message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(AuctionCarDetailsActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getApplicationContext()))
                                getCarDetail(vinNumber);
                            else
                                Toast.makeText(getApplicationContext(),Constant.ERR_INTERNET , Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(AuctionCarDetailsActivity.this, "Get CarData Error", Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_vin, vinNumber.trim());
                return params;
            }
        };

        stringRequest.setTag(Constants.REQUEST_TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constants.VOLLEY_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    void init()
    {
        TextView makeModelModelNumber = (TextView) findViewById(R.id.tv_makeModelModelNumber);
        makeModelModelNumber.setText(car.make + " " + car.model + " " + car.modelNumber);

        TextView tv_auctionvacancy = (TextView) findViewById(R.id.tv_auction_vacancy);
        if (car.vacancy != null && car.vacancy.length() != 0) {
            tv_auctionvacancy.setText(car.vacancy);
        }
        else
        {
            tv_auctionvacancy.setText(Constant.KEY_NOT_AVAILABLE);
        }

        TextView tv_auctionName = (TextView) findViewById(R.id.tv_auction_name);
        if (car.auctionName != null && car.auctionName.length() != 0) {
            tv_auctionName.setText(car.auctionName);
        }
        else
        {
            tv_auctionName.setText(Constant.KEY_NOT_AVAILABLE);
        }
        TextView tv_floorPrice = (TextView) findViewById(R.id.tv_floor_price);
        if (car.floorPrice != null && car.floorPrice.length() != 0) {
            tv_floorPrice.setText(car.floorPrice);
        }
        else
        {
            tv_floorPrice.setText(Constant.KEY_NOT_AVAILABLE);
        }

        TextView tv_condition = (TextView) findViewById(R.id.tv_condition);
        if (car.conditions != null && car.conditions.length() != 0) {
            tv_condition.setText(car.conditions);
        }
        else
        {
            tv_condition.setText(Constant.KEY_NOT_AVAILABLE);
        }

        TextView tv_miles = (TextView) findViewById(R.id.tv_miles);
        if (car.auctionMile != null && car.auctionMile.length() != 0) {
            tv_miles.setText(car.auctionMile);
        }
        else
        {
            tv_miles.setText(Constant.KEY_NOT_AVAILABLE);
        }

        TextView tv_notes = (TextView) findViewById(R.id.tv_note);
        if (car.auctionNote != null && car.auctionNote.length() != 0) {
            tv_notes.setText(car.auctionNote);
        }
        else
        {
            tv_notes.setText(Constant.KEY_NOT_AVAILABLE);
        }

        TextView tv_auctionDate = (TextView) findViewById(R.id.tv_auction_date);
        if (car.auctiondate != null && car.auctiondate.length() != 0) {
            tv_auctionDate.setText(car.auctiondate);
        }
        else
        {
            tv_auctionDate.setText(Constant.KEY_NOT_AVAILABLE);
        }

        TextView tv_carReady = (TextView) findViewById(R.id.tv_car_ready);
        if (car.carReady != null && car.carReady.length() != 0) {
            tv_carReady.setText(car.carReady);
        }
        else
        {
            tv_carReady.setText(Constant.KEY_NOT_AVAILABLE);
        }


        TextView vin = (TextView) findViewById(R.id.tv_vin);
        if (car.vin != null && car.vin.length() != 0) {
            vin.setText(car.vin);
        }
        else
        {
            vin.setText(Constant.KEY_NOT_AVAILABLE);
        }

        TextView rfid = (TextView) findViewById(R.id.tv_rfid);
        if (car.rfid != null && car.rfid.length() != 0) {
            rfid.setText(car.rfid);
        }
        else
        {
            rfid.setText(Constant.KEY_NOT_AVAILABLE);
        }

        TextView problem = (TextView) findViewById(R.id.tv_problem);
        if (car.problem != null && car.problem.length() != 0) {
            problem.setText(car.problem);
        }
        else
        {
            problem.setText(Constant.KEY_NOT_AVAILABLE);
        }


        TextView status = (TextView) findViewById(R.id.tv_status);
        if (car.vehicleStatus != null && car.vehicleStatus.length() != 0) {
            status.setText(car.vehicleStatus);
        }
        else
        {
            status.setText(Constant.KEY_NOT_AVAILABLE);
        }

        TextView stock = (TextView) findViewById(R.id.tv_stock);
        if (car.stockNumber != null && car.stockNumber.length() != 0) {
            stock.setText(car.stockNumber);
        }
        else
            stock.setText(Constant.KEY_NOT_AVAILABLE);

        TextView salesPrice = (TextView) findViewById(R.id.tv_salesPrice);
        if (car.price != null && car.price.length() != 0) {
            salesPrice.setText(car.price);
        }
        else {
            salesPrice.setText(Constant.KEY_NOT_AVAILABLE);
        }

        TextView stage = (TextView) findViewById(R.id.tv_stage);
        if (car.stage != null && car.stage.length() != 0) {
            stage.setText(car.stage);
        }
        else
        {
            stage.setText(Constant.KEY_NOT_AVAILABLE);
        }

        TextView color = (TextView) findViewById(R.id.tv_color);
        CharSequence[] colorNameList = getResources().getStringArray(R.array.ColorName);
        CharSequence[] colorValueList = getResources().getStringArray(R.array.ColorValue);

        for (int i = 0; i < colorNameList.length; i++) {
            if (car.color!=null && car.color.equals(colorValueList[i])) {
                color.setText(colorNameList[i]);
                break;
            }
            else
            {
                color.setText(Constant.KEY_NOT_AVAILABLE);
            }
        }

        TextView cylinders = (TextView) findViewById(R.id.tv_cylinders);
        if (car.cylinder != null && car.cylinder.length() != 0) {
            cylinders.setText(car.cylinder);
        }
        else
        {
            cylinders.setText(Constant.KEY_NOT_AVAILABLE);
        }

        TextView driveType = (TextView) findViewById(R.id.tv_driveType);
        if (car.driveType != null && car.driveType.length() != 0) {
            driveType.setText(car.driveType);
        }
        else {
            driveType.setText(Constant.KEY_NOT_AVAILABLE);
        }

        TextView tv_lotcode = (TextView) findViewById(R.id.tv_lotcode);
        if (car.lotCode != null && !car.lotCode.equalsIgnoreCase("")) {
            tv_lotcode.setText(car.lotCode);
        }
        else
        {
            tv_lotcode.setText(Constant.KEY_NOT_AVAILABLE);
        }
    }

    @Override
    public void onDialogPositiveClick(MessageDialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(MessageDialogFragment dialog) {

    }

    @Override
    public void onDialogNeutralClick(MessageDialogFragment dialog) {

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        if (i == (-maxScroll)) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.accentColor));
        } else {
            toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }

    public class PhotoPagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<String> photoUrlList;

        public PhotoPagerAdapter(FragmentManager fm, ArrayList<String> photoUrlList) {
            super(fm);
            this.photoUrlList = photoUrlList;
        }

        @Override
        public Fragment getItem(int position) {
            return ImageHolderFragment
                    .newInstance(photoUrlList.get(position),0);
        }

        @Override
        public int getCount() {
            return photoUrlList.size();
        }
    }

    public void onPhotoClicked() {
        Intent intent = new Intent(AuctionCarDetailsActivity.this, GalleryActivity.class);
        intent.putExtra(Constant.EXTRA_KEY_PHOTO_LIST, car.images);
        intent.putExtra(Constant.EXTRA_KEY_CURRENT_ITEM, viewPager.getCurrentItem());
        intent.putExtra(Constant.EXTRA_KEY_TITLE, car.Make + " " + car.Model + " " + car.ModelYear);
        startActivity(intent);
    }

    void initPhotoPager() {
        if (car.images.size() == 0) {
            ImageView noImage = (ImageView) findViewById(R.id.default_image);
            noImage.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
        } else {
            PhotoPagerAdapter imagePagerAdapter = new PhotoPagerAdapter(getSupportFragmentManager(), car.images);
            viewPager.setAdapter(imagePagerAdapter);
            final TextView photoIndex = (TextView) findViewById(R.id.index);
            photoIndex.setText("1" + " / " + car.images.size());

            final CirclePageIndicator titleIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
            titleIndicator.setViewPager(viewPager);

            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    titleIndicator.setCurrentItem(position);
                    photoIndex.setText("" + (position + 1) + "/" + car.images.size());
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menudetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;

            case R.id.inquiries:
                if (car != null) {
                    Intent i = new Intent(AuctionCarDetailsActivity.this, InquiryActivity.class);
                    i.putExtra(Constant.EXTRA_KEY_EACH_CAR, car);
                    startActivity(i);
                }
                break;

            case R.id.dataone:
                if (car != null)
                {
                    Intent intent = new Intent(AuctionCarDetailsActivity.this, DataoneActivity.class);
                    intent.putExtra(Constant.EXTRA_KEY_VIN, car.vin);
                    startActivity(intent);
                }
                break;

            case R.id.carfax:
                if(car != null) {
                    Toast.makeText(AuctionCarDetailsActivity.this, "CarFax Data Not Available.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.edit_auction:
                if(car != null) {
                    Intent intentAuction = new Intent(AuctionCarDetailsActivity.this, CarAuctionActivity.class);
                    intentAuction.putExtra(CarDetailsActivity.EXTRAKEY_VIN, car);
                    intentAuction.putExtra(EXTRA_KEY_IS_FROM_AUCTION, true);
                    startActivityForResult(intentAuction, REQUEST_ACTION_AUCTION);
                }
                break;

            default:
        }
        return super.onOptionsItemSelected(item);
    }

    void initsetRsult(boolean finsh) {
        Intent intent = new Intent();
        intent.putExtra(Constant.EXTRA_KEY_EACH_CAR, car);
        intent.putExtra(Constant.EXTRA_KEY_IS_UPDATE, isUpdate);
        intent.putExtra(Constant.EXTRA_KEY_ITEM_POSITION, itemPosition);
        setResult(RESULT_OK, intent);
        if (finsh)
            finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_ACTION_AUCTION)) {

            if (resultCode == Activity.RESULT_OK) {
                car = (CarInventory) data.getExtras().getSerializable(Constant.EXTRA_KEY_EACH_CAR);
                itemPosition = data.getIntExtra(Constant.EXTRA_KEY_ITEM_POSITION, 0);
                init();
                initsetRsult(false);
            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    class MultipartRequest extends Request<String> {
        private HttpEntity mHttpEntity;
        private Response.Listener mListener;

        public MultipartRequest(String url,
                                Response.Listener<String> listener,
                                Response.ErrorListener errorListener) {
            super(Method.POST, url, errorListener);
            mListener = listener;
            mHttpEntity = buildMultipartEntity();
        }

        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            try {
                return Response.success(new String(response.data, "UTF-8"),
                        getCacheEntry());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

                return Response.success(new String(response.data), getCacheEntry());
            }
        }

        @Override
        protected void deliverResponse(String response) {
            mListener.onResponse(response);
        }

        @Override
        public String getBodyContentType() {
            return mHttpEntity.getContentType().getValue();
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                mHttpEntity.writeTo(bos);
            } catch (IOException e) {
                VolleyLog.e("IOException writing to ByteArrayOutputStream");
            }
            return bos.toByteArray();
        }
    }

    public HttpEntity buildMultipartEntity()
    {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody(ParamsKey.KEY_userId, AppSingleTon.SHARED_PREFERENCE.getUserId());
        builder.addTextBody(ParamsKey.KEY_type, Constant.APP_TYPE);
        builder.addTextBody(ParamsKey.KEY_carId, car.carId);
        builder.addTextBody(ParamsKey.KEY_vacancy, "sold");
        return builder.build();
    }
}
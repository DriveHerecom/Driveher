package com.yukti.driveherenew.search;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.creadigol.admin.library.CirclePageIndicator;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yukti.driveherenew.AddNewCarActivity;
import com.yukti.driveherenew.AuctionCarDetailsActivity;
import com.yukti.driveherenew.BaseActivity;
import com.yukti.driveherenew.CarAuctionActivity;
import com.yukti.driveherenew.LeaseActivity;
import com.yukti.driveherenew.MainActivity;
import com.yukti.driveherenew.MessageDialogFragment;
import com.yukti.driveherenew.MessageDialogFragment.MessageDialogListener;
import com.yukti.driveherenew.MyApplication;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment.ListDialogListener;
import com.yukti.jsonparser.AppJsonParser;
import com.yukti.jsonparser.FindMatch;
import com.yukti.jsonparser.Login;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.AppUrl;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.Constants;
import com.yukti.utils.GetAddress;
import com.yukti.utils.ParamsKey;
import com.yukti.utils.RestClient;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CarDetailsActivity extends BaseActivity implements MessageDialogListener, AppBarLayout.OnOffsetChangedListener {
    public static final int REQUEST_ACTION_AUCTION = 7894;
    public static final int REQUEST_ACTION_EDIT = 1015;
    public static String EXTRAKEY_VIN = "carVin";
    public static boolean isNeeded = false;
    CarInventory car;
    int itemPosition;
    ViewPager viewPager;
    TextView photoIndex;
    TextView stage;
    TextView lastScanAddress, lastScanDate;
    boolean isUpdate = false;
    LinearLayout ll_progressCarDetail;
    String token = "", carVin;
    Toolbar toolbar;
    TextView tb_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);
        initToolbar();
        photoIndex = (TextView) findViewById(R.id.index);
        viewPager = (ViewPager) findViewById(R.id.gallery_viewpager);
        ll_progressCarDetail = (LinearLayout) findViewById(R.id.ll_progressCarDetail);
        itemPosition = getIntent().getIntExtra(Constant.EXTRA_KEY_ITEM_POSITION, 0);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_profile);
        collapsingToolbar.setExpandedTitleGravity(View.TEXT_ALIGNMENT_CENTER);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_place_profile);
        appBarLayout.addOnOffsetChangedListener(this);

        Intent intent = getIntent();
        carVin = intent.getStringExtra(EXTRAKEY_VIN);
        getMissCarDataUsingVolley(carVin);
    }

    void initData() {
        init();
        initPhotoPager();
    }

    void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.activity_car_details_app_bar);
        tb_text = (TextView) toolbar.findViewById(R.id.tb_text);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void init() {
        TextView makeModelModelNumber = (TextView) findViewById(R.id.makeModelModelNumber);
        String carname = "";

        if (car.make != null) {
            carname = carname + car.make + " ";
        }

        if (car.model != null) {
            carname = carname + car.model + " ";
        }

        if (car.modelNumber != null) {
            if (car.modelNumber != null && !car.modelNumber.equalsIgnoreCase(""))
                carname = carname + " (" + car.modelNumber + ")";
            else {
                carname = carname + "";
            }
        }

        tb_text.setText(carname);
        makeModelModelNumber.setText(carname);
        TextView miles = (TextView) findViewById(R.id.miles);

        if (car.miles != null) {
            String milesSubstr = car.miles.substring(car.miles.length() - 3);
            if (milesSubstr.equalsIgnoreCase(".00")) {
                miles.setText(car.miles.substring(0, car.miles.length() - 3));
                car.miles = car.miles.substring(0, car.miles.length() - 3);
            } else {
                miles.setText(car.miles);
            }
        } else {
            miles.setText(Constant.KEY_NOT_AVAILABLE);
        }

        TextView vin = (TextView) findViewById(R.id.vin);
        if (car.vin != null)
            vin.setText(car.vin);
        else
            vin.setText(Constant.KEY_NOT_AVAILABLE);

        TextView rfid = (TextView) findViewById(R.id.rfid);
        if (car.rfid != null&&!car.rfid.equalsIgnoreCase("")&&!car.rfid.equalsIgnoreCase(" "))
            rfid.setText(car.rfid);
        else
            rfid.setText(Constant.KEY_NOT_AVAILABLE);


        TextView vacancy = (TextView) findViewById(R.id.tv_carvacancy);
        if (car.vacancy != null)
            vacancy.setText(car.vacancy);
        else
            vacancy.setText(Constant.KEY_NOT_AVAILABLE);

        TextView problem = (TextView) findViewById(R.id.problem);
        if (car.problem != null && !car.problem.equalsIgnoreCase(""))
            problem.setText(car.problem);
        else
            problem.setText(Constant.KEY_NOT_AVAILABLE);

        TextView status = (TextView) findViewById(R.id.status);
        if (car.vehicleStatus != null && !car.vehicleStatus.equalsIgnoreCase(""))
            status.setText(car.vehicleStatus);
        else
            status.setText(Constant.KEY_NOT_AVAILABLE);


        TextView stock = (TextView) findViewById(R.id.stock);
        if (car.stockNumber != null && !car.stockNumber.equalsIgnoreCase(""))
            stock.setText(car.stockNumber);
        else
            stock.setText(Constant.KEY_NOT_AVAILABLE);

        TextView salesPrice = (TextView) findViewById(R.id.salesPrice);
        if (car.price != null)
            salesPrice.setText(car.price + " $");
        else
            salesPrice.setText(Constant.KEY_NOT_AVAILABLE);

        TextView modelYear = (TextView) findViewById(R.id.modelYear);
        if (car.modelYear != null && !car.modelYear.equalsIgnoreCase(""))
            modelYear.setText(car.modelYear);
        else
            modelYear.setText(Constant.KEY_NOT_AVAILABLE);

        stage = (TextView) findViewById(R.id.stage);
        if (car.stage != null && !car.stage.equalsIgnoreCase(""))
            stage.setText(car.stage);
        else
            stage.setText(Constant.KEY_NOT_AVAILABLE);

        TextView color = (TextView) findViewById(R.id.color);
        CharSequence[] colorNameList = getResources().getStringArray(R.array.ColorName);
        CharSequence[] colorValueList = getResources().getStringArray(R.array.ColorValue);

        for (int i = 0; i < colorNameList.length; i++) {
            if (car.color != null && car.color.equals(colorValueList[i] + "")) {
                color.setText(colorNameList[i] + "");
                break;
            } else {
                color.setText(Constant.KEY_NOT_AVAILABLE);
            }
        }

        TextView cylinders = (TextView) findViewById(R.id.cylinders);
        if (car.cylinder != null && !car.cylinder.equalsIgnoreCase(""))
            cylinders.setText(car.cylinder);
        else
            cylinders.setText(Constant.KEY_NOT_AVAILABLE);

        TextView driveType = (TextView) findViewById(R.id.driveType);
        if (car.driveType != null && !car.driveType.equalsIgnoreCase(""))
            driveType.setText(car.driveType);
        else
            driveType.setText(Constant.KEY_NOT_AVAILABLE);

        TextView mechanic = (TextView) findViewById(R.id.mechanic);
        if (car.mechanic != null && !car.mechanic.equalsIgnoreCase(""))
            mechanic.setText(car.mechanic);
        else
            mechanic.setText(Constant.KEY_NOT_AVAILABLE);

        TextView note = (TextView) findViewById(R.id.note);
        if (car.note != null && !car.note.equalsIgnoreCase(""))
            note.setText(car.note);
        else
            note.setText(Constant.KEY_NOT_AVAILABLE);

        lastScanAddress = (TextView) findViewById(R.id.address);
        lastScanDate = (TextView) findViewById(R.id.lastScanTime);

        ArrayList<Location> locations = car.locations;
        GetAddress getAddress = new GetAddress();
        if (locations != null && locations.size() > 0) {
            if ((locations.get(0).Address == null || locations.get(0).Address.length() < 4) && locations.get(0).Latitude.length() > 3) {
                String address = getAddress.getAddressUsingLatLong(getApplicationContext(),
                        Double.parseDouble(locations.get(0).Latitude), Double.parseDouble(locations.get(0).Longitude));
                if (address != null && address.length() > 3)
                    lastScanAddress.setText(address);
                else
                    lastScanAddress.setText(Constant.KEY_NOT_AVAILABLE);
            } else if (locations.get(0).Address != null && locations.get(0).Address.length() > 3) {
                lastScanAddress.setText(locations.get(0).Address);
            } else
                lastScanAddress.setText(Constant.KEY_NOT_AVAILABLE);


            if (locations.get(0).CreatedDate != null && locations.get(0).CreatedTime != null) {
                lastScanDate.setText(locations.get(0).CreatedDate + " " + locations.get(0).CreatedTime);
            } else {
                lastScanDate.setText(Constant.KEY_NOT_AVAILABLE);
            }

            for (int i = 0; i < locations.size(); i++) {
                GetAddress getAddress1 = new GetAddress();
                if ((locations.get(i).Address == null || locations.get(i).Address.length() < 4) && locations.get(i).Latitude.length() > 3 &&
                        locations.get(i).Longitude.length() > 3) {
                    String address = getAddress1.getAddressUsingLatLong(getApplicationContext(),
                            Double.parseDouble(locations.get(i).Latitude), Double.parseDouble(locations.get(i).Longitude));
                    if (address != null && address.length() > 3)
                        locations.get(i).Address = address;
                    else
                        locations.get(i).Address = Constant.KEY_NOT_AVAILABLE;
                }
            }
        } else {
            lastScanAddress.setText(Constant.KEY_NOT_AVAILABLE);
            lastScanDate.setText(Constant.KEY_NOT_AVAILABLE);
        }

        LinearLayout lotcodeContainer = (LinearLayout) findViewById(R.id.lotcodeContainer);
        TextView tv_lotcode = (TextView) findViewById(R.id.tv_lotcode);

        if (car.lotCode != null && !car.lotCode.equalsIgnoreCase("")) {
            lotcodeContainer.setVisibility(View.VISIBLE);
            tv_lotcode.setText(car.lotCode);
        } else {
            lotcodeContainer.setVisibility(View.GONE);
        }
    }

    public void onPhotoClicked() {
        Intent intent = new Intent(CarDetailsActivity.this, GalleryActivity.class);
        intent.putExtra(Constant.EXTRA_KEY_PHOTO_LIST, car.images);
        intent.putExtra(Constant.EXTRA_KEY_CURRENT_ITEM, viewPager.getCurrentItem());
        intent.putExtra(Constant.EXTRA_KEY_TITLE, car.Make + " " + car.Model + " " + car.ModelYear);
        startActivity(intent);
    }

    @SuppressWarnings("deprecation")
    void initPhotoPager() {
        ImageView noImage = (ImageView) findViewById(R.id.default_image);
        LinearLayout photoCountContainer = (LinearLayout) findViewById(R.id.photoCountContainer);
        if (car.images.size() == 0) {
            noImage.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            photoCountContainer.setVisibility(View.GONE);
        } else {
            PhotoPagerAdapter imagePagerAdapter = new PhotoPagerAdapter(getSupportFragmentManager(), car.images);
            viewPager.setAdapter(imagePagerAdapter);

            final CirclePageIndicator titleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
            titleIndicator.setViewPager(viewPager);

            photoIndex.setText("1" + " / " + car.images.size());
            viewPager.setOnPageChangeListener(new OnPageChangeListener() {
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

    void initsetRsult() {
        Intent intent = new Intent();
        intent.putExtra(Constant.EXTRA_KEY_EACH_CAR, car);
        intent.putExtra(Constant.EXTRA_KEY_IS_UPDATE, isUpdate);
        intent.putExtra(Constant.EXTRA_KEY_ITEM_POSITION, itemPosition);
        setResult(RESULT_OK, intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.car_details, menu);
        return true;
    }

    public void OpenDialogStage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(CarDetailsActivity.this);
        builder.setMessage("car is not done in previous stage, Do you want to done previous stage and change car stage to new stage?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogForEditStage();
            }
        });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        Dialog d = builder.create();
        d.show();
    }

    public void dialogForEditStage() {
        final String title = "Choose Stage";
        final CharSequence[] driveTypeList = getResources().getStringArray(
                R.array.StageType);
        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                editCarStageUsingVolley((String) driveTypeList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {
            }
        };

        SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                title, driveTypeList, listener);
        dialog.show(getSupportFragmentManager(), Constant.TAG_CHOOSE_OPTION);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_map) {
            if (car.locations.size() > 0) {
                Intent intent = new Intent(
                        CarDetailsActivity.this,
                        com.yukti.driveherenew.search.HistoryOfEditCarActivity.class);
                intent.putExtra("location_list", car.locations);
                startActivity(intent);
            } else {
                showToast("No Location Found for this Vehicle!");
            }
            return true;
        } else if (id == R.id.action_edit) {
            Intent intent = new Intent(CarDetailsActivity.this, AddNewCarActivity.class);
            intent.putExtra("redirect", true);
            intent.putExtra("carVin", car.vin);
            startActivityForResult(intent, REQUEST_ACTION_EDIT);
            return true;
        } else if (id == R.id.action_stages) {
            if (car.is_done.equalsIgnoreCase("0")) {
                OpenDialogStage();
            } else {
                dialogForEditStage();
            }
            return true;
        } else if (id == R.id.action_auction) {
            if (car.vehicleStatus != null && (car.vehicleStatus.equalsIgnoreCase("AUCTION") || car.caratauction.equalsIgnoreCase("1"))) {
                Intent intent = new Intent(CarDetailsActivity.this, AuctionCarDetailsActivity.class);
                intent.putExtra(EXTRAKEY_VIN, car.vin);
                startActivityForResult(intent, REQUEST_ACTION_AUCTION);
            } else {
                Intent intent = new Intent(CarDetailsActivity.this, CarAuctionActivity.class);
                intent.putExtra(EXTRAKEY_VIN, car);
                startActivityForResult(intent, REQUEST_ACTION_AUCTION);
            }

            return true;

        } else if (id == R.id.dataone) {

            if (car.vin != null && car.vin.length() == 17) {
                Intent intent = new Intent(CarDetailsActivity.this, com.yukti.driveherenew.DataoneActivity.class);
                intent.putExtra(Constant.EXTRA_KEY_VIN, car.vin);
                startActivity(intent);
            } else {
                showToast("Vin Length Should be 17");
            }
            return true;
        } else if (id == R.id.doneDate) {
            Intent i = new Intent(CarDetailsActivity.this, DonedateLotcodeActivity.class);
            i.putExtra(DonedateLotcodeActivity.EXTRAKEY_ID, car.carId);
            i.putExtra("is_done", car.is_done);
            startActivity(i);
            return true;
        } else if (id == R.id.leasehistory) {
            Intent intent = new Intent(CarDetailsActivity.this, LeaseActivity.class);
            intent.putExtra(Constant.EXTRA_KEY_VIN, car.vin);
            startActivity(intent);
            return true;
        } else if (id == R.id.goldstarlocation) {
            if (Constants.isGoldstarLogin) {
                FetchSerialNumberUsingVolley();
            } else {
                getCreadentialUsingVolley();
            }
        } else if (id == R.id.lastScanHistory) {
            Intent intent = new Intent(CarDetailsActivity.this, LastScanHistory.class);
            intent.putExtra("carid", car.carId);
            intent.putExtra(Constant.EXTRA_KEY_EACH_CAR, car);
            startActivity(intent);
        } else if (id == R.id.title) {
            Intent intent = new Intent(CarDetailsActivity.this, TitleHistoryActivity.class);
            intent.putExtra("carid", car.carId);
            startActivity(intent);
        } else if (id == R.id.webPictures) {
            Intent intent = new Intent(CarDetailsActivity.this, WebPicsHistoryActivity.class);
            intent.putExtra("carid", car.carId);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    void FetchSerialNumberUsingVolley() {
        ll_progressCarDetail.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.GET_SERIALNUMBER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ll_progressCarDetail.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        String serialnumber = jsonObject.getString("serialNumber");
                        getlatlong(serialnumber);
                        Toast.makeText(getApplicationContext(),
                                "Serialnumber  Found", Toast.LENGTH_LONG)
                                .show();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("2")) {
                        Toast.makeText(getApplicationContext(),
                                "Serialnumber Not Found", Toast.LENGTH_LONG)
                                .show();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("0")) {
                        Toast.makeText(getApplicationContext(),
                                "Location Not Found", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ll_progressCarDetail.setVisibility(View.GONE);
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("vin", car.vin);
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

    public void getlatlong(String serialnumber) {

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("Failure..", responseString.toString() + " ");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                AppJsonParser parser = new AppJsonParser();
                LatLong latlong = parser.getlatlong(response);

                if (latlong.success.equals("true")) {
                    try {
                        JSONObject jobj = response.getJSONObject("data");
                        Double lat = jobj.getDouble("latitude");
                        Double longi = jobj.getDouble("longitude");
                        Intent i = new Intent(getApplicationContext(), GoldstarMap.class);
                        i.putExtra(Constant.EXTRA_KEY_LAT, lat);
                        i.putExtra(Constant.EXTRA_KEY_LONG, longi);
                        startActivity(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("Not success", "Not in success");
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                ll_progressCarDetail.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ll_progressCarDetail.setVisibility(View.GONE);
            }
        };
        String url = AppUrl.GOLDSTAR_VEHICLE_INFO + serialnumber;
        RequestParams params = new RequestParams();
        RestClient.get(this, url, params, handler);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ll_progressCarDetail.setVisibility(View.GONE);
        cancleRequest();
    }

    void cancleRequest() {
        if (MyApplication.getInstance(this.getApplicationContext()).getRequestQueue() != null) {
            MyApplication.getInstance(this.getApplicationContext()).getRequestQueue().cancelAll(Constants.REQUEST_TAG);
        }
    }

    void editCarStageUsingVolley(final String selectedstage) {
        ll_progressCarDetail.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppSingleTon.APP_URL.URL_UPDATESTAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ll_progressCarDetail.setVisibility(View.GONE);

                try {
                    Login orm = AppSingleTon.APP_JSON_PARSER.login(response);
                    if (orm.status_code.equals("1")) {
                        stage.setText(selectedstage);
                        car.Stage = selectedstage;
                        isUpdate = true;
                        initsetRsult();
                        MessageDialogFragment fragment = new MessageDialogFragment(
                                "Success", "Updated Successfully", true, "Ok",
                                false, "", false, "", CarDetailsActivity.this);
                        fragment.show(getSupportFragmentManager(),
                                Constant.TAG_CHOOSE_OPTION);

                    } else if (orm.status_code.equals("2")) {
                        Toast.makeText(CarDetailsActivity.this, orm.message, Toast.LENGTH_SHORT).show();
                    } else if (orm.status_code.equals("3")) {
                        Toast.makeText(CarDetailsActivity.this, orm.message, Toast.LENGTH_SHORT).show();
                    } else if (orm.status_code.equalsIgnoreCase("4")) {
                        Toast.makeText(CarDetailsActivity.this, "" + orm.message, Toast.LENGTH_SHORT).show();
                        AppSingleTon.logOut(CarDetailsActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(CarDetailsActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getApplicationContext()))
                                editCarStageUsingVolley(selectedstage);
                            else
                                Toast.makeText(getApplicationContext(), Constant.ERR_INTERNET, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ll_progressCarDetail.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_carId, car.carId);
                params.put(ParamsKey.KEY_lotCode, car.lotCode);
                params.put(ParamsKey.KEY_stage, selectedstage);
                params.put(ParamsKey.KEY_userId, AppSingleTon.SHARED_PREFERENCE.getUserId());
                params.put(ParamsKey.KEY_type, Constant.APP_TYPE);
                Log.e("parames", "" + params);
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

    void getMissCarDataUsingVolley(final String vinNumber) {
        final ProgressDialog mProgressDialog = new ProgressDialog(CarDetailsActivity.this);
        mProgressDialog.setMessage("Loading.....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppSingleTon.APP_URL.URL_CAR_DEATAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Resposnes", response + " test");
                try {
                    FindMatch findmatch = AppSingleTon.APP_JSON_PARSER.findMatch(response);
                    if (findmatch.status_code.equals("1")) {
                        car = findmatch.cardetail;
                        initData();
                        mProgressDialog.dismiss();
                    } else if (findmatch.status_code.equals("0")) {
                        mProgressDialog.dismiss();
                        Toast.makeText(CarDetailsActivity.this, "No data founds", Toast.LENGTH_SHORT).show();
                    } else if (findmatch.status_code.equalsIgnoreCase("4")) {
                        Toast.makeText(CarDetailsActivity.this, "" + findmatch.message, Toast.LENGTH_SHORT).show();
                        AppSingleTon.logOut(CarDetailsActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(CarDetailsActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getApplicationContext()))
                                getMissCarDataUsingVolley(vinNumber);
                            else
                                Toast.makeText(getApplicationContext(), Constant.ERR_INTERNET, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast("Get CarData Error");
                mProgressDialog.dismiss();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_ACTION_EDIT)) {
            if (resultCode == Activity.RESULT_OK) {
                car = (CarInventory) data.getExtras().getSerializable(Constant.EXTRA_KEY_EACH_CAR);
                isUpdate = data.getBooleanExtra(Constant.EXTRA_KEY_IS_UPDATE, false);
                itemPosition = data.getIntExtra(Constant.EXTRA_KEY_ITEM_POSITION, 0);
                init();
                initsetRsult();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("Request", "Cancelled");
            }
        } else if ((requestCode == REQUEST_ACTION_AUCTION)) {
            if (resultCode == Activity.RESULT_OK) {
                car = (CarInventory) data.getExtras().getSerializable(Constant.EXTRA_KEY_EACH_CAR);
                isUpdate = data.getBooleanExtra(Constant.EXTRA_KEY_IS_UPDATE, false);
                itemPosition = data.getIntExtra(Constant.EXTRA_KEY_ITEM_POSITION, 0);
                init();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("Request", "Cancelled");
            }
        }
    }

    @Override
    public void onDialogPositiveClick(MessageDialogFragment dialog) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDialogNegativeClick(MessageDialogFragment dialog) {
    }

    @Override
    public void onDialogNeutralClick(MessageDialogFragment dialog) {
    }

    void getCreadentialUsingVolley() {
        String loginUrl = AppUrl.LOGIN_URL + "username=" + "developer1" + "&password=" + "1drivehere" + "&format=json";
        final ProgressDialog pdial = new ProgressDialog(CarDetailsActivity.this);
        ll_progressCarDetail.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ll_progressCarDetail.setVisibility(View.GONE);
                    JSONObject data = new JSONObject(response);
                    String username1 = data.getString("username");
                    token = data.getString("token");
                    logedin(token, "developer1");
                    Constants.isGoldstarLogin = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        ll_progressCarDetail.setVisibility(View.GONE);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                return super.getParams();
            }
        };
        stringRequest.setTag(Constants.REQUEST_TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constants.VOLLEY_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void logedin(String token, String username) {
        final ProgressDialog pdial = new ProgressDialog(CarDetailsActivity.this);
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Log.e("Json HTTP Failure", responseString.toString() + " ");
                FetchSerialNumberUsingVolley();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onStart() {
                super.onStart();
                pdial.setTitle("Please Wait....");
                pdial.setMessage("Check login");
                pdial.setCancelable(false);
                pdial.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (pdial.isShowing())
                    pdial.dismiss();
            }
        };

        String url = AppUrl.SESSION_URL;

        RequestParams params = new RequestParams();
        params.put(Constant.KEY_AUTH_TOKEN, token);
        params.put(Constant.KEY_AUTH_USERNAME, username);

        RestClient.post(this, url, params, handler);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (isNeeded) {
            MainActivity.isneeded = false;
            isNeeded = false;
            getMissCarDataUsingVolley(carVin);

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
            return ImageHolderFragment.newInstance(photoUrlList.get(position), 1);
        }

        @Override
        public int getCount() {
            return photoUrlList.size();
        }
    }
}
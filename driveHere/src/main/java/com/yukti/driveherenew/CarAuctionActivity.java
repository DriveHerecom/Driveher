package com.yukti.driveherenew;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.yukti.driveherenew.MessageDialogFragment.MessageDialogListener;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment.ListDialogListener;
import com.yukti.driveherenew.search.CarDetailsActivity;
import com.yukti.driveherenew.search.CarInventory;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.CamOperation;
import com.yukti.utils.CamOperation.CameraResponse;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.Constants;
import com.yukti.utils.ParamsKey;
import com.yukti.utils.SDCardManager;

import org.json.JSONException;
import org.json.JSONObject;

        /*
                *
                * $userId=$_POST['userId'];
	$type=$_POST['type'];
	$pageNo=$_POST['pageNo'];
	$inOnepage=$_POST['inOnepage'];
	$vin=$_POST['vin'];
	$rfid=$_POST['rfid'];
	$modelYear=$_POST['modelYear'];
	$lotCode=$_POST['lotCode'];
	$modelNumber=$_POST['modelNumber'];
	$price=$_POST['price'];
	$miles=$_POST['miles'];
	$fuel=$_POST['fuel'];
	$driveType=$_POST['driveType'];
	$cylinder=$_POST['cylinder'];
	$vehicleType=$_POST['vehicleType'];
	$hasTitle=$_POST['hasTitle'];
	$locationTitle=$_POST['locationTitle'];
	$company=$_POST['company'];
	$stockNumber=$_POST['stockNumber'];
	$purchasedFrom=$_POST['purchasedFrom'];
	$note=$_POST['note'];
	$noteDate=$_POST['noteDate'];
	$gpsInstalled=$_POST['gpsInstalled'];
	$vehicleStatus=$_POST['vehicleStatus'];
	$hasRfid=$_POST['hasRfid'];
	$vacancy=$_POST['vacancy'];
	$vehicleStage=$_POST['vehicleStage'];
	$serviceStage=$_POST['serviceStage'];
	$problem=$_POST['problem'];
	$doneDate=$_POST['doneDate'];
	$doneDateLotCode=$_POST['doneDateLotCode'];
	$mechanic=$_POST['mechanic'];
	$auctionName=$_POST['auctionName'];
	$auctionDate=$_POST['auctionDate'];
	$carReadyForAuction=$_POST['carReadyForAuction'];
	$carAtAuction=$_POST['carAtAuction'];*/

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntityBuilder;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;

public class CarAuctionActivity extends BaseActivity implements
        MessageDialogListener {

    public static final int CAR_AUCTION_REQUEST_CAMERA = 119;

    EditText edt_auction_name, edt_floor_price, edt_condition, edt_miles, edt_note, edt_AuctionDate;
    LinearLayout ll_container, camera, ll_edit_update, ll_progressAuction;

    TextView txt_edit_update, cameraTxt;
    CheckBox ch_carReadyForAuction, ch_carAtAuction;
    ImageView iv_addImage;

    String EDIT = "Edit";
    String UPDATE = "Update";

    CarInventory car;
    // ......................camera part..................................//
    ArrayList<File> photoList;
    SDCardManager sdcardManager;
    CamOperation camOperation = null;
    CameraResponse cameraResponse = null;
    File currentImageFile = null;
    boolean isFromCarDetail = false;
    ImageView iv1, iv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_auction);

        initToolbar();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            car = (CarInventory) getIntent().getExtras().getSerializable(CarDetailsActivity.EXTRAKEY_VIN);
            isFromCarDetail = getIntent().getExtras().getBoolean(AuctionCarDetailsActivity.EXTRA_KEY_IS_FROM_AUCTION);
        }
        ll_progressAuction = (LinearLayout) findViewById(R.id.ll_progressAuction);

        initAuction();
        initFloorPrice();
        initCondition();
        initMiles();
        initNote();
        initAuctiondate();
        initPhoto();
        initCamera();
        initEditUpdate();
        initCheckBox();
        initAddImage();
    }

    void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_car_auction_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void initAddImage() {
        iv_addImage = (ImageView) findViewById(R.id.iv_addImage);
        iv_addImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppSingleTon.VERSION_OS.checkVersion()) {
                    // Marshmallow+
                    if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        startCameraIntent();

                    } else {
                        if (!shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CarAuctionActivity.this);

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
                                    requestPermissions(new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            CAR_AUCTION_REQUEST_CAMERA);
                                }
                            });
                            builder.show();
                        }
                    }
                } else {
                    // Pre-Marshmallow
                    startCameraIntent();
                }
            }
        });
    }

    void initCheckBox() {
        ch_carReadyForAuction = (CheckBox) findViewById(R.id.ch_carReadyForAuction);
        ch_carAtAuction = (CheckBox) findViewById(R.id.ch_carAtAuction);

        if (car != null && car.carReady != null && car.carReady.equalsIgnoreCase("Yes")) {
            ch_carReadyForAuction.setChecked(true);
        } else {
            ch_carReadyForAuction.setChecked(false);
        }

        if (car != null && car.carAtAuction != null && car.carAtAuction.equalsIgnoreCase("Yes")) {
            ch_carAtAuction.setChecked(true);
        } else {
            ch_carAtAuction.setChecked(false);
        }
    }

    void initAuctiondate() {
        edt_AuctionDate = (EditText) findViewById(R.id.edt_auctiondate);
        edt_AuctionDate.setText(car.auctiondate);
        edt_AuctionDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
    }

    void openDatePicker() {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog dpd = new DatePickerDialog(
                CarAuctionActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String donedate;
                        monthOfYear = monthOfYear + 1;
                        String date;
                        if (dayOfMonth < 10) {
                            date = "0" + Integer.toString(dayOfMonth);
                        } else
                            date = Integer.toString(dayOfMonth);

                        if (monthOfYear < 10) {
                            donedate = year + "-" + "0" + monthOfYear + "-" + date;
                        } else {
                            donedate = year + "-" + monthOfYear + "-" + dayOfMonth;
                        }

                        edt_AuctionDate.setText(donedate);
                    }

                }, mYear, mMonth, mDay);

        dpd.show();
    }

    void initAuction() {

        if (car == null) {
            Log.e("car", "car null");
        }
        edt_auction_name = (EditText) findViewById(R.id.edt_auction_name);
        if (car.auctionName != null && car.auctionName.length() > 0)
            edt_auction_name.setText(car.auctionName);
        else
            edt_auction_name.setText("");

        final String title = "Action Name:";
        final CharSequence[] statusList = getResources().getStringArray(R.array.Auction_Name);
        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_auction_name.setText(statusList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_auction_name = (EditText) findViewById(R.id.edt_auction_name);
        edt_auction_name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(title, statusList, listener);
                dialog.show(getSupportFragmentManager(), Constant.TAG_CHOOSE_OPTION);
            }
        });
    }

    void initFloorPrice() {
        edt_floor_price = (EditText) findViewById(R.id.edt_floor_price);
        if (car.floorPrice != null && car.floorPrice.length() > 0)
            edt_floor_price.setText(car.floorPrice);
        else
            edt_floor_price.setText("");
    }

    void initCondition() {
        edt_condition = (EditText) findViewById(R.id.edt_condition);
        if (car.conditions != null && car.conditions.length() > 0)
            edt_condition.setText(car.conditions);
        else
            edt_condition.setText("");

        final String title = "Select Condition";
        final CharSequence[] driveTypeList = getResources().getStringArray(
                R.array.condition);

        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_condition.setText(driveTypeList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_condition.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(title, driveTypeList, listener);
                dialog.show(getSupportFragmentManager(), Constant.TAG_CHOOSE_OPTION);
            }
        });
    }

    void initMiles() {
        edt_miles = (EditText) findViewById(R.id.edt_miles);
        if (car.auctionMile != null && car.auctionMile.length() > 0)
            edt_miles.setText(car.auctionMile);
        else
            edt_miles.setText("");
    }

    void initNote() {
        edt_note = (EditText) findViewById(R.id.edt_note);
        if (car.auctionNote != null && car.auctionNote.length() > 0)
            edt_note.setText(car.auctionNote);
        else
            edt_note.setText("");
    }

    void initPhoto() {
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
    }

    void initEditUpdate() {
        ll_edit_update = (LinearLayout) findViewById(R.id.ll_edit_update);
        txt_edit_update = (TextView) findViewById(R.id.txt_edit_update);

        if (isFromCarDetail)
        {
            txt_edit_update.setText("EDIT AUCTION");
        }

        ll_edit_update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateAuctionUsingVolley();
            }

        });
    }

    public void initCamera() {

        photoList = new ArrayList<File>();
        sdcardManager = new SDCardManager();
        camOperation = new CamOperation(this);

        camera = (LinearLayout) findViewById(R.id.ll_camera);
        cameraTxt = (TextView) findViewById(R.id.camera_txt);

        camera.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AppSingleTon.VERSION_OS.checkVersion()) {
                    // Marshmallow+
                    if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        startCameraIntent();

                    } else {
                        if (!shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CarAuctionActivity.this);

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
                                    requestPermissions(new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            CAR_AUCTION_REQUEST_CAMERA);
                                }
                            });
                            builder.show();
                        }
                    }
                } else {
                    // Pre-Marshmallow
                    startCameraIntent();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAR_AUCTION_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startCameraIntent();
            } else {
                Toast.makeText(getApplicationContext(), "Both Camera & Storage Permissions Are Needed", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void startCameraIntent() {

        if (!sdcardManager.isSDCardExists()) {
            showToast("Device don't have any sdcard.");
            return;
        }

        cameraResponse = new CameraResponse() {

            @Override
            public void onNoSdcardFound() {
                showToast("No sdcard found!");
            }

            @Override
            public void onFileCreatingError() {
                showToast("Image file creating error on sdcard!");
            }

            @Override
            public void onCameraReady(File file) {
                currentImageFile = file;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(takePictureIntent, CamOperation.ACTION_TAKE_PHOTO);
            }

            @Override
            public void onSuccess() {

                try {

                    byte[] photoByte = camOperation.fileToByteOne(currentImageFile);
                    FileOutputStream fos = new FileOutputStream(currentImageFile);

                    fos.write(photoByte);
                    fos.flush();
                    fos.close();

                    photoList.add(currentImageFile);
                    cameraTxt.setText("ADD PHOTO" + "(" + photoList.size() + ")");
                    addViewInlist(camOperation.fileToBitmap(currentImageFile));
                    galleryAddPic(currentImageFile);
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {
                currentImageFile.delete();
                currentImageFile = null;
            }
        };

        if (camOperation.isIntentAvailable(MediaStore.ACTION_IMAGE_CAPTURE)) {
            camOperation.createESPhotoFile(cameraResponse);

        } else {
            showToast("No Camera intent found on this device");
        }
    }

    private void galleryAddPic(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    void addViewInlist(Bitmap bitmap) {
        try {
            View itemList = null;
            itemList = CarAuctionActivity.this.getLayoutInflater().inflate(R.layout.row_bad_pic, ll_container, false);
            iv1 = (ImageView) itemList.findViewById(R.id.iv_bad_pic1);
            iv1.setImageBitmap(bitmap);
            ll_container.setTag(photoList.size());
            ll_container.addView(itemList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == CamOperation.ACTION_TAKE_PHOTO)) {
            if (resultCode == Activity.RESULT_OK) {
                cameraResponse.onSuccess();
            } else if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }

    void updateAuctionUsingVolley() {
        ll_progressAuction.setVisibility(View.VISIBLE);
        MultipartRequest multipartRequest = new MultipartRequest(AppSingleTon.APP_URL.URL_UPDATE_AUCTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response + " test");
                        ll_progressAuction.setVisibility(View.GONE);

                        try {
                            String status_code = "";
                            String message = "";
                            try {
                                JSONObject main = new JSONObject(response);
                                status_code = main.getString("status_code");
                                message = main.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (status_code.equals("1")) {
                                finish();
                                Toast.makeText(getApplicationContext(), "Update Successfully..", Toast.LENGTH_SHORT).show();
                            } else if (status_code.equalsIgnoreCase("4")) {
                                Toast.makeText(CarAuctionActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                                AppSingleTon.logOut(getApplicationContext());
                            } else {
                                Toast.makeText(CarAuctionActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                            CommonUtils.showAlertDialog(CarAuctionActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    if (Common.isNetworkConnected(getApplicationContext()))
                                        updateAuctionUsingVolley();
                                    else
                                        Toast.makeText(getApplicationContext(),Constant.ERR_INTERNET , Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ll_progressAuction.setVisibility(View.GONE);
            }
        });
        multipartRequest.setTag(Constants.REQUEST_TAG);
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance(this.getApplicationContext()).addToRequestQueue(multipartRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ll_progressAuction.setVisibility(View.GONE);
        cancleRequest();
    }

    void cancleRequest() {
        if (MyApplication.getInstance(this.getApplicationContext()).getRequestQueue() != null) {
            MyApplication.getInstance(this.getApplicationContext()).getRequestQueue().cancelAll(Constants.REQUEST_TAG);
        }
    }

    void setEditText() {
        edt_auction_name.setText(car.auctionname);
        edt_floor_price.setText(car.floorprice);
        edt_condition.setText(car.conditions);
        edt_miles.setText(car.Miles);
        edt_note.setText(car.auctionNote);
        edt_AuctionDate.setText(car.auctiondate);

        Log.e("Car ready ", car.carready + " car " + car.carAtAuction);
        if (car != null && car.carReady != null && car.carReady.equalsIgnoreCase("yes")) {
            ch_carReadyForAuction.setSelected(true);
        } else {
            ch_carReadyForAuction.setSelected(false);
        }

        if (car != null && car.carAtAuction != null && car.carAtAuction.equalsIgnoreCase("yes")) {
            ch_carAtAuction.setSelected(true);
        } else {
            ch_carAtAuction.setSelected(false);
        }
    }

   /* void setEnanble(boolean edit) {

        edt_auction_name.setEnabled(edit);
        edt_floor_price.setEnabled(edit);
        edt_condition.setEnabled(edit);
        edt_miles.setEnabled(edit);
        edt_note.setEnabled(edit);
        edt_AuctionDate.setEnabled(edit);
        ch_carAtAuction.setEnabled(edit);
        ch_carReadyForAuction.setEnabled(edit);

//        if (edit) {
        camera.setVisibility(View.VISIBLE);
        txt_edit_update.setText(UPDATE);
        *//*} else {
            camera.setVisibility(View.INVISIBLE);
            txt_edit_update.setText(EDIT);
        }*//*
    }*/

    @Override
    public void onDialogPositiveClick(MessageDialogFragment dialog) {
        finish();
    }

    @Override
    public void onDialogNegativeClick(MessageDialogFragment dialog) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogNeutralClick(MessageDialogFragment dialog) {
        // TODO Auto-generated method stub

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

    public HttpEntity buildMultipartEntity() {

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        Log.e("Test",car.carId + " " + AppSingleTon.SHARED_PREFERENCE.getUserId() + " " + Constant.APP_TYPE + " " + edt_auction_name.getText().toString() + " " + edt_floor_price.getText().toString()+
                edt_condition.getText().toString() + " " + edt_miles.getText().toString() + " " + edt_note.getText().toString() + " " +
                edt_AuctionDate.getText().toString() + " " + ch_carAtAuction.isChecked() + " " + ch_carReadyForAuction.isChecked());
        builder.addTextBody(ParamsKey.KEY_carId, car.carId);
        builder.addTextBody(ParamsKey.KEY_userId,AppSingleTon.SHARED_PREFERENCE.getUserId());
        builder.addTextBody(ParamsKey.KEY_type, Constant.APP_TYPE);
        builder.addTextBody(ParamsKey.KEY_auctionName, edt_auction_name.getText().toString());
        builder.addTextBody(ParamsKey.KEY_floorPrice, edt_floor_price.getText().toString());
        builder.addTextBody(ParamsKey.KEY_condition, edt_condition.getText().toString());
        builder.addTextBody(ParamsKey.KEY_miles, edt_miles.getText().toString());
        builder.addTextBody(ParamsKey.KEY_auctionNote, edt_note.getText().toString());
        builder.addTextBody(ParamsKey.KEY_auctionDate, edt_AuctionDate.getText().toString());
        String carAtAuction = null;
        if (ch_carAtAuction.isChecked()) {
            carAtAuction = "Yes";
        } else {
            carAtAuction = "No";
        }
        builder.addTextBody(ParamsKey.KEY_carAtAuction, carAtAuction);

        String carReady = null;
        if (ch_carReadyForAuction.isChecked()) {
            carReady = "Yes";
        } else {
            carReady = "No";
        }
        builder.addTextBody(ParamsKey.KEY_carReady, carReady);

        if (photoList != null && photoList.size() > 0) {

            builder.addTextBody(ParamsKey.KEY_count, photoList.size() + "");

            for (int k = 0; k < photoList.size(); k++) {
                File file = photoList.get(k);
                FileBody fileBody = new FileBody(file);
                builder.addPart(ParamsKey.KEY_image_ + (k + 1), fileBody);
            }
        }
        return builder.build();
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
                return Response.success(new String(response.data, "UTF-8"), getCacheEntry());
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
}


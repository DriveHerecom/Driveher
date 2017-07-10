package com.yukti.driveherenew;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.zxing.Result;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yukti.driveherenew.MessageDialogFragment.MessageDialogListener;
import com.yukti.driveherenew.search.CarDetailsActivity;
import com.yukti.jsonparser.FindMatch;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.GetAddress;
import com.yukti.utils.RestClient;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class VinScannerActivity extends BaseActivity implements ZXingScannerView.ResultHandler, MessageDialogListener {
    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    String TAG_SCAN_RESULT_FOUND = "SCAN_RESULT_FOUND";
    String TAG_SCAN_RESULT_NOT_FOUND = "SCAN_RESULT_NOT_FOUND";
    String codeFinal;
    private ZXingScannerView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        if (state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false);
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
        } else {
            mFlash = false;
            mAutoFocus = true;
        }

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();

        mScannerView.setResultHandler(this);
        resumeCamera();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem menuItem;
        if (mFlash) {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_off);
        }

        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);


        if (mAutoFocus) {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_off);
        }

        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_flash:
                mFlash = !mFlash;
                if (mFlash) {
                    item.setTitle(R.string.flash_on);
                } else {
                    item.setTitle(R.string.flash_off);
                }
                mScannerView.setFlash(mFlash);

                return true;
            case R.id.menu_auto_focus:
                mAutoFocus = !mAutoFocus;
                if (mAutoFocus) {
                    item.setTitle(R.string.auto_focus_on);
                } else {
                    item.setTitle(R.string.auto_focus_off);
                }
                mScannerView.setAutoFocus(mAutoFocus);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void handleResult(Result rawResult) {
        codeFinal = "";
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {

        }
        Log.e("Barcode", "Barcode " + rawResult.getText() + " Type " + rawResult.getBarcodeFormat());
        Toast.makeText(getBaseContext(), "Barcode " + rawResult.getText() + " Type " + rawResult.getBarcodeFormat(), Toast.LENGTH_LONG).show();
        findMatch1(rawResult.getText());
    }

    //Resume the camera
    void resumeCamera() {
        mScannerView.startCamera();
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    void findMatch(final String vinNumber, final String lotCode) {
        final Location location = AppSingleTon.PLAY_MANAGER.getLastLocation();
        if (location != null) {
            final GetAddress getAddress = new GetAddress();
            final String LotCode = AppSingleTon.CalculationByDistance(new LatLng(location.getLatitude(), location.getLongitude()));

            if (lotCode != null && lotCode.equalsIgnoreCase(LotCode)) {
                findMatch1(vinNumber, location.getLatitude() + "", location.getLongitude() + "", LotCode, "" + getAddress.getAddressUsingLatLong(VinScannerActivity.this, location.getLatitude(), location.getLongitude()));
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Found LotCode is " + LotCode + " . Do You want to Change it ?");
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        findMatch1(vinNumber, location.getLatitude() + "", location.getLongitude() + "", LotCode, "" + getAddress.getAddressUsingLatLong(VinScannerActivity.this, location.getLatitude(), location.getLongitude()));
                    }
                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        findMatch1(vinNumber, location.getLatitude() + "", location.getLongitude() + "", null + "", "" + getAddress.getAddressUsingLatLong(VinScannerActivity.this, location.getLatitude(), location.getLongitude()));
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        } else {
            findMatch1(vinNumber, "", "", null + "", "");
        }
    }

    @Override
    public void onDialogPositiveClick(MessageDialogFragment dialog) {
        //resumeCamera();

        if (dialog.getTag().equals(TAG_SCAN_RESULT_NOT_FOUND)) {
            Intent intent = new Intent(VinScannerActivity.this, AddNewCarActivity.class);
            intent.putExtra("code", codeFinal);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.Addnewcar = false;
    }

    @Override
    public void onDialogNegativeClick(MessageDialogFragment dialog) {
        resumeCamera();
    }

    @Override
    public void onDialogNeutralClick(MessageDialogFragment dialog) {

    }

    public String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }


    void findMatch1(final String code, final String lat, final String lng, final String LotCode, final String address) {

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("findMatch response", responseString + " ");
                showToast("findMatch Error");
                resumeCamera();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    FindMatch findmatch = AppSingleTon.APP_JSON_PARSER.findMatch(response);
                    if (findmatch.match.trim().equals("1")) {
                        Intent intent = new Intent(VinScannerActivity.this, CarDetailsActivity.class);
                        intent.putExtra(CarDetailsActivity.EXTRAKEY_VIN, codeFinal);
                        Log.e("data ", " =" + code);
                        startActivity(intent);

                    } else {
                        if (MainActivity.Addnewcar) {
                            Intent intent = new Intent(VinScannerActivity.this, AddNewCarActivity.class);
                            intent.putExtra("code", codeFinal);
                            startActivity(intent);
                            finish();
                        } else {
                            MessageDialogFragment fragment = new MessageDialogFragment(
                                    "Not Found",
                                    "Not found in the Car Inventory.Do you want to save it?",
                                    true,
                                    "SAVE",
                                    true,
                                    "CANCEL",
                                    false,
                                    "",
                                    VinScannerActivity.this);
                            fragment.setData(codeFinal);
                            fragment.show(getSupportFragmentManager(), TAG_SCAN_RESULT_NOT_FOUND);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(VinScannerActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getApplicationContext()))
                                findMatch1(code,lat,lng,LotCode,address);
                            else
                                Toast.makeText(getApplicationContext(), Constant.ERR_INTERNET, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                showUpdateProgressDialog("Finding match...");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissProgressDialog();
            }
        };

        String url = AppSingleTon.APP_URL.URL_FIND_VEHICLE_NEW;
        final RequestParams params = new RequestParams();
        String tmpCode = code.trim();
        if (tmpCode.length() == 18
                && (tmpCode.startsWith("i") || tmpCode.startsWith("I"))) {
            tmpCode = tmpCode.substring(1, tmpCode.length());
        }

        codeFinal = tmpCode;

        if (tmpCode.length() == 17) {
            params.put("Vin", tmpCode.trim());
        } else if (code.length() == 7) {
            params.put("Rfid", tmpCode.trim());
        }

        if (getCurrentDate() != null) {
            params.put("CreatedDate", getCurrentDate() + "");
        }

        params.put("flag", "2");
        params.put("type", "2");
        params.put("Latitude", lat + "");
        params.put("Longitude", lng + "");
        params.put("LotCode", LotCode);
        params.put("address", "" + address);

        RestClient.post(this, url, params, handler);
    }


    void findMatch1(final String vinNumber) {
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                findMatch(vinNumber, null);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    int status = response.getInt("status_code");

                    if (status == 1) {
                        String lotCode = response.getString("LotCode");
                        findMatch(codeFinal, lotCode);
                    } else if (status == 2) {
                        if (MainActivity.Addnewcar) {
                            Intent intent = new Intent(VinScannerActivity.this, AddNewCarActivity.class);
                            intent.putExtra("code", codeFinal);
                            startActivity(intent);
                            finish();
                        } else {
                            MessageDialogFragment fragment = new MessageDialogFragment(
                                    "Not Found",
                                    "Not found in the Car Inventory.Do you want to save it?",
                                    true, "SAVE", true, "CANCEL", false, "",
                                    VinScannerActivity.this);
                            fragment.setData(codeFinal);
                            fragment.show(getSupportFragmentManager(),
                                    TAG_SCAN_RESULT_NOT_FOUND);
                        }
                    } else {
                        findMatch(vinNumber, null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(VinScannerActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getApplicationContext()))
                                findMatch1(vinNumber);
                            else
                                Toast.makeText(getApplicationContext(), Constant.ERR_INTERNET, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                showUpdateProgressDialog("Finding match...");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissProgressDialog();
            }
        };

        String url = AppSingleTon.APP_URL.URL_FIND_VEHICLE_NEW;
        final RequestParams params = new RequestParams();
        String tmpCode = vinNumber.trim();
        if (tmpCode.length() == 18
                && (tmpCode.startsWith("i") || tmpCode.startsWith("I"))) {
            tmpCode = tmpCode.substring(1, tmpCode.length());
        }
        codeFinal = tmpCode;
        if (tmpCode.length() == 17) {
            params.put("Vin", tmpCode.trim());
        } else if (tmpCode.length() == 7) {
            params.put("Rfid", tmpCode.trim());
        }

        params.put("type", "1");
        Log.e("params"," ="+params);
        RestClient.post(this, url, params, handler);
    }
}

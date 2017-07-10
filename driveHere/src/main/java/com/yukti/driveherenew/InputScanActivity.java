package com.yukti.driveherenew;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
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
import java.util.Calendar;

public class InputScanActivity extends BaseActivity implements
        MessageDialogListener {

    Toolbar toolbar;
    EditText editVin;
    Button search;
    String TAG_SCAN_RESULT_FOUND = "SCAN_RESULT_FOUND";
    String TAG_SCAN_RESULT_NOT_FOUND = "SCAN_RESULT_NOT_FOUND";
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input_scan);
        toolbar = (Toolbar) findViewById(R.id.activity_input_scan_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editVin = (EditText) findViewById(R.id.edit_input_number);
        search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppSingleTon.METHOD_BOX.hidekeyBoard(InputScanActivity.this);
                String code = editVin.getText().toString().trim();
                if (code.length() == 17 || code.length() == 7) {
                    findMatch1(code);
                } else {
                    showToast("Invalid Code! Length should be 7 or 17.");
                }

            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void findMatch(final String vinNumber, final String lotCode) {
        final Location location = AppSingleTon.PLAY_MANAGER.getLastLocation();
        if (location != null) {
            final GetAddress getAddress = new GetAddress();
            final String LotCode = AppSingleTon.CalculationByDistance(new LatLng(location.getLatitude(), location.getLongitude()));

            if (lotCode != null && lotCode.equalsIgnoreCase(LotCode)) {
                findMatch1(vinNumber, location.getLatitude() + "", location.getLongitude() + "", LotCode, "" + getAddress.getAddressUsingLatLong(InputScanActivity.this, location.getLatitude(), location.getLongitude()));
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Found LotCode is " + LotCode + " . Do You want to Change it ?");
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        findMatch1(vinNumber, location.getLatitude() + "", location.getLongitude() + "", LotCode, "" + getAddress.getAddressUsingLatLong(InputScanActivity.this, location.getLatitude(), location.getLongitude()));
                    }
                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        findMatch1(vinNumber, location.getLatitude() + "", location.getLongitude() + "", null + "", "" + getAddress.getAddressUsingLatLong(InputScanActivity.this, location.getLatitude(), location.getLongitude()));
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        } else {
            findMatch1(vinNumber, "", "", null + "", "");
        }
    }

    void findMatch1(final String vinNumber, final String lat, final String lng, final String LotCode, final String address) {
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("findMatch error", responseString);
                showToast("findMatch Error");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {

                    FindMatch findmatch = AppSingleTon.APP_JSON_PARSER.findMatch(response);
                    if (findmatch.status_code.trim().equals("1")) {
                        Intent intent = new Intent(InputScanActivity.this, CarDetailsActivity.class);
                        intent.putExtra(CarDetailsActivity.EXTRAKEY_VIN, vinNumber);
                        startActivity(intent);
                    } else {
                        if (MainActivity.Addnewcar) {
                            Intent intent = new Intent(InputScanActivity.this,
                                    AddNewCarActivity.class);
                            intent.putExtra("code", editVin.getText().toString().trim());
                            startActivity(intent);
                        } else {
                            MessageDialogFragment fragment = new MessageDialogFragment(
                                    "Not Found",
                                    "Not found in the Car Inventory.Do you want to save it?",
                                    true, "SAVE", true, "CANCEL", false, "",
                                    InputScanActivity.this);

                            fragment.setData(vinNumber);
                            fragment.show(getSupportFragmentManager(),
                                    TAG_SCAN_RESULT_NOT_FOUND);
                        }
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(InputScanActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getApplicationContext()))
                                findMatch1(vinNumber,lat,lng,LotCode,address);
                            else
                                Toast.makeText(getApplicationContext(),Constant.ERR_INTERNET , Toast.LENGTH_LONG).show();
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

        if (vinNumber.length() == 17) {
            params.put("Vin", vinNumber.trim());
        } else if (vinNumber.length() == 7) {
            params.put("Rfid", vinNumber.trim());
        }

        params.put("flag", "1");
        params.put("type", "2");

        if (getCurrentDate() != null) {
            params.put("CreatedDate", getCurrentDate() + "");
        }
        params.put("Latitude", lat + "");
        params.put("Longitude", lng + "");
        params.put("LotCode", LotCode);
        params.put("address", "" + address);

        RestClient.post(this, url, params, handler);
    }

    void findMatch1(final String vinNumber) {
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                findMatch(vinNumber, null);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("response", response.toString());
                try {

                    int status = response.getInt("status_code");

                    if (status == 1) {
                        String lotCode = response.getString("LotCode");
                        findMatch(vinNumber, lotCode);
                    } else if (status == 2) {
                        if (MainActivity.Addnewcar) {
                            Intent intent = new Intent(InputScanActivity.this,
                                    AddNewCarActivity.class);
                            intent.putExtra("code", editVin.getText().toString().trim());
                            startActivity(intent);
                        } else {
                            MessageDialogFragment fragment = new MessageDialogFragment(
                                    "Not Found",
                                    "Not found in the Car Inventory.Do you want to save it?",
                                    true, "SAVE", true, "CANCEL", false, "",
                                    InputScanActivity.this);
                            fragment.setData(vinNumber);
                            fragment.show(getSupportFragmentManager(),
                                    TAG_SCAN_RESULT_NOT_FOUND);
                        }
                    } else {
                        findMatch(vinNumber, null);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(InputScanActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getApplicationContext()))
                                findMatch1(vinNumber);
                            else
                                Toast.makeText(getApplicationContext(),Constant.ERR_INTERNET , Toast.LENGTH_LONG).show();
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

        if (vinNumber.length() == 17) {
            params.put("Vin", vinNumber.trim());
        } else if (vinNumber.length() == 7) {
            params.put("Rfid", vinNumber.trim());
        }
        params.put("type", "1");
        RestClient.post(this, url, params, handler);
    }

    @Override
    public void onDialogPositiveClick(MessageDialogFragment dialog) {

        if (dialog.getTag().equals(TAG_SCAN_RESULT_NOT_FOUND)) {
            Intent intent = new Intent(InputScanActivity.this, AddNewCarActivity.class);
            intent.putExtra("code", editVin.getText().toString().trim());
            startActivity(intent);
        }
    }

    @Override
    public void onDialogNegativeClick(MessageDialogFragment dialog) {

    }

    @Override
    public void onDialogNeutralClick(MessageDialogFragment dialog) {

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "InputScan Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.yukti.driveherenew/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "InputScan Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.yukti.driveherenew/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.Addnewcar = false;
    }
}

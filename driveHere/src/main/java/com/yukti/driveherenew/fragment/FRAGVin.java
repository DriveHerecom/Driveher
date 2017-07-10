package com.yukti.driveherenew.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yukti.dataone.model.BasicData;
import com.yukti.dataone.model.Engine;
import com.yukti.dataone.model.ParentNode;
import com.yukti.dataone.model.Query_Error;
import com.yukti.driveherenew.AddNewCarActivity;
import com.yukti.driveherenew.AddNewCarActivity.Fragments;
import com.yukti.driveherenew.BarCodeScannerActivity;
import com.yukti.driveherenew.MyApplication;
import com.yukti.driveherenew.R;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.Constants;
import com.yukti.utils.ParamsKey;
import com.yukti.utils.RestClient;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FRAGVin extends Fragment {

    private static final int VIN_REQUEST_CAMERA = 212;
    public static String dataoneInformation;
    CallbackAdd callbackAdd;
    EditText edt_vin;
    int REQUEST_SCAN_CODE = 25743;
    String code;
    BasicData basicData;
    private View mView;

    public static FRAGVin newInstance() {
        FRAGVin f = new FRAGVin();
        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callbackAdd = (CallbackAdd) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CallbackAdd");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_vin, container, false);
        edt_vin = (EditText) mView.findViewById(R.id.edt_vin);
//        progressWheel = (ProgressWheel)mView.findViewById(R.carId.progress_dialog);
//        progressWheel.setBarColor(Color.BLUE);


        edt_vin.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                final String edt_values = edt_vin.getText().toString();
                if (s.length() == 17) {

                    Log.e("OnText Change..", "OnText Change..");
//                    pullDataoneInformations();
//                    findMatchVin(edt_values);
                    pullDataoneInformationsUsingVolley();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                Log.e("After TextChange..", AddNewCarActivity.isFromEditTextSearch + "");
//                AddNewCarActivity.isFromEditTextSearch=true;
            }
        });

        initNextButton();
        initButtonScan();
        return mView;
    }

    void initNextButton() {
        final Button btn_next = (Button) mView.findViewById(R.id.btn_next);
        if (!AddNewCarActivity.addCarModelObject.isFrmInfo()) {
            if (AddNewCarActivity.arryFragments.get(0) == Fragments.Vin)
                btn_next.setText("Next >> Rfid");
            else
                btn_next.setText("Next >> Vacancy");
        } else {
            btn_next.setText("Back << All Info");
        }

        btn_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String vin = edt_vin.getText().toString().trim();
                if (vin.length() != 0 && vin.length() == 17) {
                    if (AddNewCarActivity.addCarModelObject.isDataOneInfoFound()) {

//                        AddNewCarActivity.strVin = vin.trim();
                        if (!AddNewCarActivity.addCarModelObject.isFrmInfo()) {
                            callbackAdd.onNextSecected(false, null);
                        } else {
                            callbackAdd.onNextSecected(false,
                                    Fragments.AllDetail);
                        }
//                        findMatchVin(vin);
//                        AddNewCarActivity.addCarModelObject.setStrVin(vin.trim());
//                        if (!AddNewCarActivity.addCarModelObject.isFrmInfo()) {
//                            callbackAdd.onNextSecected(false, null);
//                        } else {
//                            callbackAdd.onNextSecected(false,
//                                    Fragments.AllDetail);
//                        }
                    } else {
                        Toast.makeText(getActivity(), "DataOne Information Not found", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // show mandatory message
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Invalid VIN!", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });
    }

    void initButtonScan() {
        Button btn_scan = (Button) mView.findViewById(R.id.btn_scanvin);
        btn_scan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                initScanVin();
            }
        });
    }

    void getVin() {
        Intent intent = new Intent(getActivity(), BarCodeScannerActivity.class);
        startActivityForResult(intent, REQUEST_SCAN_CODE);
    }

    void initScanVin() {

        if (AppSingleTon.VERSION_OS.checkVersion()) {
            // Marshmallow+
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                getVin();

            } else {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle("");
                    builder.setMessage("Camera Permission Needed For Scanning Vin,Allow It?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            final Intent i = new Intent();
                            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            i.addCategory(Intent.CATEGORY_DEFAULT);
                            i.setData(Uri.parse("package:" + getActivity().getPackageName()));
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(i);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, VIN_REQUEST_CAMERA);
                        }
                    });
                    builder.show();

                }


            }

        } else {
            // Pre-Marshmallow
            getVin();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == VIN_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getVin();
            } else {
                Toast.makeText(getActivity(), "Camera Permission was not granted", Toast.LENGTH_SHORT).show();
            }
        } else {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int scanCodeLength = 0;

        if (resultCode == Activity.RESULT_OK) {
            code = data.getStringExtra("code");
            scanCodeLength = code.length();
            Log.e("Code...", code.length() + "");
            if (code.length() == 17) {
                edt_vin.setText(code);
                findMatchVin(code);
//                pullDataoneInformations();

//  pullDataoneInformationsUsingVolley();

//                AddNewCarActivity.isFromEditTextSearch=true;
                Log.e("set EditText in if", "set EditText in if");
            } else if (requestCode == REQUEST_SCAN_CODE) {
                // set Scan VIN
                Log.e("set EditText in Elseif", "set EditText in elksif");
                if (resultCode == Activity.RESULT_OK) {
                    code = data.getStringExtra("code");
                    scanCodeLength = code.length();
                    if (scanCodeLength == 18
                            && (code.startsWith("i") || code.startsWith("I"))) {
                        code = code.substring(1, code.length());
                    }
                    Log.e("scanCode", code + " " + code.length());
                    scanCodeLength = code.length();
                    if (scanCodeLength == 17) {
                        edt_vin.setText(code);
//                        AddNewCarActivity.strVin = code;
                        AddNewCarActivity.addCarModelObject.setStrVin(code);
                        // tvScanVin.setVisibility(View.GONE);
                    } else
                        Toast.makeText(getActivity(), "Not a Vin", Toast.LENGTH_SHORT).show();
                    /*
                     * else if (scanCodeLength == 7) { rfid.setText(scanCode);
					 * tvScanRfid.setVisibility(View.GONE); }
					 */
                    AppSingleTon.METHOD_BOX.hidekeyBoard(getActivity());
                    Log.e("Code...else if", code.length() + "");
//                    Toast.makeText(getActivity(), code.length() + "", Toast.LENGTH_SHORT).show();
                    if (code.length() == 17) {
                        findMatchVin(code);
//                        pullDataoneInformations();
//                        pullDataoneInformationsUsingVolley();
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (AddNewCarActivity.addCarModelObject.getStrVin() != null && AddNewCarActivity.addCarModelObject.getStrVin().length() != 0) {
            edt_vin.setText(AddNewCarActivity.addCarModelObject.getStrVin());
        }
    }

    void pullDataoneInformationsUsingVolley() {

        final LinearLayout ll_progresslayout = (LinearLayout) mView.findViewById(R.id.ll_progressbar);
        code = edt_vin.getText().toString();
        if (AddNewCarActivity.addCarModelObject.getPreVin().trim().equals(code.trim())) {
            return;
        }
        ll_progresslayout.setVisibility(View.VISIBLE);
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Please wait.....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppSingleTon.APP_URL.URL_DATA_ONE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ll_progresslayout.setVisibility(View.GONE);

                try {
                    if (FRAGVin.this.isVisible()) {
                        mProgressDialog.dismiss();
                        ParentNode orm = AppSingleTon.APP_JSON_PARSER.parseDataoneResponse(response);
                        Query_Error queryError = orm.query_responses.RequestSample.query_error;
                        AddNewCarActivity.addCarModelObject.setDataOneInfoFound(true);
                        if (!queryError.error_code.equals("")) {
                            Toast.makeText(getActivity(), queryError.error_message, Toast.LENGTH_SHORT).show();
                            Log.e("error_message", queryError.error_message);
                            AddNewCarActivity.addCarModelObject.setDataOneInfoFound(false);
                            return;
                        }

                        dataoneInformation = response.toString();

                        basicData = orm.query_responses.RequestSample.us_market_data.common_us_data.basic_data;

                        if (basicData != null) {
                            AddNewCarActivity.addCarModelObject.setStrMake(basicData.make);
                            AddNewCarActivity.addCarModelObject.setStrModel(basicData.model);
                            AddNewCarActivity.addCarModelObject.setStrModelNumber(basicData.model_number);
                            AddNewCarActivity.addCarModelObject.setStrModelYear(basicData.year);
                            AddNewCarActivity.addCarModelObject.setStrVehicleType(basicData.vehicle_type);
                            AddNewCarActivity.addCarModelObject.setStrDriveType(basicData.drive_type);
                        }

                        ArrayList<Engine> engines = orm.query_responses.RequestSample.us_market_data.common_us_data.engines;

                        if (engines != null && engines.size() > 0) {

                            Engine engine = engines.get(0);
                            AddNewCarActivity.addCarModelObject.setStrMaxHp(engine.max_hp);
                            AddNewCarActivity.addCarModelObject.setStrMaxTorque(engine.max_torque);
                            AddNewCarActivity.addCarModelObject.setStrCylinder(engine.cylinders);
                            AddNewCarActivity.addCarModelObject.setStrOilCapacity(engine.oil_capacity);

                            if (!engine.fuel_type.equals("")) {
                                String[] sk = getResources().getStringArray(R.array.FuelTypeShortKey);
                                String[] ft = getResources().getStringArray(R.array.FuelType);
                                for (int i = 0; i < sk.length; i++) {
                                    if (sk[i].equals(engine.fuel_type.trim())) {
                                        String fueltype = ft[i];
                                        Log.e("FuelType is: ", "" + fueltype);
                                        AddNewCarActivity.addCarModelObject.setStrFuelType(fueltype);
                                        break;
                                    }
                                }
                            }
                        }
                        logDataOneData(dataoneInformation);
                        AppSingleTon.METHOD_BOX.hidekeyBoard(getActivity());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(getActivity(), Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getActivity()))
                                pullDataoneInformationsUsingVolley();
                            else
                                Toast.makeText(getActivity(), Constant.ERR_INTERNET, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressDialog.dismiss();
                        ll_progresslayout.setVisibility(View.GONE);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String decoder_query = getQueryParameters();
                params.put("client_id", getString(R.string.dataone_client_id));
                params.put("authorization_code",
                        getString(R.string.dataone_authorization_code));
                params.put("decoder_query", decoder_query);

                Log.e("DataoneInfo", params + "");

                return params;
            }
        };
        stringRequest.setTag(Constants.REQUEST_TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constants.VOLLEY_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public void logDataOneData(String res) {

        Log.e("DataOne Data", "DataOneData : " + res);
        Log.e("DataOne Data", "AddNewCarActivity.preVin : "
                + AddNewCarActivity.addCarModelObject.getPreVin());
        Log.e("DataOne Data", "AddNewCarActivity.strMake : "
                + AddNewCarActivity.addCarModelObject.getStrMake());

        Log.e("DataOne Data", "AddNewCarActivity.strModel : "
                + AddNewCarActivity.addCarModelObject.getStrModel());
        Log.e("DataOne Data", "AddNewCarActivity.strModelNumber : "
                + AddNewCarActivity.addCarModelObject.getStrModelNumber());
        Log.e("DataOne Data", "AddNewCarActivity.strModelYear : "
                + AddNewCarActivity.addCarModelObject.getStrModelYear());
        Log.e("DataOne Data", "AddNewCarActivity.strVehicleType : "
                + AddNewCarActivity.addCarModelObject.getStrVehicleType());
        Log.e("DataOne Data", "AddNewCarActivity.strDriveType : "
                + AddNewCarActivity.addCarModelObject.getStrDriveType());
        Log.e("DataOne Data", "AddNewCarActivity.strMaxHp : "
                + AddNewCarActivity.addCarModelObject.getStrMaxHp());
        Log.e("DataOne Data", "AddNewCarActivity.strMaxTorque : "
                + AddNewCarActivity.addCarModelObject.getStrMaxTorque());
        Log.e("DataOne Data", "AddNewCarActivity.strCylinder : "
                + AddNewCarActivity.addCarModelObject.getStrCylinder());
        Log.e("DataOne Data", "AddNewCarActivity.strOilCapacity : "
                + AddNewCarActivity.addCarModelObject.getStrOilCapacity());
        Log.e("DataOne Data", "AddNewCarActivity.strFuelType : "
                + AddNewCarActivity.addCarModelObject.getStrFuelType());
        Log.e("DataOne Data", "AddNewCarActivity.strColor : "
                + AddNewCarActivity.addCarModelObject.getStrColor());
        Log.e("DataOne Data", "AddNewCarActivity.strMiles : "
                + AddNewCarActivity.addCarModelObject.getStrMiles());
        if (AddNewCarActivity.isedit) {
            if (!AddNewCarActivity.addCarModelObject.getStrVin().equalsIgnoreCase(edt_vin.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_vin)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_vin);
            }
        }
        AddNewCarActivity.addCarModelObject.setStrVin(edt_vin.getText().toString().trim());

    }

    String getQueryParameters() {
        String decoder_query = getString(R.string.decoder_query_json);
        decoder_query = decoder_query.replace("xxxxx", code);
        // Log.d("decoder_query", decoder_query);
        return decoder_query;
    }

    void findMatchVin(final String VinNumber) {
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Please wait.....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                mProgressDialog.dismiss();
                try {
                    int status = response.getInt("status_code");
                    if (status == 1) {
                        Toast.makeText(getActivity(), "Vin Already Exits", Toast.LENGTH_SHORT).show();
                        edt_vin.setText("");
                    } else if (status == 2) {
                        pullDataoneInformationsUsingVolley();
                    } else {
                        Toast.makeText(getActivity(), "Vin Already Exits", Toast.LENGTH_SHORT).show();
                        edt_vin.setText("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(getActivity(), Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getActivity()))
                                findMatchVin(VinNumber);
                            else
                                Toast.makeText(getActivity(), Constant.ERR_INTERNET, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mProgressDialog.dismiss();
            }
        };
        String url = AppSingleTon.APP_URL.URL_FIND_VEHICLE_NEW;
        final RequestParams params = new RequestParams();
        params.put("Vin", VinNumber.trim());
        params.put("type", "1");
        RestClient.post(getActivity(), url, params, handler);
    }


    public interface OnTagSelectedListener {
        public void onTagSelected(int tagPosition, String strTag);

        public void onTagLongPress(int tagPosition, String strTag);
    }
}

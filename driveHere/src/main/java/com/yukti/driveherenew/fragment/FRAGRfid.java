package com.yukti.driveherenew.fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yukti.dataone.model.BasicData;
import com.yukti.dataone.model.Colors;
import com.yukti.dataone.model.Engine;
import com.yukti.dataone.model.ParentNode;
import com.yukti.dataone.model.Query_Error;
import com.yukti.dataone.model.Warranties;
import com.yukti.driveherenew.AddNewCarActivity;
import com.yukti.driveherenew.AddNewCarActivity.Fragments;
import com.yukti.driveherenew.BarCodeScannerActivity;
import com.yukti.driveherenew.InputScanActivity;
import com.yukti.driveherenew.MainActivity;
import com.yukti.driveherenew.MessageDialogFragment;
import com.yukti.driveherenew.R;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.ParamsKey;
import com.yukti.utils.RestClient;

public class FRAGRfid extends Fragment {

    private View mView;
    CallbackAdd callbackAdd;
    EditText edt_rfid;
    int REQUEST_SCAN_CODE = 25743;
    String code = "";

    private static final int RFID_REQUEST_CAMERA = 211;


    public static FRAGRfid newInstance() {
        FRAGRfid f = new FRAGRfid();
        return f;
    }

    public interface OnTagSelectedListener {
        public void onTagSelected(int tagPosition, String strTag);

        public void onTagLongPress(int tagPosition, String strTag);
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

        mView = inflater.inflate(R.layout.fragment_rfid, container, false);
        edt_rfid = (EditText) mView.findViewById(R.id.edt_rfid);

        edt_rfid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        initBtnNext();
        initbtnscan();
        return mView;
    }

    void initbtnscan() {

        Button btn_scan = (Button) mView.findViewById(R.id.btn_scanRfid);
        btn_scan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                initRFID();
            }
        });
    }

    void getBarcode() {
        Intent intent = new Intent(getActivity(), BarCodeScannerActivity.class);
        startActivityForResult(intent, REQUEST_SCAN_CODE);
    }

    void initRFID() {

        if (AppSingleTon.VERSION_OS.checkVersion()) {
            // Marshmallow+
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                getBarcode();

            } else {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle("");
                    builder.setMessage("Camera Permission Needed For Scanning Rfid,Allow It?");
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
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, RFID_REQUEST_CAMERA);
                        }
                    });
                    builder.show();
                }
            }
        } else {
            // Pre-Marshmallow
            getBarcode();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RFID_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getBarcode();
            } else {
                Toast.makeText(getActivity(), "Camera Permission was not granted", Toast.LENGTH_SHORT).show();
            }
        } else {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void initBtnNext() {
        Button btn_next = (Button) mView.findViewById(R.id.btn_next);
        if (!AddNewCarActivity.addCarModelObject.isFrmInfo()) {
            if (AddNewCarActivity.arryFragments.get(0) == Fragments.Rfid)
                btn_next.setText("Next >> Vin");
            else
                btn_next.setText("Next >> Vacancy");
        } else {
            btn_next.setText("Back << All Info");
        }
        btn_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (edt_rfid.getText().toString().length() == 0) {
                    if (AddNewCarActivity.isedit) {
                        if (!AddNewCarActivity.addCarModelObject.getStrRfid().equalsIgnoreCase(edt_rfid.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_rfid)) {
                            AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_rfid);
                        }
                    }
                    AddNewCarActivity.addCarModelObject.setStrRfid(" ");
                    callbackAdd.onNextSecected(false, null);
                } else {
                    if (edt_rfid.getText().toString().length() == 7) {
                        if (!AddNewCarActivity.addCarModelObject.getStrRfid().equalsIgnoreCase(edt_rfid.getText().toString())) {
                            findMatchRfid(edt_rfid.getText().toString().trim());
                        } else {
                            if (AddNewCarActivity.isedit) {
                                if (!AddNewCarActivity.addCarModelObject.getStrRfid().equalsIgnoreCase(edt_rfid.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_rfid)) {
                                    AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_rfid);
                                }
                            }
                            AddNewCarActivity.addCarModelObject.setStrRfid(edt_rfid.getText().toString());
                            callbackAdd.onNextSecected(false, null);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Enter proper RFID!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_SCAN_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                code = data.getStringExtra("code");
                int scanCodeLength = code.length();

                Log.e("scanCode", code);

                if (scanCodeLength == 7) {
                    edt_rfid.setText(code);
                }
                AppSingleTon.METHOD_BOX.hidekeyBoard(getActivity());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (code.equalsIgnoreCase("") && AddNewCarActivity.addCarModelObject.getStrRfid() != null && AddNewCarActivity.addCarModelObject.getStrRfid().length() != 0) {
            edt_rfid.setText(AddNewCarActivity.addCarModelObject.getStrRfid());
        }
    }

    void findMatchRfid(final String RfidNumber) {
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
                Log.e("response", "" + response);
                mProgressDialog.dismiss();
                try {
                    int status = response.getInt("status_code");
                    if (status == 1) {
                        Toast.makeText(getActivity(), "Rfid Already Exits", Toast.LENGTH_SHORT).show();
//                        if (AddNewCarActivity.addCarModelObject.getStrRfid().equalsIgnoreCase("")) {
                        edt_rfid.setText(AddNewCarActivity.addCarModelObject.getStrRfid());
//                        }
                    } else if (status == 2) {
                        if (AddNewCarActivity.isedit) {
                            if (!AddNewCarActivity.addCarModelObject.getStrRfid().equalsIgnoreCase(RfidNumber.toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_rfid)) {
                                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_rfid);
                            }
                        }
                        AddNewCarActivity.addCarModelObject.setStrRfid(RfidNumber);
                        callbackAdd.onNextSecected(false, null);
                    } else {
                        Toast.makeText(getActivity(), "Rfid Already Exits", Toast.LENGTH_SHORT).show();
                        edt_rfid.setText("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(getActivity(), Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getActivity()))
                                findMatchRfid(RfidNumber);
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
            }
        };
        String url = AppSingleTon.APP_URL.URL_FIND_VEHICLE_NEW;
        final RequestParams params = new RequestParams();
        params.put("Rfid", RfidNumber);
        params.put("type", "1");
        RestClient.post(getActivity(), url, params, handler);
    }
}

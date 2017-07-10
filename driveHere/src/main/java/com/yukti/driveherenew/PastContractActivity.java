package com.yukti.driveherenew;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yukti.driveherenew.search.SearchResultPastContract;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.ContractData;

public class PastContractActivity extends BaseActivity {
    public static final int REQUEST_SCAN_VIN = 1001;
    public static final int REQUEST_SCAN_RFID = 1002;
    public static final int REQUEST_CAMERA_FOR_SCANNING = 1003;
    EditText edt_customerName, edt_carNumber, edt_stockNumber, edt_Tag, edt_rfid, edt_contract_id;
    boolean isOneFieldSelected = false;
    int vid;
    View.OnClickListener scanListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            vid = v.getId();

            if (AppSingleTon.VERSION_OS.checkVersion()) {
                // Marshmallow+
                if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    getScanner(vid);

                } else {
                    if (!shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PastContractActivity.this);

                        builder.setTitle("");
                        builder.setMessage("Camera Permission Needed For Scanning,Allow It?");
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
                                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_FOR_SCANNING);
                            }
                        });
                        builder.show();
                    }
                }
            } else {
                // Pre-Marshmallow
                getScanner(vid);

            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_contract);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_search_past_contract);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvScanVin = (TextView) findViewById(R.id.tv_scan_vin1);
        TextView tvScanRFID = (TextView) findViewById(R.id.tv_scan_rfid1);

        tvScanRFID.setOnClickListener(scanListener);
        tvScanVin.setOnClickListener(scanListener);

        initcustomerNumber();
        initcarNumber();
        initstockNumber();
        inittag();
        initRFID();
        initContractId();
        initSearch();
    }

    void initContractId() {
        edt_contract_id = (EditText) findViewById(R.id.contract_number);
        edt_contract_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    void initcustomerNumber() {
        edt_customerName = (EditText) findViewById(R.id.customer_name);
        edt_customerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    void initcarNumber() {
        edt_carNumber = (EditText) findViewById(R.id.edt_car_number);

        edt_carNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                isOneFieldSelected = true;
                edt_carNumber.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edt_carNumber.length() == 0)
                    isOneFieldSelected = false;
            }
        });
    }

    void initRFID() {
        edt_rfid = (EditText) findViewById(R.id.edt_rfid_number);

        edt_rfid.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isOneFieldSelected = true;
                edt_rfid.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edt_rfid.length() == 0)
                    isOneFieldSelected = false;
            }
        });
    }

    void initstockNumber() {
        edt_stockNumber = (EditText) findViewById(R.id.edt_stockNumber);
        edt_stockNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    void inittag() {
        edt_Tag = (EditText) findViewById(R.id.vehicle_tag);
        edt_Tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    void getScanner(int id) {
        switch (id) {
            case R.id.tv_scan_rfid1:

                Intent scannerRfid = new Intent(PastContractActivity.this, AddCarScannerActivity.class);
                scannerRfid.putExtra("IS_VIN", false);
                startActivityForResult(scannerRfid, REQUEST_SCAN_RFID);
                break;

            case R.id.tv_scan_vin1:

                Intent scannerVin = new Intent(PastContractActivity.this, AddCarScannerActivity.class);
                scannerVin.putExtra("IS_VIN", true);
                startActivityForResult(scannerVin, REQUEST_SCAN_VIN);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_FOR_SCANNING) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getScanner(vid);
            } else {
                Toast.makeText(getApplicationContext(), "Camera Permission was not granted", Toast.LENGTH_SHORT).show();
            }
        } else {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    boolean validateValues() {
        isOneFieldSelected = false;
        int validate = 0;

        if ((edt_carNumber.getText().toString() != null && edt_carNumber.getText().toString().length() > 0))
            validate = 1;
        if ((edt_customerName.getText().toString() != null && edt_customerName.getText().toString().length() > 0))
            validate = 1;
        if ((edt_Tag.getText().toString() != null && edt_Tag.getText().toString().length() > 0))
            validate = 1;
        if ((edt_stockNumber.getText().toString() != null && edt_stockNumber.getText().toString().length() > 0))
            validate = 1;
        if ((edt_contract_id.getText().toString() != null && edt_contract_id.getText().toString().length() > 0))
            validate = 1;
        if ((edt_rfid.getText().toString() != null && edt_rfid.getText().toString().length() > 0))
            validate = 1;

        if (validate == 0)
            isOneFieldSelected = false;
        else
            isOneFieldSelected = true;

        return isOneFieldSelected;
    }

    public void initSearch() {
        LinearLayout search = (LinearLayout) findViewById(R.id.search_past_contract);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContractData search_query = new ContractData();
                isOneFieldSelected = validateValues();
                Log.e("VALIDATE VALUES", " " + isOneFieldSelected);

                if (isOneFieldSelected) {
                    if (Common.isNetworkConnected(getApplicationContext())) {
                        search_query.car_vin = edt_carNumber.getText().toString();
                        search_query.customer_name = edt_customerName.getText().toString();
                        search_query.stock_number = edt_stockNumber.getText().toString();
                        search_query.tag = edt_Tag.getText().toString();
                        search_query.car_rfid = edt_rfid.getText().toString();
                        search_query.contract_id = edt_contract_id.getText().toString();

                        Intent intent = new Intent(PastContractActivity.this, SearchResultPastContract.class);
                        intent.putExtra("search_query", search_query);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please Connect To Internet", Toast.LENGTH_LONG)
                                .show();
                    }
                } else
                    Toast.makeText(getApplicationContext(),
                            "Enter one value atleast", Toast.LENGTH_LONG)
                            .show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SCAN_RFID) {
            if (resultCode == Activity.RESULT_OK) {
                String scanCode = data.getStringExtra("code");
                int scanCodeLength = scanCode.length();
                Log.e("scanCode", scanCode);
                scanCodeLength = scanCode.length();

                if (scanCodeLength == 7) {
                    edt_rfid.setText(scanCode);
                    isOneFieldSelected = true;
                }
            }
        } else if (requestCode == REQUEST_SCAN_VIN) {
            // TODO set Scan VIN
            if (resultCode == Activity.RESULT_OK) {
                String scanCode = data.getStringExtra("code");
                int scanCodeLength = scanCode.length();
                if (scanCodeLength == 18 && (scanCode.startsWith("i") || scanCode.startsWith("I"))) {
                    scanCode = scanCode.substring(1, scanCode.length());
                }
                scanCodeLength = scanCode.length();
                if (scanCodeLength == 17) {
                    edt_carNumber.setText(scanCode);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
package com.yukti.driveherenew.search;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yukti.driveherenew.AddCarScannerActivity;
import com.yukti.driveherenew.BaseActivity;
import com.yukti.driveherenew.ColorChoiceDialogFragment;
import com.yukti.driveherenew.LotcodeChoiceDialogFragment;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment.ListDialogListener;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;

import java.util.Calendar;

public class SearchActivity extends BaseActivity implements OnClickListener {
    public static final int REQUEST_SCAN_VIN = 1001;
    public static final int REQUEST_SCAN_RFID = 1002;
    public static final int SEARCH_ACTIVITY_REQUEST_CAMERA = 1003;

    String TAG_MAKE = "TAG_MAKE";
    String TAG_MODEL_YEAR = "TAG_MODEL_YEAR";
    String TAG_COLOR = "TAG_COLOR";
    String TAG_PRICE = "TAG_PRICE";
    String TAG_VEHICLE_STATUS = "TAG_VEHICLE_STATUS";
    String TAG_VEHICLE_STAGE = "TAG_VEHICLE_STAGE";
    String TAG_SERVICE_STAGE = "TAG_SERVICE_STAGE";
    String TAG_PROBLEM = "TAG_PROBLEM";
    String TAG_FUEL_TYPE = "TAG_FUEL_TYPE";
    String TAG_GPS_INSTALLED = "TAG_GPS_INSTALLED";

    LinearLayout ll_basic, ll_carDetail, ll_statuses, ll_service, ll_auction;
    ImageView iv_basicRi, iv_carDetailRi, iv_statusesRi, iv_serviceRi, iv_auctionRi,
            iv_basicDo, iv_carDetailDo, iv_statusesDo, iv_serviceDo, iv_auctionDo;


    boolean isOneFieldSelected = false;

    EditText make, model, modelYear, miles, salesPrice, vin, rfid,
            vehicleStatus, vehicleStage, auctionName, fuelType, color, problem,
            edt_title, edt_location, edt_auctionDate, edt_car_ready, edt_noteDate,
            edt_rfid, edt_vacancy, edt_carAtAuction, edt_serviceStage;

    String minSalesPrice = "", maxSalesPrice = "";

    // new addedd field.....
    EditText modelNumber, maxHP, maxTorque, oilCapacity, driveType, company,
            lotCode, stockNumber, purchasedfrom, cylinders, edt_gas_tank,
            vehicleType, note, done_date, gps_installed, edt_mechanic, edt_donedate_lotcode;
    String TAG_DRIVE_TYPE = "TAG_DRIVE_TYPE";

    TextView button;


    boolean isFromAuctionDate = true;
    LinearLayout ll_allOption, ll_topBasic, ll_topCarDeatil, ll_topStatuses, ll_topService, ll_topAuction;
    int scanId;
    OnClickListener scanListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            scanId = v.getId();
            if (AppSingleTon.VERSION_OS.checkVersion()) {
                // Marshmallow+
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    scanTest(scanId);
                } else {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);

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
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, SEARCH_ACTIVITY_REQUEST_CAMERA);
                            }
                        });
                        builder.show();
                    }


                }

            } else {
                // Pre-Marshmallow
                scanTest(scanId);

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_search_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvScanRfid = (TextView) findViewById(R.id.tv_scan_rfid);
        TextView tvScanVin = (TextView) findViewById(R.id.tv_scan_vin);

        tvScanRfid.setOnClickListener(scanListener);
        tvScanVin.setOnClickListener(scanListener);
        ll_allOption = (LinearLayout) findViewById(R.id.ll_allOption);
        button = (TextView) findViewById(R.id.txt_more);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_allOption.getVisibility() == View.GONE) {
                    ll_allOption.setVisibility(View.VISIBLE);
                    button.setText("Less...");
                } else if (ll_allOption.getVisibility() == View.VISIBLE) {
                    ll_allOption.setVisibility(View.GONE);
                    button.setText("More...");
                }
            }
        });


        initView();
        initVehicleStatus();
        initVehicleStage();
        initServiceStage();
        initAuctionName();

        // new added fields
        initauctiondate();
        initcarReady();
        initcarAtAuction();

        initVin();
        initRFID();
        initMiles();
        initMake();
        initModel();
        initModelYear();
        initColor();
        initSalesPrice();
        initFuelType();
        initProblem();
        initTitle();
        initLocation();

        // new added field.....
        initModelNumber();
        initMaxHP();
        initMaxTorque();
        initOilCapacity();
        initDriveType();
        initCompany();
        initLotCode();
        initStockNumber();
        initPurchasedFrom();
        initCylinders();
        initGasTank();

        initVehicleType();
        initNote();
        initDoneDate();
        initDonedateLotcode();
        initGpsInstalled();

        initSearch();
        initmechanic();
        initNoteDate();

        initHasRfid();
        initVacancy();

    }

    public void initView() {
        ll_basic = (LinearLayout) findViewById(R.id.ll_basic);
        ll_carDetail = (LinearLayout) findViewById(R.id.ll_carDetails);
        ll_statuses = (LinearLayout) findViewById(R.id.ll_statuses);
        ll_service = (LinearLayout) findViewById(R.id.ll_service);
        ll_auction = (LinearLayout) findViewById(R.id.ll_auction);

        iv_basicRi = (ImageView) findViewById(R.id.iv_rightBasic);
        iv_basicRi.setOnClickListener(this);
        iv_carDetailRi = (ImageView) findViewById(R.id.iv_rightCarDetails);
        iv_carDetailRi.setOnClickListener(this);
        iv_statusesRi = (ImageView) findViewById(R.id.iv_rightStatuses);
        iv_statusesRi.setOnClickListener(this);
        iv_serviceRi = (ImageView) findViewById(R.id.iv_rightServices);
        iv_serviceRi.setOnClickListener(this);
        iv_auctionRi = (ImageView) findViewById(R.id.iv_rightAuction);
        iv_auctionRi.setOnClickListener(this);
        iv_basicDo = (ImageView) findViewById(R.id.iv_downBasic);
        iv_basicDo.setOnClickListener(this);
        iv_carDetailDo = (ImageView) findViewById(R.id.iv_downCarDetails);
        iv_carDetailDo.setOnClickListener(this);
        iv_statusesDo = (ImageView) findViewById(R.id.iv_downStatuses);
        iv_statusesDo.setOnClickListener(this);
        iv_serviceDo = (ImageView) findViewById(R.id.iv_downServices);
        iv_serviceDo.setOnClickListener(this);
        iv_auctionDo = (ImageView) findViewById(R.id.iv_downAuction);
        iv_auctionDo.setOnClickListener(this);

        ll_topBasic = (LinearLayout) findViewById(R.id.ll_topBasic);
        ll_topBasic.setOnClickListener(this);
        ll_topCarDeatil = (LinearLayout) findViewById(R.id.ll_topCarDetail);
        ll_topCarDeatil.setOnClickListener(this);
        ll_topService = (LinearLayout) findViewById(R.id.ll_topService);
        ll_topService.setOnClickListener(this);
        ll_topStatuses = (LinearLayout) findViewById(R.id.ll_topStatuses);
        ll_topStatuses.setOnClickListener(this);
        ll_topAuction = (LinearLayout) findViewById(R.id.ll_topAuction);
        ll_topAuction.setOnClickListener(this);
    }

    void initDonedateLotcode() {

        edt_donedate_lotcode = (EditText) findViewById(R.id.done_dateLotcode);
        final String title = "Done date Lotcode";
        final String TAG_HAS_RFID = "CHOOSE OPTION";
        final CharSequence[] doneDateLotcodeList = getResources().getStringArray(
                R.array.Lotcode);
        final ListDialogListener DonedateLotListener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_donedate_lotcode.setText(doneDateLotcodeList[position]);
                isOneFieldSelected = true;
                edt_donedate_lotcode.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };
        edt_donedate_lotcode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, doneDateLotcodeList, DonedateLotListener);
                dialog.show(getSupportFragmentManager(), TAG_HAS_RFID);
            }
        });

    }

    void initHasRfid() {
        edt_rfid = (EditText) findViewById(R.id.edt_hasRfid);
        final String title = "Car Has Rfid";
        final String TAG_HAS_RFID = "CHOOSE OPTION";
        final CharSequence[] rfIdOptionArray = getResources().getStringArray(
                R.array.car_ready);
        final ListDialogListener listener_has_rfid = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_rfid.setText(rfIdOptionArray[position]);
                isOneFieldSelected = true;
                edt_rfid.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };
        edt_rfid.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, rfIdOptionArray, listener_has_rfid);
                dialog.show(getSupportFragmentManager(), TAG_HAS_RFID);
            }
        });
    }

    void initVacancy() {
        edt_vacancy = (EditText) findViewById(R.id.edt_vacancySearch);
        final String title = "Vacancy";
        final String TAG_HAS_RFID = "CHOOSE OPTION";
        final CharSequence[] vacancyList = getResources().getStringArray(
                R.array.vacancylist);
        final ListDialogListener VacancyListener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_vacancy.setText(vacancyList[position]);
                isOneFieldSelected = true;
                edt_vacancy.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };
        edt_vacancy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, vacancyList, VacancyListener);
                dialog.show(getSupportFragmentManager(), TAG_HAS_RFID);
            }
        });
    }

    void initmechanic() {
        edt_mechanic = (EditText) findViewById(R.id.edt_MechanicSearch);
        edt_mechanic.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                isOneFieldSelected = true;
                edt_mechanic.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (edt_mechanic.length() == 0)
                    isOneFieldSelected = false;
            }
        });

    }

    void initcarReady() {
        edt_car_ready = (EditText) findViewById(R.id.edt_car_ready);
        final String title = "Car Ready For Auction?";
        final CharSequence[] statusList = getResources().getStringArray(
                R.array.car_ready);
        final ListDialogListener listener_car_ready = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_car_ready.setText(statusList[position]);
                isOneFieldSelected = true;
                edt_car_ready.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };
        edt_car_ready.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(title, statusList, listener_car_ready);
                dialog.show(getSupportFragmentManager(), "Choose Option");
            }
        });
    }

    void initcarAtAuction() {
        edt_carAtAuction = (EditText) findViewById(R.id.edt_car_at_auction);
        final String title = "Car At The Auction?";
        final CharSequence[] statusList = getResources().getStringArray(
                R.array.car_atauction);
        final ListDialogListener listener_car_ready = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_carAtAuction.setText(statusList[position]);
                isOneFieldSelected = true;
                edt_carAtAuction.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };
        edt_carAtAuction.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, statusList, listener_car_ready);
                dialog.show(getSupportFragmentManager(), "Choose Option");
            }
        });
    }

    void initauctiondate() {
        edt_auctionDate = (EditText) findViewById(R.id.edt_auctiondate);
        edt_auctionDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isFromAuctionDate = true;
                openDatePicker();
            }
        });
    }

    void initNoteDate() {
        edt_noteDate = (EditText) findViewById(R.id.note_date);
        edt_noteDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isFromAuctionDate = false;
                openDatePicker();
            }
        });
    }

    void DatePicker() {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog dpd = new DatePickerDialog(SearchActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        // MM/DD/YYYY
                        Log.e("donedatejhugk", monthOfYear + 1 + "/"
                                + dayOfMonth + "/" + year);

                        String donedate;
                        int month;
                        monthOfYear = monthOfYear + 1;

                        String date;
                        if (dayOfMonth < 10) {

//                            date = "0" + Integer.toString(dayOfMonth);
                            date = Integer.toString(dayOfMonth);

                        } else
                            date = Integer.toString(dayOfMonth);

                        if (monthOfYear < 10) {

//                            donedate = "0" + monthOfYear + "/" + date + "/" + year;
                            donedate = monthOfYear + "/" + date + "/" + year;

                        } else {
                            donedate = monthOfYear + "/" + dayOfMonth + "/" + year;

                        }
                        isOneFieldSelected = true;
                        done_date.setText(donedate);
                        done_date.setTextColor(Color.parseColor("#CC2900"));
                        Log.e("Date and Done date", " " + date + " " + donedate);
                        // editDoneDate(donedate);

                    }

                }, mYear, mMonth, mDay);

        dpd.show();
    }

    private void initGasTank() {
        edt_gas_tank = (EditText) findViewById(R.id.edt_gas_tank);
        // edt_location = (EditText) findViewById(R.carId.edt_location);
        // edt_location.setText("No");
        final String title = "Select GasTank";
        final CharSequence[] driveTypeList = getResources().getStringArray(
                R.array.GasTankList);

        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_gas_tank.setText(driveTypeList[position]);
                isOneFieldSelected = true;
                edt_gas_tank.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_gas_tank.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, driveTypeList, listener);
                dialog.show(getSupportFragmentManager(), "TAG_GAS_TANK");
            }
        });
    }

    void scanTest(int id) {
        switch (id) {
            case R.id.tv_scan_rfid:

                Intent scannerRfid = new Intent(SearchActivity.this,
                        AddCarScannerActivity.class);
                scannerRfid.putExtra("IS_VIN", false);
                startActivityForResult(scannerRfid, REQUEST_SCAN_RFID);
                break;
            case R.id.tv_scan_vin:

                Intent scannerVin = new Intent(SearchActivity.this,
                        AddCarScannerActivity.class);
                scannerVin.putExtra("IS_VIN", true);
                startActivityForResult(scannerVin, REQUEST_SCAN_VIN);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == SEARCH_ACTIVITY_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanTest(scanId);
            } else {
                Toast.makeText(getApplicationContext(), "Camera Permission was not granted", Toast.LENGTH_SHORT).show();
            }
        } else {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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
                /*
                 * if (scanCodeLength == 17) { vin.setText(scanCode);
				 * tvScanVin.setVisibility(View.GONE); } else
				 */
                if (scanCodeLength == 7) {
                    rfid.setText(scanCode);
                    isOneFieldSelected = true;
                    // tvScanRfid.setVisibility(View.GONE);
                }
                if (data.getBooleanExtra("FOUND", false)) {
                    CarInventory car = (CarInventory) data
                            .getSerializableExtra("each_car");
                    Intent intent = new Intent(SearchActivity.this,
                            CarDetailsActivity.class);
                    intent.putExtra("each_car", car);
                    startActivity(intent);
                }
                // }

                // AppSingleTon.METHOD_BOX.hidekeyBoard(this);
                // if (scanCode.length() == 17) {
                // pullDataoneInformations();
                // }
                //
                //
                // if (code.length() == 17) {
                // vin.setText(code);
                // pullDataoneInformations();
                // } else if (code.length() == 7) {
                // rfid.setText(code);
                // }
            }
        } else if (requestCode == REQUEST_SCAN_VIN) {
            // TODO set Scan VIN
            if (resultCode == Activity.RESULT_OK) {
                String scanCode = data.getStringExtra("code");
                int scanCodeLength = scanCode.length();
                if (scanCodeLength == 18
                        && (scanCode.startsWith("i") || scanCode
                        .startsWith("I"))) {
                    scanCode = scanCode.substring(1, scanCode.length());
                }

                Log.e("scanCode", scanCode);
                scanCodeLength = scanCode.length();

                if (scanCodeLength == 17) {
                    vin.setText(scanCode);

                    // tvScanVin.setVisibility(View.GONE);
                } /*
                 * else if (scanCodeLength == 7) { rfid.setText(scanCode);
				 * tvScanRfid.setVisibility(View.GONE); }
				 */
                if (data.getBooleanExtra("FOUND", false)) {

                    CarInventory car = (CarInventory) data
                            .getSerializableExtra("each_car");
                    Intent intent = new Intent(SearchActivity.this,
                            CarDetailsActivity.class);
                    intent.putExtra("each_car", car);
                    startActivity(intent);
                }

            }
        }
    }

    void initMake() {

        final String title = "Choose Make";
        final CharSequence[] makeList = getResources().getStringArray(
                R.array.Make);
        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                make.setText(makeList[position]);
                isOneFieldSelected = true;
                make.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };
        make = (EditText) findViewById(R.id.make);
        make.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, makeList, listener);
                dialog.show(getSupportFragmentManager(), TAG_MAKE);
            }
        });
    }

    void initModel() {
        model = (EditText) findViewById(R.id.model);
    }

    void initVehicleStatus() {

        final String title = "Choose Vehicle Status";
        final CharSequence[] statusList = getResources().getStringArray(
                R.array.VehicleStatus);
        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                vehicleStatus.setText(statusList[position]);
                isOneFieldSelected = true;
                vehicleStatus.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        vehicleStatus = (EditText) findViewById(R.id.vehicle_status);
        vehicleStatus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, statusList, listener);
                dialog.show(getSupportFragmentManager(), TAG_VEHICLE_STATUS);
            }
        });
    }

    void initVehicleStage() {

        final String title = "Choose Vehicle Stage";
        final CharSequence[] statusList = getResources().getStringArray(
                R.array.StageType);
        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                vehicleStage.setText(statusList[position]);
                isOneFieldSelected = true;
                vehicleStage.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        vehicleStage = (EditText) findViewById(R.id.vehicle_stage);
        vehicleStage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, statusList, listener);
                dialog.show(getSupportFragmentManager(), TAG_VEHICLE_STAGE);
            }
        });
    }

    void initServiceStage() {
        final String title = "Choose Service Stage";
        final CharSequence[] statusList = getResources().getStringArray(
                R.array.ServiceStage);
        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_serviceStage.setText(statusList[position]);
                isOneFieldSelected = true;
                edt_serviceStage.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_serviceStage = (EditText) findViewById(R.id.service_stage);
        edt_serviceStage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, statusList, listener);
                dialog.show(getSupportFragmentManager(), TAG_SERVICE_STAGE);
            }
        });
    }

    void initAuctionName() {

        final String title = "Auction Name";
        final CharSequence[] statusList = getResources().getStringArray(
                R.array.Auction_Name);
        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                auctionName.setText(statusList[position]);
                isOneFieldSelected = true;
                auctionName.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        auctionName = (EditText) findViewById(R.id.auctionName);
        auctionName.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, statusList, listener);
                dialog.show(getSupportFragmentManager(), TAG_VEHICLE_STAGE);
            }
        });
    }

    void initProblem() {

        final String title = "Choose Problem";
        final CharSequence[] statusList = getResources().getStringArray(
                R.array.Problem);
        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                problem.setText(statusList[position]);
                isOneFieldSelected = true;
                problem.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        problem = (EditText) findViewById(R.id.problem);
        problem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, statusList, listener);
                dialog.show(getSupportFragmentManager(), TAG_PROBLEM);
            }
        });
    }

    void initTitle() {

        final String title = "Choose Title";
        final CharSequence[] statusList = getResources().getStringArray(
                R.array.Title_Serch);
        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_title.setText(statusList[position]);
                isOneFieldSelected = true;
                edt_title.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_title = (EditText) findViewById(R.id.title);
        edt_title.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, statusList, listener);
                dialog.show(getSupportFragmentManager(), TAG_PROBLEM);
            }
        });
    }

    void initLocation() {

        edt_location = (EditText) findViewById(R.id.edt_location);
        // edt_location.setText("No");
        final String title = "Choose Location Title";
        final CharSequence[] driveTypeList = getResources().getStringArray(
                R.array.location_title);
        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {

                edt_location.setText(driveTypeList[position]);
                isOneFieldSelected = true;
                edt_location.setTextColor(Color.parseColor("#CC2900"));

            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_location.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, driveTypeList, listener);
                dialog.show(getSupportFragmentManager(), TAG_PROBLEM);
            }
        });
    }

    void initModelYear() {

        final String title = "Choose Model Year";
        final CharSequence[] yearList = getYearList();

        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                modelYear.setText(yearList[position]);
                isOneFieldSelected = true;
                modelYear.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        modelYear = (EditText) findViewById(R.id.model_year);
        modelYear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, yearList, listener);
                dialog.show(getSupportFragmentManager(), TAG_MODEL_YEAR);
            }
        });
    }

    CharSequence[] getYearList() {

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int sz = (currentYear - 1980) + 1;
        CharSequence[] yearList = new CharSequence[sz];

        for (int i = 0; i < sz; i++) {
            yearList[i] = String.valueOf(1980 + i) + "";
        }

        return yearList;
    }

    void initMiles() {
        miles = (EditText) findViewById(R.id.edt_Miles);
        miles.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                isOneFieldSelected = true;
                miles.setTextColor(Color.parseColor("#CC2900"));
                Log.e(" Before isOneFieldSelected", isOneFieldSelected + "");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (miles.length() == 0)
                    isOneFieldSelected = false;
                Log.e("After isOneFieldSelected", isOneFieldSelected + "");
            }
        });

    }

    void initSalesPrice() {

        final String title = "Choose Price(USD)";
        final CharSequence[] priceList = getResources().getStringArray(
                R.array.PriceList);
        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                String value = priceList[position].toString();
                String[] minMax = value.split("-");
                minSalesPrice = minMax[0];
                maxSalesPrice = minMax[1];
                salesPrice.setText(value);
                isOneFieldSelected = true;
                salesPrice.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        salesPrice = (EditText) findViewById(R.id.sales_price);
        salesPrice.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, priceList, listener);
                dialog.show(getSupportFragmentManager(), TAG_PRICE);
            }
        });
    }

    void initVin() {
        vin = (EditText) findViewById(R.id.vin);
        vin.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                isOneFieldSelected = true;
                vin.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                if (vin.length() == 0)
                    isOneFieldSelected = false;
            }
        });
    }

    void initRFID() {
        rfid = (EditText) findViewById(R.id.rfid);
        rfid.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                isOneFieldSelected = true;
                rfid.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (rfid.length() == 0)
                    isOneFieldSelected = false;
            }
        });

    }

    void initColor() {

        final String title = "Choose Color";
        final CharSequence[] colorNameList = getResources().getStringArray(
                R.array.ColorName);
        final CharSequence[] colorValueList = getResources().getStringArray(
                R.array.ColorValue);

        final ListDialogListener listener = new ListDialogListener() {
            @Override
            public void onItemClick(int position) {
                color.setText(colorNameList[position]);
                color.setTag(colorValueList[position]);
                isOneFieldSelected = true;
                color.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        color = (EditText) findViewById(R.id.color);
        color.setTag("");
        color.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                ColorChoiceDialogFragment dialog = new ColorChoiceDialogFragment(
                        listener);
                dialog.show(getSupportFragmentManager(), TAG_COLOR);
            }
        });

    }

    void initFuelType() {

        final String title = "Choose Fuel Type";
        final CharSequence[] statusList = getResources().getStringArray(
                R.array.FuelType);
        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                fuelType.setText(statusList[position]);
                isOneFieldSelected = true;
                fuelType.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        fuelType = (EditText) findViewById(R.id.fuel_type);
        fuelType.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, statusList, listener);
                dialog.show(getSupportFragmentManager(), TAG_FUEL_TYPE);
            }
        });
    }

    // .......new Added Field................//

    void initModelNumber() {
        modelNumber = (EditText) findViewById(R.id.modelNumber);

        modelNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                isOneFieldSelected = true;
                modelNumber.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (modelNumber.length() == 0)
                    isOneFieldSelected = false;
            }
        });

    }

    void initMaxHP() {
        maxHP = (EditText) findViewById(R.id.maxHP);
        maxHP.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                isOneFieldSelected = true;
                maxHP.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (maxHP.length() == 0)
                    isOneFieldSelected = false;
            }
        });
    }

    void initMaxTorque() {
        maxTorque = (EditText) findViewById(R.id.maxTorque);
        maxTorque.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                isOneFieldSelected = true;
                maxTorque.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (maxTorque.length() == 0)
                    isOneFieldSelected = false;
            }
        });
    }

    void initOilCapacity() {
        oilCapacity = (EditText) findViewById(R.id.oilCapacity);
        oilCapacity.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                isOneFieldSelected = true;
                oilCapacity.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (oilCapacity.length() == 0)
                    isOneFieldSelected = false;
            }
        });
    }

    void initDriveType() {
        driveType = (EditText) findViewById(R.id.driveType);

        final String title = "Choose Drive Type";
        final CharSequence[] driveTypeList = getResources().getStringArray(
                R.array.DriveType);
        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                driveType.setText(driveTypeList[position]);
                isOneFieldSelected = true;
                driveType.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        driveType.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, driveTypeList, listener);
                dialog.show(getSupportFragmentManager(), TAG_DRIVE_TYPE);
            }
        });
    }

    void initDoneDate() {

        done_date = (EditText) findViewById(R.id.done_date);

        done_date.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePicker();

            }

        });

    }

    void initGpsInstalled() {

        gps_installed = (EditText) findViewById(R.id.gps_installed);

        final String title = "Gps Installed?";
        final CharSequence[] driveTypeList = getResources().getStringArray(
                R.array.Gps);

        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                gps_installed.setText(driveTypeList[position]);
                isOneFieldSelected = true;
                gps_installed.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        gps_installed.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, driveTypeList, listener);
                dialog.show(getSupportFragmentManager(), TAG_GPS_INSTALLED);
            }
        });

    }

    void openDatePicker() {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog dpd = new DatePickerDialog(SearchActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        // MM/DD/YYYY
                        Log.e("donedatejhugk", monthOfYear + 1 + "/"
                                + dayOfMonth + "/" + year);

                        String donedate;
                        monthOfYear = monthOfYear + 1;

                        String date;
                        if (dayOfMonth < 10) {

                            date = "0" + Integer.toString(dayOfMonth);

                        } else
                            date = Integer.toString(dayOfMonth);

                        Log.e("Day of mont hnote or auction....", " " + date);

                        if (monthOfYear < 10) {
                            donedate = year + "-" + "0" + monthOfYear + "-"
                                    + date;
                        } else {
                            donedate = year + "-" + monthOfYear + "-"
                                    + date;
                        }

                        Log.e("Date for note or auction....", " " + donedate);

                        if (isFromAuctionDate) {
                            edt_auctionDate.setText(donedate);
                            edt_auctionDate.setTextColor(Color.parseColor("#CC2900"));
                        } else {
                            edt_noteDate.setText(donedate);
                            edt_noteDate.setTextColor(Color.parseColor("#CC2900"));
                        }
                        isOneFieldSelected = true;
                        // editDoneDate(donedate);
                    }

                }, mYear, mMonth, mDay);

        dpd.show();
    }

    void initCompany() {
        company = (EditText) findViewById(R.id.company);
        company.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                isOneFieldSelected = true;
                company.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (company.length() == 0)
                    isOneFieldSelected = false;
            }
        });
    }

    void initLotCode() {
        // lotCode = (EditText) findViewById(R.carId.lotCode);
        final String title = "Choose Lot Code";
        final CharSequence[] lotList = getResources().getStringArray(
                R.array.Lotcode);
        final CharSequence[] colorValueList = getResources().getStringArray(
                R.array.ColorValue);

        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                lotCode.setText(lotList[position]);
                lotCode.setTag(colorValueList);
                isOneFieldSelected = true;
                lotCode.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        lotCode = (EditText) findViewById(R.id.lotCode);
        color.setTag("");

        // lotCode.setText("Unknown");
        lotCode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                /*
                 * SingleChoiceTextDialogFragment dialog = new
				 * SingleChoiceTextDialogFragment( title,lotList,listener);
				 * dialog.show(getSupportFragmentManager(),TAG_VEHICLE_STATUS);
				 */
                /*
                 * ColorChoiceDialogFragment dialog1 = new
				 * ColorChoiceDialogFragment( listener);
				 * dialog1.show(getSupportFragmentManager(), TAG_COLOR);
				 */
                LotcodeChoiceDialogFragment dialog1 = new LotcodeChoiceDialogFragment(
                        listener);
                dialog1.show(getSupportFragmentManager(), TAG_COLOR);

            }
        });

    }

    void initStockNumber() {
        stockNumber = (EditText) findViewById(R.id.stockNumber);
        stockNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                isOneFieldSelected = true;
                stockNumber.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (stockNumber.length() == 0)
                    isOneFieldSelected = false;
            }
        });
    }

    void initPurchasedFrom() {
        purchasedfrom = (EditText) findViewById(R.id.purchasedFrom);
        // purchasedfrom.setOnEditorActionListener(doneListener);
        purchasedfrom.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                isOneFieldSelected = true;
                purchasedfrom.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (purchasedfrom.length() == 0)
                    isOneFieldSelected = false;
            }
        });
    }

    void initCylinders() {
        cylinders = (EditText) findViewById(R.id.edcylinder);
        cylinders.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                isOneFieldSelected = true;
                cylinders.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (cylinders.length() == 0)
                    isOneFieldSelected = false;
            }
        });
    }

    void initVehicleType() {
        vehicleType = (EditText) findViewById(R.id.vehicleType);
        vehicleType.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                isOneFieldSelected = true;
                vehicleType.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (vehicleType.length() == 0)
                    isOneFieldSelected = false;
            }
        });
    }

    void initNote() {
        note = (EditText) findViewById(R.id.note);

        note.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                isOneFieldSelected = true;
                note.setTextColor(Color.parseColor("#CC2900"));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (note.length() == 0)
                    isOneFieldSelected = false;
            }
        });
    }

    boolean validateValues() {
        isOneFieldSelected = false;
        int validate = 0;

        if ((vin.getText().toString() != null && vin.getText().toString().length() > 0))
            validate = 1;
        if ((make.getText().toString() != null && make.getText().toString().length() > 0))
            validate = 1;
        if ((rfid.getText().toString() != null && rfid.getText().toString().length() > 0))
            validate = 1;
        if ((model.getText().toString() != null && model.getText().toString().length() > 0))
            validate = 1;
        if ((modelYear.getText().toString() != null && modelYear.getText().toString().length() > 0))
            validate = 1;
        if ((miles.getText().toString() != null && miles.getText().toString().length() > 0))
            validate = 1;
        if ((color.getTag().toString() != null && color.getText().toString().length() > 0))
            validate = 1;
        if ((minSalesPrice.trim() != null && minSalesPrice.length() > 0))
            validate = 1;
        if ((maxSalesPrice.trim() != null && maxSalesPrice.length() > 0))
            validate = 1;
        if ((fuelType.getText().toString() != null && fuelType.getText().toString().length() > 0))
            validate = 1;
        if ((vehicleStatus.getText().toString() != null && vehicleStatus.getText().toString().length() > 0))
            validate = 1;
        if ((vehicleStage.getText().toString() != null && vehicleStage.getText().toString().length() > 0))
            validate = 1;
        if ((edt_serviceStage.getText().toString() != null && edt_serviceStage.getText().toString().length() > 0))
            validate = 1;
        if ((problem.getText().toString() != null && problem.getText().toString().length() > 0))
            validate = 1;
        if ((edt_auctionDate.getText().toString() != null && edt_auctionDate.getText().toString().length() > 0))
            validate = 1;
        if ((edt_car_ready.getText().toString() != null && edt_car_ready.getText().toString().length() > 0))
            validate = 1;
        if ((edt_carAtAuction.getText().toString() != null && edt_carAtAuction.getText().toString().length() > 0))
            validate = 1;
        if ((edt_title.getText().toString() != null && edt_title.getText().toString().length() > 0))
            validate = 1;
        if ((edt_location.getText().toString() != null && edt_location.getText().toString().length() > 0))
            validate = 1;
        if ((modelNumber.getText().toString() != null && modelNumber.getText().toString().length() > 0))
            validate = 1;
        if ((maxHP.getText().toString() != null && maxHP.getText().toString().length() > 0))
            validate = 1;
        if ((maxTorque.getText().toString() != null && maxTorque.getText().toString().length() > 0))
            validate = 1;
        if ((oilCapacity.getText().toString() != null && oilCapacity.getText().toString().length() > 0))
            validate = 1;
        if ((driveType.getText().toString() != null && driveType.getText().toString().length() > 0))
            validate = 1;
        if ((company.getText().toString() != null && company.getText().toString().length() > 0))
            validate = 1;
        if ((lotCode.getText().toString() != null && lotCode.getText().toString().length() > 0))
            validate = 1;
        if ((stockNumber.getText().toString() != null && stockNumber.getText().toString().length() > 0))
            validate = 1;
        if ((purchasedfrom.getText().toString() != null && purchasedfrom.getText().toString().length() > 0))
            validate = 1;
        if ((cylinders.getText().toString() != null && cylinders.getText().toString().length() > 0))
            validate = 1;
        if ((edt_gas_tank.getText().toString() != null && edt_gas_tank.getText().toString().length() > 0))
            validate = 1;
        if ((auctionName.getText().toString() != null && auctionName.getText().toString().length() > 0))
            validate = 1;
        if ((vehicleType.getText().toString() != null && vehicleType.getText().toString().length() > 0))
            validate = 1;
        if ((note.getText().toString() != null && note.getText().toString().length() > 0))
            validate = 1;
        if ((done_date.getText().toString() != null && done_date.getText().toString().length() > 0))
            validate = 1;
        if ((gps_installed.getText().toString() != null && gps_installed.getText().toString().length() > 0))
            validate = 1;
        if ((edt_mechanic.getText().toString() != null && edt_mechanic.getText().toString().length() > 0))
            validate = 1;
        if ((edt_noteDate.getText().toString() != null && edt_noteDate.getText().toString().length() > 0))
            validate = 1;
        if ((edt_rfid.getText().toString() != null && edt_rfid.getText().toString().length() > 0))
            validate = 1;
        if ((edt_donedate_lotcode.getText().toString() != null && edt_donedate_lotcode.getText().toString().length() > 0))
            validate = 1;
        if ((edt_vacancy.getText().toString() != null && edt_vacancy.getText().toString().length() > 0))
            validate = 1;

        if (validate == 0)
            isOneFieldSelected = false;
        else
            isOneFieldSelected = true;

        return isOneFieldSelected;
    }

    // ...........new addded field over..........//

    public void initSearch() {
        LinearLayout search = (LinearLayout) findViewById(R.id.search);

        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                CarInventory search_query = new CarInventory();
                isOneFieldSelected = validateValues();
                Log.e("VALIDATE VALUES", " " + isOneFieldSelected);
                if (isOneFieldSelected) {
                    if (Common.isNetworkConnected(getApplicationContext())) {
                        search_query.Vin = vin.getText().toString();
                        search_query.Rfid = rfid.getText().toString();
                        search_query.Make = make.getText().toString();
                        search_query.Model = model.getText().toString();
                        search_query.ModelYear = modelYear.getText().toString();
                        search_query.Miles = miles.getText().toString();
                        search_query.Color = color.getTag().toString();
                        search_query.MinSalesPrice = minSalesPrice.trim();
                        search_query.MaxSalesPrice = maxSalesPrice.trim();
                        search_query.FuelType = fuelType.getText().toString();
                        search_query.VehicleStatus = vehicleStatus.getText()
                                .toString();
                        search_query.Stage = vehicleStage.getText().toString();
                        search_query.ServiceStage = edt_serviceStage.getText().toString();
                        search_query.Problem = problem.getText().toString();

                        search_query.auctiondate = edt_auctionDate.getText()
                                .toString();
                        search_query.carready = edt_car_ready.getText()
                                .toString();
                        search_query.caratauction = edt_carAtAuction.getText()
                                .toString();

                        if (edt_title.getText().toString().equals("All")) {
                            search_query.Title = "";
                        } else {
                            search_query.Title = edt_title.getText().toString();
                        }
                        search_query.has_location = edt_location.getText()
                                .toString();

                        // new added field....................

                        search_query.ModelNumber = modelNumber.getText()
                                .toString();
                        search_query.MaxHP = maxHP.getText().toString();
                        search_query.MaxTorque = maxTorque.getText().toString();
                        search_query.OilCapacity = oilCapacity.getText()
                                .toString();
                        search_query.DriveType = driveType.getText().toString();
                        search_query.Company = company.getText().toString();
                        search_query.LotCode = lotCode.getText().toString();
                        search_query.StockNumber = stockNumber.getText()
                                .toString();
                        search_query.PurchasedFrom = purchasedfrom.getText()
                                .toString();

                        search_query.Cylinders = cylinders.getText().toString();
                        search_query.Gastank = edt_gas_tank.getText()
                                .toString();
                        search_query.auctionname = auctionName.getText()
                                .toString();

                        search_query.VehicleType = vehicleType.getText()
                                .toString();
                        search_query.Note = note.getText().toString();
                        search_query.DoneDate = done_date.getText().toString();
                        search_query.Gps_Installed = gps_installed.getText()
                                .toString();
                        search_query.mechanic = edt_mechanic.getText()
                                .toString();
                        search_query.NoteDate = edt_noteDate.getText().toString();

                        search_query.HasRfid = edt_rfid.getText().toString();
                        search_query.vacancy = edt_vacancy.getText().toString();
                        search_query.DonedateLot = edt_donedate_lotcode.getText().toString();

                        Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                        intent.putExtra("search_query", search_query);

                        Log.e("Done Date in search", " " + done_date.getText().toString());
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

    void clear() {

        isOneFieldSelected = false;
        auctionName.setText("");
        vin.setText("");
        rfid.setText("");
        make.setText("");
        model.setText("");
        modelYear.setText("");
        miles.setText("");
        salesPrice.setText("");
        minSalesPrice = "";
        maxSalesPrice = "";
        color.setTag("");
        color.setText("");
        fuelType.setText("");
        vehicleStatus.setText("");
        vehicleStage.setText("");
        edt_serviceStage.setText("");
        problem.setText("");
        edt_title.setText("");
        edt_location.setText("");

        // new added field........
        modelNumber.setText("");
        maxHP.setText("");
        maxTorque.setText("");
        oilCapacity.setText("");
        driveType.setText("");
        company.setText("");
        lotCode.setText("");
        stockNumber.setText("");
        purchasedfrom.setText("");

        vehicleType.setText("");
        note.setText("");
        edt_noteDate.setText("");
        gps_installed.setText("");
        done_date.setText("");

        cylinders.setText("");
        edt_gas_tank.setText("");

        edt_auctionDate.setText("");
        edt_car_ready.setText("");
        edt_carAtAuction.setText("");
        edt_rfid.setText("");
        edt_vacancy.setText("");
        edt_mechanic.setText("");
        edt_donedate_lotcode.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (carId == R.carId.clear) {
//            clear();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_rightBasic:
                iv_basicRi.setVisibility(View.GONE);
                iv_basicDo.setVisibility(View.VISIBLE);
                ll_basic.setVisibility(View.VISIBLE);
                break;

            case R.id.iv_rightAuction:
                iv_auctionRi.setVisibility(View.GONE);
                iv_auctionDo.setVisibility(View.VISIBLE);
                ll_auction.setVisibility(View.VISIBLE);
                break;

            case R.id.iv_rightCarDetails:
                iv_carDetailRi.setVisibility(View.GONE);
                iv_carDetailDo.setVisibility(View.VISIBLE);
                ll_carDetail.setVisibility(View.VISIBLE);
                break;

            case R.id.iv_rightStatuses:
                iv_statusesRi.setVisibility(View.GONE);
                iv_statusesDo.setVisibility(View.VISIBLE);
                ll_statuses.setVisibility(View.VISIBLE);
                break;

            case R.id.iv_rightServices:
                iv_serviceRi.setVisibility(View.GONE);
                iv_serviceDo.setVisibility(View.VISIBLE);
                ll_service.setVisibility(View.VISIBLE);
                break;

            case R.id.iv_downBasic:
                iv_basicRi.setVisibility(View.VISIBLE);
                iv_basicDo.setVisibility(View.GONE);
                ll_basic.setVisibility(View.GONE);
                break;

            case R.id.iv_downAuction:
                iv_auctionRi.setVisibility(View.VISIBLE);
                iv_auctionDo.setVisibility(View.GONE);
                ll_auction.setVisibility(View.GONE);
                break;

            case R.id.iv_downCarDetails:
                iv_carDetailRi.setVisibility(View.VISIBLE);
                iv_carDetailDo.setVisibility(View.GONE);
                ll_carDetail.setVisibility(View.GONE);
                break;

            case R.id.iv_downStatuses:
                iv_statusesRi.setVisibility(View.VISIBLE);
                iv_statusesDo.setVisibility(View.GONE);
                ll_statuses.setVisibility(View.GONE);
                break;

            case R.id.iv_downServices:
                iv_serviceRi.setVisibility(View.VISIBLE);
                iv_serviceDo.setVisibility(View.GONE);
                ll_service.setVisibility(View.GONE);
                break;

            case R.id.ll_topBasic:
                if (ll_basic.getVisibility() == View.VISIBLE) {
                    iv_basicRi.setVisibility(View.VISIBLE);
                    iv_basicDo.setVisibility(View.GONE);
                    ll_basic.setVisibility(View.GONE);
                } else {
                    iv_basicRi.setVisibility(View.GONE);
                    iv_basicDo.setVisibility(View.VISIBLE);
                    ll_basic.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.ll_topCarDetail:
                if (ll_carDetail.getVisibility() == View.VISIBLE) {
                    iv_carDetailRi.setVisibility(View.VISIBLE);
                    iv_carDetailDo.setVisibility(View.GONE);
                    ll_carDetail.setVisibility(View.GONE);
                } else {
                    iv_carDetailRi.setVisibility(View.GONE);
                    iv_carDetailDo.setVisibility(View.VISIBLE);
                    ll_carDetail.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.ll_topService:
                if (ll_service.getVisibility() == View.VISIBLE) {
                    iv_serviceRi.setVisibility(View.VISIBLE);
                    iv_serviceDo.setVisibility(View.GONE);
                    ll_service.setVisibility(View.GONE);
                } else {
                    iv_serviceRi.setVisibility(View.GONE);
                    iv_serviceDo.setVisibility(View.VISIBLE);
                    ll_service.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.ll_topStatuses:
                if (ll_statuses.getVisibility() == View.VISIBLE) {
                    iv_statusesRi.setVisibility(View.VISIBLE);
                    iv_statusesDo.setVisibility(View.GONE);
                    ll_statuses.setVisibility(View.GONE);
                } else {
                    iv_statusesRi.setVisibility(View.GONE);
                    iv_statusesDo.setVisibility(View.VISIBLE);
                    ll_statuses.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.ll_topAuction:
                if (ll_auction.getVisibility() == View.VISIBLE) {
                    iv_auctionRi.setVisibility(View.VISIBLE);
                    iv_auctionDo.setVisibility(View.GONE);
                    ll_auction.setVisibility(View.GONE);
                } else {
                    iv_auctionRi.setVisibility(View.GONE);
                    iv_auctionDo.setVisibility(View.VISIBLE);
                    ll_auction.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}

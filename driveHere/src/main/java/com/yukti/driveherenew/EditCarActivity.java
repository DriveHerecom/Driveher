//package com.yukti.driveherenew;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//
//import org.apache.http.Header;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.graphics.drawable.BitmapDrawable;
//import android.location.Location;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.provider.Settings;
//import android.support.annotation.NonNull;
//import android.support.v4.app.DialogFragment;
//import android.support.v7.widget.Toolbar;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.inputmethod.EditorInfo;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.TextView.OnEditorActionListener;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.NetworkResponse;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.VolleyLog;
//import com.loopj.android.http.JsonHttpResponseHandler;
//import com.loopj.android.http.RequestParams;
//import com.qualcomm.snapdragon.sdk.face.FacialProcessing;
//import com.qualcomm.snapdragon.sdk.face.FacialProcessing.FEATURE_LIST;
//import com.yukti.driveherenew.MessageDialogFragment.MessageDialogListener;
//import com.yukti.driveherenew.PassConfirmDialougueFrag.PassDialogListener;
//import com.yukti.driveherenew.SingleChoiceTextDialogFragment.ListDialogListener;
//import com.yukti.driveherenew.search.CarInventory;
//import com.yukti.driveherenew.search.Gps;
//import com.yukti.driveherenew.search.Photo;
//import com.yukti.jsonparser.AddCarResponse;
//import com.yukti.jsonparser.UpdateResponse;
//import com.yukti.utils.AppSingleTon;
//import com.yukti.utils.CamOperation;
//import com.yukti.utils.CamOperation.CameraResponse;
//import com.yukti.utils.Common;
//import com.yukti.utils.Constants;
//import com.yukti.utils.ParamsKey;
//import com.yukti.utils.RestClient;
//import com.yukti.utils.SDCardManager;
//
//import ch.boye.httpclientandroidlib.HttpEntity;
//import ch.boye.httpclientandroidlib.entity.mime.MultipartEntityBuilder;
//import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;
//
//public class EditCarActivity extends BaseActivity implements
//        MessageDialogListener, OnClickListener {
//
//    public static final int REQUEST_SCAN_VIN = 1001;
//    public static final int REQUEST_SCAN_RFID = 1002;
//    public static final int LIVE_RECG_REQ = 1018;
//    public static final int REQ_UPDATE_FACE = 1078;
//
//    public static final int EDIT_CAR_ACTIVITY_REQUEST_CAMERA_SCANNING = 114;
//    public static final int EDIT_CAR_ACTIVITY_REQUEST_CAMERA_FACE_UPDATE = 115;
//    public static final int EDIT_CAR_ACTIVITY_REQUEST_CAMERA = 116;
//    public static final int EDIT_CAR_ACTIVITY_REQUEST_CAMERA_GPS = 117;
//    public static final int EDIT_CAR_ACTIVITY_REQUEST_CAMERA_INSURANCE = 118;
//
//    private int year;
//    private int month;
//    private int day;
//
//    static final int DATE_PICKER_ID = 1111;
//
//    //    Toolbar toolbar;
////    LinearLayout camera, update, show_photo;
//    LinearLayout show_photo;
//    EditText make, model, note, modelNumber, modelYear, vin, rfid, color,
//            stage, serviceStage, stockNumber, lotCode, salesPrice, statusOfVehicle,
//            purchasedfrom, cylinders, edt_gas_tank, company, miles,
//            oilCapacity, driveType, maxHP, maxTorque, fuelType, vehicleType,
//            edt_problem, edt_title, edt_location, edt_gps_installed, mechanic, edt_vacanyUpadate;
//
//    static EditText ed_registrationdate;
//    static EditText ed_inspectiondate;
//    static EditText ed_insuranceDate;
//
//    public static String deleteImage;
//
//    TextView cameraTxt, tvScanVin, tvScanRfid;
//    CamOperation camOperation = null;
//    CameraResponse cameraResponse = null;
//    File currentImageFile = null;
//    SDCardManager sdcardManager;
//    public static ArrayList<File> photoList;
//    public static ArrayList<Photo> photoListWeb;
//    String scanCode;
//    OnEditorActionListener doneListener;
//    int REQUEST_SCAN_CODE = 25743;
//    int scanCodeLength = 0;
//    String TAG_PUSH_RESULT = "TAG_PUSH_RESULT";
//    String dataoneInformation = "";
//    String TAG_DRIVE_TYPE = "TAG_DRIVE_TYPE";
//    String TAG_MAKE = "TAG_MAKE";
//    String TAG_MODEL_YEAR = "TAG_MODEL_YEAR";
//    String TAG_COLOR = "TAG_COLOR";
//    String TAG_VEHICLE_STATUS = "TAG_VEHICLE_STATUS";
//    String TAG_FUEL_TYPE = "TAG_FUEL_TYPE";
//    String TAG_STAGE_TYPE = "TAG_STAGE_TYPE";
//    String TAG_SERVICE_STAGE_TYPE = "TAG_SERVICE_STAGE_TYPE";
//    String TAG_PROBLEM = "TAG_PROBLEM";
//    String TAG_TITLE = "TAG_TITLE";
//    String TAG_GPS_INSTALLED = "TAG_GPS_INSTALLED";
//    String TAG_CONFIRM_PASS = "TAG_CONFIRM_PASS";
//    CarInventory car;
//    int itemPosition;
//    static int click;
//    boolean isUpdate = false;
//    LinearLayout ll_progress;
//    EditText ed_gps;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_car_edit);
//        initToolbar();
//        Bundle bundle = getIntent().getExtras();
//        ll_progress = (LinearLayout) findViewById(R.carId.ll_progressEditCar);
//        if (bundle != null) {
//            car = (CarInventory) getIntent().getExtras().getSerializable(
//                    "each_car");
//            itemPosition = getIntent().getIntExtra("itemPosition", 0);
//        }
//
//        if (car != null && car.photos != null) {
//            photoListWeb = car.photos;
//        }
//
//        tvScanRfid = (TextView) findViewById(R.carId.tv_scan_rfid);
//        tvScanVin = (TextView) findViewById(R.carId.tv_scan_vin);
//        tvScanRfid.setOnClickListener(scanListener);
//        tvScanVin.setOnClickListener(scanListener);
//        initVin();
//        initRFID();
//        initMiles();
//        initCompany();
//        initdate();
//        initMake();
//        initModel();
//        initModelNumber();
//        initModelYear();
//        initColor();
//        initNote();
//        initLotCode();
//        initPrice();
//        initStockNumber();
//        initStatusOfVehicle();
//        initPurchasedFrom();
//        initOilCapacity();
//        initFuelType();
//        initDriveType();
//        initVehicleType();
//        initMaxHP();
//        initMaxTorque();
//        initCylinders();
//        initGasTank();
//
//        initStage();
//        initServiceStage();
//        initMechanic();
//        initProblem();
//        initTitle();
//        initLocation();
//        initGPSInstalled();
//        initCompanyInsurance();
//
//        initGps();
//
//        initCamera();
//        initShowPhoto();
//
//        initUpdate();
//        initclicklistener();
//        initDoneListener();
//
//        initVacancy();
//        AppSingleTon.METHOD_BOX.hidekeyBoard(this);
//
//    }
//
//    void initVacancy() {
//        edt_vacanyUpadate = (EditText) findViewById(R.carId.edt_vacancyUpadate);
//        edt_vacanyUpadate.setText(car.vacancy);
//        final String title = "Choose Vacancy";
//        final CharSequence[] VacacnyArray = getResources().getStringArray(
//                R.array.vacancylist);
//        // final CharSequence[] colorValueList = getResources().getStringArray(
//        // R.array.ColorValue);
//
//        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener() {
//
//            @Override
//            public void onItemClick(int position) {
//                edt_vacanyUpadate.setText(VacacnyArray[position]);
//            }
//
//            @Override
//            public void onDialogNegativeClick(
//                    android.support.v4.app.DialogFragment dialog) {
//            }
//        };
//        edt_vacanyUpadate.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                String TAG_VACANCY = "VACANCY";
//                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
//                        title, VacacnyArray, listener);
//                dialog.show(getSupportFragmentManager(), TAG_VACANCY);
//            }
//        });
//
//    }
//
//    void initGasTank() {
//        edt_gas_tank = (EditText) findViewById(R.carId.edt_gas_tank);
//        // edt_gas_tank = (EditText) findViewById(R.carId.edt_gas_tank);
//        edt_gas_tank.setText(car.Gastank);
//        final String title = "Select GasTank";
//        final CharSequence[] driveTypeList = getResources().getStringArray(
//                R.array.GasTankList);
//
//        final ListDialogListener listener = new ListDialogListener() {
//
//            @Override
//            public void onItemClick(int position) {
//                edt_gas_tank.setText(driveTypeList[position]);
//            }
//
//            @Override
//            public void onDialogNegativeClick(DialogFragment dialog) {
//
//            }
//        };
//
//        edt_gas_tank.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
//                        title, driveTypeList, listener);
//                dialog.show(getSupportFragmentManager(), "TAG_GAS_TANK");
//            }
//        });
//    }
//
//    int scanId;
//
//    void scanTest(int carId) {
//        switch (carId) {
//            case R.carId.tv_scan_rfid:
//
//                Intent scannerRfid = new Intent(EditCarActivity.this,
//                        AddCarScannerActivity.class);
//                scannerRfid.putExtra("IS_VIN", false);
//                startActivityForResult(scannerRfid, REQUEST_SCAN_RFID);
//                break;
//            case R.carId.tv_scan_vin:
//                Intent scannerVin = new Intent(EditCarActivity.this,
//                        AddCarScannerActivity.class);
//                scannerVin.putExtra("IS_VIN", true);
//                startActivityForResult(scannerVin, REQUEST_SCAN_VIN);
//                break;
//            default:
//                break;
//        }
//    }
//
//    OnClickListener scanListener = new OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//
//            scanId = v.getId();
//            if (AppSingleTon.VERSION_OS.checkVersion()) {
//                // Marshmallow+
//                if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                    scanTest(scanId);
//
//                } else {
//                    if (!shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(EditCarActivity.this);
//
//                        builder.setTitle("");
//                        builder.setMessage("Camera Permission Needed For Scanning,Allow It?");
//                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                final Intent i = new Intent();
//                                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                i.addCategory(Intent.CATEGORY_DEFAULT);
//                                i.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
//                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                                startActivity(i);
//                            }
//                        });
//                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, EDIT_CAR_ACTIVITY_REQUEST_CAMERA_SCANNING);
//                            }
//                        });
//                        builder.show();
//                    }
//
//
//                }
//
//            } else {
//                // Pre-Marshmallow
//                scanTest(scanId);
//
//            }
//        }
//    };
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == EDIT_CAR_ACTIVITY_REQUEST_CAMERA_SCANNING) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                scanTest(scanId);
//            } else {
//                Toast.makeText(getApplicationContext(), "Camera Permission was not granted", Toast.LENGTH_SHORT).show();
//            }
//        } else if (requestCode == EDIT_CAR_ACTIVITY_REQUEST_CAMERA_FACE_UPDATE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                updateFace();
//            } else {
//                Toast.makeText(getApplicationContext(), "Both Camera & Storage Permissions Are Needed", Toast.LENGTH_SHORT).show();
//            }
//
//        } else if (requestCode == EDIT_CAR_ACTIVITY_REQUEST_CAMERA) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                startCameraIntent(false);
//            } else {
//                Toast.makeText(getApplicationContext(), "Both Camera & Storage Permissions Are Needed", Toast.LENGTH_SHORT).show();
//            }
//
//        } else if (requestCode == EDIT_CAR_ACTIVITY_REQUEST_CAMERA_GPS) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Intent scanintent = new Intent(EditCarActivity.this,
//                        BarCodeScannerActivity.class);
//                startActivityForResult(scanintent, 101);
//            } else {
//                Toast.makeText(getApplicationContext(), "Camera Permission was not granted", Toast.LENGTH_SHORT).show();
//            }
//        } else if (requestCode == EDIT_CAR_ACTIVITY_REQUEST_CAMERA_INSURANCE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                startCameraIntent(true);
//            } else {
//                Toast.makeText(getApplicationContext(), "Both Camera & Storage Permissions Are Needed", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }
//
//    // ed_insuranceDate
//    // ed_inspectiondate
//    // ed_registrationdate
//    void initdate() {
//
//        ed_insuranceDate = (EditText) findViewById(R.carId.ed_date_insurance);
//        ed_inspectiondate = (EditText) findViewById(R.carId.ed_date_inspection);
//        ed_registrationdate = (EditText) findViewById(R.carId.ed_date_Registration);
//
//        ed_insuranceDate.setText(car.insurancedate);
//        ed_inspectiondate.setText(car.inspectiondate);
//        ed_registrationdate.setText(car.registrationdate);
//
//        // Log.e("cardate",""+car.inspectiondate);
//        // Log.e("cardateregidster",""+car.registrationdate);
//        // Log.e("cardateinsurance",""+car.insurancedate);
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        cameraTxt.setText("ADD PHOTO" + "(" + photoList.size() + ")");
//        showPhoto.setText("SHOW PHOTO" + "(" + (photoList.size() + photoListWeb.size()) + ")");
//    }
//
//    void initToolbar() {
//        Toolbar toolbar = (Toolbar) findViewById(R.carId.activity_car_edit_app_bar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//    }
//
//    void initStage() {
//
//        stage = (EditText) findViewById(R.carId.stage);
//        stage.setText(car.Stage);
//
//        final String title = "Choose Stage";
//        final CharSequence[] driveTypeList = getResources().getStringArray(
//                R.array.StageType);
//
//        final ListDialogListener listener = new ListDialogListener() {
//
//            @Override
//            public void onItemClick(int position) {
//                stage.setText(driveTypeList[position]);
//            }
//
//            @Override
//            public void onDialogNegativeClick(DialogFragment dialog) {
//
//            }
//        };
//
//        stage.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
//                        title, driveTypeList, listener);
//                dialog.show(getSupportFragmentManager(), TAG_STAGE_TYPE);
//            }
//        });
//    }
//
//    void initServiceStage() {
//
//        serviceStage = (EditText) findViewById(R.carId.servicestage);
//        serviceStage.setText(car.ServiceStage);
//        Log.e("init ServiceStage", "" + car.ServiceStage);
//
//        final String title = "Choose Service Stage";
//        final CharSequence[] driveTypeList = getResources().getStringArray(
//                R.array.ServiceStage);
//
//        final ListDialogListener listener = new ListDialogListener() {
//
//            @Override
//            public void onItemClick(int position) {
//                serviceStage.setText(driveTypeList[position]);
//            }
//
//            @Override
//            public void onDialogNegativeClick(DialogFragment dialog) {
//
//            }
//        };
//
//        serviceStage.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
//                        title, driveTypeList, listener);
//                dialog.show(getSupportFragmentManager(), TAG_SERVICE_STAGE_TYPE);
//            }
//        });
//    }
//
//    void initProblem() {
//
//        edt_problem = (EditText) findViewById(R.carId.edt_problem);
//        edt_problem.setText(car.Problem);
//
//        final String title = "Choose Problem";
//        final CharSequence[] driveTypeList = getResources().getStringArray(
//                R.array.Problem);
//        final ListDialogListener listener = new ListDialogListener() {
//
//            @Override
//            public void onItemClick(int position) {
//                edt_problem.setText(driveTypeList[position]);
//            }
//
//            @Override
//            public void onDialogNegativeClick(DialogFragment dialog) {
//
//            }
//        };
//
//        edt_problem.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
//                        title, driveTypeList, listener);
//                dialog.show(getSupportFragmentManager(), TAG_PROBLEM);
//            }
//        });
//    }
//
//    void initTitle() {
//
//        edt_title = (EditText) findViewById(R.carId.edt_title);
//        edt_title.setText(car.Title);
//
//        final String title = "Choose Title";
//        final CharSequence[] driveTypeList = getResources().getStringArray(
//                R.array.Title);
//
//        final ListDialogListener listener = new ListDialogListener() {
//
//            @Override
//            public void onItemClick(int position) {
//
//                edt_title.setText("");
//                edt_title.setText(driveTypeList[position]);
//
//            }
//
//            @Override
//            public void onDialogNegativeClick(DialogFragment dialog) {
//
//            }
//        };
//
//        edt_title.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
//                        title, driveTypeList, listener);
//                dialog.show(getSupportFragmentManager(), TAG_TITLE);
//            }
//        });
//    }
//
//    void initGPSInstalled() {
//
//        edt_gps_installed = (EditText) findViewById(R.carId.edt_gps_installed);
//        edt_gps_installed.setText(car.Gps_Installed);
//        final String title = "Choose One";
//        final CharSequence[] driveTypeList = getResources().getStringArray(
//                R.array.Title);
//
//        final ListDialogListener listener = new ListDialogListener() {
//
//            @Override
//            public void onItemClick(int position) {
//
//                edt_gps_installed.setText(driveTypeList[position]);
//
//            }
//
//            @Override
//            public void onDialogNegativeClick(DialogFragment dialog) {
//
//            }
//        };
//
//        edt_gps_installed.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
//                        title, driveTypeList, listener);
//                dialog.show(getSupportFragmentManager(), TAG_GPS_INSTALLED);
//            }
//        });
//    }
//
//    Button btn_company_insurance;
//    ImageView iv_company_insurance;
//
//    private void initCompanyInsurance() {
//
//        btn_company_insurance = (Button) findViewById(R.carId.btn_company_insurance);
//        iv_company_insurance = (ImageView) findViewById(R.carId.iv_company_insurance);
//        // http://drivehere.com/inventory2/app/webroot/files/images/
//        if (!car.company_insurance.equalsIgnoreCase("")
//                && !car.company_insurance.equalsIgnoreCase("null")
//                && !car.company_insurance
//                .equalsIgnoreCase("http://drivehere.com/inventory2/app/webroot/files/images/")
//                && !car.company_insurance
//                .equalsIgnoreCase("http://drivehere.com/inventory2/app/webroot/files/")) {
//            iv_company_insurance.setVisibility(View.VISIBLE);
//            Log.e("car.company_insurance", car.company_insurance);
//
//            // Bitmap bitmap =Common.filepathTobitmap(car.company_insurance);
//            // iv_company_insurance.setImageBitmap(bitmap);
//            // String path = car.company_insurance.replace(" ", "%20");
//            // Picasso.with(this).load(path).into(iv_company_insurance);
//
//            // companyInsuranceFile
//            // companyInsuranceFile = Common.bitmapToFilePath(bitmap);
//            iv_company_insurance.setEnabled(false);
//            setCompanyInsuranceImage();
//        }
//        btn_company_insurance.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // isFromCompanyInsurance =true;
////                startCameraIntent(true);
//
//                if (AppSingleTon.VERSION_OS.checkVersion()) {
//                    // Marshmallow+
//                    if ((checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) &&
//                            (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
//                        startCameraIntent(true);
//
//                    } else {
//                        if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(EditCarActivity.this);
//
//                            builder.setTitle("");
//                            builder.setMessage("Both Camera & Storage Permissions Are Need To Take Pictures,Allow It?");
//                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    final Intent i = new Intent();
//                                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                    i.addCategory(Intent.CATEGORY_DEFAULT);
//                                    i.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
//                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                                    startActivity(i);
//                                }
//                            });
//                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                            EDIT_CAR_ACTIVITY_REQUEST_CAMERA_INSURANCE);
//                                }
//                            });
//                            builder.show();
//                        }
//
//
//                    }
//
//                } else {
//                    // Pre-Marshmallow
//                    startCameraIntent(true);
//
//                }
//            }
//        });
//
//        // companyInsuranceFile
//        iv_company_insurance.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                RotateImage();
//
//            }
//        });
//
//    }
//
//    public interface ImageDataListener {
//        public void getImageBitmap(Bitmap bitmap);
//    }
//
//    private void setCompanyInsuranceImage() {
//        ImageDataListener imageDataListener = new ImageDataListener() {
//            @Override
//            public void getImageBitmap(Bitmap bitmap) {
//
//                if (bitmap == null) {
//                    iv_company_insurance
//                            .setImageResource(R.drawable.ic_launcher);
//                } else {
//                    iv_company_insurance.setImageBitmap(bitmap);
//                    iv_company_insurance.setEnabled(true);
//                    File file;
//                    file = Common.bitmapToFilePath(bitmap);
//                    companyInsuranceFile = file;
//                }
//            }
//        };
//
//        DownloadImageTask downloadImageTask = new DownloadImageTask(
//                imageDataListener);
//
//        downloadImageTask.execute(car.company_insurance);
//
//    }
//
//    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//
//        // private ProgressDialog mDialog;
//        ImageDataListener imageDataListener;
//
//        public DownloadImageTask(ImageDataListener imageDataListener) {
//            this.imageDataListener = imageDataListener;
//        }
//
//        protected void onPreExecute() {
//            // MLog.d("downloading iamge");
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            // MLog.d("image url in download image task : " + urls[0]);
//
//            Log.e("urls", "urls" + urls);
//            Bitmap mIcon11 = null;
//            try {
//
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//
//            } catch (Exception e) {
//                Log.e("Error", "image download error");
//                Log.e("Error", "" + e.getMessage());
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            // set image of your imageview
//            // bmImage.setImageBitmap(CommonUtils.getRoundedShape(result));
//            // MLog.d("image width before crop: " +result.getWidth() +
//            // "image height before crop : " + result.getHeight());
//            // Bitmap bitmap = Bitmap.createScaledBitmap(result, 170, 170,
//            // true);//Bitmap(result, 0, 0, 170, 170);
//            // int image_height = bitmap.getHeight();
//            // int image_width = bitmap.getWidth();
//            // MLog.d("image_height : " + image_height + "  image_width : " +
//            // image_width);
//            imageDataListener.getImageBitmap(result);
//            // close
//            // mDialog.dismiss();
//        }
//    }
//
//    File companyInsuranceFile = null;
//    Dialog dialog;
//    ImageView dialogImage;
//    Button RotateButton;
//    Button DoneButton;
//    int bitmapWidth;
//    int bitmapHeight;
//
//    public void RotateImage() {
//
//        dialog = new Dialog(EditCarActivity.this);
//        dialog.setTitle("Choose Action");
//        @SuppressWarnings("static-access")
//        LayoutInflater inflater = getLayoutInflater().from(
//                getApplicationContext());
//
//        View view = inflater.inflate(R.layout.activity_custom_dialog, null);
//
//        dialog.setContentView(view);
//        // dialog.setCancelable(false);
//        dialogImage = (ImageView) view.findViewById(R.carId.selectedImage);
//        RotateButton = (Button) view.findViewById(R.carId.RotateButton);
//        DoneButton = (Button) view.findViewById(R.carId.DoneButton);
//        dialogImage.setImageBitmap(Common.filepathTobitmap(companyInsuranceFile
//                .getPath()));
//
//        DoneButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                if (resizedBitmap != null) {
//
//                    // ImageView img = (ImageView) ll_container
//                    // .findViewWithTag(pos);
//                    iv_company_insurance.setImageBitmap(resizedBitmap);
//
//                    File file;
//                    file = Common.bitmapToFilePath(resizedBitmap);
//
//                    companyInsuranceFile = file;
//                    // if (file != null) {
//                    // AddNewCarActivity.photoList.set(pos, file);
//                    // Log.e("rotate image"+ pos, file.getPath());
//                    // }
//                }
//
//                dialog.cancel();
//
//            }
//        });
//        RotateButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                Bitmap dialog_imageBitmap = ((BitmapDrawable) dialogImage
//                        .getDrawable()).getBitmap();
//                bitmapWidth = dialog_imageBitmap.getWidth();
//                bitmapHeight = dialog_imageBitmap.getHeight();
//                drawMatrix(dialog_imageBitmap);
//
//            }
//        });
//        dialog.show();
//    }
//
//    Bitmap resizedBitmap = null;
//
//    private void drawMatrix(Bitmap dialog_imageBitmap) {
//        Matrix matrix = new Matrix();
//        matrix.preRotate(90);
//        resizedBitmap = Bitmap.createBitmap(dialog_imageBitmap, 0, 0,
//                bitmapWidth, bitmapHeight, matrix, true);
//        BitmapDrawable b = new BitmapDrawable(resizedBitmap);
//        dialogImage.setImageBitmap(resizedBitmap);
//        // dialogImage.setRotation(angle);
//    }
//
//    void initLocation() {
//
//        edt_location = (EditText) findViewById(R.carId.edt_location);
//        edt_location.setText(car.has_location);
//
//        final String title = "Choose Location Title";
//        final CharSequence[] driveTypeList = getResources().getStringArray(
//                R.array.location_title);
//        final ListDialogListener listener = new ListDialogListener() {
//
//            @Override
//            public void onItemClick(int position) {
//
//                edt_location.setText(driveTypeList[position]);
//
//            }
//
//            @Override
//            public void onDialogNegativeClick(DialogFragment dialog) {
//
//            }
//        };
//
//        edt_location.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
//                        title, driveTypeList, listener);
//                dialog.show(getSupportFragmentManager(), TAG_TITLE);
//            }
//        });
//    }
//
//    void initclicklistener() {
//        ed_insuranceDate.setOnClickListener(this);
//        ed_registrationdate.setOnClickListener(this);
//        ed_inspectiondate.setOnClickListener(this);
//
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//            case R.carId.ed_date_insurance:
//
//                showDatePickerDialog(v);
//                click = 1;
//                break;
//
//            case R.carId.ed_date_inspection:
//
//                showDatePickerDialog(v);
//                click = 2;
//                break;
//
//            case R.carId.ed_date_Registration:
//
//                click = 3;
//                showDatePickerDialog(v);
//                break;
//
//            default:
//                break;
//        }
//    }
//
//    void initCylinders() {
//        cylinders = (EditText) findViewById(R.carId.cylinders);
//        cylinders.setText(car.Cylinders);
//    }
//
//    void initMaxTorque() {
//        maxTorque = (EditText) findViewById(R.carId.maxTorque);
//        maxTorque.setText(car.MaxTorque);
//    }
//
//    void initMaxHP() {
//        maxHP = (EditText) findViewById(R.carId.maxHP);
//        maxHP.setText(car.MaxHP);
//    }
//
//    void initVehicleType() {
//        vehicleType = (EditText) findViewById(R.carId.vehicleType);
//        vehicleType.setText(car.VehicleType);
//    }
//
//    void initDriveType() {
//        driveType = (EditText) findViewById(R.carId.driveType);
//        driveType.setText(car.DriveType);
//
//        final String title = "Choose Drive Type";
//        final CharSequence[] driveTypeList = getResources().getStringArray(
//                R.array.DriveType);
//        final ListDialogListener listener = new ListDialogListener() {
//
//            @Override
//            public void onItemClick(int position) {
//                driveType.setText(driveTypeList[position]);
//            }
//
//            @Override
//            public void onDialogNegativeClick(DialogFragment dialog) {
//
//            }
//        };
//
//        driveType.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
//                        title, driveTypeList, listener);
//                dialog.show(getSupportFragmentManager(), TAG_DRIVE_TYPE);
//            }
//        });
//    }
//
//    public void showDatePickerDialog(View v) {
//        DialogFragment newFragment = new DatePickerFragment();
//        newFragment.show(getSupportFragmentManager(), "datePicker");
//    }
//
//    public static class DatePickerFragment extends DialogFragment implements
//            DatePickerDialog.OnDateSetListener {
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the current date as the default date in the picker
//            final Calendar c = Calendar.getInstance();
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//            int day = c.get(Calendar.DAY_OF_MONTH);
//
//            // Create a new instance of DatePickerDialog and return it
//            return new DatePickerDialog(getActivity(), this, year, month, day);
//        }
//
//        public void onDateSet(DatePicker view, int year, int month, int day) {
//            // Do something with the date chosen by the user
//
//            DecimalFormat formatter = new DecimalFormat("00");
//            String date = String.valueOf(year) + "-"
//                    + String.valueOf(formatter.format(month + 1)) + "-"
//                    + String.valueOf(formatter.format(day));
//
//            if (click == 1) {
//
//                Log.e("click 1", "click 1");
//                ed_insuranceDate.setText(date);
//
//            } else if (click == 2) {
//
//                Log.e("click 2", "click 2");
//                ed_inspectiondate.setText(date);
//
//            } else {
//
//                ed_registrationdate.setText(date);
//            }
//        }
//    }
//
//    void initFuelType() {
//
//        final String title = "Choose Fuel Type";
//        final CharSequence[] statusList = getResources().getStringArray(
//                R.array.FuelType);
//        final ListDialogListener listener = new ListDialogListener() {
//
//            @Override
//            public void onItemClick(int position) {
//                fuelType.setText(statusList[position]);
//            }
//
//            @Override
//            public void onDialogNegativeClick(DialogFragment dialog) {
//
//            }
//        };
//
//        fuelType = (EditText) findViewById(R.carId.fuelType);
//        fuelType.setText(car.FuelType);
//        fuelType.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
//                        title, statusList, listener);
//                dialog.show(getSupportFragmentManager(), TAG_FUEL_TYPE);
//
//            }
//        });
//    }
//
//    void initOilCapacity() {
//        oilCapacity = (EditText) findViewById(R.carId.oilCapacity);
//        oilCapacity.setText(car.OilCapacity);
//    }
//
//    void initDoneListener() {
//        doneListener = new OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId,
//                                          KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    AppSingleTon.METHOD_BOX.hidekeyBoard(EditCarActivity.this);
//                }
//                return false;
//            }
//        };
//    }
//
//    void initMake() {
//        final String title = "Choose Make";
//        final CharSequence[] makeList = getResources().getStringArray(
//                R.array.Make);
//        final ListDialogListener listener = new ListDialogListener() {
//
//            @Override
//            public void onItemClick(int position) {
//                make.setText(makeList[position]);
//            }
//
//            @Override
//            public void onDialogNegativeClick(DialogFragment dialog) {
//
//            }
//        };
//
//        make = (EditText) findViewById(R.carId.make);
//        make.setText(car.Make);
//        make.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
//                        title, makeList, listener);
//                dialog.show(getSupportFragmentManager(), TAG_MAKE);
//            }
//        });
//    }
//
//    void initModel() {
//        model = (EditText) findViewById(R.carId.model);
//        model.setText(car.Model);
//    }
//
//    void initCompany() {
//        company = (EditText) findViewById(R.carId.company);
//        company.setText(car.Company);
//    }
//
//    void initMiles() {
//        miles = (EditText) findViewById(R.carId.miles);
//
//        if (car.Miles != null && car.Miles.length() > 3) {
//            String milesSubstr = car.Miles.substring(car.Miles.length() - 3);
//            if (milesSubstr.equalsIgnoreCase(".00")) {
//                miles.setText(car.Miles.substring(0, car.Miles.length() - 3));
//            } else {
//                miles.setText(car.Miles);
//            }
//        } else {
//            miles.setText(car.Miles);
//        }
//
//    }
//
//    void initModelYear() {
//
//        final String title = "Choose Model Year";
//        final CharSequence[] yearList = getYearList();
//
//        final ListDialogListener listener = new ListDialogListener() {
//
//            @Override
//            public void onItemClick(int position) {
//                modelYear.setText(yearList[position]);
//            }
//
//            @Override
//            public void onDialogNegativeClick(DialogFragment dialog) {
//
//            }
//        };
//
//        modelYear = (EditText) findViewById(R.carId.modelYear);
//        modelYear.setText(car.ModelYear);
//        modelYear.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
//                        title, yearList, listener);
//                dialog.show(getSupportFragmentManager(), TAG_MODEL_YEAR);
//            }
//        });
//    }
//
//    CharSequence[] getYearList() {
//
//        Calendar calendar = Calendar.getInstance();
//        int currentYear = calendar.get(Calendar.YEAR);
//        int sz = (currentYear - 1980) + 1;
//        CharSequence[] yearList = new CharSequence[sz];
//
//        for (int i = 0; i < sz; i++) {
//            yearList[i] = String.valueOf(1980 + i) + "";
//        }
//        return yearList;
//    }
//
//    void initModelNumber() {
//        modelNumber = (EditText) findViewById(R.carId.modelNumber);
//        modelNumber.setText(car.ModelNumber);
//    }
//
//    void initNote() {
//        note = (EditText) findViewById(R.carId.note);
//        note.setText(car.Note);
//    }
//
//    void initMechanic() {
//        mechanic = (EditText) findViewById(R.carId.mechanic);
//        mechanic.setText(car.mechanic);
//    }
//
//    void initLotCode() {
//        // lotCode = (EditText) findViewById(R.carId.lotCode);
//        final String title = "Choose Lot Code";
//        final CharSequence[] lotList = getResources().getStringArray(
//                R.array.Lotcode);
//        // final CharSequence[] colorValueList = getResources().getStringArray(
//        // R.array.ColorValue);
//
//        final ListDialogListener listener = new ListDialogListener() {
//
//            @Override
//            public void onItemClick(int position) {
//                // lotCode.setText(lotList[position]);
//                // lotCode.setTag(colorValueList);
//                lotCode.setText(lotList[position]);
//            }
//
//            @Override
//            public void onDialogNegativeClick(DialogFragment dialog) {
//
//            }
//        };
//
//        lotCode = (EditText) findViewById(R.carId.lotCode);
//        // color.setTag("");
//
//        lotCode.setText(car.LotCode);
//        lotCode.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                 * SingleChoiceTextDialogFragment dialog = new
//				 * SingleChoiceTextDialogFragment( title,lotList,listener);
//				 * dialog.show(getSupportFragmentManager(),TAG_VEHICLE_STATUS);
//
//
//                 * ColorChoiceDialogFragment dialog1 = new
//				 * ColorChoiceDialogFragment( listener);
//				 * dialog1.show(getSupportFragmentManager(), TAG_COLOR);
//
//
//                LotcodeChoiceDialogFragment dialog1 = new LotcodeChoiceDialogFragment(
//                        listener);
//                dialog1.show(getSupportFragmentManager(), TAG_COLOR);
//
//            }
//        });
//
//    }
//
//    void initPrice() {
//        salesPrice = (EditText) findViewById(R.carId.salesPrice);
//        salesPrice.setText(car.SalesPrice);
//    }
//
//    void initStockNumber() {
//        stockNumber = (EditText) findViewById(R.carId.stockNumber);
//        stockNumber.setText(car.StockNumber);
//    }
//
//    void initStatusOfVehicle() {
//        final String title = "Choose Vehicle Status";
//        final CharSequence[] statusList = getResources().getStringArray(
//                R.array.VehicleStatus);
//        final ListDialogListener listener = new ListDialogListener() {
//
//            @Override
//            public void onItemClick(int position) {
//                statusOfVehicle.setText(statusList[position]);
//            }
//
//            @Override
//            public void onDialogNegativeClick(DialogFragment dialog) {
//
//            }
//        };
//
//        statusOfVehicle = (EditText) findViewById(R.carId.vehicleStatus);
//        if (car.VehicleStatus == null || car.VehicleStatus.equalsIgnoreCase(""))
//            statusOfVehicle.setText("Null");
//        else
//            statusOfVehicle.setText(car.VehicleStatus);
//
//        statusOfVehicle.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(title, statusList, listener);
//                dialog.show(getSupportFragmentManager(), TAG_VEHICLE_STATUS);
//            }
//        });
//    }
//
//    void initPurchasedFrom() {
//        purchasedfrom = (EditText) findViewById(R.carId.purchasedFrom);
//        purchasedfrom.setText(car.PurchasedFrom);
//        purchasedfrom.setOnEditorActionListener(doneListener);
//    }
//
//    public void initUpdate() {
//        LinearLayout update = (LinearLayout) findViewById(R.carId.update);
//        update.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (Common.isNetworkConnected(getApplicationContext())) {
//                    if (vin.getText().toString().equalsIgnoreCase(car.Vin)
//                            && rfid.getText().toString().equalsIgnoreCase(car.Rfid)) {
//
//                        Log.e("vin and rfid", "vin and rfid" + car.Vin + car.Rfid);
//                        String clr = (String) color.getTag();
////                    updateCar();
//                        upadateCarUsingVolley();
//                    } else {
//                        // Toast.makeText(EditCarActivity.this, "vin rfid change",
//                        // Toast.LENGTH_SHORT).show();
//                        openFaceRecognizationActivity();
//                    }
//                } else {
//                    Toast.makeText(EditCarActivity.this, "Please Connect To Internet", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//    }
//
//    LinearLayout ll_container;
//    boolean isFirst;
//    ArrayList<Gps> gpsList = new ArrayList<Gps>();
//    ;
//
//    void initGps() {
//
//        isFirst = true;
//
//        if (car.gps != null && car.gps.size() > 0) {
//            gpsList = car.gps;
//
//            for (int i = 0; i < gpsList.size(); i++) {
//                Log.e("init gpslist",
//                        "carId: " + gpsList.get(i).carId + " gpsid:"
//                                + gpsList.get(i).gpsid + "  carInventoryid:"
//                                + gpsList.get(i).CarinventoryId
//                                + "  createddate:" + gpsList.get(i).createddate);
//            }
//        }
//        ll_container = (LinearLayout) findViewById(R.carId.ll_container);
//
//        if (gpsList != null && gpsList.size() > 0) {
//
//            for (int i = 0; i < gpsList.size(); i++) {
//                addGpsView(gpsList.get(i));
//            }
//        } else {
//            addGpsView(null);
//        }
//
//    }
//
//    Gps gps;
//    int i = 0;
//
//    EditText edtselected;
//
//    void addGpsView(Gps gpsv) {
//        this.gps = gpsv;
//        final View itemList = EditCarActivity.this.getLayoutInflater().inflate(
//                R.layout.row_for_gps, ll_container, false);
//        // itemList = AddNewCarActivity.this
//        // .getLayoutInflater()
//        // .inflate(R.layout.row_for_gps,
//        // ll_container, false);
//        ed_gps = (EditText) itemList.findViewById(R.carId.ed_gps);
//        Button btn_minus = (Button) itemList.findViewById(R.carId.btn_minus);
//
//        if (gps != null) {
//            ed_gps.setText(gps.gpsid);
//            btn_minus.setTag(i);
//            ed_gps.setTag(i);
//        } else {
//            gps = new Gps();
//            gps.carId = "-1";
//            gps.CarinventoryId = "";
//            gps.gpsid = "";
//            gps.createddate = "";
//            gpsList.add(gps);
//            btn_minus.setTag(i);
//            ed_gps.setTag(i);
//        }
//
//        i++;
//        // final int ic = 0;
//        // ed_gps.setOnClickListener(new View.OnClickListener() {
//        //
//        // @Override
//        // public void onClick(View v) {
//
//        // int ic = (Integer) v.getTag();
//        // }
//        // });
//
//        ed_gps.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                // gps.gpsid = s.toString();
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // gps.gpsid = ed_gps.getText().toString();
//                Log.e("dhgfjhgsagdfhj", ed_gps.getText().toString());
//                gpsList.get((Integer) ed_gps.getTag()).gpsid = ed_gps.getText()
//                        .toString();
//                // if(ed_gps.getTag().equals("")){
//                // gps.gpsid = ed_gps.getText().toString();
//                // }else{
//                // gps.gpsid =ed_gps.getText().toString();
//                // }
//
//            }
//        });
//
//        Button btn_plus = (Button) itemList.findViewById(R.carId.btn_plus);
//        btn_plus.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // Toast.makeText(getApplication(), "Clicked on plus",
//                // Toast.LENGTH_SHORT).show();
//                addGpsView(null);
//            }
//        });
//
//        btn_minus.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int i = (Integer) v.getTag();
//
//                gpsList.get(i).gpsid = "";
//                ((LinearLayout) itemList.getParent()).removeView(itemList);
//                // containerlayout.removeView(itemList.getTag());
//
//            }
//        });
//
//        TextView tv_scan = (TextView) itemList.findViewById(R.carId.tv_gpsscan);
//        tv_scan.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
////jkjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj
//                if (AppSingleTon.VERSION_OS.checkVersion()) {
//                    // Marshmallow+
//                    if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                        View view = (View) v.getParent();
//                        edtselected = (EditText) view.findViewById(R.carId.ed_gps);
//                        Intent scanintent = new Intent(EditCarActivity.this,
//                                BarCodeScannerActivity.class);
//                        startActivityForResult(scanintent, 101);
//
//                    } else {
//                        if (!shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(EditCarActivity.this);
//
//                            builder.setTitle("");
//                            builder.setMessage("Camera Permission Needed For Scanning,Allow It?");
//                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    final Intent i = new Intent();
//                                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                    i.addCategory(Intent.CATEGORY_DEFAULT);
//                                    i.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
//                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                                    startActivity(i);
//                                }
//                            });
//                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, EDIT_CAR_ACTIVITY_REQUEST_CAMERA_GPS);
//                                }
//                            });
//                            builder.show();
//                        }
//
//
//                    }
//
//                } else {
//                    // Pre-Marshmallow
//                    View view = (View) v.getParent();
//                    edtselected = (EditText) view.findViewById(R.carId.ed_gps);
//                    Intent scanintent = new Intent(EditCarActivity.this,
//                            BarCodeScannerActivity.class);
//                    startActivityForResult(scanintent, 101);
//
//                }
//
//
//            }
//        });
//        if (isFirst) {
//
//            isFirst = false;
//
//        } else {
//
//            btn_plus.setVisibility(View.GONE);
//            btn_minus.setVisibility(View.VISIBLE);
//
//        }
//        ll_container.addView(itemList);
//    }
//
//
//    public String getGpsJsonString() {
//
//        JSONObject object = new JSONObject();
//
//        JSONArray jArray = new JSONArray();
//
//        try {
//
//            for (int i = 0; i < gpsList.size(); i++) {
//
//                if (gpsList.get(i).carId.equalsIgnoreCase("-1")
//                        && gpsList.get(i).gpsid.equalsIgnoreCase("")) {
//
//                } else {
//                    JSONObject Obj = new JSONObject();
//                    Obj.put("carId", gpsList.get(i).carId);
//                    Obj.put("gpsid", gpsList.get(i).gpsid);
//                    jArray.put(Obj);
//                }
//
//            }
//
//            object.put("Gps", jArray);
//
//        } catch (Exception e) {
//            // Log.e("Error in getGpsJsonString() :" + e.toString());
//        }
//        // Log.e("Item To ADD:  " + object.toString());
//
//        return object.toString();
//
//    }
//
//    void openFaceRecognizationActivity() {
//        // Check if Facial Recognition feature is supported in the device
//        boolean isSupported = FacialProcessing
//                .isFeatureSupported(FEATURE_LIST.FEATURE_FACIAL_RECOGNITION);
//        boolean hasFroncamera = Common.isFrontCameraAvailable();
//
//        if (isSupported && hasFroncamera) {
//
//
//            PassConfirmDialogue(false);
//            if (AppSingleTon.SHARED_PREFERENCE.getUserFace() != null
//                    && AppSingleTon.SHARED_PREFERENCE.getUserFace().length() > 0) {
//                // Toast.makeText(EditCarActivity.this, "face"
//                // +AppSingleTon.SHARED_PREFERENCE.getUserFace() ,
//                // Toast.LENGTH_SHORT).show();
//                // the user face is already added...........
//                Intent intent = new Intent(
//                        EditCarActivity.this,
//                        com.yukti.facerecognization.FacialRecognitionActivity.class);
//                intent.putExtra("isaddPhoto", false);
//                startActivityForResult(intent, LIVE_RECG_REQ);
//
//            } else {
//                // user face is not adeed....
//                // Toast.makeText(EditCarActivity.this, "first add face",
//                // Toast.LENGTH_SHORT).show();
//                // userhasNoFace
//                PassConfirmDialogue(true);
//            }
//
//
//
//        } else {
//
//            // face recognize does not support ...
//            // Toast.makeText(EditCarActivity.this,
//            // "Device not Supported Face recognization",
//            // Toast.LENGTH_SHORT).show();
//            PassConfirmDialogue(false);
//
//        }
//    }
//
//    void PassConfirmDialogue(final boolean userhasNoFace) {
//        final PassDialogListener listener = new PassDialogListener() {
//            @Override
//            public void onOkClick(String pass) {
//                if (!pass.equalsIgnoreCase("")) {
//
//                    if (pass.equalsIgnoreCase(AppSingleTon.SHARED_PREFERENCE
//                            .getPassword())) {
//
//                        if (userhasNoFace) {
//                            if (AppSingleTon.VERSION_OS.checkVersion()) {
//                                // Marshmallow+
//                                if ((checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) &&
//                                        (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
//                                    updateFace();
//
//                                } else {
//                                    if (!shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(EditCarActivity.this);
//
//                                        builder.setTitle("");
//                                        builder.setMessage("Both Camera & Storage Permissions Are Needed For Scanning & Update Face,Allow It?");
//                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//
//                                                final Intent i = new Intent();
//                                                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                                i.addCategory(Intent.CATEGORY_DEFAULT);
//                                                i.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
//                                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                                                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                                                startActivity(i);
//                                            }
//                                        });
//                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                                        EDIT_CAR_ACTIVITY_REQUEST_CAMERA_FACE_UPDATE);
//                                            }
//                                        });
//                                        builder.show();
//                                    }
//
//
//                                }
//
//                            } else {
//                                // Pre-Marshmallow
//                                updateFace();
//
//                            }
//
//                        } else {
////                            updateCar();
//                            upadateCarUsingVolley();
//                        }
//
//                    } else {
//
//                        Toast.makeText(getApplication(), "Wrong Password",
//                                Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//
//            }
//
//        };
//
//        PassConfirmDialougueFrag dialog = new PassConfirmDialougueFrag(
//                "Enter Your Password", "msg", listener);
//        dialog.show(getSupportFragmentManager(), TAG_CONFIRM_PASS);
//
//    }
//
//    protected void updateFace() {
//
//        new AlertDialog.Builder(EditCarActivity.this)
//                .setMessage("Do you Want to add Face")
//                .setCancelable(false)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int carId) {
//                        Intent intent = new Intent(
//                                EditCarActivity.this,
//                                com.yukti.facerecognization.FacialRecognitionActivity.class);
//                        intent.putExtra("isaddPhoto", true);
//                        intent.putExtra("isFromEditCar", true);
//                        // intent.putExtra("Userid", user.carId);
//                        startActivityForResult(intent, REQ_UPDATE_FACE);
//                    }
//                })
//                .setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int carId) {
////                                updateCar();
//                                upadateCarUsingVolley();
//                            }
//                        })
//
//                .show();
//    }
//
//    void initVin() {
//
//        vin = (EditText) findViewById(R.carId.vin);
//        vin.setText(car.Vin);
//
//        if (car.Vin != null && car.Vin.trim().length() > 0) {
//            String scanCode = car.Vin.trim();
//            int scanCodeLength = car.Vin.length();
//            if (scanCodeLength == 18
//                    && (scanCode.startsWith("i") || scanCode.startsWith("I"))) {
//                scanCode = scanCode.substring(1, scanCode.length());
//            }
//            Log.e("scanCode", scanCode);
//            scanCodeLength = scanCode.length();
//
//            if (scanCodeLength == 17) {
//                vin.setText(scanCode);
//                tvScanVin.setVisibility(View.GONE);
//            }
//        }
//    }
//
//    void initRFID() {
//        rfid = (EditText) findViewById(R.carId.rfid);
//        rfid.setText(car.Rfid);
//        String scanCode = null;
//        if (car.Rfid != null)
//            scanCode = car.Rfid.trim();
//
//        if (scanCode != null && scanCode.trim().length() > 0) {
//            int scanCodeLength = scanCode.length();
//            Log.e("scanCode", scanCode);
//            scanCodeLength = scanCode.length();
//
//            if (scanCodeLength == 7) {
//                rfid.setText(scanCode);
//                tvScanRfid.setVisibility(View.GONE);
//            }
//        }
//    }
//
//    void initColor() {
//
//        final String title = "Choose Color";
//        final CharSequence[] colorNameList = getResources().getStringArray(
//                R.array.ColorName);
//        final CharSequence[] colorValueList = getResources().getStringArray(
//                R.array.ColorValue);
//
//        final ListDialogListener listener = new ListDialogListener() {
//
//            @Override
//            public void onItemClick(int position) {
//                color.setText(colorNameList[position]);
//                color.setTag(colorValueList[position]);
//            }
//
//            @Override
//            public void onDialogNegativeClick(DialogFragment dialog) {
//
//            }
//        };
//
//        color = (EditText) findViewById(R.carId.color);
//
//        if (car.Color == null || car.Color.equalsIgnoreCase("")) {
//            color.setTag("");
//        } else {
//            for (int i = 0; i < colorNameList.length; i++) {
//                if (car.Color.equals(colorValueList[i])) {
//                    color.setText(colorNameList[i]);
//                    color.setTag(colorValueList[i]);
//                    break;
//                }
//            }
//        }
//
//        color.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ColorChoiceDialogFragment dialog = new ColorChoiceDialogFragment(listener);
//                dialog.show(getSupportFragmentManager(), TAG_COLOR);
//            }
//        });
//    }
//
//    // public static CarInventory car_updated_data = null;
//
//    void upadateCarUsingVolley() {
//
//        ll_progress.setVisibility(View.VISIBLE);
//
//        MultipartRequest multipartRequest = new MultipartRequest(AppSingleTon.APP_URL.URL_UPDATE_VEHICLE_NEW, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e("response", response + " test");
//                ll_progress.setVisibility(View.GONE);
//                UpdateResponse respose = AppSingleTon.APP_JSON_PARSER.update(response);
//                ll_progress.setVisibility(View.GONE);
//                if (respose.status_code.equals("1")) {
//                    // car_updated_data = new CarInventory();
//                    car.Make = make.getText().toString();
//                    car.Model = model.getText().toString();
//                    car.Note = note.getText().toString();
//                    car.mechanic = mechanic.getText().toString();
//                    car.ModelNumber = modelNumber.getText().toString();
//                    car.ModelYear = modelYear.getText().toString();
//                    car.Vin = vin.getText().toString();
//                    car.Rfid = rfid.getText().toString();
//                    car.vacancy = edt_vacanyUpadate.getText().toString();
//                    String clr = (String) color.getTag();
//                    // Color = clr.toString().trim();
//                    if (clr != null && clr.length() > 0) {
//                        car.Color = color.getTag().toString().trim();
//                    } else {
//                        car.Color = "";
//                    }
//                    // car.Color= color.getText().toString();
//                    car.Stage = stage.getText().toString();
//                    car.ServiceStage = serviceStage.getText().toString();
//                    car.StockNumber = stockNumber.getText().toString();
//                    car.LotCode = lotCode.getText().toString();
//                    car.SalesPrice = salesPrice.getText().toString();
//                    car.VehicleStatus = statusOfVehicle.getText().toString();
//                    car.PurchasedFrom = purchasedfrom.getText().toString();
//                    car.Cylinders = cylinders.getText().toString();
//                    car.Gastank = edt_gas_tank.getText().toString();
//                    car.Company = company.getText().toString();
//                    car.Miles = miles.getText().toString();
//                    car.OilCapacity = oilCapacity.getText().toString();
//                    car.DriveType = driveType.getText().toString();
//                    car.MaxHP = maxHP.getText().toString();
//                    car.MaxTorque = maxTorque.getText().toString();
//                    car.FuelType = fuelType.getText().toString();
//                    car.VehicleType = vehicleType.getText().toString();
//                    car.Problem = edt_problem.getText().toString();
//                    car.Title = edt_title.getText().toString();
//                    car.has_location = edt_location.getText().toString();
//                    car.Gps_Installed = edt_gps_installed.getText().toString();
//
//                    car.registrationdate = ed_registrationdate.getText()
//                            .toString().trim();
//
//                    car.insurancedate = ed_insuranceDate.getText().toString()
//                            .trim();
//
//                    car.inspectiondate = ed_inspectiondate.getText().toString()
//                            .trim();
//
//                    car.company_insurance = respose.company_insurace;
//                    CarInventory cv = respose.result;
//                    car.gps = cv.gps;
//                    for (int i = 0; i < car.gps.size(); i++) {
//                        Log.e("car.gps", "carId: " + car.gps.get(i).carId + " gpsid:"
//                                + car.gps.get(i).gpsid + "  carInventoryid:"
//                                + car.gps.get(i).CarinventoryId
//                                + "  createddate:" + car.gps.get(i).createddate);
//                    }
//                    isUpdate = true;
//                    Intent intent = new Intent();
//                    intent.putExtra("each_car", car);
//                    intent.putExtra("isUpdate", isUpdate);
//                    intent.putExtra("itemPosition", itemPosition);
//                    setResult(RESULT_OK, intent);
//                    MessageDialogFragment fragment = new MessageDialogFragment(
//                            "Success", "Updated Successfully", true, "Ok",
//                            false, "", false, "", EditCarActivity.this);
//                    try {
//                        fragment.show(getSupportFragmentManager(), TAG_PUSH_RESULT);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else if (respose.status_code.equals("2")) {
//                    rfid.setError("RFID already used .");
//                    Toast.makeText(EditCarActivity.this, "Please enter valid RFID . Current RFID already in system", Toast.LENGTH_SHORT).show();
//                } else {
//                    MessageDialogFragment fragment = new MessageDialogFragment(
//                            "Failed", "Failed to update.", true, "Ok", false,
//                            "", false, "", EditCarActivity.this);
//                    try {
//                        fragment.show(getSupportFragmentManager(), TAG_PUSH_RESULT);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        ll_progress.setVisibility(View.GONE);
//                    }
//                });
//        multipartRequest.setTag(Constants.REQUEST_TAG);
//        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
//                Constants.VOLLEY_TIMEOUT,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        MyApplication.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);
//    }
//
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        ll_progress.setVisibility(View.GONE);
//        cancleRequest();
//    }
//
//    void cancleRequest() {
//        if (MyApplication.getInstance(this.getApplicationContext()).getRequestQueue() != null) {
//            Log.e("On Stop", "Cancle request");
//            MyApplication.getInstance(this.getApplicationContext()).getRequestQueue().cancelAll(Constants.REQUEST_TAG);
//        }
//    }
//
//    private boolean isLastPhoto = false;
//
//    @Override
//    protected Dialog onCreateDialog(int carId) {
//        switch (carId) {
//            case DATE_PICKER_ID:
//                return new DatePickerDialog(this, pickerListener, year, month, day);
//        }
//        return null;
//    }
//
//    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
//
//        // when dialog box is closed, below method will be called.
//        @Override
//        public void onDateSet(DatePicker view, int selectedYear,
//                              int selectedMonth, int selectedDay) {
//
//            year = selectedYear;
//            month = selectedMonth;
//            day = selectedDay;
//
//            // Show selected date
//            ed_insuranceDate.setText(new StringBuilder().append(month + 1)
//                    .append("-").append(day).append("-").append(year)
//                    .append(" "));
//        }
//    };
//
//    public void initCamera() {
//        photoList = null;
//        photoList = new ArrayList<File>();
//        sdcardManager = new SDCardManager();
//        camOperation = new CamOperation(this);
//        LinearLayout camera = (LinearLayout) findViewById(R.carId.camera);
//        cameraTxt = (TextView) findViewById(R.carId.camera_txt);
//        camera.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (AppSingleTon.VERSION_OS.checkVersion()) {
//                    // Marshmallow+
//                    if ((checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) &&
//                            (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
//                        startCameraIntent(false);
//
//                    } else {
//                        if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(EditCarActivity.this);
//
//                            builder.setTitle("");
//                            builder.setMessage("Both Camera & Permissions Are Needed To Take Pictures,Allow It?");
//                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    final Intent i = new Intent();
//                                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                    i.addCategory(Intent.CATEGORY_DEFAULT);
//                                    i.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
//                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                                    startActivity(i);
//                                }
//                            });
//                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                            EDIT_CAR_ACTIVITY_REQUEST_CAMERA);
//                                }
//                            });
//                            builder.show();
//                        }
//                    }
//                } else {
//                    // Pre-Marshmallow
//                    startCameraIntent(false);
//                }
//            }
//        });
//    }
//
//    TextView showPhoto;
//
//    void initShowPhoto() {
//
//        show_photo = (LinearLayout) findViewById(R.carId.show_photo);
//        showPhoto = (TextView) findViewById(R.carId.show_photo_txt);
//        showPhoto.setText("SHOW PHOTO" + "(" + (photoList.size() + photoListWeb.size()) + ")");
//
//        show_photo.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPhotoIntent();
//            }
//        });
//    }
//
//    void showPhotoIntent() {
//        if ((photoList != null && photoList.size() > 0) || (photoListWeb != null && photoListWeb.size() > 0)) {
//            Intent showPhotoIntent = new Intent(getApplication(), ShowPhotoActivityEdit.class);
//            startActivity(showPhotoIntent);
//        } else {
//            Toast.makeText(EditCarActivity.this, "No Image Available", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void startCameraIntent(final boolean isFromCompanyInsurance) {
//
//        if (!sdcardManager.isSDCardExists()) {
//            showToast("Device don't have any sdcard.");
//            return;
//        }
//        cameraResponse = new CameraResponse() {
//            @Override
//            public void onNoSdcardFound() {
//                showToast("No sdcard found!");
//            }
//
//            @Override
//            public void onFileCreatingError() {
//                showToast("Image file creating error on sdcard!");
//            }
//
//            @Override
//            public void onCameraReady(File file) {
//                currentImageFile = file;
//                Intent takePictureIntent = new Intent(
//                        MediaStore.ACTION_IMAGE_CAPTURE);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(file));
//                startActivityForResult(takePictureIntent,
//                        CamOperation.ACTION_TAKE_PHOTO);
//            }
//
//            @Override
//            public void onSuccess() {
//                try {
//                    byte[] photoByte = camOperation
//                            .fileToByteOne(currentImageFile);
//                    FileOutputStream fos = new FileOutputStream(
//                            currentImageFile);
//
//                    fos.write(photoByte);
//                    fos.flush();
//                    fos.close();
//
//                    if (isFromCompanyInsurance) {
//
//                        companyInsuranceFile = currentImageFile;
//                        iv_company_insurance.setImageBitmap(Common
//                                .filepathTobitmap(companyInsuranceFile
//                                        .getPath()));
//                        iv_company_insurance.setVisibility(View.VISIBLE);
//                    } else {
//                        photoList.add(currentImageFile);
//                        show_photo.setVisibility(View.VISIBLE);
//                        cameraTxt.setText("ADD PHOTO" + "(" + photoList.size() + ")");
//                        showPhoto.setText("SHOW PHOTO" + "(" + (photoList.size() + photoListWeb.size()) + ")");
//                    }
//                    galleryAddPic(currentImageFile);
//                } catch (java.io.IOException e) {
//                    Log.e("photo size decrease error", "Exception in photo size decrease", e);
//                }
//            }
//
//            @Override
//            public void onFailure() {
//                currentImageFile.delete();
//                currentImageFile = null;
//            }
//        };
//
//        if (camOperation.isIntentAvailable(MediaStore.ACTION_IMAGE_CAPTURE)) {
//            camOperation.createESPhotoFile(cameraResponse);
//
//        } else {
//            showToast("No Camera intent found on this device");
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if ((requestCode == CamOperation.ACTION_TAKE_PHOTO)) {
//
//            if (resultCode == Activity.RESULT_OK) {
//                try {
//                    cameraResponse.onSuccess();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//            }
//        } else if ((requestCode == REQUEST_SCAN_CODE)) {
//
//            if (resultCode == Activity.RESULT_OK) {
//                String code = data.getStringExtra("code");
//                if (code.length() == 17) {
//                    vin.setText(code);
//                } else if (code.length() == 7) {
//                    rfid.setText(code);
//                }
//            }
//        } else if (requestCode == REQUEST_SCAN_RFID) {
//
//            if (resultCode == Activity.RESULT_OK) {
//                scanCode = data.getStringExtra("code");
//                scanCodeLength = scanCode.length();
//
//                Log.e("scanCode", scanCode);
//                scanCodeLength = scanCode.length();
//
//                 * if (scanCodeLength == 17) { vin.setText(scanCode);
//				 * tvScanVin.setVisibility(View.GONE); } else
//
//
//                if (scanCodeLength == 7) {
//                    rfid.setText(scanCode);
//                    tvScanRfid.setVisibility(View.GONE);
//                }
//                // }
//
//                // AppSingleTon.METHOD_BOX.hidekeyBoard(this);
//                // if (scanCode.length() == 17) {
//                // pullDataoneInformations();
//                // }
//                //
//                //
//                // if (code.length() == 17) {
//                // vin.setText(code);
//                // pullDataoneInformations();
//                // } else if (code.length() == 7) {
//                // rfid.setText(code);
//                // }
//            }
//        } else if (requestCode == REQUEST_SCAN_VIN) {
//
//            if (resultCode == Activity.RESULT_OK) {
//                scanCode = data.getStringExtra("code");
//                scanCodeLength = scanCode.length();
//                if (scanCodeLength == 18
//                        && (scanCode.startsWith("i") || scanCode
//                        .startsWith("I"))) {
//                    scanCode = scanCode.substring(1, scanCode.length());
//                }
//
//                Log.e("scanCode", scanCode);
//                scanCodeLength = scanCode.length();
//
//                if (scanCodeLength == 17) {
//                    vin.setText(scanCode);
//                    tvScanVin.setVisibility(View.GONE);
//                }
//                 * else if (scanCodeLength == 7) { rfid.setText(scanCode);
//				 * tvScanRfid.setVisibility(View.GONE); }
//
//
//            }
//        } else if (requestCode == LIVE_RECG_REQ) {
//
//            if (resultCode == Activity.RESULT_OK) {
//                // Toast.makeText(getApplication(), "yoo",
//                // Toast.LENGTH_SHORT).show();
////                updateCar();
//                upadateCarUsingVolley();
//            }
//        } else if (requestCode == REQ_UPDATE_FACE) {
//
//            if (resultCode == Activity.RESULT_OK) {
//                // Toast.makeText(getApplication(), "yoo",
//                // Toast.LENGTH_SHORT).show();
////                updateCar();
//                upadateCarUsingVolley();
//            } else {
////                updateCar();
//                upadateCarUsingVolley();
//            }
//
//        } else if (requestCode == 101) {
//            if (resultCode == RESULT_OK) {
//
//                edtselected.setText(data.getStringExtra("code"));
//            }
//        }
//    }
//
//    private void galleryAddPic(File file) {
//        Intent mediaScanIntent = new Intent(
//                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        Uri contentUri = Uri.fromFile(file);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }
//
//    @Override
//    public void onDialogPositiveClick(MessageDialogFragment dialog) {
//
//        if (dialog.getTag().equals(TAG_PUSH_RESULT)) {
//            // Intent intent = new Intent();
//            // intent.putExtra("each_car", car);
//            // intent.putExtra("isUpdate", isUpdate);
//            // intent.putExtra("itemPosition", itemPosition);
//            // setResult(RESULT_OK, intent);
//            finish();
//        }
//    }
//
//    @Override
//    public void onDialogNegativeClick(MessageDialogFragment dialog) {
//
//    }
//
//    @Override
//    public void onDialogNeutralClick(MessageDialogFragment dialog) {
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//            case android.R.carId.home:
//                finish();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    class MultipartRequest extends Request<String> {
//        private HttpEntity mHttpEntity;
//        private Response.Listener mListener;
//
//        public MultipartRequest(String url,
//                                Response.Listener<String> listener,
//                                Response.ErrorListener errorListener) {
//            super(Method.POST, url, errorListener);
//            mListener = listener;
//            mHttpEntity = buildMultipartEntity();
//        }
//
//        @Override
//        protected Response<String> parseNetworkResponse(NetworkResponse response) {
//            try {
//                return Response.success(new String(response.data, "UTF-8"),
//                        getCacheEntry());
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//                return Response.success(new String(response.data), getCacheEntry());
//            }
//        }
//
//        @Override
//        protected void deliverResponse(String response) {
//            mListener.onResponse(response);
//        }
//
//        @Override
//        public String getBodyContentType() {
//            return mHttpEntity.getContentType().getValue();
//        }
//
//        @Override
//        public byte[] getBody() throws AuthFailureError {
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            try {
//                mHttpEntity.writeTo(bos);
//            } catch (IOException e) {
//                VolleyLog.e("IOException writing to ByteArrayOutputStream");
//            }
//            return bos.toByteArray();
//        }
//
//        public HttpEntity buildMultipartEntity() {
//            String carId = "", Make = "", Model = "", Note = "", Mechanic = "", ModelNumber = "", ModelYear = "", Company = "", Miles = "",
//                    StockNumber = "", LotCode = "", SalesPrice = "", VehicleStatus = "", PurchasedFrom = "", Vin = "", Rfid = "",
//                    Color = "", Cylinders = "", GasTank = "", MaxHP = "", MaxTorque = "", VehicleType = "", DriveType = "",
//                    FuelType = "", OilCapacity = "", Stage = "", Problem = "", Title = "", InsuranceDate = "", InspectionDate = "",
//                    RegistrationDate = "", Location = "", Gps_Installed = "", ServiceStage = "";
//
//            String vacancy = "";
//
//            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//            carId = car.carId;
//            Vin = vin.getText().toString().trim();
//            Rfid = rfid.getText().toString().trim();
//            Make = make.getText().toString().trim();
//            Model = model.getText().toString().trim();
//            ModelNumber = modelNumber.getText().toString().trim();
//            Miles = miles.getText().toString().trim();
//            Company = company.getText().toString().trim();
//            ModelYear = modelYear.getText().toString().trim();
//            Note = note.getText().toString().trim();
//            Mechanic = mechanic.getText().toString().trim();
//            SalesPrice = salesPrice.getText().toString().trim();
//            PurchasedFrom = purchasedfrom.getText().toString().trim();
//            VehicleStatus = statusOfVehicle.getText().toString().trim();
//            StockNumber = stockNumber.getText().toString().trim();
//            LotCode = lotCode.getText().toString().trim();
//            vacancy = edt_vacanyUpadate.getText().toString().trim();
//            String clr = (String) color.getTag();
//
//            // Color = clr.toString().trim();
//
//            if (clr != null && clr.length() > 0) {
//                Color = color.getTag().toString().trim();
//            } else {
//                Color = "";
//            }
//            // Color = color.getText().toString().trim();
//
//            VehicleType = vehicleType.getText().toString().trim();
//            DriveType = driveType.getText().toString().trim();
//            FuelType = fuelType.getText().toString().trim();
//            MaxHP = maxHP.getText().toString().trim();
//            MaxTorque = maxTorque.getText().toString().trim();
//            Cylinders = cylinders.getText().toString().trim();
//            GasTank = edt_gas_tank.getText().toString().trim();
//
//            OilCapacity = oilCapacity.getText().toString().trim();
//            Stage = stage.getText().toString().trim();
//            ServiceStage = serviceStage.getText().toString().trim();
//            Problem = edt_problem.getText().toString().trim();
//
//            if (edt_title.getText().toString().trim().equalsIgnoreCase("Blank")) {
//                Title = "";
//            } else {
//                Title = edt_title.getText().toString().trim();
//            }
//
//            Location = edt_location.getText().toString().trim();
//
//            Gps_Installed = edt_gps_installed.getText().toString().trim();
//
//            if (gpsList != null && gpsList.size() > 0) {
//
//                for (int i = 0; i < gpsList.size(); i++) {
//                    Log.e("gpslist",
//                            "carId: " + gpsList.get(i).carId + " gpsid:"
//                                    + gpsList.get(i).gpsid + "  carInventoryid:"
//                                    + gpsList.get(i).CarinventoryId
//                                    + "  createddate:" + gpsList.get(i).createddate);
//                }
//                String gpsJsonStr = getGpsJsonString();
//                Log.e("gpsJsonStr", gpsJsonStr);
//
//                builder.addTextBody(ParamsKey.KEY_GPS, gpsJsonStr);
//            } else {
//                builder.addTextBody(ParamsKey.KEY_GPS, "");
//            }
//
//            // if (!carId.equals(""))
//            builder.addTextBody(ParamsKey.KEY_EDITID, carId);
//
//
//            // if (!Vin.equals(""))
//            builder.addTextBody(ParamsKey.KEY_vin, Vin);
//
//
//            // if (!Rfid.equals(""))
//            builder.addTextBody(ParamsKey.KEY_RFID, Rfid);
//
//            // if (!Make.equals(""))
//            builder.addTextBody(ParamsKey.KEY_make, Make);
//
//            // if (!Model.equals(""))
//            builder.addTextBody(ParamsKey.KEY_model, Model);
//
//            // if (!Miles.equals(""))
//            builder.addTextBody(ParamsKey.KEY_miles, Miles);
//
//            // if (!Company.equals(""))
//            builder.addTextBody(ParamsKey.KEY_company, Company);
//
//            // if (!ModelYear.equals(""))
//            builder.addTextBody(ParamsKey.KEY_modelYear, ModelYear);
//
//            // if (!ModelNumber.equals(""))
//            builder.addTextBody(ParamsKey.KEY_CARMODELNUMBER, ModelNumber);
//
//            // if (!Note.equals(""))
//            builder.addTextBody(ParamsKey.KEY_NOTE, Note);
//
//            // if (!Mechanic.equals(""))
//            builder.addTextBody(ParamsKey.KEY_CARMECHANIC, Mechanic);
//
//            // if (!SalesPrice.equals(""))
//            builder.addTextBody(ParamsKey.KEY_SALSEPRICE, SalesPrice);
//
//            // if (!PurchasedFrom.equals(""))
//            builder.addTextBody(ParamsKey.KEY_CARPURCHASEDDATE, PurchasedFrom);
//
//            // if (!VehicleStatus.equals(""))
//            builder.addTextBody(ParamsKey.KEY_vehicleStatus, VehicleStatus);
//            //
//            // if (!StockNumber.equals(""))
//            builder.addTextBody(ParamsKey.KEY_CARSTOCKNUMBER, StockNumber);
//
//            // if (!dataoneInformation.equals(""))
//            builder.addTextBody(ParamsKey.KEY_DATAONEINFORMATION, dataoneInformation);
//
//            // if (!LotCode.equals(""))
//            builder.addTextBody(ParamsKey.KEY_CARLOTCODE, LotCode);
//
//            // Toast.makeText(getApplicationContext(), Color,
//            // Toast.LENGTH_LONG).show();
//            // if (!Color.equals(""))
//            builder.addTextBody(ParamsKey.KEY_color, Color);
//
//            // if (!VehicleType.equals(""))
//            builder.addTextBody(ParamsKey.KEY_EDITVEHICALTYPE, VehicleType);
//
//            // if (!DriveType.equals(""))
//            builder.addTextBody(ParamsKey.KEY_EDITDRIVETYPE, DriveType);
//
//            // if (!FuelType.equals(""))/
//            builder.addTextBody(ParamsKey.KEY_fuelType, FuelType);
//
//            // if (!MaxHP.equals(""))
//            builder.addTextBody(ParamsKey.KEY_CARMAXHP, MaxHP);
//
//            // if (!MaxTorque.equals(""))
//            builder.addTextBody(ParamsKey.KEY_MAXTORQUE, MaxTorque);
//
//            // if (!Cylinders.equals(""))
//            builder.addTextBody(ParamsKey.KEY_EDITCYLINDER, Cylinders);
//
//            builder.addTextBody(ParamsKey.KEY_CARGASTANK, GasTank);
//
//            // if (!OilCapacity.equals(""))
//            builder.addTextBody(ParamsKey.KEY_OILCAPACITY, OilCapacity);
//
//            // if (!Stage.equals(""))
//            builder.addTextBody(ParamsKey.KEY_stage, Stage);
//
//            builder.addTextBody(ParamsKey.KEY_serviceStage, ServiceStage);
//
//            Log.e("stage", "Stage" + Stage);
//            Log.e("ServiceStage", "ServiceStage" + ServiceStage);
//            //
//            // if (!Problem.equals(""))
//            builder.addTextBody(ParamsKey.KEY_problem, Problem);
//
//            // if (!Title.equals(""))
//            builder.addTextBody(ParamsKey.KEY_hasTitle, Title);
//
//            // if (!Title.equals(""))
//            builder.addTextBody(ParamsKey.KEY_CARLOCATION, Location);
//
//            builder.addTextBody(ParamsKey.KEY_VACANCY, vacancy);
//
//            if (deleteImage != null && deleteImage.length() > 4) {
//                builder.addTextBody(ParamsKey.KEY_IMAGEDELETE, deleteImage.replace("null,", ""));
//                Log.e("DeleteImage", deleteImage.replace("null,", ""));
//            }
//
//            builder.addTextBody(ParamsKey.KEY_GPSINSTRALLEDCHECK, Gps_Installed);
//            InsuranceDate = ed_insuranceDate.getText().toString();
//            RegistrationDate = ed_registrationdate.getText().toString();
//            InspectionDate = ed_inspectiondate.getText().toString();
//
//            builder.addTextBody(ParamsKey.KEY_INSURANCEDATE, InsuranceDate);
//            builder.addTextBody(ParamsKey.KEY_REGISTRATIONDATE, RegistrationDate);
//            builder.addTextBody(ParamsKey.KEY_INSPECTIONDATE, InspectionDate);
//
//            if (companyInsuranceFile != null) {
//                Log.e("companyInsuranceFile", companyInsuranceFile.getPath()
//                        .toString());
//                FileBody fileBody = new FileBody(companyInsuranceFile);
//                builder.addPart(ParamsKey.KEY_COMPANYINSURANCE, fileBody);
//            }
//
//            // if(!AppSingleTon.SHARED_PREFERENCE.getUserId().equals(""))
//            builder.addTextBody(ParamsKey.KEY_UPLOADEDID,
//                    AppSingleTon.SHARED_PREFERENCE.getUserId());
//
//            Location location = AppSingleTon.PLAY_MANAGER.getLastLocation();
//            if (location != null) {
//                builder.addTextBody(ParamsKey.KEY_LATITUTE, location.getLatitude() + "");
//                builder.addTextBody(ParamsKey.KEY_LONGITITU, location.getLongitude() + "");
//            } else {
//                builder.addTextBody(ParamsKey.KEY_LATITUTE, "");
//                builder.addTextBody(ParamsKey.KEY_LONGITITU, "");
//            }
//
//            if (photoList != null && photoList.size() > 0) {
//                builder.addTextBody(ParamsKey.KEY_PHOTOCOUNT, photoList.size() + "");
//                for (int k = 0; k < photoList.size(); k++) {
//                    File file = photoList.get(k);
//                    FileBody fileBody = new FileBody(file);
//                    builder.addPart(ParamsKey.KEY_CARIMAGE + (k + 1), fileBody);
//                }
//            }
//            return builder.build();
//        }
//    }
//}

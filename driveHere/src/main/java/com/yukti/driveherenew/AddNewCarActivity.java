package com.yukti.driveherenew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yukti.dataone.model.AddCarModel;
import com.yukti.driveherenew.fragment.CallbackAdd;
import com.yukti.driveherenew.fragment.FRAGAddphoto;
import com.yukti.driveherenew.fragment.FRAGAllInOneInfo;
import com.yukti.driveherenew.fragment.FRAGAllInfo;
import com.yukti.driveherenew.fragment.FRAGDataOne;
import com.yukti.driveherenew.fragment.FRAGGps;
import com.yukti.driveherenew.fragment.FRAGLotcode;
import com.yukti.driveherenew.fragment.FRAGMiles;
import com.yukti.driveherenew.fragment.FRAGRfid;
import com.yukti.driveherenew.fragment.FRAGService;
import com.yukti.driveherenew.fragment.FRAGStage;
import com.yukti.driveherenew.fragment.FRAGStatus;
import com.yukti.driveherenew.fragment.FRAGTitle;
import com.yukti.driveherenew.fragment.FRAGVacancy;
import com.yukti.driveherenew.fragment.FRAGVin;
import com.yukti.driveherenew.search.CarInventory;
import com.yukti.jsonparser.FindMatch;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.Constants;
import com.yukti.utils.ParamsKey;
import com.yukti.utils.UniversalImageLoader;

public class AddNewCarActivity extends BaseActivity implements CallbackAdd {

    public static AddCarModel addCarModelObject;
    public static boolean isedit = false;
    public static ArrayList<String> arrayListGpsSerial;
    static Activity activity;
    public static int imageViewWidth = 0;
    public static int imageViewHeight = 0;
    public static ArrayList<Fragments> arryFragments;
    private static FragmentTransaction ft;
    private static Fragment objFragment;
    private static Fragments selectedFrag;
    private static int container;
    boolean fragment = false;
    String carVin = "";
    public CarInventory carInventory;
    android.support.v7.app.ActionBar mActionBar;

    public static enum Fragments {

        // Fragments for our local info
        Rfid, Vin, Vacancy, Status, Stage, Service, Lotcode, Miles, Gps, Addphoto, AddTitle,

        // These All in one fragment
        AllRemainingData,

        // Fragment for data one info
        AllDataOneData,

        // fragment for display all detail at last
        AllDetail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        activity = this;
        addCarModelObject = new AddCarModel(getBaseContext());

        UniversalImageLoader.initImageLoader(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_add_car_app_bar);
        setSupportActionBar(toolbar);
        setActionBar("Add New Car");

        initDefaultValues();

        container = R.id.frameLayout;
        if (arryFragments != null && arryFragments.size() > 0) {
            if (!fragment) {
                showFragment(arryFragments.get(0));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("ONSTART :", " ADD NEW CAR ACTIVITY");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("ONRESUME :", " ADD NEW CAR ACTIVITY");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.e("ON SAVE INSTANCE STATE", "ADD NEWCARACTIVITY");
        addCarModelObject.setArrayListGpsSerial(arrayListGpsSerial);
        //addCarModelObject = null;
        outState.putParcelable("addCarModelObject", (Parcelable) addCarModelObject);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null)
            addCarModelObject = savedInstanceState.getParcelable("addCarModelObject");

        Log.e("ON RESTORE INSTANCE STATE", "ADD NEWCARACTIVITY");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.e("ON RESTORE INSTANCE STATE", "ADD NEWCARACTIVITY");
    }

    private void initDefaultValues() {
        addCarModelObject.setFrmInfo(false);
        arrayListGpsSerial = new ArrayList<String>();

        addCarModelObject.arrayImagePath = new ArrayList<String>();

        Bundle bundle = getIntent().getExtras();
        fragment = bundle.getBoolean("redirect");
        if (fragment) {
            if (bundle != null) {
                carVin = getIntent().getStringExtra("carVin");
                getCarDetails(carVin);
            }
        } else {
            String bundleCode = bundle.getString("code");
            if (bundleCode.length() == 18 && (bundleCode.startsWith("i") || bundleCode.startsWith("I"))) {
                bundleCode = bundleCode.substring(1, bundleCode.length());
            }
            if (bundleCode.length() == 17) {
                addCarModelObject.setStrVin(bundleCode);
            } else if (bundleCode.length() == 7) {
                addCarModelObject.setStrRfid(bundleCode);
            }
        }
        initarrayFragments();
    }

    void getCarDetails(final String vinNumber) {
        final ProgressDialog mProgressDialog = new ProgressDialog(AddNewCarActivity.this);
        mProgressDialog.setMessage("Loading.....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppSingleTon.APP_URL.URL_CAR_DEATAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgressDialog.dismiss();
                try {
                    FindMatch findmatch = AppSingleTon.APP_JSON_PARSER.findMatch(response);
                    if (findmatch.status_code.equals("1")) {
                        carInventory = findmatch.cardetail;
                        setEditValues();

                    } else if (findmatch.status_code.equals("0")) {
                        Toast.makeText(getApplicationContext(), "No data founds", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(AddNewCarActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getApplicationContext()))
                                getCarDetails(vinNumber);
                            else
                                Toast.makeText(getApplicationContext(),Constant.ERR_INTERNET , Toast.LENGTH_LONG).show();
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

    public void setEditValues() {
        isedit = true;
        addCarModelObject.setCarId(carInventory.carId);
        addCarModelObject.setStrVin(carInventory.vin);
        addCarModelObject.setStrRfid(carInventory.rfid);
        addCarModelObject.setStrLotCode(carInventory.lotCode);
        addCarModelObject.setStrStatus(carInventory.vehicleStatus);
        addCarModelObject.setStrStage(carInventory.stage);
        addCarModelObject.setStrServiceStage(carInventory.ServiceStage);
        addCarModelObject.setStrStockNumber(carInventory.stockNumber);
        addCarModelObject.setStrVehicleProblem(carInventory.problem);
        addCarModelObject.setStrMechanic(carInventory.mechanic);
        addCarModelObject.setStrVehicleNote(carInventory.note);
        addCarModelObject.setStrColor(carInventory.color);
        addCarModelObject.setStrMiles(carInventory.miles);
        addCarModelObject.setStrMake(carInventory.make);
        addCarModelObject.setStrModel(carInventory.model);
        addCarModelObject.setStrModelNumber(carInventory.modelNumber);
        addCarModelObject.setStrModelYear(carInventory.modelYear);
        addCarModelObject.setStrMaxHp(carInventory.maxHp);
        addCarModelObject.setStrMaxTorque(carInventory.maxTorque);
        addCarModelObject.setStrFuelType(carInventory.fuel);
        addCarModelObject.setStrOilCapacity(carInventory.oilCapacity);
        addCarModelObject.setStrDriveType(carInventory.driveType);
        addCarModelObject.setStrCompany(carInventory.company);
        addCarModelObject.setStrSalesPrice(carInventory.salesPrice);
        addCarModelObject.setStrPurchaseForm(carInventory.purchasedFrom);
        addCarModelObject.setStrInspectionDate(carInventory.inspectiondate);
        addCarModelObject.setStrRegistrationDate(carInventory.registrationDate);
        addCarModelObject.setStrInsuranceDate(carInventory.insuranceDate);
        addCarModelObject.setStrCylinder(carInventory.cylinder);
        addCarModelObject.setStrGasTank(carInventory.gasTank);
        addCarModelObject.setStrVehicleType(carInventory.vehicleType);
        addCarModelObject.setStrLocationTitle(carInventory.hasLocation);
        addCarModelObject.setStrGpsInstall(carInventory.gpsInstalled);
        addCarModelObject.setStrHasTitle(carInventory.Title);
        addCarModelObject.setStrTitleLot(carInventory.lotCode);
        addCarModelObject.setIs_done(carInventory.is_done);
        addCarModelObject.setStrVacancy(carInventory.vacancy);
        Log.e("is done ", " =" + addCarModelObject.getIs_done());
        addCarModelObject.setStrCompanyInsurance(carInventory.company_insurance);
        for (int i = 0; i < carInventory.images.size(); i++) {
            addCarModelObject.arrayImagePath.add(String.valueOf(carInventory.images.get(i)));
        }
        Log.e("compnay Insurance", "" + carInventory.company_insurance);
        if (fragment) {
            showFragment(arryFragments.get(13));
        }
    }

    private void initarrayFragments() {

        arryFragments = new ArrayList<AddNewCarActivity.Fragments>();
        if (addCarModelObject.getStrVin() != null && addCarModelObject.getStrVin().length() > 0) {
            arryFragments.add(Fragments.Vin);
            arryFragments.add(Fragments.Rfid);
        } else {
            arryFragments.add(Fragments.Rfid);
            arryFragments.add(Fragments.Vin);
        }
        arryFragments.add(Fragments.Vacancy);
        arryFragments.add(Fragments.Status);
        arryFragments.add(Fragments.Stage);
        arryFragments.add(Fragments.Service);
        arryFragments.add(Fragments.Lotcode);
        arryFragments.add(Fragments.Miles);
        arryFragments.add(Fragments.Gps);
        arryFragments.add(Fragments.Addphoto);
        arryFragments.add(Fragments.AddTitle);
        arryFragments.add(Fragments.AllRemainingData);
        arryFragments.add(Fragments.AllDataOneData);
        arryFragments.add(Fragments.AllDetail);
    }

    private void setActionBar(String title) {
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(title);
    }

    @Override
    public void onNextSecected(Boolean isInfo, Fragments nextFragment) {

        if (!isInfo) {
            if (!addCarModelObject.isFrmInfo()) {
                int selectedFragPosition = arryFragments.indexOf(selectedFrag);

                if (selectedFragPosition == arryFragments.size() - 1) {

                } else {
                    showFragment(arryFragments.get(selectedFragPosition + 1));
                }
            } else {
                showFragment(Fragments.AllDetail);
            }
        } else {
            showFragment(nextFragment);
        }
    }

    private void goBack() {

        if (!addCarModelObject.isFrmInfo()) {
            if (selectedFrag != null) {
                int selectedFragPosition = arryFragments.indexOf(selectedFrag);

                if (selectedFragPosition == 0) {
                    finish();

                } else {
                    showFragment(arryFragments.get(selectedFragPosition - 1));
                }
            }
        } else {
            if (selectedFrag == Fragments.AllDetail) {
                OpenDialog();
            } else
                showFragment(Fragments.AllDetail);
        }
    }

    public void OpenDialog() {

        Builder builder = new Builder(AddNewCarActivity.this);
        builder.setTitle("Want To Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            goBack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void showFragment(Fragments fragmentType) {
        switch (fragmentType) {

            case Rfid:
                objFragment = FRAGRfid.newInstance(); // make Instant of
                selectedFrag = Fragments.Rfid;
                setActionBar("Add Rfid");
                break;

            case Vin:
                objFragment = FRAGVin.newInstance(); // make Instant of
                selectedFrag = Fragments.Vin;
                setActionBar("Add Vin");
                break;

            case Status:
                objFragment = FRAGStatus.newInstance(); // make Instant of
                selectedFrag = Fragments.Status;
                setActionBar("Add Status");
                break;

            case Stage:
                objFragment = FRAGStage.newInstance(); // make Instant of
                selectedFrag = Fragments.Stage;
                setActionBar("Add Stage");
                break;

            case Service:
                objFragment = FRAGService.newInstance(); // make Instant of
                selectedFrag = Fragments.Service;
                setActionBar("Add ServiceStage");
                break;

            case Lotcode:
                objFragment = FRAGLotcode.newInstance(); // make Instant of
                selectedFrag = Fragments.Lotcode;
                setActionBar("Add LotCode");
                break;

            case Miles:
                objFragment = FRAGMiles.newInstance(); // make Instant of
                selectedFrag = Fragments.Miles;
                setActionBar("Add Miles");
                break;

            case Gps:
                objFragment = FRAGGps.newInstance(); // make Instant of
                selectedFrag = Fragments.Gps;
                setActionBar("Gps");
                break;

            case AllRemainingData:
                objFragment = FRAGAllInOneInfo.newInstance(); // make Instant of
                selectedFrag = Fragments.AllRemainingData;
                setActionBar("AllRemainingData");
                break;

            case AllDataOneData:
                objFragment = FRAGDataOne.newInstance(); // make Instant of
                selectedFrag = Fragments.AllDataOneData;
                setActionBar("AllDataOneData");
                break;
            case AllDetail:
                objFragment = FRAGAllInfo.newInstance(); // make Instant of
                selectedFrag = Fragments.AllDetail;
                mActionBar.hide();
                break;
            case Addphoto:
                objFragment = FRAGAddphoto.newInstance(); // make Instant of
                selectedFrag = Fragments.Addphoto;
                setActionBar("Add Photo");
                break;
            case Vacancy:
                objFragment = FRAGVacancy.newInstance();
                selectedFrag = Fragments.Vacancy;
                setActionBar("Add Vacancy");
                break;
            case AddTitle:
                objFragment = FRAGTitle.newInstance();
                selectedFrag = Fragments.AddTitle;
                setActionBar("Add Title");
                break;
            default:
                break;
        }
        ft = ((FragmentActivity) activity).getSupportFragmentManager()
                .beginTransaction();

        ft.replace(container, objFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        addCarModelObject = null;
        isedit = false;
        fragment = false;
//        Common.clearstaticvalues();
        // AddNewCarActivity.addCarModelObject.companyInsuranceFile = null;
    }
}

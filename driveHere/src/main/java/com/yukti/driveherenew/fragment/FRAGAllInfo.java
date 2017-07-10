package com.yukti.driveherenew.fragment;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yukti.driveherenew.AddNewCarActivity;
import com.yukti.driveherenew.MainActivity;
import com.yukti.driveherenew.MessageDialogFragment;
import com.yukti.driveherenew.MultipartRequest;
import com.yukti.driveherenew.MyApplication;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.search.CarDetailsActivity;
import com.yukti.jsonparser.AddCarResponse;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;


public class FRAGAllInfo extends Fragment {

    View view_main;
    EditText edt_lotcode, edt_hastitle;
    CallbackAdd callbackAdd;
    TextView sendDeatilsServer;
//    RequestQueue queue;

    public static FRAGAllInfo newInstance() {
        FRAGAllInfo f = new FRAGAllInfo();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view_main = (inflater.inflate(R.layout.fragment_all_info, container,
                false));
        Toolbar toolbar = (Toolbar) view_main.findViewById(R.id.allDetailsToolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        TextView tvCatTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_name);
        sendDeatilsServer = (TextView) toolbar.findViewById(R.id.tvSend);
        if (AddNewCarActivity.isedit) {
            sendDeatilsServer.setText("Update");
        }
        tvCatTitle.setText("All Details");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        return view_main;
    }

    void init() {
//        queue = Volley.newRequestQueue(getActivity());
        logData();
        initrfid();
        initvin();
        initlotecode();
        initvehiclestatus();
        initstage();
        initservicestage();
        initstocknumber();
        initvehicleproblem();
        initvehiclenote();
        initvehiclecolor();
        initmiles();
        initmake();
        initmodel();
        initmodelnumber();
        initvehiclemodelyear();
        initvehiclemaxhp();
        initmaxtorque();
        initfueltype();
        initoilcapacity();
        initdrivetype();
        initcompany();
        initsalesprice();
        initpurchaseform();
        initinspectiondate();
        initregistrationdate();
        initinsurancedate();
        initcylinder();
        initgastank();
        initvehicletype();
        inithastitle();
        initlocationtitle();
        initgpsinstall();
        initcompanyinsurance();
        initgpslayout();
        initmechanic();
        initsenddata();
        initphotos();
        initVacancy();
        initTitle();

//        AddNewCarActivity.isFrmInfo = true;
        AddNewCarActivity.addCarModelObject.setFrmInfo(true);
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

    void initrfid() {
        EditText edt_rfid = (EditText) view_main
                .findViewById(R.id.edt_vehiclerfid);
        if (AddNewCarActivity.addCarModelObject.getStrRfid() != null
                && AddNewCarActivity.addCarModelObject.getStrRfid().length() != 0) {
            edt_rfid.setText(AddNewCarActivity.addCarModelObject.getStrRfid());
        }
        edt_rfid.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("RFID", 1);
            }
        });
    }

    void initvin() {
        EditText edt_vin = (EditText) view_main
                .findViewById(R.id.edt_vehiclevin);
        if (AddNewCarActivity.addCarModelObject.getStrVin() != null
                && AddNewCarActivity.addCarModelObject.getStrVin().length() != 0) {
            edt_vin.setText(AddNewCarActivity.addCarModelObject.getStrVin());
        }
        edt_vin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

//                OpenDialog("VIN", 2);

            }
        });
    }

    void initlotecode() {
        EditText edt_lotecode = (EditText) view_main
                .findViewById(R.id.edt_vehiclelotcode);
        if (AddNewCarActivity.addCarModelObject.getStrLotCode() != null
                && AddNewCarActivity.addCarModelObject.getStrLotCode().length() != 0) {
            edt_lotecode.setText(AddNewCarActivity.addCarModelObject.getStrLotCode());
        }
        edt_lotecode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("LOTCODE", 3);
            }
        });
    }

    void initVacancy() {
        EditText edt_vacancy = (EditText) view_main
                .findViewById(R.id.edt_VacacyValue);
        if (AddNewCarActivity.addCarModelObject.getStrVacancy() != null
                && AddNewCarActivity.addCarModelObject.getStrVacancy().length() != 0) {
            edt_vacancy.setText(AddNewCarActivity.addCarModelObject.getStrVacancy());
        }
        edt_vacancy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("VACANCY", 36);
            }
        });
    }

    void initvehiclestatus() {
        EditText edt_vehiclestatus = (EditText) view_main
                .findViewById(R.id.edt_vehiclestatus);
        if (AddNewCarActivity.addCarModelObject.getStrStatus() != null
                && AddNewCarActivity.addCarModelObject.getStrStatus().length() != 0) {
            edt_vehiclestatus.setText(AddNewCarActivity.addCarModelObject.getStrStatus());
        }
        edt_vehiclestatus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("STATUS", 4);
            }
        });
    }

    void initstage() {
        EditText edt_stage = (EditText) view_main
                .findViewById(R.id.edt_vehicle_stage);
        if (AddNewCarActivity.addCarModelObject.getStrStage() != null
                && AddNewCarActivity.addCarModelObject.getStrStage().length() != 0) {
            edt_stage.setText(AddNewCarActivity.addCarModelObject.getStrStage());
        }
        edt_stage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("STAGE", 5);
            }
        });
    }

    void initservicestage() {
        EditText edt_ServiceStage = (EditText) view_main
                .findViewById(R.id.edt_service_stage);
        if (AddNewCarActivity.addCarModelObject.getStrServiceStage() != null
                && AddNewCarActivity.addCarModelObject.getStrServiceStage().length() != 0) {
            edt_ServiceStage.setText(AddNewCarActivity.addCarModelObject.getStrServiceStage());
        }
        edt_ServiceStage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("SERVICE STAGE", 38);
            }
        });
    }

    void initstocknumber() {
        EditText edt_stocknumber = (EditText) view_main
                .findViewById(R.id.edt_vehiclestocknumber);
        if (AddNewCarActivity.addCarModelObject.getStrStockNumber() != null
                && AddNewCarActivity.addCarModelObject.getStrStockNumber().length() != 0) {
            edt_stocknumber.setText(AddNewCarActivity.addCarModelObject.getStrStockNumber());
        }
        edt_stocknumber.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("STOCKNUMBER", 6);
            }
        });
    }

    void initvehicleproblem() {
        EditText edt_problem = (EditText) view_main
                .findViewById(R.id.edt_vehicleproblem);
        if (AddNewCarActivity.addCarModelObject.getStrVehicleProblem() != null
                && AddNewCarActivity.addCarModelObject.getStrVehicleProblem().length() != 0) {
            edt_problem.setText(AddNewCarActivity.addCarModelObject.getStrVehicleProblem());
        }
        edt_problem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("VEHICLE PROBLEM", 7);
            }
        });
    }

    void initvehiclenote() {
        EditText edt_note = (EditText) view_main
                .findViewById(R.id.edt_vehiclenote);
        if (AddNewCarActivity.addCarModelObject.getStrVehicleNote() != null
                && AddNewCarActivity.addCarModelObject.getStrVehicleNote().length() != 0) {
            edt_note.setText(AddNewCarActivity.addCarModelObject.getStrVehicleNote());
        }
        edt_note.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("NOTE", 8);
            }
        });
    }

    void initvehiclecolor() {
        EditText edt_vehiclecolor = (EditText) view_main
                .findViewById(R.id.edt_vehicleColor);
        if (AddNewCarActivity.addCarModelObject.getStrColor() != null
                && AddNewCarActivity.addCarModelObject.getStrColor().length() != 0) {
            edt_vehiclecolor.setText(AddNewCarActivity.addCarModelObject.getStrColor());
        }
        edt_vehiclecolor.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("COLOR", 9);
            }
        });
    }

    void initmiles() {
        EditText edt_miles = (EditText) view_main
                .findViewById(R.id.etd_vehiclemiles);
        if (AddNewCarActivity.addCarModelObject.getStrMiles() != null
                && AddNewCarActivity.addCarModelObject.getStrMiles().length() != 0) {
            edt_miles.setText(AddNewCarActivity.addCarModelObject.getStrMiles());
        }
        edt_miles.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialog("MILES", 10);
            }
        });
    }

    void initmake() {
        EditText edt_make = (EditText) view_main
                .findViewById(R.id.edt_vehicle_make);
        if (AddNewCarActivity.addCarModelObject.getStrMake() != null
                && AddNewCarActivity.addCarModelObject.getStrMake().length() != 0) {
            edt_make.setText(AddNewCarActivity.addCarModelObject.getStrMake());
        }
        edt_make.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("MAKE", 11);
            }
        });
    }

    void initmodel() {
        EditText edt_model = (EditText) view_main
                .findViewById(R.id.edt_vehicle_model);
        if (AddNewCarActivity.addCarModelObject.getStrModel() != null
                && AddNewCarActivity.addCarModelObject.getStrModel().length() != 0) {
            edt_model.setText(AddNewCarActivity.addCarModelObject.getStrModel());
        }
        edt_model.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("MODEL", 12);
            }
        });
    }

    void initmodelnumber() {
        EditText edt_modelnumber = (EditText) view_main
                .findViewById(R.id.edt_vehicle_modelnumber);
        if (AddNewCarActivity.addCarModelObject.getStrModelNumber() != null
                && AddNewCarActivity.addCarModelObject.getStrModelNumber().length() != 0) {
            edt_modelnumber.setText(AddNewCarActivity.addCarModelObject.getStrModelNumber());
        }
        edt_modelnumber.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("MODEL NUMBER", 13);
            }
        });
    }

    void initvehiclemodelyear() {
        EditText edt_modelyear = (EditText) view_main
                .findViewById(R.id.edt_vehicle_modelyear);
        if (AddNewCarActivity.addCarModelObject.getStrModelYear() != null
                && AddNewCarActivity.addCarModelObject.getStrModelYear().length() != 0) {
            edt_modelyear.setText(AddNewCarActivity.addCarModelObject.getStrModelYear());
        }
        edt_modelyear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("MODEL YEAR", 14);
            }
        });
    }

    void initvehiclemaxhp() {
        EditText edt_maxhp = (EditText) view_main
                .findViewById(R.id.edt_vehicle_maxhp);
        if (AddNewCarActivity.addCarModelObject.getStrMaxHp() != null
                && AddNewCarActivity.addCarModelObject.getStrMaxHp().length() != 0) {
            edt_maxhp.setText(AddNewCarActivity.addCarModelObject.getStrMaxHp());
        }
        edt_maxhp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("MAX HP", 15);
            }
        });
    }

    void initmaxtorque() {
        EditText edt_maxtorque = (EditText) view_main
                .findViewById(R.id.edt_vehiclemaxtorque);
        if (AddNewCarActivity.addCarModelObject.getStrMaxTorque() != null
                && AddNewCarActivity.addCarModelObject.getStrMaxTorque().length() != 0) {
            edt_maxtorque.setText(AddNewCarActivity.addCarModelObject.getStrMaxTorque());
        }
        edt_maxtorque.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("MAX TORQUE", 16);
            }
        });
    }

    void initfueltype() {
        EditText edt_fueltype = (EditText) view_main
                .findViewById(R.id.edt_vehiclefuel_type);
        if (AddNewCarActivity.addCarModelObject.getStrFuelType() != null
                && AddNewCarActivity.addCarModelObject.getStrFuelType().length() != 0) {
            edt_fueltype.setText(AddNewCarActivity.addCarModelObject.getStrFuelType());
        }
        edt_fueltype.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("FUEL TYPE", 17);
            }
        });
    }

    void initoilcapacity() {
        EditText edt_oilcapacity = (EditText) view_main
                .findViewById(R.id.edt_vehicleoil_capacity);
        if (AddNewCarActivity.addCarModelObject.getStrOilCapacity() != null
                && AddNewCarActivity.addCarModelObject.getStrOilCapacity().length() != 0) {
            edt_oilcapacity.setText(AddNewCarActivity.addCarModelObject.getStrOilCapacity());
        }
        edt_oilcapacity.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("OIL CAPACITY", 18);
            }
        });
    }

    void initdrivetype() {
        EditText edt_drivetype = (EditText) view_main
                .findViewById(R.id.edt_vehicledrive_type);
        if (AddNewCarActivity.addCarModelObject.getStrDriveType() != null
                && AddNewCarActivity.addCarModelObject.getStrDriveType().length() != 0) {
            edt_drivetype.setText(AddNewCarActivity.addCarModelObject.getStrDriveType());
        }
        edt_drivetype.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                OpenDialog("DRIVE TYPE", 19);
            }
        });
    }

    void initcompany() {
        EditText edt_company = (EditText) view_main
                .findViewById(R.id.edt_vehicleCompany);
        if (AddNewCarActivity.addCarModelObject.getStrCompany() != null
                && AddNewCarActivity.addCarModelObject.getStrCompany().length() != 0) {
            edt_company.setText(AddNewCarActivity.addCarModelObject.getStrCompany());
        }
        edt_company.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("COMPANY", 20);
            }
        });
    }

    void initsalesprice() {
        EditText edt_salesprice = (EditText) view_main
                .findViewById(R.id.edt_vehicleSalesPrice);
        if (AddNewCarActivity.addCarModelObject.getStrSalesPrice() != null
                && AddNewCarActivity.addCarModelObject.getStrSalesPrice().length() != 0) {
            edt_salesprice.setText(AddNewCarActivity.addCarModelObject.getStrSalesPrice());
        }
        edt_salesprice.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("SALES PRICE", 21);
            }
        });
    }

    void initpurchaseform() {
        EditText edt_purchaseform = (EditText) view_main
                .findViewById(R.id.edt_vehicle_Purchasedform);
        if (AddNewCarActivity.addCarModelObject.getStrPurchaseForm() != null
                && AddNewCarActivity.addCarModelObject.getStrPurchaseForm().length() != 0) {
            edt_purchaseform.setText(AddNewCarActivity.addCarModelObject.getStrPurchaseForm());
        }
        edt_purchaseform.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("PURCHASE FORM", 22);
            }
        });
    }

    void initinspectiondate() {
        EditText edt_inspectiondate = (EditText) view_main
                .findViewById(R.id.edt_vehicleinspection_date);
        if (AddNewCarActivity.addCarModelObject.getStrInspectionDate() != null
                && AddNewCarActivity.addCarModelObject.getStrInspectionDate().length() != 0) {
            edt_inspectiondate.setText(AddNewCarActivity.addCarModelObject.getStrInspectionDate());
        }
        edt_inspectiondate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                OpenDialog("INSPECTION DATE", 23);
            }
        });
    }

    void initregistrationdate() {
        EditText edt_registrationdate = (EditText) view_main
                .findViewById(R.id.edt_vehicleRegistrationDate);
        if (AddNewCarActivity.addCarModelObject.getStrRegistrationDate() != null
                && AddNewCarActivity.addCarModelObject.getStrRegistrationDate().length() != 0) {
            edt_registrationdate.setText(AddNewCarActivity.addCarModelObject.getStrRegistrationDate());
        }
        edt_registrationdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("REGISTRATION DATE", 24);
            }
        });
    }

    void initinsurancedate() {
        EditText edt_insurancedate = (EditText) view_main
                .findViewById(R.id.edt_vehicle_insurancedate);
        if (AddNewCarActivity.addCarModelObject.getStrInsuranceDate() != null
                && AddNewCarActivity.addCarModelObject.getStrInsuranceDate().length() != 0) {
            edt_insurancedate.setText(AddNewCarActivity.addCarModelObject.getStrInsuranceDate());
        }
        edt_insurancedate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("INSURANCE DATE", 25);
            }
        });
    }

    void initcylinder() {
        EditText edt_cylinder = (EditText) view_main
                .findViewById(R.id.edt_vehicle_cyclinder);
        if (AddNewCarActivity.addCarModelObject.getStrCylinder() != null
                && AddNewCarActivity.addCarModelObject.getStrCylinder().length() != 0) {
            edt_cylinder.setText(AddNewCarActivity.addCarModelObject.getStrCylinder());
        }
        edt_cylinder.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("CYLINDER", 26);
            }
        });
    }

    void initgastank() {
        EditText edt_gastank = (EditText) view_main
                .findViewById(R.id.edt_vehicle_gastank);
        if (AddNewCarActivity.addCarModelObject.getStrGasTank() != null
                && AddNewCarActivity.addCarModelObject.getStrGasTank().length() != 0) {
            edt_gastank.setText(AddNewCarActivity.addCarModelObject.getStrGasTank());
        }
        edt_gastank.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                OpenDialog("GAS TANK", 27);
            }
        });
    }

    void initvehicletype() {
        EditText edt_vehicletype = (EditText) view_main
                .findViewById(R.id.edt_Vehicle_Type);
        if (AddNewCarActivity.addCarModelObject.getStrVehicleType() != null
                && AddNewCarActivity.addCarModelObject.getStrVehicleType().length() != 0) {
            edt_vehicletype.setText(AddNewCarActivity.addCarModelObject.getStrVehicleType());
        }
        edt_vehicletype.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                OpenDialog("VEHICLE TYPE", 28);
            }
        });
    }

    void inithastitle() {

        edt_hastitle = (EditText) view_main.findViewById(R.id.edt_vehicle_HasTitle);
        edt_lotcode = (EditText) view_main.findViewById(R.id.edt_lotcode_HasTitle);

        if (AddNewCarActivity.addCarModelObject.getStrHasTitle() != null && AddNewCarActivity.addCarModelObject.getStrHasTitle().length() != 0) {
            edt_hastitle.setText(AddNewCarActivity.addCarModelObject.getStrHasTitle());

            if (AddNewCarActivity.addCarModelObject.getStrHasTitle().trim().equalsIgnoreCase("Yes")) {
                if (AddNewCarActivity.addCarModelObject.getStrTitleLot() != null
                        && AddNewCarActivity.addCarModelObject.getStrTitleLot().length() != 0) {
                    Log.e("lotcode", " " + AddNewCarActivity.addCarModelObject.getStrTitleLot());

                    edt_lotcode.setText(AddNewCarActivity.addCarModelObject.getStrTitleLot().toString());

                }
            }

            if (AddNewCarActivity.addCarModelObject.getStrHasTitle().trim().equalsIgnoreCase("No")) {
                edt_lotcode.setText("");
            }
        }

        edt_hastitle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("HAS TITLE", 37);
            }
        });
        edt_lotcode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialog("TITLE Lotcode", 37);
            }
        });
    }

    void initlocationtitle() {
        EditText edt_locationtitle = (EditText) view_main.findViewById(R.id.edt_vehicel_Locationtitle);
        if (AddNewCarActivity.addCarModelObject.getStrLocationTitle() != null
                && AddNewCarActivity.addCarModelObject.getStrLocationTitle().length() != 0) {
            edt_locationtitle.setText(AddNewCarActivity.addCarModelObject.getStrLocationTitle());
        }
        edt_locationtitle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("LOCATION TITLE", 30);
            }
        });
    }

    void initgpsinstall() {
        EditText edt_gpsinstall = (EditText) view_main.findViewById(R.id.edt_vehiclegps_installed);
        if (AddNewCarActivity.addCarModelObject.getStrGpsInstall() != null
                && AddNewCarActivity.addCarModelObject.getStrGpsInstall().length() != 0) {
            edt_gpsinstall.setText(AddNewCarActivity.addCarModelObject.getStrGpsInstall());
        }
        edt_gpsinstall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("GPS INSTALL", 31);
            }
        });
    }

    public void loadImageLoader(String path, ImageView imageView) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_default_car)
                .showImageForEmptyUri(R.drawable.ic_default_car)
                .showImageOnFail(R.drawable.ic_default_car)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        imageLoader.getInstance().displayImage(path, imageView, options);
    }

    void initcompanyinsurance() {
        ImageView img_cmpnyinsurance = (ImageView) view_main
                .findViewById(R.id.iv_comp_insurance);

        Button btnins_photo = (Button) view_main.findViewById(R.id.btnins_photo);
        if (AddNewCarActivity.addCarModelObject.companyInsuranceFile != null) {
            img_cmpnyinsurance.setVisibility(View.VISIBLE);
            btnins_photo.setVisibility(View.GONE);
            Bitmap bitmap = Common.decodeSampledBitmapFromPath(
                    AddNewCarActivity.addCarModelObject.companyInsuranceFile.getPath(),
                    AddNewCarActivity.imageViewWidth,
                    AddNewCarActivity.imageViewHeight);
            if (bitmap != null) {
                img_cmpnyinsurance.setImageBitmap(bitmap);
                bitmap = null;
            }
            Log.e("Not null", "not null" + AddNewCarActivity.addCarModelObject.companyInsuranceFile.getPath());
        } else if (!AddNewCarActivity.addCarModelObject.getStrCompanyInsurance().equalsIgnoreCase("") && AddNewCarActivity.addCarModelObject.getStrCompanyInsurance().contains("http:")) {
            btnins_photo.setVisibility(View.GONE);
            img_cmpnyinsurance.setVisibility(View.VISIBLE);
            loadImageLoader(AddNewCarActivity.addCarModelObject.getStrCompanyInsurance(), img_cmpnyinsurance);
        } else
            Log.e("Null", "null");
        btnins_photo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("COMPANY INSURANCE", 32);
            }
        });
        img_cmpnyinsurance.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialog("COMPANY INSURANCE", 32);
            }
        });
    }

    void initgpslayout() {

        Log.e("Add Detail Gps Size ",
                "" + AddNewCarActivity.arrayListGpsSerial.size());
        LinearLayout linearDynamic = (LinearLayout) view_main
                .findViewById(R.id.gpslayout);

        for (int i = 0; i < AddNewCarActivity.arrayListGpsSerial.size(); i++) {
            EditText ed = new EditText(getActivity());
            ed.setBackgroundResource(R.drawable.borderedittext);
            ed.setText(AddNewCarActivity.arrayListGpsSerial.get(i));
            ed.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            linearDynamic.addView(ed);
            ed.setFocusable(false);
            ed.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    OpenDialog("AllGPS", 31);
                }
            });
        }
    }

    void initmechanic() {

        EditText edt_mechanic = (EditText) view_main
                .findViewById(R.id.edt_mechanic);
        if (AddNewCarActivity.addCarModelObject.getStrMechanic() != null
                && AddNewCarActivity.addCarModelObject.getStrMechanic().length() != 0) {
            edt_mechanic.setText(AddNewCarActivity.addCarModelObject.getStrMechanic());
        }
        edt_mechanic.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("Mechanic", 34);
            }
        });
    }

    void initphotos() {

        // for (int i = 0; i < AddNewCarActivity.arrayImagePath.size(); i++) {
        // setImage(AddNewCarActivity.arrayImagePath.get(i));
        // }
        setImage();
    }

    public void setImage() {
//        LinearLayout linearPhotocontainer = (LinearLayout) view_main.findViewById(R.carId.container_photos);
        Button btnAddphoto = (Button) view_main.findViewById(R.id.btn_photoAdd);
        if (AddNewCarActivity.addCarModelObject.arrayImagePath.size() > 0) {
            btnAddphoto.setText("Show Photos (" + AddNewCarActivity.addCarModelObject.arrayImagePath.size() + ")");
        } else {
            btnAddphoto.setText("+ Add Photo");
        }
        btnAddphoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialog("PHOTOS", 35);
            }
        });
       /* ImageView img_photo1 = new ImageView(getActivity());
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(150, 150);
        lp1.setMargins(0, 10, 0, 0);

        img_photo1.setLayoutParams(lp1);
        img_photo1.setImageDrawable(getResources().getDrawable(R.drawable.fragimg));
        linearPhotocontainer.addView(img_photo1);


        Log.e("Array size ", AddNewCarActivity.addCarModelObject.arrayImagePath.size() + "");

        for (int i = 0; i < AddNewCarActivity.addCarModelObject.arrayImagePath.size(); i++) {

            if (AddNewCarActivity.addCarModelObject.arrayImagePath.get(i) != null
                    && AddNewCarActivity.addCarModelObject.arrayImagePath.get(i).length() > 0) {

                // File img = new File(path);
                ImageView img_photo = new ImageView(getActivity());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 10, 0, 0);

                img_photo.setLayoutParams(lp);

                Log.e("path" + i, AddNewCarActivity.addCarModelObject.arrayImagePath.get(i));
                String path;
                if (AddNewCarActivity.addCarModelObject.arrayImagePath.get(i).contains("file://")) {
                    path = AddNewCarActivity.addCarModelObject.arrayImagePath.get(i).replace("file://", "");
                } else
                    path = AddNewCarActivity.addCarModelObject.arrayImagePath.get(i);

                Bitmap bitmap = Common.decodeSampledBitmapFromPath(
                        path,
                        AddNewCarActivity.imageViewWidth,
                        AddNewCarActivity.imageViewHeight);
                if (bitmap != null) {
                    img_photo.setImageBitmap(bitmap);
                    img_photo.setTag(AddNewCarActivity.addCarModelObject.arrayImagePath.get(i));
                    bitmap = null;

                    linearPhotocontainer.addView(img_photo);
                } else {

                }
            }
        }
        linearPhotocontainer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("PHOTOS", 35);
            }
        });*/
    }

    void initTitle() {

        setImageTitle();

    }

    public void setImageTitle() {
//        LinearLayout linearPhotocontainer2 = (LinearLayout) view_main.findViewById(R.carId.container_photos2);
        Button addPhoto = (Button) view_main.findViewById(R.id.btnadd_photo);
        if (AddNewCarActivity.addCarModelObject.getStrHasTitle().equalsIgnoreCase("Yes")) {
            if (AddNewCarActivity.addCarModelObject.arrayTitleImagePath.size() > 0) {
                addPhoto.setText("Show Photos (" + AddNewCarActivity.addCarModelObject.arrayTitleImagePath.size() + ")");
            } else {
                addPhoto.setText("+ Add Photo");
            }
            addPhoto.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenDialog("TITLE PICTURE", 37);
                }
            });
        } else {
            addPhoto.setVisibility(View.GONE);
        }
        /*ImageView img_photo1 = new ImageView(getActivity());
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(150, 150);
        lp1.setMargins(0, 10, 0, 0);

        img_photo1.setLayoutParams(lp1);
        img_photo1.setImageDrawable(getResources().getDrawable(R.drawable.fragimg));
        linearPhotocontainer2.addView(img_photo1);

        Log.e("Array size Title", AddNewCarActivity.addCarModelObject.arrayTitleImagePath.size() + "");

        if (AddNewCarActivity.addCarModelObject.getStrHasTitle().trim().equalsIgnoreCase("No")) {
            if (linearPhotocontainer2.getChildCount() > 0) {
                linearPhotocontainer2.removeAllViews();
            }
        } else {
            if (AddNewCarActivity.addCarModelObject.arrayTitleImagePath != null && AddNewCarActivity.addCarModelObject.arrayTitleImagePath.size() > 0) {
                for (int i = 0; i < AddNewCarActivity.addCarModelObject.arrayTitleImagePath.size(); i++) {

                    if (AddNewCarActivity.addCarModelObject.arrayTitleImagePath.get(i) != null
                            && AddNewCarActivity.addCarModelObject.arrayTitleImagePath.get(i).length() > 0) {

                        // File img = new File(path);
                        ImageView img_photo = new ImageView(getActivity());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(150, 150);
                        lp.setMargins(0, 10, 0, 0);

                        img_photo.setLayoutParams(lp);

                        Log.e("path" + i, AddNewCarActivity.addCarModelObject.arrayTitleImagePath.get(i));
                        String path;
                        if (AddNewCarActivity.addCarModelObject.arrayTitleImagePath.get(i).contains("file://")) {
                            path = AddNewCarActivity.addCarModelObject.arrayTitleImagePath.get(i).replace("file://", "");
                        } else
                            path = AddNewCarActivity.addCarModelObject.arrayTitleImagePath.get(i);

                        Bitmap bitmap = Common.decodeSampledBitmapFromPath(
                                path,
                                AddNewCarActivity.imageViewWidth,
                                AddNewCarActivity.imageViewHeight);
                        if (bitmap != null) {
                            img_photo.setImageBitmap(bitmap);
                            img_photo.setTag(AddNewCarActivity.addCarModelObject.arrayTitleImagePath.get(i));
                            bitmap = null;

                            linearPhotocontainer2.addView(img_photo);
                        } else {

                        }
                    }
                }
            } else {
            }
        }


        linearPhotocontainer2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OpenDialog("TITLE PICTURE", 37);
            }
        });*/
    }

    public void logData() {

      /*  Log.e("rfid", AddNewCarActivity.addCarModelObject.getStrRfid());
        Log.e("vin", "AddNewCarActivity.preVin : " + AddNewCarActivity.addCarModelObject.getPreVin());

        Log.e("lotecode", AddNewCarActivity.addCarModelObject.getStrLotCode());
        Log.e("status", AddNewCarActivity.addCarModelObject.getStrStatus());
        Log.e("stage", AddNewCarActivity.addCarModelObject.getStrStage());
        Log.e("stock number", AddNewCarActivity.addCarModelObject.getStrStockNumber());

        Log.e("problem", AddNewCarActivity.addCarModelObject.getStrVehicleProblem());
        Log.e("Note", AddNewCarActivity.addCarModelObject.getStrVehicleNote());

        Log.e("company", AddNewCarActivity.addCarModelObject.getStrCompany());
        Log.e("sales KEY_price", AddNewCarActivity.addCarModelObject.getStrSalesPrice());
        Log.e("purchase form", AddNewCarActivity.addCarModelObject.getStrPurchaseForm());

        Log.e("inspection date", AddNewCarActivity.addCarModelObject.getStrInspectionDate());
        Log.e("registration date", AddNewCarActivity.addCarModelObject.getStrRegistrationDate());
        Log.e("insurance date", AddNewCarActivity.addCarModelObject.getStrInsuranceDate());
        Log.e("Gas Tank", AddNewCarActivity.addCarModelObject.getStrGasTank());
        Log.e("Has Title", AddNewCarActivity.addCarModelObject.getStrHasTitle());
        Log.e("location title", AddNewCarActivity.addCarModelObject.getStrLocationTitle());
        Log.e("GPS Installed", AddNewCarActivity.addCarModelObject.getStrGpsInstall());
        Log.e("Mechanics", AddNewCarActivity.addCarModelObject.getStrMechanic());
        Log.e("Vacancy", AddNewCarActivity.addCarModelObject.getStrVacancy());
*/
        if (AddNewCarActivity.addCarModelObject.companyInsuranceFile != null) {
            Log.e("Company Insurance", "Size ="
                    + AddNewCarActivity.addCarModelObject.companyInsuranceFile.length() + "");
            Log.e("Company Insurance path", "path ="
                    + AddNewCarActivity.addCarModelObject.companyInsuranceFile.getPath() + "");
        } else {
            Log.e("Company Insurance", "Not get file");
        }

        /*Log.e("make", "AddNewCarActivity.strMake : "
                + AddNewCarActivity.addCarModelObject.getStrMake());

        Log.e("model", "AddNewCarActivity.strModel : "
                + AddNewCarActivity.addCarModelObject.getStrModel());
        Log.e("model number", "AddNewCarActivity.strModelNumber : "
                + AddNewCarActivity.addCarModelObject.getStrModelNumber());
        Log.e("model year", "AddNewCarActivity.strModelYear : "
                + AddNewCarActivity.addCarModelObject.getStrModelYear());
        Log.e("vehicle type", "AddNewCarActivity.strVehicleType : "
                + AddNewCarActivity.addCarModelObject.getStrVehicleType());
        Log.e("drive type", "AddNewCarActivity.strDriveType : "
                + AddNewCarActivity.addCarModelObject.getStrDriveType());

        Log.e("max hp", "AddNewCarActivity.strMaxHp : "
                + AddNewCarActivity.addCarModelObject.getStrMaxHp());
        Log.e("max torque", "AddNewCarActivity.strMaxTorque : "
                + AddNewCarActivity.addCarModelObject.getStrMaxTorque());
        Log.e("cylinder", "AddNewCarActivity.strCylinder : "
                + AddNewCarActivity.addCarModelObject.getStrCylinder());
        Log.e("oil capacity", "AddNewCarActivity.strOilCapacity : "
                + AddNewCarActivity.addCarModelObject.getStrOilCapacity());
        Log.e("fuel type", "AddNewCarActivity.strFuelType : "
                + AddNewCarActivity.addCarModelObject.getStrFuelType());
        Log.e("color", "AddNewCarActivity.strColor : "
                + AddNewCarActivity.addCarModelObject.getStrColor());

        Log.e("colorcode", AddNewCarActivity.addCarModelObject.getStrcolorcode());
        Log.e("miles", "AddNewCarActivity.strMiles : "
                + AddNewCarActivity.addCarModelObject.getStrMiles());*/
    }

    public void OpenDialogStage() {

        Builder builder = new Builder(getActivity());
        builder.setMessage("car is not done in previous stage, Do you want to done previous stage and change car stage to new stage?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callbackAdd.onNextSecected(true, AddNewCarActivity.Fragments.Stage);
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

    public void OpenDialog(String msg, final int fragnum) {

        Builder builder = new Builder(getActivity());
        builder.setTitle("Edit");
        builder.setMessage("Want To Edit " + msg);

        builder.setPositiveButton("Edit",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (fragnum == 1) {
                            callbackAdd.onNextSecected(true, AddNewCarActivity.Fragments.Rfid);
                        } else if (fragnum == 2) {
                            callbackAdd.onNextSecected(true, AddNewCarActivity.Fragments.Vin);
                        } else if (fragnum == 3) {
                            callbackAdd.onNextSecected(true, AddNewCarActivity.Fragments.Lotcode);
                        } else if (fragnum == 4) {
                            callbackAdd.onNextSecected(true, AddNewCarActivity.Fragments.Status);
                        } else if (fragnum == 5) {
                            if (AddNewCarActivity.isedit && AddNewCarActivity.addCarModelObject.getIs_done().equalsIgnoreCase("0")) {
                                OpenDialogStage();
                            } else {
                                callbackAdd.onNextSecected(true, AddNewCarActivity.Fragments.Stage);
                            }
                        } else if (fragnum == 38) {
                            callbackAdd.onNextSecected(true, AddNewCarActivity.Fragments.Service);
                        } else if (fragnum == 6) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllRemainingData);
                        } else if (fragnum == 7) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllRemainingData);
                        } else if (fragnum == 8) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllRemainingData);
                        } else if (fragnum == 9) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllDataOneData);
                        } else if (fragnum == 10) {
                            callbackAdd.onNextSecected(true, AddNewCarActivity.Fragments.Miles);
                        } else if (fragnum == 11) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllDataOneData);
                        } else if (fragnum == 12) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllDataOneData);
                        } else if (fragnum == 13) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllDataOneData);
                        } else if (fragnum == 14) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllDataOneData);
                        } else if (fragnum == 15) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllDataOneData);
                        } else if (fragnum == 16) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllDataOneData);
                        } else if (fragnum == 17) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllDataOneData);
                        } else if (fragnum == 18) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllDataOneData);
                        } else if (fragnum == 19) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllDataOneData);
                        } else if (fragnum == 20) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllRemainingData);
                        } else if (fragnum == 21) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllRemainingData);
                        } else if (fragnum == 22) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllRemainingData);
                        } else if (fragnum == 23) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllRemainingData);
                        } else if (fragnum == 24) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllRemainingData);
                        } else if (fragnum == 25) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllRemainingData);
                        } else if (fragnum == 26) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllDataOneData);
                        } else if (fragnum == 27) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllRemainingData);
                        } else if (fragnum == 28) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllDataOneData);
                        } else if (fragnum == 29) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllRemainingData);
                        } else if (fragnum == 30) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllRemainingData);
                        } else if (fragnum == 31) {
                            callbackAdd.onNextSecected(true, AddNewCarActivity.Fragments.Gps);
                        } else if (fragnum == 32) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllRemainingData);
                        } else if (fragnum == 33) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllRemainingData);
                        } else if (fragnum == 34) {
                            callbackAdd.onNextSecected(true,
                                    AddNewCarActivity.Fragments.AllRemainingData);
                        } else if (fragnum == 35) {
                            callbackAdd
                                    .onNextSecected(true, AddNewCarActivity.Fragments.Addphoto);
                        } else if (fragnum == 36) {
                            callbackAdd
                                    .onNextSecected(true, AddNewCarActivity.Fragments.Vacancy);
                        } else if (fragnum == 37) {
                         /*   Intent intent = new Intent(getActivity(),TitleActivity.class);
                            Boolean isCheck= false;
                            intent.putExtra("isFrmDetail",isCheck);
                            Log.e("All arrayTitleImagePath", AddNewCarActivity.arrayTitleImagePath.size() + "");
                            startActivity(intent);*/
                            callbackAdd
                                    .onNextSecected(true, AddNewCarActivity.Fragments.AddTitle);
                        }
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

    public String getGpsJsonString() {

        JSONObject object = new JSONObject();

        JSONArray jArray = new JSONArray();

        try {
            for (int i = 0; i < AddNewCarActivity.arrayListGpsSerial.size(); i++) {
                JSONObject Obj = new JSONObject();
                Obj.put("carId", "-1");
                Obj.put("gpsid", AddNewCarActivity.arrayListGpsSerial.get(i));
                jArray.put(Obj);
            }
            object.put("Gps", jArray);
        } catch (Exception e) {
            // Log.e("Error in getGpsJsonString() :" + e.toString());
        }
        Log.e("gps json", object.toString());
        return object.toString();
    }

    void initsenddata() {
        Button btn_send = (Button) view_main
                .findViewById(R.id.btn_SendDataToServer);
        sendDeatilsServer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Common.isNetworkConnected(getActivity())) {
                    // senddata();
                    OpenDialog();
                } else {
                    Toast.makeText(getActivity(), "Please Connect To Internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void SendDataUsingVolley() {
        Log.e("SendDataUsingVolley....", "SendDataUsingVolley");
        final String TAG_PUSH_RESULT = "TAG_PUSH_RESULT";
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.addcarlayout);
        TextView title = (TextView) dialog.findViewById(R.id.tv_addcar);
        if (AddNewCarActivity.isedit) {
            title.setText("Please Wait Car is Updating...");
        } else {
            title.setText("Please Wait Car is Adding...");
        }
        dialog.setCancelable(false);
        dialog.setTitle("Please Wait");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        String url = AppSingleTon.APP_URL.URL_ADD_NEW_CAR_DEAILS;
        Log.e("url ", "= " + url);
        MultipartRequest multipartRequest = new MultipartRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.e("response....", response);
                try {
                    AddCarResponse addCarResponse = AppSingleTon.APP_JSON_PARSER.addCarResponse(response);
                    Log.e("status code", addCarResponse.status_code);
                    if (addCarResponse.status_code.equals("1")) {
                        Log.e("Success...", "Car Addedd successfully..");

                        AddNewCarActivity.addCarModelObject.companyInsuranceFile = null;
                        AddNewCarActivity.addCarModelObject.arrayImagePath = null;
                        Toast.makeText(getActivity(), addCarResponse.message,
                                Toast.LENGTH_SHORT).show();
                        MainActivity.isneeded = true;
                        if (AddNewCarActivity.isedit) {
                            CarDetailsActivity.isNeeded = true;
                        }
                        getActivity().finish();
                    } else if (addCarResponse.status_code.equals("2")) {
                        Log.e("Failure", "fail to add car");
                        MessageDialogFragment fragment = new MessageDialogFragment(
                                "Info", addCarResponse.message, true, "Ok", false,
                                "", false, "", null);
                        fragment.show(getActivity().getSupportFragmentManager(), "");
                    } else if (addCarResponse.status_code.equals("3")) {
                        Toast.makeText(getActivity(), addCarResponse.message, Toast.LENGTH_SHORT).show();
                    } else if (addCarResponse.status_code.equalsIgnoreCase("4")) {
                        Toast.makeText(getActivity(), "" + addCarResponse.message, Toast.LENGTH_SHORT).show();
                        AppSingleTon.logOut(getActivity());
                    } else {
                        MessageDialogFragment fragment = new MessageDialogFragment(
                                "Failed", "Failed to add in back end.", true, "Ok",
                                false, "", false, "", null);
                        fragment.show(getActivity().getSupportFragmentManager(),
                                TAG_PUSH_RESULT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(getActivity(), Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getActivity()))
                                SendDataUsingVolley();
                            else
                                Toast.makeText(getActivity(), Constant.ERR_INTERNET, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error..", error.toString());
                Log.e("error", "error response");
                Toast.makeText(getActivity(), "Some thing was wrong please try again", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constants.VOLLEY_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance(getActivity()).addToRequestQueue(multipartRequest);
    }

    public void OpenDialog() {
        Builder builder = new Builder(getActivity());
        if (AddNewCarActivity.isedit) {
            builder.setTitle("Want To Update Car?");
        } else {
            builder.setTitle("Want To Add Car?");
        }

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//                senddata();
                if (AddNewCarActivity.isedit && AddNewCarActivity.addCarModelObject.editFilied.size() == 0) {
                    Toast.makeText(getActivity(), "No filled edited", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else
                    SendDataUsingVolley();
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
    public void onResume() {
        super.onResume();
        Log.e("onResume all", "on Resume call");
//        initTitle();

    }

    public interface OnTagSelectedListener {
        public void onTagSelected(int tagPosition, String strTag);

        public void onTagLongPress(int tagPosition, String strTag);
    }
}

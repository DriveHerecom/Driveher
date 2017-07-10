package com.yukti.driveherenew;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yukti.dataone.model.BasicData;
import com.yukti.dataone.model.CommonUsData;
import com.yukti.dataone.model.DecoderMessages;
import com.yukti.dataone.model.Efficiency;
import com.yukti.dataone.model.Engine;
import com.yukti.dataone.model.Installed_Equipment;
import com.yukti.dataone.model.OptionalEquipment;
import com.yukti.dataone.model.OptionsC;
import com.yukti.dataone.model.OtherInfo;
import com.yukti.dataone.model.ParentNode;
import com.yukti.dataone.model.Pricing;
import com.yukti.dataone.model.Query_Error;
import com.yukti.dataone.model.RequestSample;
import com.yukti.dataone.model.SafetyEquipment;
import com.yukti.dataone.model.Simple;
import com.yukti.dataone.model.Specifications;
import com.yukti.dataone.model.Transmission;
import com.yukti.dataone.model.US_Styles;
import com.yukti.dataone.model.UsMarketData;
import com.yukti.dataone.model.Warranties;
import com.yukti.driveherenew.search.CircleView;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.ParamsKey;
import com.yukti.utils.RestClient;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataoneActivity extends BaseActivity {

    String dataoneInformation = "";
    String vin;
    LinearLayout ll_EXColors, ll_ITColors, ll_RFColors, ll_Engines, ll_Efficiency, ll_BasicData, ll_Specification, ll_OtherInfo,
            ll_Pricing, ll_transmission, ll_warranties, ll_SafetyEquipment, ll_InstalledEquipment, ll_InstalledEquipmentCommanUs,
            ll_PricingCommanUs, ll_optionalEquipment, ll_TransmissionCommanUs, ll_WarrantiesCommanUs, ll_EXColorsCommanUs,
            ll_ITColorsCommanUs, ll_RFColorsCommanUs, ll_EnginesCommanUs, ll_SafetyEquipmentCommanUs, ll_EfficiencyCommanUs,
            ll_BasicDataCommanUs, ll_specificationCommanUs, ll_Equipment, ll_Values, ll_Specifics;

    TextView tv_ExColor, tv_ItColor, tv_RfColor, tv_fleet, tv_market, tv_complete, tv_name, tv_ExColorCommonUs,
            tv_ItColorCommonUs, tv_RfColorCommonUs;

    View itemColor;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dataonedata);
        showUpdateProgressDialog("Getting Local Server Data...");
        initToolbar();

        Intent i = getIntent();
        vin = i.getStringExtra(Constant.EXTRA_KEY_VIN);

        if (vin.length() == 17 && vin != null) {
            getDataFromLocalServer();
            client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        } else {
            showToast("incorrect vin");
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        tv_ExColor = (TextView) findViewById(R.id.tv_exterior_colors);
        tv_ItColor = (TextView) findViewById(R.id.tv_interior_colors);
        tv_RfColor = (TextView) findViewById(R.id.tv_roof_colors);
        tv_ExColorCommonUs = (TextView) findViewById(R.id.tv_CUDexterior_colors);
        tv_ItColorCommonUs = (TextView) findViewById(R.id.tv_CUDinterior_colors);
        tv_RfColorCommonUs = (TextView) findViewById(R.id.tv_CUDroof_colors);

        tv_fleet = (TextView) findViewById(R.id.tv_fleet);
        tv_market = (TextView) findViewById(R.id.tv_market);
        tv_complete = (TextView) findViewById(R.id.tv_complete);
        tv_name = (TextView) findViewById(R.id.tv_name);

        ll_EXColors = (LinearLayout) findViewById(R.id.ll_ex_color);
        ll_RFColors = (LinearLayout) findViewById(R.id.ll_rf_color);
        ll_ITColors = (LinearLayout) findViewById(R.id.ll_it_color);
        ll_Engines = (LinearLayout) findViewById(R.id.ll_engines);
        ll_Efficiency = (LinearLayout) findViewById(R.id.ll_efficiency);
        ll_BasicData = (LinearLayout) findViewById(R.id.ll_basic_data);
        ll_Specification = (LinearLayout) findViewById(R.id.ll_specification);

        ll_OtherInfo = (LinearLayout) findViewById(R.id.ll_other_info);
        ll_Pricing = (LinearLayout) findViewById(R.id.ll_pricing);
        ll_transmission = (LinearLayout) findViewById(R.id.ll_transmissions);
        ll_warranties = (LinearLayout) findViewById(R.id.ll_warranties);
        ll_SafetyEquipment = (LinearLayout) findViewById(R.id.ll_safety_equipment);
        ll_InstalledEquipment = (LinearLayout) findViewById(R.id.ll_installed_equipment);
        ll_PricingCommanUs = (LinearLayout) findViewById(R.id.ll_CUDpricing);
        ll_optionalEquipment = (LinearLayout) findViewById(R.id.ll_OptionalEquipment);
        ll_TransmissionCommanUs = (LinearLayout) findViewById(R.id.ll_CUDTransmission);
        ll_WarrantiesCommanUs = (LinearLayout) findViewById(R.id.ll_CUDWarranties);
        ll_EXColorsCommanUs = (LinearLayout) findViewById(R.id.ll_CUDex_color);
        ll_RFColorsCommanUs = (LinearLayout) findViewById(R.id.ll_CUDrf_color);
        ll_ITColorsCommanUs = (LinearLayout) findViewById(R.id.ll_CUDit_color);
        ll_EnginesCommanUs = (LinearLayout) findViewById(R.id.ll_CUDengines);
        ll_SafetyEquipmentCommanUs = (LinearLayout) findViewById(R.id.ll_CUDsafety_equipment);
        ll_EfficiencyCommanUs = (LinearLayout) findViewById(R.id.ll_CUDefficiency);
        ll_BasicDataCommanUs = (LinearLayout) findViewById(R.id.ll_CUDbasic_data);
        ll_specificationCommanUs = (LinearLayout) findViewById(R.id.ll_CUDspecification);
        ll_InstalledEquipmentCommanUs = (LinearLayout) findViewById(R.id.ll_CUDinstalled_equipment);
    }

    void getDataFromLocalServer() {
        Log.e("getDataFromLocalServer", "getDataFromLocalServer");
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("response", responseString);
                showToast("Dataone Request Failed.");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                dismissProgressDialog();
                try {
                    Simple simple = AppSingleTon.APP_JSON_PARSER.parseData(response);
                    if (simple.status_code.equals("0"))//car not found
                    {
                    } else if (simple.status_code.equals("2"))//missing parameter
                    {
                        pullDataoneInformations();
                    } else if (simple.status_code.equals("1"))//dataone info not found
                    {
                        JSONObject res = null;
                        try {
                            res = new JSONObject(Common.Decode_String(simple.dataOneInformation));
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        ParentNode orm = AppSingleTon.APP_JSON_PARSER.parseDataoneResponse(res);
                        setData(orm, true);

                    } else if (simple.status_code.equals("4"))//dataone info
                    {
                        finishAffinity();
                        AppSingleTon.logOut(DataoneActivity.this);
                        finish();
                    } else {
                        showToast(simple.message);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(DataoneActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getApplicationContext()))
                                getDataFromLocalServer();
                            else
                                Toast.makeText(getApplicationContext(),Constant.ERR_INTERNET , Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissProgressDialog();
            }

            @Override
            public void onStart() {
                super.onStart();
            }
        };

        String url = AppSingleTon.APP_URL.URL_DATA_ONE_NEW;
        RequestParams params = new RequestParams();
        params.put(ParamsKey.KEY_vin, vin);
        params.put(ParamsKey.KEY_userId, AppSingleTon.SHARED_PREFERENCE.getUserId());
        params.put(ParamsKey.KEY_type, Constant.APP_TYPE);
        Log.e("Posting Params",params + " ");
        RestClient.post(this, url, params, handler);
    }

    void setData(ParentNode orm, boolean is_from_local_server) {

        DecoderMessages decoderMessages = orm.decoder_messages;

        RequestSample requestSample = new RequestSample();
        Query_Error queryError = orm.query_responses.RequestSample.query_error;
        UsMarketData usMarketData = orm.query_responses.RequestSample.us_market_data;
        CommonUsData commonUsData = orm.query_responses.RequestSample.us_market_data.common_us_data;
        requestSample.transaction_id = orm.query_responses.RequestSample.transaction_id;
        ArrayList<US_Styles> mUStyles = new ArrayList<US_Styles>();
        mUStyles = orm.query_responses.RequestSample.us_market_data.us_styles;

        if (!queryError.error_code.equals("")) {
            Log.e("Querry Error", " " + queryError.error_message + " " + queryError.error_code);
            return;
        }

        int flag = 0;
        boolean IS_FROM_USDATA = true;
        int i, j, k, l;

        for (i = 0; i < mUStyles.size(); i++) {
            for (j = 0; j < mUStyles.get(i).colors.exterior_colors.size(); j++) {
                String ex_ColorHex = mUStyles.get(i).colors.exterior_colors.get(j).primary_rgb_code.hex;
                String ex_ColorName = mUStyles.get(i).colors.exterior_colors.get(j).generic_color_name;
                flag = 1;
                addViewInColor(ex_ColorHex, ex_ColorName, flag, IS_FROM_USDATA, j);
            }

            if (j == 0)
                tv_ExColor.setVisibility(View.GONE);

            for (k = 0; k < mUStyles.get(i).colors.roof_colors.size(); k++) {
                String ex_ColorHex = mUStyles.get(i).colors.roof_colors.get(k).primary_rgb_code.hex;
                String ex_ColorName = mUStyles.get(i).colors.roof_colors.get(k).generic_color_name;
                flag = 2;
                addViewInColor(ex_ColorHex, ex_ColorName, flag, IS_FROM_USDATA, k);
            }

            if (k == 0)
                tv_RfColor.setVisibility(View.GONE);

            for (l = 0; l < mUStyles.get(i).colors.interior_colors.size(); l++) {
                String ex_ColorHex = mUStyles.get(i).colors.interior_colors.get(l).primary_rgb_code.hex;
                String ex_ColorName = mUStyles.get(i).colors.interior_colors.get(l).generic_color_name;
                flag = 3;
                addViewInColor(ex_ColorHex, ex_ColorName, flag, IS_FROM_USDATA, l);
            }

            if (l == 0)
                tv_ItColor.setVisibility(View.GONE);
        }

        Engine engineObject = null;
        IS_FROM_USDATA = true;
        for (i = 0; i < mUStyles.size(); i++) {
            for (j = 0; j < mUStyles.get(i).engines.size(); j++) {
                engineObject = new Engine();
                engineObject.setMax_hp(mUStyles.get(i).engines.get(j).max_hp);
                engineObject.setMarketing_name(mUStyles.get(i).engines.get(j).marketing_name);
                engineObject.setValve_timing(mUStyles.get(i).engines.get(j).valve_timing);
                engineObject.setBore(mUStyles.get(i).engines.get(j).bore);
                engineObject.setMax_payload(mUStyles.get(i).engines.get(j).max_payload);
                engineObject.setRedline(mUStyles.get(i).engines.get(j).redline);
                engineObject.setOrder_code(mUStyles.get(i).engines.get(j).order_code);
                engineObject.setMax_torque_at(mUStyles.get(i).engines.get(j).max_torque_at);
                engineObject.setMax_hp_at(mUStyles.get(i).engines.get(j).max_hp_at);
                engineObject.setStroke(mUStyles.get(i).engines.get(j).stroke);
                engineObject.setMsrp(mUStyles.get(i).engines.get(j).msrp);
                engineObject.setDisplacement(mUStyles.get(i).engines.get(j).displacement);
                engineObject.setFuel_quality(mUStyles.get(i).engines.get(j).fuel_quality);
                engineObject.setName(mUStyles.get(i).engines.get(j).name);
                engineObject.setFuel_induction(mUStyles.get(i).engines.get(j).fuel_induction);
                engineObject.setCompression(mUStyles.get(i).engines.get(j).compression);
                engineObject.setAvailability(mUStyles.get(i).engines.get(j).availability);
                engineObject.setCylinders(mUStyles.get(i).engines.get(j).cylinders);
                engineObject.setCam_type(mUStyles.get(i).engines.get(j).cam_type);
                engineObject.setInvoice_price(mUStyles.get(i).engines.get(j).invoice_price);
                engineObject.setBlock_type(mUStyles.get(i).engines.get(j).block_type);
                engineObject.setFuel_type(mUStyles.get(i).engines.get(j).fuel_type);
                engineObject.setValves(mUStyles.get(i).engines.get(j).valves);
                engineObject.setMax_torque(mUStyles.get(i).engines.get(j).max_torque);
                engineObject.setAspiration(mUStyles.get(i).engines.get(j).aspiration);
                engineObject.setBrand(mUStyles.get(i).engines.get(j).brand);
                engineObject.setOil_capacity(mUStyles.get(i).engines.get(j).oil_capacity);
                AddEngineLayout(engineObject, IS_FROM_USDATA);
            }

        }

        Efficiency mEfficiency = null;
        IS_FROM_USDATA = true;
        for (i = 0; i < mUStyles.size(); i++) {
            for (j = 0; j < mUStyles.get(i).epa_fuel_efficiency.size(); j++) {
                mEfficiency = new Efficiency();
                mEfficiency.setFuel_type(mUStyles.get(i).epa_fuel_efficiency.get(j).fuel_type);
                mEfficiency.setHighway(mUStyles.get(i).epa_fuel_efficiency.get(j).highway);
                mEfficiency.setFuel_grade(mUStyles.get(i).epa_fuel_efficiency.get(j).fuel_grade);
                mEfficiency.setCombined(mUStyles.get(i).epa_fuel_efficiency.get(j).combined);
                mEfficiency.setCity(mUStyles.get(i).epa_fuel_efficiency.get(j).city);
            }
            AddViewInEfficiency(mEfficiency, IS_FROM_USDATA);
        }

        BasicData mBasicData = null;
        IS_FROM_USDATA = true;
        for (i = 0; i < mUStyles.size(); i++) {
            mBasicData = new BasicData();
            mBasicData.setPlant(mUStyles.get(i).basic_data.plant);
            mBasicData.setDoors(mUStyles.get(i).basic_data.doors);
            mBasicData.setCountry_of_manufacture(mUStyles.get(i).basic_data.country_of_manufacture);
            mBasicData.setModel(mUStyles.get(i).basic_data.model);
            mBasicData.setBrake_system(mUStyles.get(i).basic_data.brake_system);
            mBasicData.setBody_subtype(mUStyles.get(i).basic_data.body_subtype);
            mBasicData.setBody_type(mUStyles.get(i).basic_data.body_type);
            mBasicData.setVehicle_type(mUStyles.get(i).basic_data.vehicle_type);
            mBasicData.setOem_body_style(mUStyles.get(i).basic_data.oem_body_style);
            mBasicData.setOem_doors(mUStyles.get(i).basic_data.oem_doors);
            mBasicData.setRestraint_type(mUStyles.get(i).basic_data.restraint_type);
            mBasicData.setModel_number(mUStyles.get(i).basic_data.model_number);
            mBasicData.setMarket(mUStyles.get(i).basic_data.market);
            mBasicData.setPackage_code(mUStyles.get(i).basic_data.package_code);
            mBasicData.setDrive_type(mUStyles.get(i).basic_data.drive_type);
            mBasicData.setYear(mUStyles.get(i).basic_data.year);
            mBasicData.setTrim(mUStyles.get(i).basic_data.trim);
            mBasicData.setMake(mUStyles.get(i).basic_data.make);

            AddViewInBasicData(mBasicData, IS_FROM_USDATA);
        }

        Specifications mSpecifications = null;
        ArrayList<Specifications> SpList = new ArrayList<>();
        IS_FROM_USDATA = true;
        for (i = 0; i < mUStyles.size(); i++) {
            for (j = 0; j < mUStyles.get(i).specifications.size(); j++) {
                mSpecifications = new Specifications();

                mSpecifications.category = mUStyles.get(i).specifications.get(j).category;
                mSpecifications.specifications = mUStyles.get(i).specifications.get(j).specifications;
                SpList.add(mSpecifications);
            }
        }
        AddSpecification(SpList, IS_FROM_USDATA);


        OtherInfo mOtherInfo = null;
        for (i = 0; i < mUStyles.size(); i++) {
            mOtherInfo = new OtherInfo();
            mOtherInfo.setFleet(mUStyles.get(i).fleet);
            mOtherInfo.setMarket(mUStyles.get(i).market);
            mOtherInfo.setComplete(mUStyles.get(i).complete);
            mOtherInfo.setName(mUStyles.get(i).name);
            AddViewInOtherInfo(mOtherInfo);
        }

        Pricing mPricing = null;
        IS_FROM_USDATA = true;
        for (i = 0; i < mUStyles.size(); i++) {
            mPricing = new Pricing();
            mPricing.setMsrp(mUStyles.get(i).pricing.msrp);
            mPricing.setGas_guzzler_tax(mUStyles.get(i).pricing.gas_guzzler_tax);
            mPricing.setInvoice_price(mUStyles.get(i).pricing.invoice_price);
            mPricing.setDestination_charge(mUStyles.get(i).pricing.destination_charge);
            AddViewInPricing(mPricing, IS_FROM_USDATA);
        }

        Transmission transmissionObject = null;
        IS_FROM_USDATA = true;
        for (i = 0; i < mUStyles.size(); i++) {
            for (j = 0; j < mUStyles.get(i).transmissions.size(); j++) {
                transmissionObject = new Transmission();
                transmissionObject.setMsrp(mUStyles.get(i).transmissions.get(j).getMsrp());
                transmissionObject.setMarketing_name(mUStyles.get(i).transmissions.get(j).getMarketing_name());
                transmissionObject.setName(mUStyles.get(i).transmissions.get(j).getName());
                transmissionObject.setBrand(mUStyles.get(i).transmissions.get(j).getBrand());
                transmissionObject.setInvoice_price(mUStyles.get(i).transmissions.get(j).getInvoice_price());
                transmissionObject.setGears(mUStyles.get(i).transmissions.get(j).getGears());
                transmissionObject.setOrder_code(mUStyles.get(i).transmissions.get(j).getOrder_code());
                transmissionObject.setDetail_type(mUStyles.get(i).transmissions.get(j).getDetail_type());
                transmissionObject.setAvailability(mUStyles.get(i).transmissions.get(j).getAvailability());
                transmissionObject.setTransmission_id(mUStyles.get(i).transmissions.get(j).getTransmission_id());
                AddTransmission(transmissionObject, IS_FROM_USDATA);
            }
        }

        Warranties warrantiesObject = null;
        IS_FROM_USDATA = true;
        for (i = 0; i < mUStyles.size(); i++) {
            for (j = 0; j < mUStyles.get(i).warranties.size(); j++) {
                warrantiesObject = new Warranties();
                warrantiesObject.setName(mUStyles.get(i).warranties.get(j).getName());
                warrantiesObject.setType(mUStyles.get(i).warranties.get(j).getType());
                warrantiesObject.setMiles(mUStyles.get(i).warranties.get(j).getMiles());
                warrantiesObject.setMonths(mUStyles.get(i).warranties.get(j).getMonths());
                AddWarranties(warrantiesObject, IS_FROM_USDATA);
            }
        }

        SafetyEquipment mSafetyEquipment = null;
        IS_FROM_USDATA = true;
        for (i = 0; i < mUStyles.size(); i++) {
            mSafetyEquipment = new SafetyEquipment();
            mSafetyEquipment.setRollover_stability_control(mUStyles.get(i).safety_equipment.rollover_stability_control);
            mSafetyEquipment.setTire_pressure_monitoring_system(mUStyles.get(i).safety_equipment.tire_pressure_monitoring_system);
            mSafetyEquipment.setDaytime_running_lights(mUStyles.get(i).safety_equipment.daytime_running_lights);
            mSafetyEquipment.setAbs_two_wheel(mUStyles.get(i).safety_equipment.abs_two_wheel);
            mSafetyEquipment.setAirbags_front_passenger(mUStyles.get(i).safety_equipment.airbags_front_passenger);
            mSafetyEquipment.setAirbags_front_driver(mUStyles.get(i).safety_equipment.airbags_front_driver);
            mSafetyEquipment.setElectronic_stability_control(mUStyles.get(i).safety_equipment.electronic_stability_control);
            mSafetyEquipment.setAirbags_side_impact(mUStyles.get(i).safety_equipment.airbags_side_impact);
            mSafetyEquipment.setAbs_four_wheel(mUStyles.get(i).safety_equipment.abs_four_wheel);
            mSafetyEquipment.setBrake_assist(mUStyles.get(i).safety_equipment.brake_assist);
            mSafetyEquipment.setAirbags_side_curtain(mUStyles.get(i).safety_equipment.airbags_side_curtain);
            mSafetyEquipment.setElectronic_traction_control(mUStyles.get(i).safety_equipment.electronic_traction_control);

            AddViewInSafetyEquipment(mSafetyEquipment, IS_FROM_USDATA);
        }

        Installed_Equipment mInstalled_equipment = null;
        ArrayList<Installed_Equipment> IEList = new ArrayList<>();
        IS_FROM_USDATA = true;
        for (i = 0; i < mUStyles.size(); i++) {
            for (j = 0; j < mUStyles.get(i).installed_equipment.size(); j++) {
                mInstalled_equipment = new Installed_Equipment();

                mInstalled_equipment.setCategory(mUStyles.get(i).installed_equipment.get(j).category);
                mInstalled_equipment.setEquipment(mUStyles.get(i).installed_equipment.get(j).equipment);
                IEList.add(mInstalled_equipment);
            }
        }

        AddViewInInstalledEquipment(IEList, IS_FROM_USDATA);


        Pricing pricingObject = new Pricing();
        IS_FROM_USDATA = false;
        pricingObject.setMsrp(commonUsData.pricing.getMsrp());
        pricingObject.setGas_guzzler_tax(commonUsData.pricing.getGas_guzzler_tax());
        pricingObject.setInvoice_price(commonUsData.pricing.getInvoice_price());
        pricingObject.setDestination_charge(commonUsData.pricing.getDestination_charge());
        AddViewInPricing(pricingObject, IS_FROM_USDATA);

        OptionalEquipment optionalEquipment = null;
        OptionsC optionsC = null;
        for (i = 0; i < commonUsData.optional_equipment.size(); i++) {
            optionalEquipment = new OptionalEquipment();
            optionalEquipment.setCategory(commonUsData.optional_equipment.get(i).getCategory());
            String category = commonUsData.optional_equipment.get(i).getCategory();
            for (j = 0; j < commonUsData.optional_equipment.get(i).options.size(); j++) {
                optionsC = new OptionsC();
                optionsC.setFleet(commonUsData.optional_equipment.get(i).options.get(j).getFleet());
                optionsC.setInstall_type(commonUsData.optional_equipment.get(i).options.get(j).getInstall_type());
                optionsC.setMsrp(commonUsData.optional_equipment.get(i).options.get(j).getMsrp());
                optionsC.setDescription(commonUsData.optional_equipment.get(i).options.get(j).getDescription());
                optionsC.setInstalled(commonUsData.optional_equipment.get(i).options.get(j).getInstalled());
                optionsC.setName(commonUsData.optional_equipment.get(i).options.get(j).getName());
                optionsC.setInvoice_price(commonUsData.optional_equipment.get(i).options.get(j).getInvoice_price());
                optionsC.setOption_id(commonUsData.optional_equipment.get(i).options.get(j).getOption_id());
                optionsC.setOrder_code(commonUsData.optional_equipment.get(i).options.get(j).getOrder_code());
                AddOptionalEquipment(category, optionsC);
            }
        }
        Transmission mTransmissionCUD = null;
        IS_FROM_USDATA = false;
        for (j = 0; j < commonUsData.transmissions.size(); j++) {
            mTransmissionCUD = new Transmission();
            transmissionObject.setMsrp(commonUsData.transmissions.get(j).getMsrp());
            transmissionObject.setMarketing_name(commonUsData.transmissions.get(j).getMarketing_name());
            transmissionObject.setName(commonUsData.transmissions.get(j).getName());
            transmissionObject.setBrand(commonUsData.transmissions.get(j).getBrand());
            transmissionObject.setInvoice_price(commonUsData.transmissions.get(j).getInvoice_price());
            transmissionObject.setGears(commonUsData.transmissions.get(j).getGears());
            transmissionObject.setOrder_code(commonUsData.transmissions.get(j).getOrder_code());
            transmissionObject.setDetail_type(commonUsData.transmissions.get(j).getDetail_type());
            transmissionObject.setAvailability(commonUsData.transmissions.get(j).getAvailability());
            transmissionObject.setTransmission_id(commonUsData.transmissions.get(j).getTransmission_id());

            AddTransmission(mTransmissionCUD, IS_FROM_USDATA);
        }

        Warranties mWarranties = null;
        IS_FROM_USDATA = false;
        for (j = 0; j < commonUsData.warranties.size(); j++) {
            mWarranties = new Warranties();
            mWarranties.setName(commonUsData.warranties.get(j).getName());
            mWarranties.setType(commonUsData.warranties.get(j).getType());
            mWarranties.setMiles(commonUsData.warranties.get(j).getMiles());
            mWarranties.setMonths(commonUsData.warranties.get(j).getMonths());
            AddWarranties(mWarranties, IS_FROM_USDATA);
        }

        // SHOWING COLORS IN COMMON US DATA

        for (j = 0; j < commonUsData.colors.exterior_colors.size(); j++) {
            String ex_ColorHex = commonUsData.colors.exterior_colors.get(j).primary_rgb_code.hex;
            String ex_ColorName = commonUsData.colors.exterior_colors.get(j).generic_color_name;
            flag = 1;
            addViewInColor(ex_ColorHex, ex_ColorName, flag, IS_FROM_USDATA, j);
        }

        if (j == 0)
            tv_ExColorCommonUs.setVisibility(View.GONE);

        for (k = 0; k < commonUsData.colors.roof_colors.size(); k++) {
            String ex_ColorHex = commonUsData.colors.roof_colors.get(k).primary_rgb_code.hex;
            String ex_ColorName = commonUsData.colors.roof_colors.get(k).generic_color_name;
            flag = 2;
            addViewInColor(ex_ColorHex, ex_ColorName, flag, IS_FROM_USDATA, k);
        }

        if (k == 0)
            tv_RfColorCommonUs.setVisibility(View.GONE);

        for (l = 0; l < commonUsData.colors.interior_colors.size(); l++) {
            String ex_ColorHex = commonUsData.colors.interior_colors.get(l).primary_rgb_code.hex;
            String ex_ColorName = commonUsData.colors.interior_colors.get(l).generic_color_name;
            flag = 3;
            addViewInColor(ex_ColorHex, ex_ColorName, flag, IS_FROM_USDATA, l);
        }

        if (l == 0)
            tv_ItColorCommonUs.setVisibility(View.GONE);


        Engine mEngine = null;
        IS_FROM_USDATA = false;

        for (j = 0; j < commonUsData.engines.size(); j++) {
            mEngine = new Engine();
            mEngine.setMax_hp(commonUsData.engines.get(j).max_hp);
            mEngine.setMarketing_name(commonUsData.engines.get(j).marketing_name);
            mEngine.setValve_timing(commonUsData.engines.get(j).valve_timing);
            mEngine.setBore(commonUsData.engines.get(j).bore);
            mEngine.setMax_payload(commonUsData.engines.get(j).max_payload);
            mEngine.setRedline(commonUsData.engines.get(j).redline);
            mEngine.setOrder_code(commonUsData.engines.get(j).order_code);
            mEngine.setMax_torque_at(commonUsData.engines.get(j).max_torque_at);
            mEngine.setMax_hp_at(commonUsData.engines.get(j).max_hp_at);
            mEngine.setStroke(commonUsData.engines.get(j).stroke);
            mEngine.setMsrp(commonUsData.engines.get(j).msrp);
            mEngine.setDisplacement(commonUsData.engines.get(j).displacement);
            mEngine.setFuel_quality(commonUsData.engines.get(j).fuel_quality);
            mEngine.setName(commonUsData.engines.get(j).name);
            mEngine.setFuel_induction(commonUsData.engines.get(j).fuel_induction);
            mEngine.setCompression(commonUsData.engines.get(j).compression);
            mEngine.setAvailability(commonUsData.engines.get(j).availability);
            mEngine.setCylinders(commonUsData.engines.get(j).cylinders);
            mEngine.setCam_type(commonUsData.engines.get(j).cam_type);
            mEngine.setInvoice_price(commonUsData.engines.get(j).invoice_price);
            mEngine.setBlock_type(commonUsData.engines.get(j).block_type);
            mEngine.setFuel_type(commonUsData.engines.get(j).fuel_type);
            mEngine.setValves(commonUsData.engines.get(j).valves);
            mEngine.setMax_torque(commonUsData.engines.get(j).max_torque);
            mEngine.setAspiration(commonUsData.engines.get(j).aspiration);
            mEngine.setBrand(commonUsData.engines.get(j).brand);
            mEngine.setOil_capacity(commonUsData.engines.get(j).oil_capacity);
            AddEngineLayout(mEngine, IS_FROM_USDATA);
        }

        SafetyEquipment safetyEquipmentObject = null;
        IS_FROM_USDATA = false;

        safetyEquipmentObject = new SafetyEquipment();
        safetyEquipmentObject.setRollover_stability_control(commonUsData.safety_equipment.rollover_stability_control);
        safetyEquipmentObject.setTire_pressure_monitoring_system(commonUsData.safety_equipment.tire_pressure_monitoring_system);
        safetyEquipmentObject.setDaytime_running_lights(commonUsData.safety_equipment.daytime_running_lights);
        safetyEquipmentObject.setAbs_two_wheel(commonUsData.safety_equipment.abs_two_wheel);
        safetyEquipmentObject.setAirbags_front_passenger(commonUsData.safety_equipment.airbags_front_passenger);
        safetyEquipmentObject.setAirbags_front_driver(commonUsData.safety_equipment.airbags_front_driver);
        safetyEquipmentObject.setElectronic_stability_control(commonUsData.safety_equipment.electronic_stability_control);
        safetyEquipmentObject.setAirbags_side_impact(commonUsData.safety_equipment.airbags_side_impact);
        safetyEquipmentObject.setAbs_four_wheel(commonUsData.safety_equipment.abs_four_wheel);
        safetyEquipmentObject.setBrake_assist(commonUsData.safety_equipment.brake_assist);
        safetyEquipmentObject.setAirbags_side_curtain(commonUsData.safety_equipment.airbags_side_curtain);
        safetyEquipmentObject.setElectronic_traction_control(commonUsData.safety_equipment.electronic_traction_control);
        AddViewInSafetyEquipment(mSafetyEquipment, IS_FROM_USDATA);

        Efficiency efficiencyObject = null;
        IS_FROM_USDATA = false;
        for (j = 0; j < commonUsData.epa_fuel_efficiency.size(); j++) {
            efficiencyObject = new Efficiency();
            efficiencyObject.setFuel_type(commonUsData.epa_fuel_efficiency.get(j).fuel_type);
            efficiencyObject.setHighway(commonUsData.epa_fuel_efficiency.get(j).highway);
            efficiencyObject.setFuel_grade(commonUsData.epa_fuel_efficiency.get(j).fuel_grade);
            efficiencyObject.setCombined(commonUsData.epa_fuel_efficiency.get(j).combined);
            efficiencyObject.setCity(commonUsData.epa_fuel_efficiency.get(j).city);
            AddViewInEfficiency(mEfficiency, IS_FROM_USDATA);
        }

        Installed_Equipment Installed_equipmentObject = null;
        ArrayList<Installed_Equipment> IEListObject = new ArrayList<>();
        IS_FROM_USDATA = false;
        for (j = 0; j < commonUsData.installed_equipment.size(); j++) {
            Installed_equipmentObject = new Installed_Equipment();

            Installed_equipmentObject.setCategory(commonUsData.installed_equipment.get(j).category);
            Installed_equipmentObject.setEquipment(commonUsData.installed_equipment.get(j).equipment);
            IEListObject.add(Installed_equipmentObject);
        }

        AddViewInInstalledEquipment(IEListObject, IS_FROM_USDATA);


        BasicData basicDataObject = null;
        IS_FROM_USDATA = false;
        basicDataObject = new BasicData();
        basicDataObject.setPlant(commonUsData.basic_data.plant);
        basicDataObject.setDoors(commonUsData.basic_data.doors);
        basicDataObject.setCountry_of_manufacture(commonUsData.basic_data.country_of_manufacture);
        basicDataObject.setModel(commonUsData.basic_data.model);
        basicDataObject.setBrake_system(commonUsData.basic_data.brake_system);
        basicDataObject.setBody_subtype(commonUsData.basic_data.body_subtype);
        basicDataObject.setBody_type(commonUsData.basic_data.body_type);
        basicDataObject.setVehicle_type(commonUsData.basic_data.vehicle_type);
        basicDataObject.setOem_body_style(commonUsData.basic_data.oem_body_style);
        basicDataObject.setOem_doors(commonUsData.basic_data.oem_doors);
        basicDataObject.setRestraint_type(commonUsData.basic_data.restraint_type);
        basicDataObject.setModel_number(commonUsData.basic_data.model_number);
        basicDataObject.setMarket(commonUsData.basic_data.market);
        basicDataObject.setPackage_code(commonUsData.basic_data.package_code);
        basicDataObject.setDrive_type(commonUsData.basic_data.drive_type);
        basicDataObject.setYear(commonUsData.basic_data.year);
        basicDataObject.setTrim(commonUsData.basic_data.trim);
        basicDataObject.setMake(commonUsData.basic_data.make);

        AddViewInBasicData(mBasicData, IS_FROM_USDATA);

        Specifications SpecificationsObject = null;
        ArrayList<Specifications> SpListObject = new ArrayList<>();
        IS_FROM_USDATA = false;
        for (j = 0; j < commonUsData.specifications.size(); j++) {
            SpecificationsObject = new Specifications();

            SpecificationsObject.category = commonUsData.specifications.get(j).category;
            SpecificationsObject.specifications = commonUsData.specifications.get(j).specifications;
            SpListObject.add(SpecificationsObject);
        }
        AddSpecification(SpList, IS_FROM_USDATA);

        if (!is_from_local_server)
            storeDataToServer();
    }


    void addViewInColor(String hexValue, String name, int f, boolean is_from_usdata, int oddeven) {
        CircleView cv = null;
        TextView tv = null;
        String hex = null;
        hex = "#";
        hex = hex.concat(hexValue);

        switch (f) {
            case 1:
                if ((oddeven % 2) == 0) {
                    itemColor = null;

                    if (is_from_usdata)
                        itemColor = DataoneActivity.this.getLayoutInflater().inflate(R.layout.dataone_row_color, ll_EXColors, false);
                    else
                        itemColor = DataoneActivity.this.getLayoutInflater().inflate(R.layout.dataone_row_color, ll_EXColorsCommanUs, false);

                    cv = (CircleView) itemColor.findViewById(R.id.colorViewOdd);
                    cv.drawAgain(hex);
                    cv.setVisibility(View.VISIBLE);
                    tv = ((TextView) itemColor.findViewById(R.id.colorNameOdd));
                    tv.setText(name);
                    tv.setVisibility(View.VISIBLE);

                    if (is_from_usdata)
                        ll_EXColors.addView(itemColor);
                    else
                        ll_EXColorsCommanUs.addView(itemColor);
                } else {
                    cv = (CircleView) itemColor.findViewById(R.id.colorViewEven);
                    cv.drawAgain(hex);
                    cv.setVisibility(View.VISIBLE);
                    tv = ((TextView) itemColor.findViewById(R.id.colorNameEven));
                    tv.setText(name);
                    tv.setVisibility(View.VISIBLE);
                }

                break;
            case 2:

                if ((oddeven % 2) == 0) {
                    itemColor = null;

                    if (is_from_usdata)
                        itemColor = DataoneActivity.this.getLayoutInflater().inflate(R.layout.dataone_row_color, ll_RFColors, false);
                    else
                        itemColor = DataoneActivity.this.getLayoutInflater().inflate(R.layout.dataone_row_color, ll_RFColorsCommanUs, false);

                    cv = (CircleView) itemColor.findViewById(R.id.colorViewOdd);
                    cv.drawAgain(hex);
                    cv.setVisibility(View.VISIBLE);
                    tv = ((TextView) itemColor.findViewById(R.id.colorNameOdd));
                    tv.setText(name);
                    tv.setVisibility(View.VISIBLE);

                    if (is_from_usdata)
                        ll_RFColors.addView(itemColor);
                    else
                        ll_RFColorsCommanUs.addView(itemColor);
                } else {
                    cv = (CircleView) itemColor.findViewById(R.id.colorViewEven);
                    cv.drawAgain(hex);
                    cv.setVisibility(View.VISIBLE);
                    tv = ((TextView) itemColor.findViewById(R.id.colorNameEven));
                    tv.setText(name);
                    tv.setVisibility(View.VISIBLE);
                }

                break;
            case 3:

                if ((oddeven % 2) == 0) {
                    itemColor = null;

                    if (is_from_usdata)
                        itemColor = DataoneActivity.this.getLayoutInflater().inflate(R.layout.dataone_row_color, ll_ITColors, false);
                    else
                        itemColor = DataoneActivity.this.getLayoutInflater().inflate(R.layout.dataone_row_color, ll_ITColorsCommanUs, false);

                    cv = (CircleView) itemColor.findViewById(R.id.colorViewOdd);
                    cv.drawAgain(hex);
                    cv.setVisibility(View.VISIBLE);
                    tv = ((TextView) itemColor.findViewById(R.id.colorNameOdd));
                    tv.setText(name);
                    tv.setVisibility(View.VISIBLE);

                    if (is_from_usdata)
                        ll_ITColors.addView(itemColor);
                    else
                        ll_ITColorsCommanUs.addView(itemColor);
                } else {
                    cv = (CircleView) itemColor.findViewById(R.id.colorViewEven);
                    cv.drawAgain(hex);
                    cv.setVisibility(View.VISIBLE);
                    tv = ((TextView) itemColor.findViewById(R.id.colorNameEven));
                    tv.setText(name);
                    tv.setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    void AddEngineLayout(Engine engineObject, boolean is_from_usdata) {
        View EngineView = null;

        if (is_from_usdata)
            EngineView = DataoneActivity.this.getLayoutInflater().inflate(R.layout.enginesvaluelayout, ll_Engines, false);
        else
            EngineView = DataoneActivity.this.getLayoutInflater().inflate(R.layout.enginesvaluelayout, ll_EnginesCommanUs, false);

        if (engineObject.getMax_hp() != null && engineObject.getMax_hp().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_maxhpValue)).setText(engineObject.getMax_hp());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_maxhpValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (engineObject.getMarketing_name() != null && engineObject.getMarketing_name().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_marketingnameValue)).setText(engineObject.getMarketing_name());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_marketingnameValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (engineObject.getValve_timing() != null && engineObject.getValve_timing().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_valve_timingValue)).setText(engineObject.getValve_timing());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_valve_timingValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getBore() != null && engineObject.getBore().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_boreValue)).setText(engineObject.getBore());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_boreValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getMax_payload() != null && engineObject.getMax_payload().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_max_payloadValue)).setText(engineObject.getMax_payload());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_max_payloadValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getRedline() != null && engineObject.getRedline().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_redlineValue)).setText(engineObject.getRedline());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_redlineValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getOrder_code() != null && engineObject.getOrder_code().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_order_codeValue)).setText(engineObject.getOrder_code());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_order_codeValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getMax_torque_at() != null && engineObject.getMax_torque_at().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_max_hp_atValue)).setText(engineObject.getMax_torque_at());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_max_hp_atValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getMax_hp_at() != null && engineObject.getMax_hp_at().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_max_hp_atValue)).setText(engineObject.getMax_hp_at());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_max_hp_atValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getStroke() != null && engineObject.getStroke().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_strokeValue)).setText(engineObject.getStroke());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_strokeValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getMsrp() != null && engineObject.getMsrp().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_msrpValue)).setText(engineObject.getMsrp());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_msrpValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getDisplacement() != null && engineObject.getDisplacement().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_displacementValue)).setText(engineObject.getDisplacement());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_displacementValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getFuel_quality() != null && engineObject.getFuel_quality().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_fuel_qualityValue)).setText(engineObject.getFuel_quality());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_fuel_qualityValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getName() != null && engineObject.getName().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_nameValue)).setText(engineObject.getName());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_nameValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getFuel_induction() != null && engineObject.getFuel_induction().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_fuel_inductionValue)).setText(engineObject.getFuel_induction());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_fuel_inductionValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getCompression() != null && engineObject.getCompression().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_compressionValue)).setText(engineObject.getCompression());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_compressionValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getAvailability() != null && engineObject.getAvailability().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_availabilityValue)).setText(engineObject.getAvailability());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_availabilityValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getCylinders() != null && engineObject.getCylinders().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_cylindersValue)).setText(engineObject.getCylinders());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_cylindersValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getCam_type() != null && engineObject.getCam_type().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_cam_typeValue)).setText(engineObject.getCam_type());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_cam_typeValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getInvoice_price() != null && engineObject.getInvoice_price().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_invoice_priceValue)).setText(engineObject.getInvoice_price());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_invoice_priceValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getBlock_type() != null && engineObject.getBlock_type().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_blocktypeValue)).setText(engineObject.getBlock_type());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_blocktypeValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getFuel_type() != null && engineObject.getFuel_type().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_fuel_typeValue)).setText(engineObject.getFuel_type());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_fuel_typeValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getValves() != null && engineObject.getValves().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_valvesValue)).setText(engineObject.getValves());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_valvesValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getMax_torque() != null && engineObject.getMax_torque().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_max_torqueValue)).setText(engineObject.getMax_torque());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_max_torqueValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getAspiration() != null && engineObject.getAspiration().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_aspirationValue)).setText(engineObject.getAspiration());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_aspirationValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getMax_torque_at() != null && engineObject.getMax_torque_at().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_max_torque_atValue)).setText(engineObject.getMax_torque_at());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_max_torque_atValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (engineObject.getBrand() != null && engineObject.getBrand().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_brandValue)).setText(engineObject.getBrand());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_brandValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (engineObject.getOil_capacity() != null && engineObject.getOil_capacity().length() != 0) {
            ((TextView) EngineView.findViewById(R.id.tv_oil_capacityValue)).setText(engineObject.getOil_capacity());
        } else {
            ((TextView) EngineView.findViewById(R.id.tv_oil_capacityValue)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (is_from_usdata)
            ll_Engines.addView(EngineView);
        else
            ll_EnginesCommanUs.addView(EngineView);
    }

    void AddViewInEfficiency(Efficiency efficiency, boolean is_from_usdata) {
        View efficiencyView = null;

        if (is_from_usdata)
            efficiencyView = DataoneActivity.this.getLayoutInflater().inflate(R.layout.epa_fuel_efficiencylayout, ll_Efficiency, false);
        else
            efficiencyView = DataoneActivity.this.getLayoutInflater().inflate(R.layout.epa_fuel_efficiencylayout, ll_EfficiencyCommanUs, false);

        if (efficiency.getFuel_type() != null && efficiency.getFuel_type().length() != 0) {
            ((TextView) efficiencyView.findViewById(R.id.tv_fuel_type)).setText(efficiency.getFuel_type());
        } else {
            ((TextView) efficiencyView.findViewById(R.id.tv_fuel_type)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (efficiency.getHighway() != null && efficiency.getHighway().length() != 0) {
            ((TextView) efficiencyView.findViewById(R.id.tv_highway)).setText(efficiency.getHighway());
        } else {
            ((TextView) efficiencyView.findViewById(R.id.tv_highway)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (efficiency.getFuel_grade() != null && efficiency.getFuel_grade().length() != 0) {
            ((TextView) efficiencyView.findViewById(R.id.tv_fuel_grade)).setText(efficiency.getFuel_grade());
        } else {
            ((TextView) efficiencyView.findViewById(R.id.tv_fuel_grade)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (efficiency.getCombined() != null && efficiency.getCombined().length() != 0) {
            ((TextView) efficiencyView.findViewById(R.id.tv_combined)).setText(efficiency.getCombined());
        } else {
            ((TextView) efficiencyView.findViewById(R.id.tv_combined)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (efficiency.getCity() != null && efficiency.getCity().length() != 0) {
            ((TextView) efficiencyView.findViewById(R.id.tv_city)).setText(efficiency.getCity());
        } else {
            ((TextView) efficiencyView.findViewById(R.id.tv_city)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (is_from_usdata)
            ll_Efficiency.addView(efficiencyView);
        else
            ll_EfficiencyCommanUs.addView(efficiencyView);
    }

    void AddViewInBasicData(BasicData basicData, boolean is_from_usdata) {
        View basicDataView = null;

        if (is_from_usdata)
            basicDataView = DataoneActivity.this.getLayoutInflater().inflate(R.layout.basic_data_layout, ll_BasicData, false);
        else
            basicDataView = DataoneActivity.this.getLayoutInflater().inflate(R.layout.basic_data_layout, ll_BasicDataCommanUs, false);

        if (basicData.getPlant() != null && basicData.getPlant().length() != 0) {
            ((TextView) basicDataView.findViewById(R.id.tv_plant)).setText(basicData.getPlant());
        } else {
            ((TextView) basicDataView.findViewById(R.id.tv_plant)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (basicData.getDoors() != null && basicData.getDoors().length() != 0) {
            ((TextView) basicDataView.findViewById(R.id.tv_doors)).setText(basicData.getDoors());
        } else {
            ((TextView) basicDataView.findViewById(R.id.tv_doors)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (basicData.getCountry_of_manufacture() != null && basicData.getCountry_of_manufacture().length() != 0) {
            ((TextView) basicDataView.findViewById(R.id.tv_country_of_manufacture)).setText(basicData.getCountry_of_manufacture());
        } else {
            ((TextView) basicDataView.findViewById(R.id.tv_country_of_manufacture)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (basicData.getModel() != null && basicData.getModel().length() != 0) {
            ((TextView) basicDataView.findViewById(R.id.tv_model)).setText(basicData.getModel());
        } else {
            ((TextView) basicDataView.findViewById(R.id.tv_model)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (basicData.getBrake_system() != null && basicData.getBrake_system().length() != 0) {
            ((TextView) basicDataView.findViewById(R.id.tv_brake_system)).setText(basicData.getBrake_system());
        } else {
            ((TextView) basicDataView.findViewById(R.id.tv_brake_system)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (basicData.getBody_subtype() != null && basicData.getBody_subtype().length() != 0) {
            ((TextView) basicDataView.findViewById(R.id.tv_body_subtype)).setText(basicData.getBody_subtype());
        } else {
            ((TextView) basicDataView.findViewById(R.id.tv_body_subtype)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (basicData.getBody_type() != null && basicData.getBody_type().length() != 0) {
            ((TextView) basicDataView.findViewById(R.id.tv_body_type)).setText(basicData.getBody_type());
        } else {
            ((TextView) basicDataView.findViewById(R.id.tv_body_type)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (basicData.getVehicle_type() != null && basicData.getVehicle_type().length() != 0) {
            ((TextView) basicDataView.findViewById(R.id.tv_vehicle_type)).setText(basicData.getVehicle_type());
        } else {
            ((TextView) basicDataView.findViewById(R.id.tv_vehicle_type)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (basicData.getOem_body_style() != null && basicData.getOem_body_style().length() != 0) {
            ((TextView) basicDataView.findViewById(R.id.tv_oem_body_style)).setText(basicData.getOem_body_style());
        } else {
            ((TextView) basicDataView.findViewById(R.id.tv_oem_body_style)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (basicData.getOem_doors() != null && basicData.getOem_doors().length() != 0) {
            ((TextView) basicDataView.findViewById(R.id.tv_oem_doors)).setText(basicData.getOem_doors());
        } else {
            ((TextView) basicDataView.findViewById(R.id.tv_oem_doors)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (basicData.getRestraint_type() != null && basicData.getRestraint_type().length() != 0) {
            ((TextView) basicDataView.findViewById(R.id.tv_restraint_type)).setText(basicData.getRestraint_type());
        } else {
            ((TextView) basicDataView.findViewById(R.id.tv_restraint_type)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (basicData.getModel_number() != null && basicData.getModel_number().length() != 0) {
            ((TextView) basicDataView.findViewById(R.id.tv_model_number)).setText(basicData.getModel_number());
        } else {
            ((TextView) basicDataView.findViewById(R.id.tv_model_number)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (basicData.getMarket() != null && basicData.getMarket().length() != 0) {
            ((TextView) basicDataView.findViewById(R.id.tv_market)).setText(basicData.getMarket());
        } else {
            ((TextView) basicDataView.findViewById(R.id.tv_market)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (basicData.getPackage_code() != null && basicData.getPackage_code().length() != 0) {
            ((TextView) basicDataView.findViewById(R.id.tv_package_code)).setText(basicData.getPackage_code());
        } else {
            ((TextView) basicDataView.findViewById(R.id.tv_package_code)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (basicData.getDrive_type() != null && basicData.getDrive_type().length() != 0) {
            ((TextView) basicDataView.findViewById(R.id.tv_drive_type)).setText(basicData.getDrive_type());
        } else {
            ((TextView) basicDataView.findViewById(R.id.tv_drive_type)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (basicData.getYear() != null && basicData.getYear().length() != 0) {
            ((TextView) basicDataView.findViewById(R.id.tv_year)).setText(basicData.getYear());
        } else {
            ((TextView) basicDataView.findViewById(R.id.tv_year)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (basicData.getTrim() != null && basicData.getTrim().length() != 0) {
            ((TextView) basicDataView.findViewById(R.id.tv_trim)).setText(basicData.getTrim());
        } else {
            ((TextView) basicDataView.findViewById(R.id.tv_trim)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (basicData.getMake() != null && basicData.getMake().length() != 0) {
            ((TextView) basicDataView.findViewById(R.id.tv_make)).setText(basicData.getMake());
        } else {
            ((TextView) basicDataView.findViewById(R.id.tv_make)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (is_from_usdata)
            ll_BasicData.addView(basicDataView);
        else
            ll_BasicDataCommanUs.addView(basicDataView);
    }

    void AddViewInOtherInfo(OtherInfo otherInfo) {
        View otherInfoView = null;

        otherInfoView = DataoneActivity.this.getLayoutInflater().inflate(R.layout.other_info_layout, ll_OtherInfo, false);

        if (otherInfo.getFleet() != null && otherInfo.getFleet().length() != 0) {
            ((TextView) otherInfoView.findViewById(R.id.tv_fleet)).setText(otherInfo.getFleet());
        } else {
            ((TextView) otherInfoView.findViewById(R.id.tv_fleet)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (otherInfo.getMarket() != null && otherInfo.getMarket().length() != 0) {
            ((TextView) otherInfoView.findViewById(R.id.tv_market)).setText(otherInfo.getMarket());
        } else {
            ((TextView) otherInfoView.findViewById(R.id.tv_market)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (otherInfo.getComplete() != null && otherInfo.getComplete().length() != 0) {
            ((TextView) otherInfoView.findViewById(R.id.tv_complete)).setText(otherInfo.getComplete());
        } else {
            ((TextView) otherInfoView.findViewById(R.id.tv_complete)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (otherInfo.getName() != null && otherInfo.getName().length() != 0) {
            ((TextView) otherInfoView.findViewById(R.id.tv_name)).setText(otherInfo.getName());
        } else {
            ((TextView) otherInfoView.findViewById(R.id.tv_name)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        ll_OtherInfo.addView(otherInfoView);
    }

    void AddViewInPricing(Pricing pricing, boolean is_fron_usdata) {
        View pricingView = null;

        if (is_fron_usdata)
            pricingView = DataoneActivity.this.getLayoutInflater().inflate(R.layout.pricing_layout, ll_Pricing, false);
        else
            pricingView = DataoneActivity.this.getLayoutInflater().inflate(R.layout.pricing_layout, ll_PricingCommanUs, false);

        if (pricing.getMsrp() != null && pricing.getMsrp().length() != 0) {
            ((TextView) pricingView.findViewById(R.id.tv_msrp)).setText(pricing.getMsrp());
        } else {
            ((TextView) pricingView.findViewById(R.id.tv_msrp)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (pricing.getGas_guzzler_tax() != null && pricing.getGas_guzzler_tax().length() != 0) {
            ((TextView) pricingView.findViewById(R.id.tv_gas_guzzler_tax)).setText(pricing.getGas_guzzler_tax());
        } else {
            ((TextView) pricingView.findViewById(R.id.tv_gas_guzzler_tax)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (pricing.getInvoice_price() != null && pricing.getInvoice_price().length() != 0) {
            ((TextView) pricingView.findViewById(R.id.tv_invoice_price)).setText(pricing.getInvoice_price());
        } else {
            ((TextView) pricingView.findViewById(R.id.tv_invoice_price)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (pricing.getDestination_charge() != null && pricing.getDestination_charge().length() != 0) {
            ((TextView) pricingView.findViewById(R.id.tv_destination_charge)).setText(pricing.getDestination_charge());
        } else {
            ((TextView) pricingView.findViewById(R.id.tv_destination_charge)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (is_fron_usdata)
            ll_Pricing.addView(pricingView);
        else
            ll_PricingCommanUs.addView(pricingView);

    }

    void AddTransmission(Transmission transmissionObject, boolean is_from_usdata) {
        View transmissionView = null;

        if (is_from_usdata)
            transmissionView = DataoneActivity.this.getLayoutInflater().inflate(R.layout.transmissionlayout, ll_transmission, false);
        else
            transmissionView = DataoneActivity.this.getLayoutInflater().inflate(R.layout.transmissionlayout, ll_TransmissionCommanUs, false);

        if (transmissionObject.getMsrp() != null && transmissionObject.getMsrp().length() != 0) {
            ((TextView) transmissionView.findViewById(R.id.tv_Msrp)).setText(transmissionObject.getMsrp());
        } else {
            ((TextView) transmissionView.findViewById(R.id.tv_Msrp)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (transmissionObject.getMarketing_name() != null && transmissionObject.getMarketing_name().length() != 0) {
            ((TextView) transmissionView.findViewById(R.id.tv_marketingname)).setText(transmissionObject.getMarketing_name());
        } else {
            ((TextView) transmissionView.findViewById(R.id.tv_marketingname)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (transmissionObject.getName() != null && transmissionObject.getName().length() != 0) {
            ((TextView) transmissionView.findViewById(R.id.tv_Name)).setText(transmissionObject.getName());
        } else {
            ((TextView) transmissionView.findViewById(R.id.tv_Name)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (transmissionObject.getBrand() != null && transmissionObject.getBrand().length() != 0) {
            ((TextView) transmissionView.findViewById(R.id.tv_brand)).setText(transmissionObject.getBrand());
        } else {
            ((TextView) transmissionView.findViewById(R.id.tv_brand)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (transmissionObject.getInvoice_price() != null && transmissionObject.getInvoice_price().length() != 0) {
            ((TextView) transmissionView.findViewById(R.id.tv_invoiceprice)).setText(transmissionObject.getInvoice_price());
        } else {
            ((TextView) transmissionView.findViewById(R.id.tv_invoiceprice)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (transmissionObject.getType() != null && transmissionObject.getType().length() != 0) {
            ((TextView) transmissionView.findViewById(R.id.tv_type)).setText(transmissionObject.getType());
        } else {
            ((TextView) transmissionView.findViewById(R.id.tv_type)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (transmissionObject.getGears() != null && transmissionObject.getGears().length() != 0) {
            ((TextView) transmissionView.findViewById(R.id.tv_gear)).setText(transmissionObject.getGears());
        } else {
            ((TextView) transmissionView.findViewById(R.id.tv_gear)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (transmissionObject.getOrder_code() != null && transmissionObject.getOrder_code().length() != 0) {
            ((TextView) transmissionView.findViewById(R.id.tv_ordercode)).setText(transmissionObject.getOrder_code());
        } else {
            ((TextView) transmissionView.findViewById(R.id.tv_ordercode)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (transmissionObject.getDetail_type() != null && transmissionObject.getDetail_type().length() != 0) {
            ((TextView) transmissionView.findViewById(R.id.tv_detail_type)).setText(transmissionObject.getDetail_type());
        } else {
            ((TextView) transmissionView.findViewById(R.id.tv_detail_type)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (transmissionObject.getAvailability() != null && transmissionObject.getAvailability().length() != 0) {
            ((TextView) transmissionView.findViewById(R.id.tv_availability)).setText(transmissionObject.getAvailability());
        } else {
            ((TextView) transmissionView.findViewById(R.id.tv_availability)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (transmissionObject.getTransmission_id() != null && transmissionObject.getTransmission_id().length() != 0) {
            ((TextView) transmissionView.findViewById(R.id.tv_transmissionid)).setText(transmissionObject.getTransmission_id());
        } else {
            ((TextView) transmissionView.findViewById(R.id.tv_transmissionid)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (is_from_usdata)
            ll_transmission.addView(transmissionView);
        else
            ll_TransmissionCommanUs.addView(transmissionView);
    }

    void AddWarranties(Warranties warrantiesobject, boolean is_from_usdata) {
        View WarrantiesView = null;

        if (is_from_usdata)
            WarrantiesView = DataoneActivity.this.getLayoutInflater().inflate(R.layout.warranties_layout, ll_warranties, false);
        else
            WarrantiesView = DataoneActivity.this.getLayoutInflater().inflate(R.layout.warranties_layout, ll_WarrantiesCommanUs, false);

        if (warrantiesobject.getMiles() != null && warrantiesobject.getMiles().length() != 0) {
            ((TextView) WarrantiesView.findViewById(R.id.tv_miles)).setText(warrantiesobject.getMiles());
        } else {
            ((TextView) WarrantiesView.findViewById(R.id.tv_miles)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (warrantiesobject.getType() != null && warrantiesobject.getType().length() != 0) {
            ((TextView) WarrantiesView.findViewById(R.id.tv_Type)).setText(warrantiesobject.getType());
        } else {
            ((TextView) WarrantiesView.findViewById(R.id.tv_Type)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (warrantiesobject.getMonths() != null && warrantiesobject.getMonths().length() != 0) {
            ((TextView) WarrantiesView.findViewById(R.id.tv_months)).setText(warrantiesobject.getMonths());
        } else {
            ((TextView) WarrantiesView.findViewById(R.id.tv_months)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (warrantiesobject.getName() != null && warrantiesobject.getName().length() != 0) {
            ((TextView) WarrantiesView.findViewById(R.id.tv_namewarranties)).setText(warrantiesobject.getName());
        } else {
            ((TextView) WarrantiesView.findViewById(R.id.tv_namewarranties)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (is_from_usdata)
            ll_warranties.addView(WarrantiesView);
        else
            ll_WarrantiesCommanUs.addView(WarrantiesView);
    }

    void AddViewInSafetyEquipment(SafetyEquipment safetyEquipment, boolean is_from_usdata) {
        View safetyEquipmentView = null;

        if (is_from_usdata)
            safetyEquipmentView = DataoneActivity.this.getLayoutInflater().inflate(R.layout.safety_equipmentlayout, ll_SafetyEquipment, false);
        else
            safetyEquipmentView = DataoneActivity.this.getLayoutInflater().inflate(R.layout.safety_equipmentlayout, ll_SafetyEquipmentCommanUs, false);

        if (safetyEquipment.getRollover_stability_control() != null && safetyEquipment.getRollover_stability_control().length() != 0) {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_rollover_stability_control)).setText(safetyEquipment.getRollover_stability_control());
        } else {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_rollover_stability_control)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (safetyEquipment.getTire_pressure_monitoring_system() != null && safetyEquipment.getTire_pressure_monitoring_system().length() != 0) {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_tire_pressure_monitoring_system)).setText(safetyEquipment.getTire_pressure_monitoring_system());
        } else {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_tire_pressure_monitoring_system)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (safetyEquipment.getDaytime_running_lights() != null && safetyEquipment.getDaytime_running_lights().length() != 0) {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_daytime_running_lights)).setText(safetyEquipment.getDaytime_running_lights());
        } else {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_daytime_running_lights)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (safetyEquipment.getAbs_two_wheel() != null && safetyEquipment.getAbs_two_wheel().length() != 0) {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_abs_two_wheel)).setText(safetyEquipment.getAbs_two_wheel());
        } else {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_abs_two_wheel)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (safetyEquipment.getAirbags_front_passenger() != null && safetyEquipment.getAirbags_front_passenger().length() != 0) {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_airbags_front_passenger)).setText(safetyEquipment.getAirbags_front_passenger());
        } else {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_airbags_front_passenger)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (safetyEquipment.getAirbags_front_driver() != null && safetyEquipment.getAirbags_front_driver().length() != 0) {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_airbags_front_driver)).setText(safetyEquipment.getAirbags_front_driver());
        } else {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_airbags_front_driver)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (safetyEquipment.getElectronic_stability_control() != null && safetyEquipment.getElectronic_stability_control().length() != 0) {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_electronic_stability_control)).setText(safetyEquipment.getElectronic_stability_control());
        } else {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_electronic_stability_control)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (safetyEquipment.getAirbags_side_impact() != null && safetyEquipment.getAirbags_side_impact().length() != 0) {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_airbags_side_impact)).setText(safetyEquipment.getAirbags_side_impact());
        } else {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_airbags_side_impact)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (safetyEquipment.getAbs_four_wheel() != null && safetyEquipment.getAbs_four_wheel().length() != 0) {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_abs_four_wheel)).setText(safetyEquipment.getAbs_four_wheel());
        } else {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_abs_four_wheel)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (safetyEquipment.getBrake_assist() != null && safetyEquipment.getBrake_assist().length() != 0) {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_brake_assist)).setText(safetyEquipment.getBrake_assist());
        } else {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_brake_assist)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (safetyEquipment.getAirbags_side_curtain() != null && safetyEquipment.getAirbags_side_curtain().length() != 0) {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_airbags_side_curtain)).setText(safetyEquipment.getAirbags_side_curtain());
        } else {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_airbags_side_curtain)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (safetyEquipment.getElectronic_traction_control() != null && safetyEquipment.getElectronic_traction_control().length() != 0) {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_electronic_traction_control)).setText(safetyEquipment.getElectronic_traction_control());
        } else {
            ((TextView) safetyEquipmentView.findViewById(R.id.tv_electronic_traction_control)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (is_from_usdata)
            ll_SafetyEquipment.addView(safetyEquipmentView);
        else
            ll_SafetyEquipmentCommanUs.addView(safetyEquipmentView);
    }

    void AddViewInInstalledEquipment(ArrayList<Installed_Equipment> list, boolean is_from_usdata) {
        for (int i = 0; i < list.size(); i++) {
            View viewIE = null;

            if (is_from_usdata)
                viewIE = DataoneActivity.this.getLayoutInflater().inflate(R.layout.installed_equipment_layout, ll_InstalledEquipment, false);
            else
                viewIE = DataoneActivity.this.getLayoutInflater().inflate(R.layout.installed_equipment_layout, ll_InstalledEquipmentCommanUs, false);

            if (list.get(i).getCategory() != null && list.get(i).getCategory().length() != 0) {
                ((TextView) viewIE.findViewById(R.id.tv_categoryIE)).setText(list.get(i).getCategory());
            } else {
                ((TextView) viewIE.findViewById(R.id.tv_categoryIE)).setText(Constant.KEY_NOT_AVAILABLE);
            }
            ll_Equipment = (LinearLayout) viewIE.findViewById(R.id.ll_equipment);

            for (int j = 0; j < list.get(i).getEquipment().size(); j++) {
                View viewE = null;

                viewE = DataoneActivity.this.getLayoutInflater().inflate(R.layout.equipment_layout, ll_Equipment, false);

                if (list.get(i).getEquipment().get(j).getName() != null && list.get(i).getEquipment().get(j).getName().length() != 0) {
                    ((TextView) viewE.findViewById(R.id.tv_nameIE)).setText(list.get(i).getEquipment().get(j).getName());
                } else {
                    ((TextView) viewE.findViewById(R.id.tv_nameIE)).setText(Constant.KEY_NOT_AVAILABLE);
                }
                ll_Values = (LinearLayout) viewE.findViewById(R.id.ll_values);

                for (int k = 0; k < list.get(i).getEquipment().get(j).getValues().size(); k++) {
                    View viewV = null;

                    viewV = DataoneActivity.this.getLayoutInflater().inflate(R.layout.values_layout, ll_Values, false);

                    if (list.get(i).getEquipment().get(j).getValues().get(k).getValue() != null &&
                            list.get(i).getEquipment().get(j).getValues().get(k).getValue().length() != 0) {
                        String strValue = list.get(i).getEquipment().get(j).getValues().get(k).getValue();
                        String upperValue = strValue.substring(0, 1).toUpperCase() + strValue.substring(1);
                        ((TextView) viewV.findViewById(R.id.tv_valueIE)).setText(upperValue);
                    } else {
                        ((TextView) viewV.findViewById(R.id.tv_valueIE)).setText(Constant.KEY_NOT_AVAILABLE);
                    }
                    ll_Values.addView(viewV);
                }
                ll_Equipment.addView(viewE);
            }

            if (is_from_usdata)
                ll_InstalledEquipment.addView(viewIE);
            else
                ll_InstalledEquipmentCommanUs.addView(viewIE);
        }
    }

    void AddOptionalEquipment(String category, OptionsC optionalEquipmentObject) {
        View optionalequipmentview = null;

        optionalequipmentview = DataoneActivity.this.getLayoutInflater().inflate(R.layout.optional_equipment, ll_optionalEquipment, false);
        if (category != null && category.length() != 0) {
            ((TextView) optionalequipmentview.findViewById(R.id.tv_categoryoptionalEquipment)).setText(category);
        } else {
            ((TextView) optionalequipmentview.findViewById(R.id.tv_categoryoptionalEquipment)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (optionalEquipmentObject.getInstall_type() != null && optionalEquipmentObject.getInstall_type().length() != 0) {
            ((TextView) optionalequipmentview.findViewById(R.id.tv_install_type)).setText(optionalEquipmentObject.getInstall_type());
        } else {
            ((TextView) optionalequipmentview.findViewById(R.id.tv_install_type)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (optionalEquipmentObject.getFleet() != null && optionalEquipmentObject.getFleet().length() != 0) {
            ((TextView) optionalequipmentview.findViewById(R.id.tv_fleet)).setText(optionalEquipmentObject.getFleet());
        } else {
            ((TextView) optionalequipmentview.findViewById(R.id.tv_fleet)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (optionalEquipmentObject.getMsrp() != null && optionalEquipmentObject.getMsrp().length() != 0) {
            ((TextView) optionalequipmentview.findViewById(R.id.tv_msrpOptionalEquipment)).setText(optionalEquipmentObject.getMsrp());
        } else {
            ((TextView) optionalequipmentview.findViewById(R.id.tv_msrpOptionalEquipment)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (optionalEquipmentObject.getDescription() != null && optionalEquipmentObject.getDescription().length() != 0) {
            ((TextView) optionalequipmentview.findViewById(R.id.tv_description)).setText(optionalEquipmentObject.getDescription());
        } else {
            ((TextView) optionalequipmentview.findViewById(R.id.tv_description)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (optionalEquipmentObject.getInstalled() != null && optionalEquipmentObject.getInstalled().length() != 0) {
            ((TextView) optionalequipmentview.findViewById(R.id.tv_installedOptionalEqipment)).setText(optionalEquipmentObject.getInstalled());
        } else {
            ((TextView) optionalequipmentview.findViewById(R.id.tv_installedOptionalEqipment)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (optionalEquipmentObject.getName() != null && optionalEquipmentObject.getName().length() != 0) {
            ((TextView) optionalequipmentview.findViewById(R.id.tv_NameOptionalEqipment)).setText(optionalEquipmentObject.getName());
        } else {
            ((TextView) optionalequipmentview.findViewById(R.id.tv_NameOptionalEqipment)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        if (optionalEquipmentObject.getInvoice_price() != null && optionalEquipmentObject.getInvoice_price().length() != 0) {
            ((TextView) optionalequipmentview.findViewById(R.id.tv_invoicepriceOptionalEqipment)).setText(optionalEquipmentObject.getInvoice_price());
        } else {
            ((TextView) optionalequipmentview.findViewById(R.id.tv_invoicepriceOptionalEqipment)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (optionalEquipmentObject.getOption_id() != null && optionalEquipmentObject.getOption_id().length() != 0) {
            ((TextView) optionalequipmentview.findViewById(R.id.tv_option_id)).setText(optionalEquipmentObject.getOption_id());
        } else {
            ((TextView) optionalequipmentview.findViewById(R.id.tv_option_id)).setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (optionalEquipmentObject.getOrder_code() != null && optionalEquipmentObject.getOrder_code().length() != 0) {
            ((TextView) optionalequipmentview.findViewById(R.id.tv_order_code)).setText(optionalEquipmentObject.getOrder_code());
        } else {
            ((TextView) optionalequipmentview.findViewById(R.id.tv_order_code)).setText(Constant.KEY_NOT_AVAILABLE);
        }
        ll_optionalEquipment.addView(optionalequipmentview);
    }

    void AddSpecification(ArrayList<Specifications> list, boolean is_from_usdata) {

        for (int i = 0; i < list.size(); i++) {
            View view = null;

            if (is_from_usdata)
                view = DataoneActivity.this.getLayoutInflater().inflate(R.layout.specificationlayout, ll_Specification, false);
            else
                view = DataoneActivity.this.getLayoutInflater().inflate(R.layout.specificationlayout, ll_specificationCommanUs, false);

            if (list.get(i).getCategory() != null && list.get(i).getCategory().length() != 0) {
                ((TextView) view.findViewById(R.id.tv_categorySpecification)).setText(list.get(i).getCategory());
            } else {
                ((TextView) view.findViewById(R.id.tv_categorySpecification)).setText(Constant.KEY_NOT_AVAILABLE);
            }

            ll_Specifics = (LinearLayout) view.findViewById(R.id.ll_specifics);

            for (int j = 0; j < list.get(i).specifications.size(); j++) {
                View viewS = null;

                viewS = DataoneActivity.this.getLayoutInflater().inflate(R.layout.specifics_layout, ll_Specifics, false);

                if (list.get(i).getSpecifications().get(j).getValue() != null &&
                        list.get(i).getSpecifications().get(j).getValue().length() != 0) {
                    String strValue = list.get(i).getSpecifications().get(j).getValue();
                    String upperValue = strValue.substring(0, 1).toUpperCase() + strValue.substring(1);

                    ((TextView) viewS.findViewById(R.id.tv_valueSpecifics)).setText(upperValue);
                } else {
                    ((TextView) viewS.findViewById(R.id.tv_valueSpecifics)).setText(Constant.KEY_NOT_AVAILABLE);
                }
                if (list.get(i).getSpecifications().get(j).getName() != null &&
                        list.get(i).getSpecifications().get(j).getName().length() != 0) {
                    ((TextView) viewS.findViewById(R.id.tv_nameSpecifics)).setText(list.get(i).getSpecifications().get(j).getName());
                } else {
                    ((TextView) viewS.findViewById(R.id.tv_nameSpecifics)).setText(Constant.KEY_NOT_AVAILABLE);
                }
                ll_Specifics.addView(viewS);
            }

            if (is_from_usdata)
                ll_Specification.addView(view);
            else
                ll_specificationCommanUs.addView(view);
        }
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.data_one_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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


    private void pullDataoneInformations() {
        Log.d("getpullDataone", "Informations getpullDataoneInformations");

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                showToast("Dataone Request Failed.");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                dataoneInformation = "";
                dataoneInformation = response.toString();

                ParentNode orm = AppSingleTon.APP_JSON_PARSER.parseDataoneResponse(response);

                DecoderMessages decoderMessages = orm.decoder_messages;

                RequestSample requestSample = new RequestSample();
                Query_Error queryError = orm.query_responses.RequestSample.query_error;
                UsMarketData usMarketData = orm.query_responses.RequestSample.us_market_data;
                CommonUsData commonUsData = orm.query_responses.RequestSample.us_market_data.common_us_data;
                requestSample.transaction_id = orm.query_responses.RequestSample.transaction_id;

                if (!queryError.error_code.equals("")) {
                    Log.e("Querry Error", " " + queryError.error_message + " " + queryError.error_code);
                    return;
                }
                setData(orm, false);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissProgressDialog();
            }

            @Override
            public void onStart() {
                super.onStart();
                showUpdateProgressDialog("Getting Dataone Data...");
            }
        };

        RequestParams params = new RequestParams();
        String decoder_query = getQueryParameters();
        params.put(Constant.KEY_CLIENT_ID, getString(R.string.dataone_client_id));
        params.put(Constant.KEY_AUTH_CODE, getString(R.string.dataone_authorization_code));
        params.put(Constant.KEY_DECODER_QUERY, decoder_query);
        RestClient.post(this, AppSingleTon.APP_URL.URL_DATA_ONE, params, handler);
    }

    void storeDataToServer() {
        Intent serviceIntent = new Intent(this, StoreDataOneDataService.class);
        serviceIntent.putExtra(Constant.EXTRA_KEY_DATAONE_INFO, dataoneInformation);
        serviceIntent.putExtra(Constant.EXTRA_KEY_VIN, vin);
        startService(serviceIntent);
    }


    String getQueryParameters() {
        String decoder_query = getString(R.string.decoder_query_json);
        decoder_query = decoder_query.replace("xxxxx", vin);
        return decoder_query;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (vin != null && vin.length() == 17) {
            client.connect();
            Action viewAction = Action.newAction(
                    Action.TYPE_VIEW, // TODO: choose an action type.
                    "Dataone Page", // TODO: Define a title for the content shown.
                    Uri.parse(Constant.URI_PARSE_MAIN),
                    Uri.parse(Constant.URI_PARSE_SUB)
            );
            AppIndex.AppIndexApi.start(client, viewAction);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (vin != null && vin.length() == 17) {
            Action viewAction = Action.newAction(
                    Action.TYPE_VIEW, // TODO: choose an action type.
                    "Dataone Page", // TODO: Define a title for the content shown.
                    Uri.parse(Constant.URI_PARSE_MAIN),
                    Uri.parse(Constant.URI_PARSE_SUB)
            );
            AppIndex.AppIndexApi.end(client, viewAction);
            client.disconnect();
        }
    }
}
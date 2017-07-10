package com.yukti.driveherenew;


import android.location.Location;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.yukti.driveherenew.fragment.FRAGVin;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.Constant;
import com.yukti.utils.ParamsKey;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntityBuilder;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;

public class MultipartRequest extends Request<String> {

    private HttpEntity mHttpEntity;
    private Response.Listener mListener;

    public MultipartRequest(String url,
                            Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        mHttpEntity = buildMultipartEntity();
    }

    private HttpEntity buildMultipartEntity() {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        FileBody fileBody;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm aa");
        String formattedDate = df.format(c.getTime());

        builder.addTextBody(ParamsKey.KEY_userId, AppSingleTon.SHARED_PREFERENCE.getUserId());
        Log.e(ParamsKey.KEY_userId, "" + AppSingleTon.SHARED_PREFERENCE.getUserId());

        builder.addTextBody(ParamsKey.KEY_type, Constant.APP_TYPE);
        Log.e(ParamsKey.KEY_type, "" + Constant.APP_TYPE);


        builder.addTextBody(ParamsKey.KEY_rfid, AddNewCarActivity.addCarModelObject.getStrRfid());
        Log.e(ParamsKey.KEY_rfid, AddNewCarActivity.addCarModelObject.getStrRfid());

        builder.addTextBody(ParamsKey.KEY_vin, AddNewCarActivity.addCarModelObject.getStrVin());
        Log.e(ParamsKey.KEY_vin, AddNewCarActivity.addCarModelObject.getStrVin());

        builder.addTextBody(ParamsKey.KEY_vacancy, AddNewCarActivity.addCarModelObject.getStrVacancy());
        Log.e(ParamsKey.KEY_vacancy, AddNewCarActivity.addCarModelObject.getStrVacancy());

        builder.addTextBody(ParamsKey.KEY_vehicleStatus, AddNewCarActivity.addCarModelObject.getStrStatus());
        Log.e(ParamsKey.KEY_vehicleStatus, AddNewCarActivity.addCarModelObject.getStrStatus());

        builder.addTextBody(ParamsKey.KEY_stage, AddNewCarActivity.addCarModelObject.getStrStage());
        Log.e(ParamsKey.KEY_stage, AddNewCarActivity.addCarModelObject.getStrStage());

        builder.addTextBody(ParamsKey.KEY_serviceStage, AddNewCarActivity.addCarModelObject.getStrServiceStage());
        Log.e(ParamsKey.KEY_serviceStage, AddNewCarActivity.addCarModelObject.getStrServiceStage());

        builder.addTextBody(ParamsKey.KEY_lotCode, AddNewCarActivity.addCarModelObject.getStrLotCode());
        Log.e(ParamsKey.KEY_lotCode, AddNewCarActivity.addCarModelObject.getStrLotCode());

        builder.addTextBody(ParamsKey.KEY_miles, AddNewCarActivity.addCarModelObject.getStrMiles());
        Log.e(ParamsKey.KEY_miles, AddNewCarActivity.addCarModelObject.getStrMiles());

        String gpsJsonStr = getGpsJsonString();
        builder.addTextBody(ParamsKey.KEY_gps, gpsJsonStr);
        Log.e(ParamsKey.KEY_gps, gpsJsonStr);

        builder.addTextBody(ParamsKey.KEY_gpsInstalled, AddNewCarActivity.addCarModelObject.getStrGpsInstall());
        Log.e(ParamsKey.KEY_gpsInstalled, AddNewCarActivity.addCarModelObject.getStrGpsInstall());


        builder.addTextBody(ParamsKey.KEY_hasTitle, AddNewCarActivity.addCarModelObject.getStrHasTitle());
        Log.e(ParamsKey.KEY_hasTitle, AddNewCarActivity.addCarModelObject.getStrHasTitle());

        builder.addTextBody(ParamsKey.KEY_titleLotCode, AddNewCarActivity.addCarModelObject.getStrTitleLot());
        Log.e(ParamsKey.KEY_titleLotCode, AddNewCarActivity.addCarModelObject.getStrTitleLot());

        builder.addTextBody(ParamsKey.KEY_stockNumber, AddNewCarActivity.addCarModelObject.getStrStockNumber());
        Log.e(ParamsKey.KEY_stockNumber, AddNewCarActivity.addCarModelObject.getStrStockNumber());

        builder.addTextBody(ParamsKey.KEY_problem, AddNewCarActivity.addCarModelObject.getStrVehicleProblem());
        Log.e(ParamsKey.KEY_problem, AddNewCarActivity.addCarModelObject.getStrVehicleProblem());

        builder.addTextBody(ParamsKey.KEY_note, AddNewCarActivity.addCarModelObject.getStrVehicleNote());
        Log.e(ParamsKey.KEY_note, AddNewCarActivity.addCarModelObject.getStrVehicleNote() + " " + AddNewCarActivity.addCarModelObject.getStrVehicleNote());

        builder.addTextBody(ParamsKey.KEY_company, AddNewCarActivity.addCarModelObject.getStrCompany());
        Log.e(ParamsKey.KEY_company, AddNewCarActivity.addCarModelObject.getStrCompany());

        builder.addTextBody(ParamsKey.KEY_purchaseFrom, AddNewCarActivity.addCarModelObject.getStrPurchaseForm());
        Log.e(ParamsKey.KEY_purchaseFrom, AddNewCarActivity.addCarModelObject.getStrPurchaseForm());

        builder.addTextBody(ParamsKey.KEY_inspectionDate, AddNewCarActivity.addCarModelObject.getStrInspectionDate());
        Log.e(ParamsKey.KEY_inspectionDate, AddNewCarActivity.addCarModelObject.getStrInspectionDate());

        builder.addTextBody(ParamsKey.KEY_registrationDate, AddNewCarActivity.addCarModelObject.getStrRegistrationDate());
        Log.e(ParamsKey.KEY_registrationDate, AddNewCarActivity.addCarModelObject.getStrRegistrationDate());

        builder.addTextBody(ParamsKey.KEY_insuranceDate, AddNewCarActivity.addCarModelObject.getStrInsuranceDate());
        Log.e(ParamsKey.KEY_insuranceDate, AddNewCarActivity.addCarModelObject.getStrInsuranceDate());

        builder.addTextBody(ParamsKey.KEY_gasTank, AddNewCarActivity.addCarModelObject.getStrGasTank());
        Log.e(ParamsKey.KEY_gasTank, AddNewCarActivity.addCarModelObject.getStrGasTank());


        builder.addTextBody(ParamsKey.KEY_titleLocation, AddNewCarActivity.addCarModelObject.getStrLocationTitle());
        Log.e(ParamsKey.KEY_titleLocation, AddNewCarActivity.addCarModelObject.getStrLocationTitle());

        builder.addTextBody(ParamsKey.KEY_salesPrice, AddNewCarActivity.addCarModelObject.getStrSalesPrice());
        Log.e(ParamsKey.KEY_salesPrice, AddNewCarActivity.addCarModelObject.getStrSalesPrice());

        builder.addTextBody(ParamsKey.KEY_mechanic, AddNewCarActivity.addCarModelObject.getStrMechanic());
        Log.e(ParamsKey.KEY_mechanic, AddNewCarActivity.addCarModelObject.getStrMechanic());

        builder.addTextBody(ParamsKey.KEY_color, AddNewCarActivity.addCarModelObject.getStrcolorcode());
        Log.e(ParamsKey.KEY_color, AddNewCarActivity.addCarModelObject.getStrcolorcode());

        builder.addTextBody(ParamsKey.KEY_make, AddNewCarActivity.addCarModelObject.getStrMake());
        Log.e(ParamsKey.KEY_make, AddNewCarActivity.addCarModelObject.getStrMake());

        builder.addTextBody(ParamsKey.KEY_model, AddNewCarActivity.addCarModelObject.getStrModel());
        Log.e(ParamsKey.KEY_model, AddNewCarActivity.addCarModelObject.getStrModel());

        builder.addTextBody(ParamsKey.KEY_modelNumber, AddNewCarActivity.addCarModelObject.getStrModelNumber());
        Log.e(ParamsKey.KEY_modelNumber, AddNewCarActivity.addCarModelObject.getStrModelNumber());

        builder.addTextBody(ParamsKey.KEY_modelYear, AddNewCarActivity.addCarModelObject.getStrModelYear());
        Log.e(ParamsKey.KEY_modelYear, AddNewCarActivity.addCarModelObject.getStrModelYear());

        builder.addTextBody(ParamsKey.KEY_maxHP, AddNewCarActivity.addCarModelObject.getStrMaxHp());
        Log.e(ParamsKey.KEY_maxHP, AddNewCarActivity.addCarModelObject.getStrMaxHp());

        builder.addTextBody(ParamsKey.KEY_maxTorque, AddNewCarActivity.addCarModelObject.getStrMaxTorque());
        Log.e(ParamsKey.KEY_maxTorque, AddNewCarActivity.addCarModelObject.getStrMaxTorque());

        builder.addTextBody(ParamsKey.KEY_fuelType, AddNewCarActivity.addCarModelObject.getStrFuelType());
        Log.e(ParamsKey.KEY_fuelType, AddNewCarActivity.addCarModelObject.getStrFuelType());

        builder.addTextBody(ParamsKey.KEY_oilCapacity, AddNewCarActivity.addCarModelObject.getStrOilCapacity());
        Log.e(ParamsKey.KEY_oilCapacity, AddNewCarActivity.addCarModelObject.getStrOilCapacity());

        builder.addTextBody(ParamsKey.KEY_driveType, AddNewCarActivity.addCarModelObject.getStrDriveType());
        Log.e(ParamsKey.KEY_driveType, AddNewCarActivity.addCarModelObject.getStrDriveType());

        builder.addTextBody(ParamsKey.KEY_cylinder, AddNewCarActivity.addCarModelObject.getStrCylinder());
        Log.e(ParamsKey.KEY_cylinder, AddNewCarActivity.addCarModelObject.getStrCylinder());

        builder.addTextBody(ParamsKey.KEY_vehicleType, AddNewCarActivity.addCarModelObject.getStrVehicleType());
        Log.e(ParamsKey.KEY_vehicleType, AddNewCarActivity.addCarModelObject.getStrVehicleType());

        //for photo all
        if (AddNewCarActivity.addCarModelObject.arrayImagePath != null && AddNewCarActivity.addCarModelObject.arrayImagePath.size() > 0) {
            for (int i = 0; i < AddNewCarActivity.addCarModelObject.arrayImagePath.size(); i++) {
                if (!AddNewCarActivity.addCarModelObject.arrayImagePath.get(i).contains("http:")) {

                    for (int k = 0; k < AddNewCarActivity.addCarModelObject.arrayImagePath.size(); k++) {
                        String final_path;
                        if (AddNewCarActivity.addCarModelObject.arrayImagePath.get(k).contains("file:")) {
                            final_path = AddNewCarActivity.addCarModelObject.arrayImagePath.get(k).replace("file:", "");
                            builder.addTextBody(ParamsKey.KEY_photoCount, k + 1 + "");
                            Log.e(ParamsKey.KEY_photoCount, "=" + k + 1);
                            File file = new File(final_path);
                            FileBody Fbody = new FileBody(file);
                            builder.addPart(ParamsKey.KEY_photo + (k + 1), Fbody);
                            Log.e(ParamsKey.KEY_photo, "" + (k + 1) + Fbody);
                        }
                    }
                }
            }
        }

        //for title photo
        if (AddNewCarActivity.addCarModelObject.arrayTitleImagePath != null
                && AddNewCarActivity.addCarModelObject.arrayTitleImagePath.size() > 0 ) {
            builder.addTextBody(ParamsKey.KEY_titlePhotoCount, AddNewCarActivity.addCarModelObject.arrayTitleImagePath.size() + "");
            Log.e(ParamsKey.KEY_titlePhotoCount, "=" + AddNewCarActivity.addCarModelObject.arrayTitleImagePath.size());

            for (int k = 0; k < AddNewCarActivity.addCarModelObject.arrayTitleImagePath.size(); k++) {
                String final_path;
                if (AddNewCarActivity.addCarModelObject.arrayTitleImagePath.get(k).contains("file:")) {
                    final_path = AddNewCarActivity.addCarModelObject.arrayTitleImagePath.get(k).replace("file:", "");
                } else {
                    final_path = AddNewCarActivity.addCarModelObject.arrayTitleImagePath.get(k);
                }
                File file = new File(final_path);
                FileBody Fbody = new FileBody(file);
                builder.addPart(ParamsKey.KEY_titlePhoto + "_" + (k + 1), Fbody);
                Log.e(ParamsKey.KEY_titlePhoto + "_", "" + (k + 1) + Fbody);

            }
        }
        //for company insurance photo
        if (AddNewCarActivity.addCarModelObject.companyInsuranceFile != null) {
            Log.e("companyInsuranceFile", " =" + AddNewCarActivity.addCarModelObject.companyInsuranceFile.getPath().toString());
            fileBody = new FileBody(AddNewCarActivity.addCarModelObject.companyInsuranceFile);
            builder.addPart(ParamsKey.KEY_companyInsurancePhoto, fileBody);
        } else {
            Log.e("File not found", "insurance file not put");
        }


        if (FRAGVin.dataoneInformation != null) {
            builder.addTextBody(ParamsKey.KEY_dataoneInformation,
                    Common.Encode_String(FRAGVin.dataoneInformation));
            Log.e(ParamsKey.KEY_dataoneInformation, FRAGVin.dataoneInformation);
            Log.e("dataoneinfo ", "  =" + Common.Encode_String(FRAGVin.dataoneInformation));
        }
        Location location = AppSingleTon.PLAY_MANAGER.getLastLocation();

        if (location != null) {
            builder.addTextBody(ParamsKey.KEY_latitude, location.getLatitude() + "");
            builder.addTextBody(ParamsKey.KEY_longitude, location.getLongitude() + "");
        } else {
            builder.addTextBody(ParamsKey.KEY_latitude, "");
            builder.addTextBody(ParamsKey.KEY_longitude, "");
        }

        if (AddNewCarActivity.isedit) {
            if (AddNewCarActivity.addCarModelObject.deleteImagePath != null && AddNewCarActivity.addCarModelObject.deleteImagePath.size() > 0) {
                builder.addTextBody(ParamsKey.KEY_deletePhotoLink, String.valueOf(AddNewCarActivity.addCarModelObject.deleteImagePath));
                Log.e(ParamsKey.KEY_deletePhotoLink, " =" + AddNewCarActivity.addCarModelObject.deleteImagePath);
            }

            builder.addTextBody(ParamsKey.KEY_carId, AddNewCarActivity.addCarModelObject.getCarId());
            Log.e(ParamsKey.KEY_carId, "" + AddNewCarActivity.addCarModelObject.getCarId());

            builder.addTextBody(ParamsKey.KEY_editParameter, String.valueOf(AddNewCarActivity.addCarModelObject.editFilied));
            Log.e(ParamsKey.KEY_editParameter, " =" + String.valueOf(AddNewCarActivity.addCarModelObject.editFilied));
        } else {
            builder.addTextBody(ParamsKey.KEY_carId, "0");
            Log.e(ParamsKey.KEY_carId, "" + "0");
        }
        return builder.build();
    }

    @Override
    public String getBodyContentType() {
        return mHttpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mHttpEntity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(new String(response.data, "UTF-8"),
                    getCacheEntry());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            return Response.success(new String(response.data), getCacheEntry());
        }
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
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
}
package com.yukti.dataone.model;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;


import com.yukti.utils.ParamsKey;

import java.io.File;
import java.util.ArrayList;

public class AddCarModel implements Parcelable {
    String preVin = "";
    String strRfid = "";
    String strVin = "";
    String strStatus = "";
    String strStage = "";
    String strServiceStage = "";
    String strGpsInstall = "";
    String strcolorcode = "";
    String strMake = "";
    String strModel = "";
    String strModelNumber = "";
    String strModelYear = "";
    String strVehicleType = "";
    String strDriveType = "";
    String strMaxHp = "";
    String strMaxTorque = "";
    String strCylinder = "";
    String strOilCapacity = "";
    String strFuelType = "";
    String strColor = "";
    String strMiles = "";
    String strType = "";
    String strSalesPrice = "";
    String strSalesType = "";
    String strStockNumber = "";
    String strVehicleProblem = "";
    String strVehicleNote = "";
    String strCompany = "";
    String strPurchaseForm = "";
    String strInspectionDate = "";
    String strRegistrationDate = "";
    String strInsuranceDate = "";
    String strGasTank = "";
    String strHasTitle = "No";
    String strLocationTitle = "";
    String strMechanic = "";
    String strCompanyInsurance = "";
    String strVacancy = "";
    String strpreVin = "";
    String strTitleLot = "";

    String strLotCode = "";
    String carId = "";
    String is_done = "";

    public String getIs_done() {
        return is_done;
    }

    public void setIs_done(String is_done) {
        if (is_done != null)
            this.is_done = is_done;
        else
            this.is_done = "0";
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        if (carId != null)
            this.carId = carId;
        else
            this.carId = "";
    }

    public File companyInsuranceFile;
    public ArrayList<String> deleteImagePath = new ArrayList<String>();
    ;
    public ArrayList<String> arrayTitleImagePath = new ArrayList<String>();
    ;
    public ArrayList<String> arrayImagePath = new ArrayList<String>();
    ;
    public ArrayList<String> arrayListGpsSerial = new ArrayList<String>();
    ;
    public ArrayList<String> editFilied = new ArrayList<>();


    public ArrayList<String> getArrayImagePath() {
        return arrayImagePath;
    }

    public void setArrayImagePath(ArrayList<String> arrayImagePath) {
        this.arrayImagePath = arrayImagePath;
    }

    public ArrayList<String> getArrayListGpsSerial() {
        return arrayListGpsSerial;
    }

    public void setArrayListGpsSerial(ArrayList<String> arrayListGpsSerial) {
        this.arrayListGpsSerial = arrayListGpsSerial;
    }


    public File getCompanyInsuranceFile() {
        return companyInsuranceFile;
    }

    public void setCompanyInsuranceFile(File companyInsuranceFile) {
        this.companyInsuranceFile = companyInsuranceFile;
    }

    public ArrayList<String> getArrayTitleImagePath() {
        return arrayTitleImagePath;
    }

    public void setArrayTitleImagePath(ArrayList<String> arrayTitleImagePath) {
        this.arrayTitleImagePath = arrayTitleImagePath;
    }

    public String getStrTitleLot() {
        return strTitleLot;
    }

    public void setStrTitleLot(String strTitleLot) {
        if (strTitleLot != null)
            this.strTitleLot = strTitleLot;
        else
            this.strTitleLot = "";
    }

    public boolean isFrmInfo = false;
    public boolean isDataOneInfoFound = false;

    Context context;

    public AddCarModel(Context context) {
        this.context = context;
    }

    public String getPreVin() {
        return preVin;
    }

    public void setPreVin(String preVin) {
        this.preVin = preVin;
    }

    public String getStrColor() {
        return strColor;
    }

    public void setStrColor(String strColor) {
        if (strColor != null)
            this.strColor = strColor;
        else
            this.strColor = "";
    }

    public String getStrcolorcode() {
        return strcolorcode;
    }

    public void setStrcolorcode(String strcolorcode) {
        if (strcolorcode != null)
            this.strcolorcode = strcolorcode;
        else
            this.strcolorcode = "";
    }

    public String getStrCompany() {
        return strCompany;
    }

    public void setStrCompany(String strCompany) {
        if (strCompany != null)
            this.strCompany = strCompany;
        else
            this.strCompany = "";
    }

    public String getStrCompanyInsurance() {
        return strCompanyInsurance;
    }

    public void setStrCompanyInsurance(String strCompanyInsurance) {
        this.strCompanyInsurance = strCompanyInsurance;
    }

    public String getStrCylinder() {
        return strCylinder;
    }

    public void setStrCylinder(String strCylinder) {
        if (strCylinder != null)
            this.strCylinder = strCylinder;
        else
            this.strCylinder = "";
    }

    public String getStrDriveType() {
        return strDriveType;
    }

    public void setStrDriveType(String strDriveType) {
        if (strDriveType != null)
            this.strDriveType = strDriveType;
        else
            this.strDriveType = "";
    }

    public String getStrFuelType() {
        return strFuelType;
    }

    public void setStrFuelType(String strFuelType) {
        if (strFuelType != null)
            this.strFuelType = strFuelType;
        else
            this.strFuelType = "";
    }

    public String getStrGasTank() {
        return strGasTank;
    }

    public void setStrGasTank(String strGasTank) {
        if (strGasTank != null)
            this.strGasTank = strGasTank;
        else
            this.strGasTank = "";
    }

    public String getStrGpsInstall() {
        return strGpsInstall;
    }

    public void setStrGpsInstall(String strGpsInstall) {

        if (strGpsInstall != null)
            this.strGpsInstall = strGpsInstall;
        else
            this.strGpsInstall = "";
    }

    public String getStrHasTitle() {
        return strHasTitle;
    }

    public void setStrHasTitle(String strHasTitle) {
        if (strHasTitle != null)
            this.strHasTitle = strHasTitle;
        else
            this.strHasTitle = "";
    }

    public String getStrInspectionDate() {
        return strInspectionDate;
    }

    public void setStrInspectionDate(String strInspectionDate) {
        if (strInspectionDate != null)
            this.strInspectionDate = strInspectionDate;
        else
            this.strInspectionDate = "";
    }

    public String getStrInsuranceDate() {
        return strInsuranceDate;
    }

    public void setStrInsuranceDate(String strInsuranceDate) {
        if (strInsuranceDate != null)
            this.strInsuranceDate = strInsuranceDate;
        else
            this.strRegistrationDate = "";
    }

    public String getStrLocationTitle() {
        return strLocationTitle;
    }

    public void setStrLocationTitle(String strLocationTitle) {
        if (strLocationTitle != null)
            this.strLocationTitle = strLocationTitle;
        else
            this.strLocationTitle = "";
    }

    public String getStrServiceStage() {
        return strServiceStage;
    }

    public void setStrServiceStage(String strServiceStage) {
        if (strServiceStage != null)
            this.strServiceStage = strServiceStage;
        else
            this.strServiceStage = "";
    }

    public String getStrMake() {
        return strMake;
    }

    public void setStrMake(String strMake) {

        if (strMake != null)
            this.strMake = strMake;
        else
            this.strMake = "";
    }

    public String getStrMaxHp() {
        return strMaxHp;
    }

    public void setStrMaxHp(String strMaxHp) {
        if (strMaxHp != null)
            this.strMaxHp = strMaxHp;
        else
            this.strMaxHp = "";
    }

    public String getStrMaxTorque() {
        return strMaxTorque;
    }

    public void setStrMaxTorque(String strMaxTorque) {
        if (strMaxTorque != null)
            this.strMaxTorque = strMaxTorque;
        else
            this.strMaxTorque = "";
    }

    public String getStrMechanic() {
        return strMechanic;
    }

    public void setStrMechanic(String strMechanic) {
        if (strMechanic != null)
            this.strMechanic = strMechanic;
        else
            this.strMechanic = "";
    }

    public String getStrMiles() {
        return strMiles;
    }

    public void setStrMiles(String strMiles) {
        if (strMiles != null)
            this.strMiles = strMiles;
        else
            this.strMiles = "";
    }

    public String getStrModel() {
        return strModel;
    }

    public void setStrModel(String strModel) {
        if (strModel != null)
            this.strModel = strModel;
        else
            this.strModel = "";
    }

    public String getStrModelNumber() {
        return strModelNumber;
    }

    public void setStrModelNumber(String strModelNumber) {
        if (strModelNumber != null)
            this.strModelNumber = strModelNumber;
        else
            this.strModelNumber = "";
    }

    public String getStrModelYear() {
        return strModelYear;
    }

    public void setStrModelYear(String strModelYear) {
        if (strModelYear != null)
            this.strModelYear = strModelYear;
        else
            this.strModelYear = "";
    }

    public String getStrOilCapacity() {
        return strOilCapacity;
    }

    public void setStrOilCapacity(String strOilCapacity) {
        if (strOilCapacity != null)
            this.strOilCapacity = strOilCapacity;
        else
            this.strOilCapacity = "";
    }

    public String getStrPurchaseForm() {
        return strPurchaseForm;
    }

    public void setStrPurchaseForm(String strPurchaseForm) {
        if (strPurchaseForm != null)
            this.strPurchaseForm = strPurchaseForm;
        else
            this.strPurchaseForm = "";
    }

    public String getStrRegistrationDate() {
        return strRegistrationDate;
    }

    public void setStrRegistrationDate(String strRegistrationDate) {
        if (strRegistrationDate != null)
            this.strRegistrationDate = strRegistrationDate;
        else
            this.strRegistrationDate = "";
    }

    public String getStrRfid() {
        return strRfid;
    }

    public void setStrRfid(String strRfid) {
        if (strRfid != null)
            this.strRfid = strRfid;
        else
            this.strRfid = "";
    }

    public String getStrSalesPrice() {
        return strSalesPrice;
    }

    public void setStrSalesPrice(String strSalesPrice) {
        if (strSalesPrice != null)
            this.strSalesPrice = strSalesPrice;
        else
            this.strSalesPrice = "";
    }

    public String getStrSalesType() {
        return strSalesType;
    }

    public void setStrSalesType(String strSalesType) {
        this.strSalesType = strSalesType;
    }

    public String getStrStage() {
        return strStage;
    }

    public void setStrStage(String strStage) {
        if (strStage != null)
            this.strStage = strStage;
        else
            this.strStage = "";
    }

    public String getStrStatus() {
        return strStatus;
    }

    public void setStrStatus(String strStatus) {
        if (strStatus != null)
            this.strStatus = strStatus;
        else
            this.strStatus = "";
    }

    public String getStrStockNumber() {
        return strStockNumber;
    }

    public void setStrStockNumber(String strStockNumber) {
        if (strStockNumber != null)
            this.strStockNumber = strStockNumber;
        else
            this.strStockNumber = "";
    }

    public String getStrType() {
        return strType;
    }

    public void setStrType(String strType) {
        this.strType = strType;
    }

    public String getStrVacancy() {
        return strVacancy;
    }

    public void setStrVacancy(String strVacancy) {
        if (strVacancy != null)
            this.strVacancy = strVacancy;
        else
            this.strVacancy = "";
    }

    public String getStrVehicleNote() {
        return strVehicleNote;
    }

    public void setStrVehicleNote(String strVehicleNote) {
        if (strVehicleNote != null)
            this.strVehicleNote = strVehicleNote;
        else
            this.strVehicleNote = "";
    }

    public String getStrVehicleProblem() {
        return strVehicleProblem;
    }

    public void setStrVehicleProblem(String strVehicleProblem) {
        if (strVehicleProblem != null)
            this.strVehicleProblem = strVehicleProblem;
        else
            this.strVehicleProblem = "";
    }

    public String getStrVehicleType() {
        return strVehicleType;
    }

    public void setStrVehicleType(String strVehicleType) {
        if (strVehicleType != null)
            this.strVehicleType = strVehicleType;
        else
            this.strVehicleType = "";
    }

    public String getStrVin() {
        return strVin;
    }

    public void setStrVin(String strVin) {
        this.strVin = strVin;
    }

    public String getStrpreVin() {
        return strpreVin;
    }

    public void setStrpreVin(String strpreVin) {
        this.strpreVin = strpreVin;
    }

    public boolean isDataOneInfoFound() {
        return isDataOneInfoFound;
    }

    public void setDataOneInfoFound(boolean dataOneInfoFound) {
        isDataOneInfoFound = dataOneInfoFound;
    }

    public boolean isFrmInfo() {
        return isFrmInfo;
    }

    public void setFrmInfo(boolean frmInfo) {
        isFrmInfo = frmInfo;
    }

    public String getStrLotCode() {
        return strLotCode;
    }

    public void setStrLotCode(String strLotCode) {
        if (strLotCode != null)
            this.strLotCode = strLotCode;
        else
            this.strLotCode = "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }


}

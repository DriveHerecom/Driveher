package com.yukti.dataone.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ravi on 20-02-2017.
 */

public class AddNewCarModel implements Parcelable {
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


    public File companyInsuranceFile;
    public ArrayList<String> arrayTitleImagePath = new ArrayList<String>();
    ;
    public ArrayList<String> arrayImagePath = new ArrayList<String>();
    ;
    public ArrayList<String> arrayListGpsSerial = new ArrayList<String>();
    ;


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
        this.strTitleLot = strTitleLot;
    }

    public boolean isFrmInfo = false;
    public boolean isDataOneInfoFound = false;

    Context context;

    public AddNewCarModel(Context context) {
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
        this.strColor = strColor;
    }

    public String getStrcolorcode() {
        return strcolorcode;
    }

    public void setStrcolorcode(String strcolorcode) {
        this.strcolorcode = strcolorcode;
    }

    public String getStrCompany() {
        return strCompany;
    }

    public void setStrCompany(String strCompany) {
        this.strCompany = strCompany;
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
        this.strCylinder = strCylinder;
    }

    public String getStrDriveType() {
        return strDriveType;
    }

    public void setStrDriveType(String strDriveType) {
        this.strDriveType = strDriveType;
    }

    public String getStrFuelType() {
        return strFuelType;
    }

    public void setStrFuelType(String strFuelType) {
        this.strFuelType = strFuelType;
    }

    public String getStrGasTank() {
        return strGasTank;
    }

    public void setStrGasTank(String strGasTank) {
        this.strGasTank = strGasTank;
    }

    public String getStrGpsInstall() {
        return strGpsInstall;
    }

    public void setStrGpsInstall(String strGpsInstall) {
        this.strGpsInstall = strGpsInstall;
    }

    public String getStrHasTitle() {
        return strHasTitle;
    }

    public void setStrHasTitle(String strHasTitle) {
        this.strHasTitle = strHasTitle;
    }

    public String getStrInspectionDate() {
        return strInspectionDate;
    }

    public void setStrInspectionDate(String strInspectionDate) {
        this.strInspectionDate = strInspectionDate;
    }

    public String getStrInsuranceDate() {
        return strInsuranceDate;
    }

    public void setStrInsuranceDate(String strInsuranceDate) {
        this.strInsuranceDate = strInsuranceDate;
    }

    public String getStrLocationTitle() {
        return strLocationTitle;
    }

    public void setStrLocationTitle(String strLocationTitle) {
        this.strLocationTitle = strLocationTitle;
    }

    public String getStrServiceStage() {
        return strServiceStage;
    }

    public void setStrServiceStage(String strServiceStage) {
        this.strServiceStage = strServiceStage;
    }

    public String getStrMake() {
        return strMake;
    }

    public void setStrMake(String strMake) {
        this.strMake = strMake;
    }

    public String getStrMaxHp() {
        return strMaxHp;
    }

    public void setStrMaxHp(String strMaxHp) {
        this.strMaxHp = strMaxHp;
    }

    public String getStrMaxTorque() {
        return strMaxTorque;
    }

    public void setStrMaxTorque(String strMaxTorque) {
        this.strMaxTorque = strMaxTorque;
    }

    public String getStrMechanic() {
        return strMechanic;
    }

    public void setStrMechanic(String strMechanic) {
        this.strMechanic = strMechanic;
    }

    public String getStrMiles() {
        return strMiles;
    }

    public void setStrMiles(String strMiles) {
        this.strMiles = strMiles;
    }

    public String getStrModel() {
        return strModel;
    }

    public void setStrModel(String strModel) {
        this.strModel = strModel;
    }

    public String getStrModelNumber() {
        return strModelNumber;
    }

    public void setStrModelNumber(String strModelNumber) {
        this.strModelNumber = strModelNumber;
    }

    public String getStrModelYear() {
        return strModelYear;
    }

    public void setStrModelYear(String strModelYear) {
        this.strModelYear = strModelYear;
    }

    public String getStrOilCapacity() {
        return strOilCapacity;
    }

    public void setStrOilCapacity(String strOilCapacity) {
        this.strOilCapacity = strOilCapacity;
    }

    public String getStrPurchaseForm() {
        return strPurchaseForm;
    }

    public void setStrPurchaseForm(String strPurchaseForm) {
        this.strPurchaseForm = strPurchaseForm;
    }

    public String getStrRegistrationDate() {
        return strRegistrationDate;
    }

    public void setStrRegistrationDate(String strRegistrationDate) {
        this.strRegistrationDate = strRegistrationDate;
    }

    public String getStrRfid() {
        return strRfid;
    }

    public void setStrRfid(String strRfid) {
        this.strRfid = strRfid;
    }

    public String getStrSalesPrice() {
        return strSalesPrice;
    }

    public void setStrSalesPrice(String strSalesPrice) {
        this.strSalesPrice = strSalesPrice;
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
        this.strStage = strStage;
    }

    public String getStrStatus() {
        return strStatus;
    }

    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }

    public String getStrStockNumber() {
        return strStockNumber;
    }

    public void setStrStockNumber(String strStockNumber) {
        this.strStockNumber = strStockNumber;
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
        this.strVacancy = strVacancy;
    }

    public String getStrVehicleNote() {
        return strVehicleNote;
    }

    public void setStrVehicleNote(String strVehicleNote) {
        this.strVehicleNote = strVehicleNote;
    }

    public String getStrVehicleProblem() {
        return strVehicleProblem;
    }

    public void setStrVehicleProblem(String strVehicleProblem) {
        this.strVehicleProblem = strVehicleProblem;
    }

    public String getStrVehicleType() {
        return strVehicleType;
    }

    public void setStrVehicleType(String strVehicleType) {
        this.strVehicleType = strVehicleType;
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
        this.strLotCode = strLotCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
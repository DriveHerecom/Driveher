package com.yukti.dataone.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.core.Persist;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ravi on 22-02-2017.
 */

public class CarDetailsObject implements Serializable {
    String vin = "", rfid = "", make = "", modelYear = "", lotCode = "", color = "", modelNumber = "", price = "", miles = "", fuel = "", driveType = "";
    String cylinder = "", vehicleType = "", hasTitle = "", locationTitle = "", companyName = "", stockNumber = "", purchasedFrom = "", note = "", noteDate = "";
    String gpsInstalled = "", vehicleStatus = "", hasRfid = "", vacancy = "", vehicleStage = "", serviceStage = "", problem = "", doneDate = "", doneDateLotCode = "";
    String mechanic = "", auctionName = "", auctionDate = "", carReadyForAuction = "", carAtAuction = "", companyInsuranceImage = "", registrationDate = "";
    String insuranceDate = "", gasTank = "", hasLocation = "", carModel = "", maxHp = "", maxTorque = "", fuelType = "", oilCapacity = "", gps = "", title = "", count = "";
    String inGps = "", salesPrice = "", userWhoUploaded = "", dateSold = "", reconExp = "", customerFullName = "", mpgCity = "", mpgHighway = "", transmissionType = "";
    String gears = "", stage = "", dateoneInformation = "", createdDate = "", modifiedDate = "", atTheAuction = "", floorPrice = "", condition = "", auctionMile = "";
    String imageCompanyInsurance = "", auctionNote = "", carReady = "", status = "", scannDate = "", stageDate = "", insuranceExpirationDate = "", inspectionExperationDate = "";
    String isDecode = "", greetingMiles = "", missing = "", purchasedPrice = "",id="0";
    int imageCount = 0, userId = 0, purchasePrice = 0, tradeInAmount = 0, cost = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public ArrayList<String> carImages=new ArrayList<>();

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModelYear() {
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMiles() {
        return miles;
    }

    public void setMiles(String miles) {
        this.miles = miles;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getDriveType() {
        return driveType;
    }

    public void setDriveType(String driveType) {
        this.driveType = driveType;
    }

    public String getCylinder() {
        return cylinder;
    }

    public void setCylinder(String cylinder) {
        this.cylinder = cylinder;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getHasTitle() {
        return hasTitle;
    }

    public void setHasTitle(String hasTitle) {
        this.hasTitle = hasTitle;
    }

    public String getLocationTitle() {
        return locationTitle;
    }

    public void setLocationTitle(String locationTitle) {
        this.locationTitle = locationTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(String stockNumber) {
        this.stockNumber = stockNumber;
    }

    public String getPurchasedFrom() {
        return purchasedFrom;
    }

    public void setPurchasedFrom(String purchasedFrom) {
        this.purchasedFrom = purchasedFrom;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    public String getGpsInstalled() {
        return gpsInstalled;
    }

    public void setGpsInstalled(String gpsInstalled) {
        this.gpsInstalled = gpsInstalled;
    }

    public String getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public String getHasRfid() {
        return hasRfid;
    }

    public void setHasRfid(String hasRfid) {
        this.hasRfid = hasRfid;
    }

    public String getVacancy() {
        return vacancy;
    }

    public void setVacancy(String vacancy) {
        this.vacancy = vacancy;
    }

    public String getVehicleStage() {
        return vehicleStage;
    }

    public void setVehicleStage(String vehicleStage) {
        this.vehicleStage = vehicleStage;
    }

    public String getServiceStage() {
        return serviceStage;
    }

    public void setServiceStage(String serviceStage) {
        this.serviceStage = serviceStage;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(String doneDate) {
        this.doneDate = doneDate;
    }

    public String getDoneDateLotCode() {
        return doneDateLotCode;
    }

    public void setDoneDateLotCode(String doneDateLotCode) {
        this.doneDateLotCode = doneDateLotCode;
    }

    public String getMechanic() {
        return mechanic;
    }

    public void setMechanic(String mechanic) {
        this.mechanic = mechanic;
    }

    public String getAuctionName() {
        return auctionName;
    }

    public void setAuctionName(String auctionName) {
        this.auctionName = auctionName;
    }

    public String getAuctionDate() {
        return auctionDate;
    }

    public void setAuctionDate(String auctionDate) {
        this.auctionDate = auctionDate;
    }

    public String getCarReadyForAuction() {
        return carReadyForAuction;
    }

    public void setCarReadyForAuction(String carReadyForAuction) {
        this.carReadyForAuction = carReadyForAuction;
    }

    public String getCarAtAuction() {
        return carAtAuction;
    }

    public void setCarAtAuction(String carAtAuction) {
        this.carAtAuction = carAtAuction;
    }

    public String getCompanyInsuranceImage() {
        return companyInsuranceImage;
    }

    public void setCompanyInsuranceImage(String companyInsuranceImage) {
        this.companyInsuranceImage = companyInsuranceImage;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getInsuranceDate() {
        return insuranceDate;
    }

    public void setInsuranceDate(String insuranceDate) {
        this.insuranceDate = insuranceDate;
    }

    public String getGasTank() {
        return gasTank;
    }

    public void setGasTank(String gasTank) {
        this.gasTank = gasTank;
    }

    public String getHasLocation() {
        return hasLocation;
    }

    public void setHasLocation(String hasLocation) {
        this.hasLocation = hasLocation;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(String maxHp) {
        this.maxHp = maxHp;
    }

    public String getMaxTorque() {
        return maxTorque;
    }

    public void setMaxTorque(String maxTorque) {
        this.maxTorque = maxTorque;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getOilCapacity() {
        return oilCapacity;
    }

    public void setOilCapacity(String oilCapacity) {
        this.oilCapacity = oilCapacity;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getInGps() {
        return inGps;
    }

    public void setInGps(String inGps) {
        this.inGps = inGps;
    }

    public String getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(String salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getUserWhoUploaded() {
        return userWhoUploaded;
    }

    public void setUserWhoUploaded(String userWhoUploaded) {
        this.userWhoUploaded = userWhoUploaded;
    }

    public String getDateSold() {
        return dateSold;
    }

    public void setDateSold(String dateSold) {
        this.dateSold = dateSold;
    }

    public String getReconExp() {
        return reconExp;
    }

    public void setReconExp(String reconExp) {
        this.reconExp = reconExp;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public String getMpgCity() {
        return mpgCity;
    }

    public void setMpgCity(String mpgCity) {
        this.mpgCity = mpgCity;
    }

    public String getMpgHighway() {
        return mpgHighway;
    }

    public void setMpgHighway(String mpgHighway) {
        this.mpgHighway = mpgHighway;
    }

    public String getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    public String getGears() {
        return gears;
    }

    public void setGears(String gears) {
        this.gears = gears;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getDateoneInformation() {
        return dateoneInformation;
    }

    public void setDateoneInformation(String dateoneInformation) {
        this.dateoneInformation = dateoneInformation;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getAtTheAuction() {
        return atTheAuction;
    }

    public void setAtTheAuction(String atTheAuction) {
        this.atTheAuction = atTheAuction;
    }

    public String getFloorPrice() {
        return floorPrice;
    }

    public void setFloorPrice(String floorPrice) {
        this.floorPrice = floorPrice;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getAuctionMile() {
        return auctionMile;
    }

    public void setAuctionMile(String auctionMile) {
        this.auctionMile = auctionMile;
    }

    public String getImageCompanyInsurance() {
        return imageCompanyInsurance;
    }

    public void setImageCompanyInsurance(String imageCompanyInsurance) {
        this.imageCompanyInsurance = imageCompanyInsurance;
    }

    public String getAuctionNote() {
        return auctionNote;
    }

    public void setAuctionNote(String auctionNote) {
        this.auctionNote = auctionNote;
    }

    public String getCarReady() {
        return carReady;
    }

    public void setCarReady(String carReady) {
        this.carReady = carReady;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScannDate() {
        return scannDate;
    }

    public void setScannDate(String scannDate) {
        this.scannDate = scannDate;
    }

    public String getStageDate() {
        return stageDate;
    }

    public void setStageDate(String stageDate) {
        this.stageDate = stageDate;
    }

    public String getInsuranceExpirationDate() {
        return insuranceExpirationDate;
    }

    public void setInsuranceExpirationDate(String insuranceExpirationDate) {
        this.insuranceExpirationDate = insuranceExpirationDate;
    }

    public String getInspectionExperationDate() {
        return inspectionExperationDate;
    }

    public void setInspectionExperationDate(String inspectionExperationDate) {
        this.inspectionExperationDate = inspectionExperationDate;
    }

    public String getIsDecode() {
        return isDecode;
    }

    public void setIsDecode(String isDecode) {
        this.isDecode = isDecode;
    }

    public String getGreetingMiles() {
        return greetingMiles;
    }

    public void setGreetingMiles(String greetingMiles) {
        this.greetingMiles = greetingMiles;
    }

    public String getMissing() {
        return missing;
    }

    public void setMissing(String missing) {
        this.missing = missing;
    }

    public String getPurchasedPrice() {
        return purchasedPrice;
    }

    public void setPurchasedPrice(String purchasedPrice) {
        this.purchasedPrice = purchasedPrice;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(int purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getTradeInAmount() {
        return tradeInAmount;
    }

    public void setTradeInAmount(int tradeInAmount) {
        this.tradeInAmount = tradeInAmount;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

}

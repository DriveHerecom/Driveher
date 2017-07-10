package com.yukti.driveherenew.search;

import java.io.Serializable;
import java.util.ArrayList;

public class CarInventory implements Serializable {

    public String carId = "", Make = "", Model = "", Note = "", DoneDate = "", ModelNumber = "", ModelYear = "",
            Company = "", Miles = "", StockNumber = "", LotCode = "", SalesPrice = "", VehicleStatus = "",
            PurchasedFrom = "", Vin = "", Rfid = "", InventoryDateReceived, ReconExp,
            TradeInAmount, Cost, UserWhoUploaded, DateSold, CustomerFullName,
            DataoneInformation, CreatedDate = "", ModifiedDate, DonedateLot = "", ServiceStage = "",

    insurancedate = "",

    registrationdate = "",

    inspectiondate = "",

			Color, Cylinders, Gastank, MaxHP, MaxTorque, VehicleType,
			DriveType, FuelType, OilCapacity, Stage, Problem, Title,
			has_location, Gps_Installed, MinSalesPrice, MaxSalesPrice,
			auctionname, auctionmile, floorprice, conditions,
			auctiondate, company_insurance, carready, caratauction,NoteDate,HasRfid,
			// New Parameter 23 Feb 2017
	        imagePath,vin,rfid,make,modelYear,lotCode,color,modelNumber,price,miles,fuel,driveType,
            cylinder,vehicleType,hasTitle,locationTitle,company,stockNumber,purchasedFrom,note,noteDate,
            gpsInstalled,vehicleStatus,hasRfid,vacancy,vehicleStage,serviceStage,problem,doneDate,
            doneDateLotCode,mechanic,auctionName,auctionDate,carReadyForAuction,carAtAuction,companyInsuranceImage,
            registrationDate,insuranceDate,gasTank,hasLocation,model,maxHp,maxTorque,fuelType,oilCapacity
//         ,gps
            ,title,count,inGps,imageCompanyInsurance,imageCount,userId,purchasePrice,reconExp,tradeInAmount,
            cost,salesPrice,userWhoUploaded,dateSold,customerFullName,mpgCity,mpgHighway,transmissionType,
            gears,stage,dateoneInformation,createdDate,modifiedDate,atTheAuction,floorPrice,condition,auctionMile,
            auctionNote,carReady,status,scannDate,stageDate,insuranceExpirationDate,inspectionExperationDate,
            isDecode,greetingMiles,missing,is_done;

	public ArrayList<String> images;
	public ArrayList<Photo> photos;
	public ArrayList<Location> locations;
	public ArrayList<Gps> gps;
}

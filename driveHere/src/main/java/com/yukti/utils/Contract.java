package com.yukti.utils;

import android.os.Parcel;
import android.os.Parcelable;

//import com.mizcoin.cardrive.enums.Constant;

import org.json.JSONObject;

/**
 * Created by gaurav on 5/20/15.
 */

/*
{"statusCode":"1","message":"record available","carId":"12","vin":"knafe161x55089164","preMilage":"12","postMilage":"",
"preGasTank":"12","postGasTank":"","preInspection":"no","postInspection":"","preNotes":"dfsf","postNotes":"",
"expectedDate":"2015\/05\/23","receivedDate":"","bookingDate":"2015\/05\/22","cId":"0","catStatus":"0","bookBy":"as",
"receivedBy":"","preLotReceived":"45","postLotReceived":"",
"contract":"http:\/\/drivehere.com\/inventory2\/app\/webroot\/files\/images\/","licenceNo":"",
"insurance":"http:\/\/drivehere.com\/inventory2\/app\/webroot\/files\/images\/","contractId":"546","status":"1"}
 */
public class Contract implements Parcelable {

    String statusCode;
    String message;
    String carId;
    String vin;
    String rfid;
   

	String preMilage;
    String postMilage;
    String preGasTank;
    String postGasTank;
    String preInspection;
    String postInspection;
    String preNotes;
    String postNotes;
    String expectedDate;
    String receivedDate;
    String bookingDate;
    String cId;
    String catStatus;
    String bookBy;
    String receivedBy;
    String preLotReceived;
    String postLotReceived;
    String contract;
    String licenceNo;
    String insurance;
    String contractId;
    String status;
    String fname;
    String lname;
    String cphone;
    String hphone;
    String wphone;
    String licImage;
    String licNo;
    String createdDate;
    String modifiedDate;
    String car1Image;
    String car2Image;
    
    public Contract (JSONObject jsonObj){
    	
    	try {
    		setStatusCode(jsonObj.getString(Constant.STATUS_CODE));	
		} catch (Exception e) {
			setStatusCode("0");
		}
    	
    	try {
    		setMessage(jsonObj.getString(Constant.MESSAGE));
		} catch (Exception e) {
			setMessage("");
		}
		
    	try{
    		setStatus(jsonObj.getString(Constant.STATUS));
    	}
    	catch(Exception e)
    	{
    		setStatus("");
    	}
    	
    	try {
    		setBookBy(jsonObj.getString(Constant.BOOK_BY));
		} catch (Exception e) {
			setBookBy("");
		}
    	
    	try {
    		setBookingDate(jsonObj.getString(Constant.BOOKING_DATE));
		} catch (Exception e) {
			setBookingDate("");
		}
    	
    	try {
    		setCarId(jsonObj.getString(Constant.CAR_ID));
		} catch (Exception e) {
			setCarId("");
		}
    	
    	try {
    		setcId(jsonObj.getString(Constant.C_ID));
		} catch (Exception e) {
			setcId("");
		}	
    	
    	try {
    		setCatStatus(jsonObj.getString(Constant.CAT_STATUS));
		} catch (Exception e) {
			setCatStatus("");
		}
    	
    	try {
    		setExpectedDate(jsonObj.getString(Constant.EXPECTED_DATE));
		} catch (Exception e) {
			setExpectedDate("");
		}
    	
    	try {
    		setContractId(jsonObj.getString(Constant.CONTRACT_ID));
		} catch (Exception e) {
			setContractId("");
		}
    	
    	try {
    		setInsurance(jsonObj.getString(Constant.INSURANCE));
		} catch (Exception e) {
			setInsurance("");
		}
    	
    	try {
    		setPreLotReceived(jsonObj.getString(Constant.PRE_LOT_RECEIVED));
		} catch (Exception e) {
			setPreLotReceived("");
		}
    	
    	try {
    		setVin(jsonObj.getString(Constant.VIN));
		} catch (Exception e) {
			setVin("");
		}
    	
    	try {
    		setRfid(jsonObj.getString(Constant.RFID));
		} catch (Exception e) {
			setRfid("");
		}
    	
    	try {
    		setPreNotes(jsonObj.getString(Constant.PRE_NOTES));
		} catch (Exception e) {
			setPreNotes("");
		}
    	
    	try {
    		setPreInspection(jsonObj.getString(Constant.PRE_INSPECTION));
		} catch (Exception e) {
			setPreInspection("0");
		}
    	
    	try {
    		setPreGasTank(jsonObj.getString(Constant.PRE_GAS_TANK));
		} catch (Exception e) {
			setPreGasTank("");
		}
    	
    	try {
    		setPreMilage(jsonObj.getString(Constant.PRE_MILAGE));
		} catch (Exception e) {
			setPreMilage("");
		}
    	
    	try {
    		setFname(jsonObj.getString(Constant.FNAME));
		} catch (Exception e) {
			setFname("");
		}
    	
    	try {
    		setLname(jsonObj.getString(Constant.LNAME));
		} catch (Exception e) {
			setLname("");
		}
    	
    	try {
    		setCphone(jsonObj.getString(Constant.CPHONE));
		} catch (Exception e) {
			setCphone("");
		}
    	
    	try {
    		setHphone(jsonObj.getString(Constant.HPHONE));
		} catch (Exception e) {
			setHphone("");
		}
    	
    	try {
    		setWphone(jsonObj.getString(Constant.WPHONE));
		} catch (Exception e) {
			setWphone("");
		}
    	
    	try {
    		setLicImage(jsonObj.getString(Constant.LIC_IMAGE));
		} catch (Exception e) {
			setLicImage("");
		}
    	
    	try {
    		setLicNo(jsonObj.getString(Constant.LIC_NO));
		} catch (Exception e) {
			setLicNo("");
		}
    	
    	try {
    		setCreatedDate(jsonObj.getString(Constant.CREATED_DATE));
		} catch (Exception e) {
			setCreatedDate("");
		}
    	
    	try {
    		setModifiedDate(jsonObj.getString(Constant.MODIFIED_DATE));
		} catch (Exception e) {
			setModifiedDate("");
		}  	
    }
    
    public String getCar1Image() {
		return car1Image;
	}

	public void setCar1Image(String car1Image) {
		this.car1Image = car1Image;
	}

	public String getCar2Image() {
		return car2Image;
	}

	public void setCar2Image(String car2Image) {
		this.car2Image = car2Image;
	}

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getCphone() {
        return cphone;
    }

    public void setCphone(String cphone) {
        this.cphone = cphone;
    }

    public String getHphone() {
        return hphone;
    }

    public void setHphone(String hphone) {
        this.hphone = hphone;
    }

    public String getWphone() {
        return wphone;
    }

    public void setWphone(String wphone) {
        this.wphone = wphone;
    }

    public String getLicImage() {
        return licImage;
    }

    public void setLicImage(String licImage) {
        this.licImage = licImage;
    }

    public String getLicNo() {
        return licNo;
    }

    public void setLicNo(String licNo) {
        this.licNo = licNo;
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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

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

    public String getPreMilage() {
        return preMilage;
    }

    public void setPreMilage(String preMilage) {
        this.preMilage = preMilage;
    }

    public String getPostMilage() {
        return postMilage;
    }

    public void setPostMilage(String postMilage) {
        this.postMilage = postMilage;
    }

    public String getPreGasTank() {
        return preGasTank;
    }

    public void setPreGasTank(String preGasTank) {
        this.preGasTank = preGasTank;
    }

    public String getPostGasTank() {
        return postGasTank;
    }

    public void setPostGasTank(String postGasTank) {
        this.postGasTank = postGasTank;
    }

    public String getPreInspection() {
        return preInspection;
    }

    public void setPreInspection(String preInspection) {
        this.preInspection = preInspection;
    }

    public String getPostInspection() {
        return postInspection;
    }

    public void setPostInspection(String postInspection) {
        this.postInspection = postInspection;
    }

    public String getPreNotes() {
        return preNotes;
    }

    public void setPreNotes(String preNotes) {
        this.preNotes = preNotes;
    }

    public String getPostNotes() {
        return postNotes;
    }

    public void setPostNotes(String postNotes) {
        this.postNotes = postNotes;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(String expectedDate) {
        this.expectedDate = expectedDate;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getCatStatus() {
        return catStatus;
    }

    public void setCatStatus(String catStatus) {
        this.catStatus = catStatus;
    }

    public String getBookBy() {
        return bookBy;
    }

    public void setBookBy(String bookBy) {
        this.bookBy = bookBy;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public String getPreLotReceived() {
        return preLotReceived;
    }

    public void setPreLotReceived(String preLotReceived) {
        this.preLotReceived = preLotReceived;
    }

    public String getPostLotReceived() {
        return postLotReceived;
    }

    public void setPostLotReceived(String postLotReceived) {
        this.postLotReceived = postLotReceived;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getLicenceNo() {
        return licenceNo;
    }

    public void setLicenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Contract() {

    }

    public static final Creator<Contract> CREATOR = new Creator<Contract>() {
        public Contract createFromParcel(Parcel in) {
            return new Contract(in);
        }

        public Contract[] newArray(int size) {
            return new Contract[size];
        }
    };

    public Contract(Parcel in) {
        this.statusCode = in.readString();
        this.message = in.readString();
        this.carId = in.readString();
        this.vin = in.readString();
        this.rfid = in.readString();
        this.preMilage = in.readString();
        this.postMilage = in.readString();
        this.preGasTank = in.readString();
        this.postGasTank = in.readString();
        this.preInspection = in.readString();
        this.postInspection = in.readString();
        this.preNotes = in.readString();
        this.postNotes = in.readString();
        this.expectedDate = in.readString();
        this.receivedDate = in.readString();
        this.bookingDate = in.readString();
        this.cId = in.readString();
        this.catStatus = in.readString();
        this.bookBy = in.readString();
        this.receivedBy = in.readString();
        this.preLotReceived = in.readString();
        this.postLotReceived = in.readString();
        this.contract = in.readString();
        this.licenceNo = in.readString();
        this.insurance = in.readString();
        this.contractId = in.readString();
        this.status = in.readString();
        this.fname = in.readString();
        this.lname = in.readString();
        this.cphone = in.readString();
        this.hphone = in.readString();
        this.wphone = in.readString();
        this.licImage = in.readString();
        this.licNo = in.readString();
        this.createdDate = in.readString();
        this.modifiedDate = in.readString();
        
        this.car1Image =  in.readString();
        this.car2Image =  in.readString();
    }

    public Contract(String statusCode,
                    String message,
                    String carId,
                    String vin,
                    String rfid,
                    String preMilage,
                    String postMilage,
                    String preGasTank,
                    String postGasTank,
                    String preInspection,
                    String postInspection,
                    String preNotes,
                    String postNotes,
                    String expectedDate,
                    String receivedDate,
                    String bookingDate,
                    String cId,
                    String catStatus,
                    String bookBy,
                    String receivedBy,
                    String preLotReceived,
                    String postLotReceived,
                    String contract,
                    String licenceNo,
                    String insurance,
                    String contractId,
                    String status,
                    String fname,
                    String lname,
                    String cphone,
                    String hphone,
                    String wphone,
                    String licImage,
                    String licNo,
                    String createdDate,
                    String modifiedDate,
                    String car1image,
                    String car2image
    		) {
        this.statusCode = statusCode;
        this.message = message;
        this.carId = carId;
        this.vin = vin;
        this.rfid = rfid;
        this.preMilage = preMilage;
        this.postMilage = postMilage;
        this.preGasTank = preGasTank;
        this.postGasTank = postGasTank;
        this.preInspection = preInspection;
        this.postInspection = postInspection;
        this.preNotes = preNotes;
        this.postNotes = postNotes;
        this.expectedDate = expectedDate;
        this.receivedDate = receivedDate;
        this.bookingDate = bookingDate;
        this.cId = cId;
        this.catStatus = catStatus;
        this.bookBy = bookBy;
        this.receivedBy = receivedBy;
        this.preLotReceived = preLotReceived;
        this.postLotReceived = postLotReceived;
        this.contract = contract;
        this.licenceNo = licenceNo;
        this.insurance = insurance;
        this.contractId = contractId;
        this.status = status;
        this.fname = fname;
        this.lname = lname;
        this.cphone = cphone;
        this.hphone = hphone;
        this.wphone = wphone;
        this.licImage = licImage;
        this.licNo = licNo;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.car1Image = car1image;
        this.car2Image = car2image;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(statusCode);
        dest.writeString(message);
        dest.writeString(carId);
        dest.writeString(vin);
        dest.writeString(rfid);
        dest.writeString(preMilage);
        dest.writeString(postMilage);
        dest.writeString(preGasTank);
        dest.writeString(postGasTank);
        dest.writeString(preInspection);
        dest.writeString(postInspection);
        dest.writeString(preNotes);
        dest.writeString(postNotes);
        dest.writeString(expectedDate);
        dest.writeString(receivedDate);
        dest.writeString(bookingDate);
        dest.writeString(cId);
        dest.writeString(catStatus);
        dest.writeString(bookBy);
        dest.writeString(receivedBy);
        dest.writeString(preLotReceived);
        dest.writeString(postLotReceived);
        dest.writeString(contract);
        dest.writeString(licenceNo);
        dest.writeString(insurance);
        dest.writeString(contractId);
        dest.writeString(status);
        dest.writeString(fname);
        
        dest.writeString(lname);
        dest.writeString(cphone);
        dest.writeString(hphone);
        dest.writeString(wphone);
        dest.writeString(licImage);
        dest.writeString(licNo);
        dest.writeString(createdDate);
        dest.writeString(modifiedDate);
        
        dest.writeString(car1Image);
        dest.writeString(car2Image);
    }
}

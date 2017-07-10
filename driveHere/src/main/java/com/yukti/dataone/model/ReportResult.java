package com.yukti.dataone.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.yukti.driveherenew.search.CarInventory;

import java.io.Serializable;
import java.util.ArrayList;

public class ReportResult implements Serializable, Parcelable {
	public String msg, status_code;
	 public ArrayList<ReportDetail> result;
	 public ArrayList<MissingCar>  missingcarresult;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

	}
}

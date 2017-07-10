package com.yukti.driveherenew.search;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class LatLong implements Serializable {

	@SerializedName("success")
	public String success;
	@SerializedName("latitude")
	public double latitude;
	@SerializedName("longitude")
	public double longitude;

}

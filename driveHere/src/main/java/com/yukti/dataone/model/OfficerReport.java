package com.yukti.dataone.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class OfficerReport  implements Serializable{

//	@SerializedName("email")
//	public String EMAIL;
	
	@SerializedName("msg")
	public String msg;
	
	
	@SerializedName("status_code")
	public int status;
	
	
	
}

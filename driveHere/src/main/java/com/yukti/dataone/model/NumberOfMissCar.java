package com.yukti.dataone.model;

import java.util.HashMap;

public class NumberOfMissCar {

	// public String
	// status_code,All,Auction,Junk,MC,WFP,PA,OneOneEight,QC,FortyMiles,Detailing,Cb,Ready;

	public NumberOfMissCar(){
		setHashStageList();
	}

	public int All = 0;
	public int OneOneEight = 0;
	public int QC = 0;
	public int Cb = 0;
	public int FortyMiles = 0;
	public int Detailing = 0;
	public int Ready = 0;
	public int HoldPPA = 0;
	public int HoldRepo = 0;
	public int MC = 0;
	//public int PH = 0;
	public int PHOneOneEight = 0;
	public int PHCb = 0;
	public int PHCustomer = 0;
	public int WPOneOneEight = 0;
	public int WPCb = 0;
	public int WPCustomer = 0;
	public int Junk = 0;
	public int Auction = 0;
	public int Unknown = 0;

	public HashMap<String, Integer> hashStageList;

	public void setHashStageList() {
		this.hashStageList = new HashMap<>();
		hashStageList.put("available", 0);
		hashStageList.put("MC", 0);
		hashStageList.put("W/P-118", 0);
		hashStageList.put("W/P-CB", 0);
		hashStageList.put("W/P-Customer", 0);
		//hashStageList.put("P/H", 0);
		hashStageList.put("P/H-118", 0);
		hashStageList.put("P/H-CB", 0);
		hashStageList.put("P/H-Customer", 0);
		hashStageList.put("118", 0);
		hashStageList.put("QC", 0);
		hashStageList.put("CB", 0);
		hashStageList.put("40 Miles", 0);
		hashStageList.put("Detailing", 0);
		hashStageList.put("Ready", 0);
		hashStageList.put("Hold PPA", 0);
		hashStageList.put("Hold Repo", 0);
		hashStageList.put("Unknown", 0);
		hashStageList.put("Junk", 0);
		//hashStageList.put("", 0);
		hashStageList.put("Auction", 0);
	}
}

package com.yukti.driveherenew.search;

import java.io.Serializable;
import java.util.ArrayList;

public class Search implements Serializable {
	public String status_code, message, count;
	public int totalrecord;
	public ArrayList<CarInventory> cars;
	public ArrayList<CarInventory> result;
}

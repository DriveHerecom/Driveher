package com.yukti.dataone.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ReportDetail implements Serializable
{
	public String lotcode,total;
	public ArrayList<com.yukti.dataone.model.StageDetail> stages = new ArrayList<>();
}